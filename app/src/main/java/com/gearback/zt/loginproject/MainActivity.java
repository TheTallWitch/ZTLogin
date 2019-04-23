package com.gearback.zt.loginproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gearback.methods.Methods;
import com.gearback.zt.login.ConsentDialog;
import com.gearback.zt.login.ExpireDialog;
import com.gearback.zt.login.ExtendDialog;
import com.gearback.zt.login.LoginActivity;
import com.gearback.zt.login.LoginDataBaseHelper;
import com.gearback.zt.login.LoginRegisterDialog;
import com.gearback.zt.login.NoNetDialog;
import com.gearback.zt.login.PreExtendDialog;
import com.gearback.zt.login.PreRegisterDialog;
import com.gearback.zt.login.RegisterDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

}
