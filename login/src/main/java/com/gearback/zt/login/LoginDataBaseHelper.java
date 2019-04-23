package com.gearback.zt.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import com.gearback.zt.login.Classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginDataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH;
    public static final String DB_NAME = "user.db3";
    private SQLiteDatabase myDataBase;
    private final Context context;
    private Classes classes = new Classes();

    public LoginDataBaseHelper(Context context) throws IOException {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        boolean dbexist = checkdatabase();
        if (dbexist) {
            opendatabase();
        } else {
            createdatabase();
        }
    }

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        SQLiteDatabase db_Read = null;
        if(!dbexist) {
            db_Read = this.getReadableDatabase();
            db_Read.close();
            //this.getReadableDatabase();
            try {
                copydatabase();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkdatabase() {
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            e.printStackTrace();
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        InputStream myinput = context.getAssets().open(DB_NAME);

        String outfilename = DB_PATH + DB_NAME;

        OutputStream myoutput = new FileOutputStream(outfilename);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        myoutput.flush();
        myoutput.close();
        myinput.close();

        opendatabase();
    }

    public void opendatabase() throws SQLException {
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    public void closeDatabase() {
        if (myDataBase != null && myDataBase.isOpen()) {
            myDataBase.close();
        }
    }

    public void addUser(Classes.User user) {
        if (!myDataBase.isOpen()) {
            opendatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put("username", user.getUsername());
        cv.put("usertoken", user.getUsertoken());
        cv.put("publictoken", user.getPublictoken());
        cv.put("userfullname", user.getUserfullname());
        cv.put("usermail", user.getUsermail());
        cv.put("usercell", user.getUsercell());
        cv.put("iscellvalid", user.getIscellvalid());
        cv.put("useravatar", user.getUseravatar());
        cv.put("usergender", user.getUsergender());
        cv.put("usercity", user.getUsercity());
        cv.put("uvalid", user.getUvalid());
        cv.put("isguest", user.getIsguest());
        cv.put("logedin", user.getLogedin());
        cv.put("currencyid", user.getCurrencyid());
        cv.put("date_register", user.getDate_register());
        cv.put("date_lasttime", user.getDate_lasttime());
        cv.put("active_acc", user.getActive_acc());
        myDataBase.insert("user", null, cv);
    }
    public void updateUsers()
    {
        if (!myDataBase.isOpen()) {
            opendatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put("active_acc", 0);
        myDataBase.update("user", cv, "", null);
    }
    public void updateUser(int ID)
    {
        if (!myDataBase.isOpen()) {
            opendatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put("active_acc", 1);
        myDataBase.update("user", cv, "ID=" + ID, null);
    }
    public boolean deleteUser()
    {
        if (!myDataBase.isOpen()) {
            opendatabase();
        }
        return myDataBase.delete("user", "active_acc=1", null) > 0;
    }
    public Classes.User getUser() {
        if (!myDataBase.isOpen()) {
            opendatabase();
        }
        Classes.User user = null;
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM user WHERE active_acc = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            user = classes.new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getInt(17), cursor.getInt(0));
            cursor.close();
        }

        return user;
    }
    public List<Classes.User> getUserList() {
        if (!myDataBase.isOpen()) {
            opendatabase();
        }
        List<Classes.User> users = new ArrayList<>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM user", null);
        if (cursor.moveToFirst()) {
            do {
                users.add(classes.new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getInt(17), cursor.getInt(0)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return users;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }
}
