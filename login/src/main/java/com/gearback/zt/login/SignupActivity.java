package com.gearback.zt.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gearback.methods.HttpPostRequest;
import com.gearback.methods.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SignupActivity extends AppCompatActivity {

    LinearLayout fieldHolder;
    Button signupBtn;

    Methods methods = new Methods();
    Classes classes = new Classes();

    EditText fieldUserFullName, fieldMelliNum, fieldMelliCode, fieldUserSite, fieldUserCell, fieldUserEmail, fieldUserName, fieldPassword, fieldPasswordRepeat;
    TextView emptyUserFullName, emptyMelliNum, emptyMelliCode, emptyUserCell, emptyUserEmail, emptyUserName, emptyPassword, emptyPasswordRepeat;
    LinearLayout fieldImageHolder;
    TextView fieldImageName, fieldImageBtn, fieldCompanyRadioText, fieldPersonalRadioText, fieldNewsLetterText;
    ImageView fieldImage;
    RadioButton fieldPersonalRadio, fieldCompanyRadio;
    CheckBox fieldNewsLetter;
    Spinner fieldGenderSpinner, fieldCitySpinner, fieldStateSpinner, fieldCountrySpinner;

    SpinnerAdapter genderAdapter;
    List<String> genderList = Arrays.asList("زن", "مرد");

    int getfullname, getimage, getusertype, getusergendre, getusersite, getusercell, usemailasusername, havenewsletter, getuserplace, validateusermail;
    String ufullname = "", umail = "", uname = "", upass = "", usersite = "", usertypenum = "", usertype = "", usercell = "";
    int usergenre = 0, subscribe = 0;

    boolean appActive = true;
    private static int sessionDepth = 0;
    public ProgressDialog loader;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.signup_fragment);

        fieldHolder = findViewById(R.id.fieldHolder);
        signupBtn = findViewById(R.id.signupBtn);

        InitializeFields();

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = 0;
                ufullname = ""; umail = ""; uname = ""; upass = ""; usersite = ""; usertypenum = ""; usertype = ""; usercell = "";
                usergenre = 0; subscribe = 0;
                if (getfullname == 1) {
                    ufullname = String.valueOf(fieldUserFullName.getText());
                    if (ufullname.equals("")) {
                        status++;
                        emptyUserFullName.setText(getString(R.string.field_empty_name));
                        emptyUserFullName.setVisibility(View.VISIBLE);
                    }
                    else {
                        emptyUserFullName.setVisibility(View.GONE);
                    }
                }
                if (getusercell == 1) {
                    usercell = String.valueOf(fieldUserCell.getText());
                    if (usercell.equals("")) {
                        status++;
                        emptyUserCell.setText(getString(R.string.field_empty_cell));
                        emptyUserCell.setVisibility(View.VISIBLE);
                    }
                    else {
                        emptyUserCell.setVisibility(View.GONE);
                    }
                }
                if (usemailasusername == 1) {
                    umail = String.valueOf(fieldUserEmail.getText());
                    if (umail.equals("")) {
                        status++;
                        emptyUserEmail.setText(getString(R.string.field_empty_email));
                        emptyUserEmail.setVisibility(View.VISIBLE);
                    }
                    else {
                        if (!methods.isValidEmail(umail)) {
                            status++;
                            emptyUserEmail.setText(getString(R.string.field_wrong_email));
                            emptyUserEmail.setVisibility(View.VISIBLE);
                        }
                        else {
                            emptyUserEmail.setVisibility(View.GONE);
                        }
                    }
                }
                else {
                    umail = String.valueOf(fieldUserEmail.getText());
                    if (umail.equals("")) {
                        status++;
                        emptyUserEmail.setText(getString(R.string.field_empty_email));
                        emptyUserEmail.setVisibility(View.VISIBLE);
                    }
                    else {
                        if (!methods.isValidEmail(umail)) {
                            status++;
                            emptyUserEmail.setText(getString(R.string.field_wrong_email));
                            emptyUserEmail.setVisibility(View.VISIBLE);
                        }
                        else {
                            emptyUserEmail.setVisibility(View.GONE);
                        }
                    }
                    uname = String.valueOf(fieldUserName.getText());
                    if (uname.equals("")) {
                        status++;
                        emptyUserName.setText(getString(R.string.field_empty_username));
                        emptyUserName.setVisibility(View.VISIBLE);
                    }
                    else {
                        emptyUserName.setVisibility(View.GONE);
                    }
                }
                if (getusersite == 1) {
                    usersite = String.valueOf(fieldUserSite.getText());
                }
                if (getusertype == 1) {
                    if (fieldPersonalRadio.isChecked()) {
                        usertype = "0";
                        usertypenum = String.valueOf(fieldMelliNum.getText());
                        if (usertypenum.equals("")) {
                            status++;
                            emptyMelliNum.setText(getString(R.string.field_empty_melli_num));
                            emptyMelliNum.setVisibility(View.VISIBLE);
                        }
                        else {
                            emptyMelliNum.setVisibility(View.GONE);
                        }
                    }
                    else {
                        usertype = "1";
                        usertypenum = String.valueOf(fieldMelliCode.getText());
                        if (usertypenum.equals("")) {
                            status++;
                            emptyMelliCode.setText(getString(R.string.field_empty_melli_code));
                            emptyMelliCode.setVisibility(View.VISIBLE);
                        }
                        else {
                            emptyMelliCode.setVisibility(View.GONE);
                        }
                    }
                }
                if (havenewsletter == 1) {
                    if (fieldNewsLetter.isChecked()) {
                        subscribe = 1;
                    }
                    else {
                        subscribe = 0;
                    }
                }
                if (getusergendre == 1) {
                    if (fieldGenderSpinner.getSelectedItemPosition() == 0) {
                        usergenre = 2;
                    }
                    else if (fieldGenderSpinner.getSelectedItemPosition() == 1) {
                        usergenre = 1;
                    }
                }

                upass = String.valueOf(fieldPassword.getText());
                String repass = String.valueOf(fieldPasswordRepeat.getText());
                if (upass.equals("")) {
                    status++;
                    emptyPassword.setText(getString(R.string.field_empty_password));
                    emptyPassword.setVisibility(View.VISIBLE);
                }
                else {
                    emptyPassword.setVisibility(View.GONE);
                }
                if (repass.equals("")) {
                    status++;
                    emptyPasswordRepeat.setText(getString(R.string.field_empty_password_repeat));
                    emptyPasswordRepeat.setVisibility(View.VISIBLE);
                }
                else {
                    emptyPasswordRepeat.setVisibility(View.GONE);
                }
                if (!upass.equals("") && !repass.equals("")) {
                    if (!upass.equals(repass)) {
                        status++;
                        emptyPasswordRepeat.setText(getString(R.string.field_diff_password_repeat));
                        emptyPasswordRepeat.setVisibility(View.VISIBLE);
                    }
                    else {
                        emptyPasswordRepeat.setVisibility(View.GONE);
                    }
                }
                if (status == 0) {
                    if (usemailasusername == 1) {
                        methods.ShowSpinner(loader, SignupActivity.this);
                        HttpPostRequest postRequest = new HttpPostRequest(SignupActivity.this, new HttpPostRequest.TaskListener() {
                            @Override
                            public void onFinished(String result) {
                                if (appActive) {
                                    if (result.equals("0")) {
                                        emptyUserEmail.setVisibility(View.GONE);
                                        HttpPostRequest postRequest = new HttpPostRequest(SignupActivity.this, new HttpPostRequest.TaskListener() {
                                            @Override
                                            public void onFinished(String result) {
                                                if (appActive) {
                                                    methods.HideSpinner(loader);
                                                    Toast.makeText(SignupActivity.this, R.string.signed_up, Toast.LENGTH_LONG).show();
                                                    try {
                                                        JSONObject mainObject = new JSONObject(result);
                                                        if (mainObject.getString("status").equals("2")) {
                                                            try {
                                                                String username, password;
                                                                if (usemailasusername == 1) {
                                                                    username = String.valueOf(fieldUserEmail.getText());
                                                                }
                                                                else {
                                                                    username = String.valueOf(fieldUserName.getText());
                                                                }
                                                                password = String.valueOf(fieldPassword.getText());

                                                                LoginRequest(username, password, false);

                                                            } catch (ExecutionException | InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        else if (mainObject.getString("status").equals("0")) {
                                                            Toast.makeText(SignupActivity.this, R.string.signup_error, Toast.LENGTH_LONG).show();
                                                        }
                                                        else {
                                                            try {
                                                                String username, password;
                                                                if (usemailasusername == 1) {
                                                                    username = String.valueOf(fieldUserEmail.getText());
                                                                }
                                                                else {
                                                                    username = String.valueOf(fieldUserName.getText());
                                                                }
                                                                password = String.valueOf(fieldPassword.getText());

                                                                LoginRequest(username, password, false);

                                                            } catch (ExecutionException | InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                        postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/console/action.aspx", "act=register&fromapp=1&ufullname=" + ufullname + "&uname=" + uname + "&upass=" + upass + "&umail=" + umail + "&usercell=" + usercell + "&usersite=" + usersite + "&usertype=" + usertype + "&usertypenum=" + usertypenum + "&usergenre=" + usergenre + "&subscribe=" + subscribe);
                                    }
                                    else {
                                        emptyUserEmail.setText(getString(R.string.field_used_email));
                                        emptyUserEmail.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                        postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/console/ajax/registeration.aspx?act=checkmail&userid=&myquery=" + umail, "");
                    }
                    else {
                        methods.ShowSpinner(loader, SignupActivity.this);
                        HttpPostRequest postRequest = new HttpPostRequest(SignupActivity.this, new HttpPostRequest.TaskListener() {
                            @Override
                            public void onFinished(String result) {
                                if (appActive) {
                                    if (result.equals("0")) {
                                        emptyUserEmail.setVisibility(View.GONE);
                                        emptyUserName.setVisibility(View.GONE);
                                        HttpPostRequest postRequest = new HttpPostRequest(SignupActivity.this, new HttpPostRequest.TaskListener() {
                                            @Override
                                            public void onFinished(String result) {
                                                if (appActive) {
                                                    methods.HideSpinner(loader);
                                                    Toast.makeText(SignupActivity.this, R.string.signed_up, Toast.LENGTH_LONG).show();
                                                    try {
                                                        JSONObject mainObject = new JSONObject(result);
                                                        if (mainObject.getString("status").equals("2")) {
                                                            try {
                                                                String username, password;
                                                                if (usemailasusername == 1) {
                                                                    username = String.valueOf(fieldUserEmail.getText());
                                                                }
                                                                else {
                                                                    username = String.valueOf(fieldUserName.getText());
                                                                }
                                                                password = String.valueOf(fieldPassword.getText());

                                                                LoginRequest(username, password, false);

                                                            } catch (ExecutionException | InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        else if (mainObject.getString("status").equals("0")) {
                                                            Toast.makeText(SignupActivity.this, R.string.signup_error, Toast.LENGTH_LONG).show();
                                                        }
                                                        else {
                                                            try {
                                                                String username, password;
                                                                if (usemailasusername == 1) {
                                                                    username = String.valueOf(fieldUserEmail.getText());
                                                                }
                                                                else {
                                                                    username = String.valueOf(fieldUserName.getText());
                                                                }
                                                                password = String.valueOf(fieldPassword.getText());

                                                                LoginRequest(username, password, false);

                                                            } catch (ExecutionException | InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                        postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/console/action.aspx", "act=register&fromapp=1&ufullname=" + ufullname + "&uname=" + uname + "&upass=" + upass + "&umail=" + umail + "&usercell=" + usercell + "&usersite=" + usersite + "&usertype=" + usertype + "&usertypenum=" + usertypenum + "&usergenre=" + usergenre + "&subscribe=" + subscribe);
                                    }
                                    else {
                                        emptyUserEmail.setText(getString(R.string.field_used_email));
                                        emptyUserName.setText(getString(R.string.field_used_username));
                                        emptyUserEmail.setVisibility(View.VISIBLE);
                                        emptyUserName.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                        postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/console/ajax/registeration.aspx?act=checkall&userid=&myquery=" + uname + "&mynquery=" + umail, "");
                    }
                }
            }
        });
    }

    void InitializeFields() {
        try {

            Bundle extras = getIntent().getExtras();
            String data = "";
            if (extras != null) {
                data = extras.getString("data");
            }

            JSONObject mainObject = new JSONObject(data);
            validateusermail = mainObject.getInt("validateusermail");
            getfullname = mainObject.getInt("getfullname");
            getimage = mainObject.getInt("getimage");
            getusertype = mainObject.getInt("getusertype");
            getusergendre = mainObject.getInt("getusergendre");
            getusersite = mainObject.getInt("getusersite");
            getusercell = mainObject.getInt("getusercell");
            getuserplace = mainObject.getInt("getuserplace");
            usemailasusername = mainObject.getInt("usemailasusername");
            havenewsletter = mainObject.getInt("havenewsletter");

            if (getimage == 1) {
                View row = View.inflate(this, R.layout.field_image, null);
                fieldImageHolder = row.findViewById(R.id.fieldImageHolder);
                fieldImageName = row.findViewById(R.id.fieldImageName);
                fieldImageBtn = row.findViewById(R.id.fieldImageBtn);
                fieldImage = row.findViewById(R.id.fieldImage);

                fieldImageHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OpenImageTypeDialog();
                    }
                });
                fieldHolder.addView(row);
            }
            if (getfullname == 1) {
                View row = View.inflate(this, R.layout.field_fullname, null);
                emptyUserFullName = row.findViewById(R.id.emptyFullName);
                emptyUserFullName.setVisibility(View.GONE);
                fieldUserFullName = row.findViewById(R.id.fieldUserFullName);
                fieldHolder.addView(row);

                fieldUserFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });
                fieldUserFullName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldUserFullName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldUserFullName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldUserFullName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldUserFullName.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldUserFullName.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldUserFullName.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }
            if (getusergendre == 1) {
                View row = View.inflate(this, R.layout.field_gender, null);
                fieldGenderSpinner = row.findViewById(R.id.fieldGenderSpinner);
                genderAdapter = new SpinnerAdapter(this, genderList, false);
                fieldGenderSpinner.setAdapter(genderAdapter);
                fieldHolder.addView(row);
            }
            if (usemailasusername == 0) {
                View row = View.inflate(this, R.layout.field_email, null);
                emptyUserEmail = row.findViewById(R.id.emptyEmail);
                emptyUserEmail.setVisibility(View.GONE);
                fieldUserEmail = row.findViewById(R.id.fieldUserEmail);
                fieldHolder.addView(row);

                fieldUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });
                fieldUserEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldUserEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldUserEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldUserEmail.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldUserEmail.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldUserEmail.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldUserEmail.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });

                View usernameRow = View.inflate(this, R.layout.field_username, null);
                emptyUserName = usernameRow.findViewById(R.id.emptyUsername);
                emptyUserName.setVisibility(View.GONE);
                fieldUserName = usernameRow.findViewById(R.id.fieldUserName);
                fieldHolder.addView(usernameRow);

                fieldUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });
                fieldUserName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldUserName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldUserName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldUserName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldUserName.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldUserName.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldUserName.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }
            else {
                View row = View.inflate(this, R.layout.field_email, null);
                emptyUserEmail = row.findViewById(R.id.emptyEmail);
                emptyUserEmail.setVisibility(View.GONE);
                fieldUserEmail = row.findViewById(R.id.fieldUserEmail);
                fieldHolder.addView(row);

                fieldUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });
                fieldUserEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldUserEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldUserEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldUserEmail.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldUserEmail.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldUserEmail.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldUserEmail.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }

            //region PASSWORD
            View passwordRow = View.inflate(this, R.layout.field_password, null);
            emptyPassword = passwordRow.findViewById(R.id.emptyPassword);
            fieldPassword = (EditText) passwordRow.findViewById(R.id.fieldPassword);
            emptyPasswordRepeat = passwordRow.findViewById(R.id.emptyPasswordRepeat);
            emptyPassword.setVisibility(View.GONE);
            emptyPasswordRepeat.setVisibility(View.GONE);
            fieldPasswordRepeat = (EditText) passwordRow.findViewById(R.id.fieldPasswordRepeat);

            fieldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        methods.HideKeyboard(SignupActivity.this, view);
                    }
                }
            });
            fieldPasswordRepeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        methods.HideKeyboard(SignupActivity.this, view);
                    }
                }
            });
            fieldPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().equals("")) {
                        fieldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
                    }
                    else {
                        fieldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }
            });
            fieldPassword.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if (fieldPassword.getCompoundDrawables()[2] != null) {
                            if(event.getX() <= (fieldPassword.getCompoundDrawables()[2].getBounds().width())) {
                                fieldPassword.setText("");
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
            fieldPasswordRepeat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().equals("")) {
                        fieldPasswordRepeat.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
                    }
                    else {
                        fieldPasswordRepeat.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }
            });
            fieldPasswordRepeat.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if (fieldPasswordRepeat.getCompoundDrawables()[2] != null) {
                            if(event.getX() <= (fieldPasswordRepeat.getCompoundDrawables()[2].getBounds().width())) {
                                fieldPasswordRepeat.setText("");
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

            fieldHolder.addView(passwordRow);
            //endregion

            if (getuserplace == 1) {
                View row = View.inflate(this, R.layout.field_place, null);
                fieldCitySpinner = (Spinner) row.findViewById(R.id.fieldCitySpinner);
                fieldStateSpinner = (Spinner) row.findViewById(R.id.fieldStateSpinner);
                fieldCountrySpinner = (Spinner) row.findViewById(R.id.fieldCountrySpinner);
                //place adapters
                fieldHolder.addView(row);
            }
            if (getusercell == 1) {
                View row = View.inflate(this, R.layout.field_cell, null);
                emptyUserCell = row.findViewById(R.id.emptyCell);
                emptyUserCell.setVisibility(View.GONE);
                fieldUserCell = (EditText) row.findViewById(R.id.fieldUserCell);
                fieldHolder.addView(row);

                fieldUserCell.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });
                fieldUserCell.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldUserCell.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldUserCell.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldUserCell.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldUserCell.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldUserCell.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldUserCell.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }
            if (getusersite == 1) {
                View row = View.inflate(this, R.layout.field_site, null);
                fieldUserSite = (EditText) row.findViewById(R.id.fieldUserSite);
                fieldHolder.addView(row);

                fieldUserSite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });
                fieldUserSite.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldUserSite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldUserSite.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldUserSite.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldUserSite.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldUserSite.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldUserSite.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }

            if (getusertype == 1) {
                View row = View.inflate(this, R.layout.field_type, null);
                fieldCompanyRadioText = (TextView) row.findViewById(R.id.fieldCompanyRadioText);
                fieldPersonalRadioText = (TextView) row.findViewById(R.id.fieldPersonalRadioText);
                fieldCompanyRadio = (RadioButton) row.findViewById(R.id.fieldCompanyRadio);
                fieldPersonalRadio = (RadioButton) row.findViewById(R.id.fieldPersonalRadio);
                emptyMelliCode = row.findViewById(R.id.emptyMelliCode);
                emptyMelliNum = row.findViewById(R.id.emptyMelliNum);
                emptyMelliCode.setVisibility(View.GONE);
                emptyMelliNum.setVisibility(View.GONE);
                fieldMelliNum = (EditText) row.findViewById(R.id.fieldMelliNum);
                fieldMelliCode = (EditText) row.findViewById(R.id.fieldMelliCode);

                fieldPersonalRadio.setChecked(true);

                fieldMelliNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });
                fieldMelliCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            methods.HideKeyboard(SignupActivity.this, view);
                        }
                    }
                });

                fieldCompanyRadioText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fieldCompanyRadio.isChecked()) {
                            fieldCompanyRadio.setChecked(false);
                        }
                        else {
                            fieldCompanyRadio.setChecked(true);
                        }
                    }
                });
                fieldPersonalRadioText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fieldPersonalRadio.isChecked()) {
                            fieldPersonalRadio.setChecked(false);
                        }
                        else {
                            fieldPersonalRadio.setChecked(true);
                        }
                    }
                });
                fieldCompanyRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            fieldPersonalRadio.setChecked(false);
                            fieldMelliNum.setVisibility(View.GONE);
                            emptyMelliNum.setVisibility(View.GONE);
                            fieldMelliCode.setVisibility(View.VISIBLE);
                            emptyMelliCode.setVisibility(View.VISIBLE);
                        }
                    }
                });
                fieldPersonalRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            fieldCompanyRadio.setChecked(false);
                            fieldMelliNum.setVisibility(View.VISIBLE);
                            emptyMelliNum.setVisibility(View.VISIBLE);
                            fieldMelliCode.setVisibility(View.GONE);
                            emptyMelliCode.setVisibility(View.GONE);
                        }
                    }
                });
                fieldMelliNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldMelliNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldMelliNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldMelliNum.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldMelliNum.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldMelliNum.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldMelliNum.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                fieldMelliCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals("")) {
                            fieldMelliCode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                        }
                        else {
                            fieldMelliCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });
                fieldMelliCode.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (fieldMelliCode.getCompoundDrawables()[0] != null) {
                                if(event.getX() <= (fieldMelliCode.getCompoundDrawables()[0].getBounds().width())) {
                                    fieldMelliCode.setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });

                fieldHolder.addView(row);
            }

            if (havenewsletter == 1) {
                View row = View.inflate(this, R.layout.field_newsletter, null);
                fieldNewsLetterText = (TextView) row.findViewById(R.id.fieldNewsLetterText);
                fieldNewsLetter = (CheckBox) row.findViewById(R.id.fieldNewsLetter);

                fieldNewsLetterText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fieldNewsLetter.isChecked()) {
                            fieldNewsLetter.setChecked(false);
                        }
                        else {
                            fieldNewsLetter.setChecked(true);
                        }
                    }
                });

                fieldHolder.addView(row);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void OpenImageTypeDialog() {
        ImageTypeDialog imageTypeDialog = ImageTypeDialog.newInstance();
        imageTypeDialog.show(getSupportFragmentManager(), "imageTypeDialog");
    }

    public boolean LoginRequest(final String username, final String password, final boolean silent) throws ExecutionException, InterruptedException {
        Activity activity = null;
        if (!silent) {
            activity = this;
        }
        methods.ShowSpinner(loader, SignupActivity.this);
        HttpPostRequest postRequest = new HttpPostRequest(activity, new HttpPostRequest.TaskListener() {
            @Override
            public void onFinished(String result) {
                if (appActive) {
                    methods.HideSpinner(loader);
                    ParseUserLogin(result, username, password, silent);
                }
            }
        });
        postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/api/json/login.aspx?lang=fa", "username=" + username + "&password=" + password);
        return true;
    }
    public void ParseUserLogin(String result, String username, String password, boolean silent) {
        try {
            JSONArray data = new JSONArray(result);
            if (data.length() != 0) {
                JSONObject usr = data.getJSONObject(0);
                if (usr.has("status")) {
                    if (usr.getString("status").equals("err")) {
                        Toast.makeText(this, R.string.login_error, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Classes.User user = classes.new User(usr.getString("username"), usr.getString("usertoken"), usr.getString("publictoken"), usr.getString("userfullname"), usr.getString("usermail"), usr.getString("usercell"), usr.getString("iscellvalid"), usr.getString("useravatar"), usr.getString("usergender"), usr.getString("usercity"), usr.getString("uvalid"), usr.getString("isguest"), usr.getString("logedin"), usr.getString("currencyid"), usr.getString("date_register"), usr.getString("date_lasttime"), 1, 0);
                    try {
                        LoginDataBaseHelper DBHelper = new LoginDataBaseHelper(this);
                        DBHelper.opendatabase();
                        DBHelper.addUser(user);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("logged_in", true);
                        resultIntent.putExtra("ud", user.getUsertoken());
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                    } catch (IOException | SQLiteException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionDepth++;
        if(sessionDepth == 1){
            appActive = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sessionDepth > 0)
            sessionDepth--;
        if (sessionDepth == 0) {
            appActive = false;
        }
    }
}
