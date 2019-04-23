package com.gearback.zt.login;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gearback.custom.CustomTypefaceSpan;
import com.gearback.methods.HttpGetRequest;
import com.gearback.methods.HttpPostRequest;
import com.gearback.methods.Methods;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText userNameText, passwordText;
    Button loginBtn, googleLoginBtn;
    TextView forgotPasswordBtn, signupBtn, emptyUsername, emptyPassword;
    Methods methods = new Methods();
    Classes classes = new Classes();
    public ProgressDialog loader;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_GET_AUTH_CODE = 9003;
    Account mAccount;
    String authCode;

    ImageView headerImage, logoImage;

    boolean appActive = true;
    private static int sessionDepth = 0, SIGNUP_CODE = 88;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.login_fragment);

        headerImage = findViewById(R.id.headerImage);
        logoImage = findViewById(R.id.logoImage);
        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        forgotPasswordBtn = findViewById(R.id.loginForgetPassBtn);
        signupBtn = findViewById(R.id.loginSignupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        googleLoginBtn = findViewById(R.id.googleLoginBtn);
        emptyUsername = findViewById(R.id.emptyUsername);
        emptyPassword = findViewById(R.id.emptyPassword);
        emptyUsername.setVisibility(View.GONE);
        emptyPassword.setVisibility(View.GONE);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra("loginBundle");
            headerImage.setImageResource(bundle.getInt("header", 0));
            logoImage.setImageResource(bundle.getInt("logo", 0));
        }

        signupBtn.setPaintFlags(signupBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signupBtn.setText(getString(R.string.signup));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://www.googleapis.com/auth/userinfo.email"))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAuthCode();
            }
        });

        userNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(userNameText.getWindowToken(), 0);
                }
            }
        });
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);
                }
            }
        });

        userNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    userNameText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                }
                else {
                    userNameText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        userNameText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (userNameText.getCompoundDrawables()[0] != null) {
                        if(event.getX() <= (userNameText.getCompoundDrawables()[0].getBounds().width())) {
                            userNameText.setText("");
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    passwordText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
                }
                else {
                    passwordText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        passwordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (passwordText.getCompoundDrawables()[0] != null) {
                        if(event.getX() <= (passwordText.getCompoundDrawables()[0].getBounds().width())) {
                            passwordText.setText("");
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyUsername.setVisibility(View.GONE);
                emptyPassword.setVisibility(View.GONE);
                //open sign up
                OpenSignupActivity();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int safe = 0;
                String user = String.valueOf(userNameText.getText());
                String pass = String.valueOf(passwordText.getText());
                if (user.equals("")) {
                    safe++;
                    emptyUsername.setVisibility(View.VISIBLE);
                }
                else {
                    emptyUsername.setVisibility(View.GONE);
                }
                if (pass.equals("")) {
                    safe++;
                    emptyPassword.setVisibility(View.VISIBLE);
                }
                else {
                    emptyPassword.setVisibility(View.GONE);
                }
                if (safe == 0) {
                    emptyUsername.setVisibility(View.GONE);
                    emptyPassword.setVisibility(View.GONE);
                    try {
                        LoginRequest(user, pass, false);

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginPasswordDialog loginPasswordDialog = LoginPasswordDialog.newInstance();
                loginPasswordDialog.show(getSupportFragmentManager(), "loginPasswordDialog");
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAuthCode() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            authCode = acct.getServerAuthCode();
            mAccount = acct.getAccount();
            RetrieveTokenTask retrieveTokenTask = new RetrieveTokenTask();
            retrieveTokenTask.execute();
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_AUTH_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) {
                handleSignInResult(result);
            }
        }
        if (requestCode == SIGNUP_CODE) {
            if (data != null) {
                if (data.getBooleanExtra("logged_in", false)) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("logged_in", true);
                    resultIntent.putExtra("ud", data.getStringExtra("ud"));
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        }
    }
    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Account accountName = mAccount;
            String scopes = "oauth2:email";
            String token;
            try {
                token = GoogleAuthUtil.getToken(LoginActivity.this, accountName, scopes);
            } catch (IOException | GoogleAuthException e) {
                token = "";
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HttpGetRequest getRequest = new HttpGetRequest(null, new HttpGetRequest.TaskListener() {
                @Override
                public void onFinished(String result) {
                    if (appActive) {
                        try {
                            JSONObject data = new JSONObject(result);
                            String id = "", email = "", verified_email = "", name = "", given_name = "", family_name = "", link = "", picture = "", gender = "";
                            if (data.has("id")) {
                                id = data.getString("id");
                            }
                            if (data.has("email")) {
                                email = data.getString("email");
                            }
                            if (data.has("verified_email")) {
                                verified_email = data.getString("verified_email");
                            }
                            if (data.has("name")) {
                                name = data.getString("name");
                            }
                            if (data.has("given_name")) {
                                given_name = data.getString("given_name");
                            }
                            if (data.has("family_name")) {
                                family_name = data.getString("family_name");
                            }
                            if (data.has("link")) {
                                link = data.getString("link");
                            }
                            if (data.has("picture")) {
                                picture = data.getString("picture");
                            }
                            if (data.has("gender")) {
                                gender = data.getString("gender");
                            }
                            String json = "{\"id\":\"" + id + "\",\"email\":\"" + email + "\",\"verified_email\":\"" + verified_email + "\",\"name\":\"" + name + "\",\"given_name\":\"" + given_name + "\",\"family_name\":\"" + family_name + "\",\"link\":\"" + link + "\",\"picture\":\"" + picture + "\",\"gender\":\"" + gender + "\"}";
                            HttpPostRequest postRequest = new HttpPostRequest(null, new HttpPostRequest.TaskListener() {
                                @Override
                                public void onFinished(String result) {
                                    if (appActive) {
                                        HideSpinner();
                                        ParseUserLogin(result);
                                        if (mGoogleApiClient.isConnected()) {
                                            revokeAccess();
                                        }
                                    }
                                }
                            });
                            postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/oauth2_google/api/default.aspx?lang=fa", "json=" + json);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            getRequest.execute("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowCustomSpinner();
        }
    }

    public boolean LoginRequest(final String username, final String password, final boolean silent) throws ExecutionException, InterruptedException {
        ShowCustomSpinner();
        HttpPostRequest postRequest = new HttpPostRequest(null, new HttpPostRequest.TaskListener() {
            @Override
            public void onFinished(String result) {
                if (appActive) {
                    ParseUserLogin(result);
                }
            }
        });
        postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/api/json/login.aspx?lang=fa", "username=" + username + "&password=" + password);
        return true;
    }
    public void ParseUserLogin(String result) {
        try {
            JSONArray data = new JSONArray(result);
            if (data.length() != 0) {
                JSONObject usr = data.getJSONObject(0);
                if (usr.has("status")) {
                    if (usr.getString("status").equals("err")) {
                        HideSpinner();
                        Toast.makeText(this, R.string.login_error, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Classes.User user = classes.new User(usr.getString("username"), usr.getString("usertoken"), usr.getString("publictoken"), usr.getString("userfullname"), usr.getString("usermail"), usr.getString("usercell"), usr.getString("iscellvalid"), usr.getString("useravatar"), usr.getString("usergender"), usr.getString("usercity"), usr.getString("uvalid"), usr.getString("isguest"), usr.getString("logedin"), usr.getString("currencyid"), usr.getString("date_register"), usr.getString("date_lasttime"), 1, 0);
                    try {
                        LoginDataBaseHelper DBHelper = new LoginDataBaseHelper(this);
                        DBHelper.opendatabase();
                        DBHelper.addUser(user);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("USER_TOKEN", user.getPublictoken());
                        editor.apply();

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

    public void OpenSignupActivity() {
        if (methods.isInternetAvailable(this)) {
            HttpPostRequest postRequest = new HttpPostRequest(null, new HttpPostRequest.TaskListener() {
                @Override
                public void onFinished(String result) {
                    if (appActive) {
                        if (!result.equals("")) {
                            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                            intent.putExtra("data", result);
                            startActivityForResult(intent, SIGNUP_CODE);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            postRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/api/json/register.aspx", "");
        } else {
            ConsentDialog consentDialog = ConsentDialog.newInstance(getString(R.string.no_internet), getString(R.string.no_net_signup), getString(R.string.try_again), "");
            consentDialog.SetListener(new ConsentDialog.OnSetClickListener() {
                @Override
                public void onAccept() {
                    OpenSignupActivity();
                }

                @Override
                public void onReject() {

                }
            });
            consentDialog.show(getSupportFragmentManager(), "loginFragment");
        }
    }

    public void ShowCustomSpinner() {
        loader = new ProgressDialog(this);
        String message = getString(R.string.please_wait);
        SpannableString spannableString =  new SpannableString(message);

        CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan(message, Typeface.createFromAsset(getAssets(), "iranyekanregular.ttf"));
        spannableString.setSpan(customTypefaceSpan, 0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        loader.setMessage(spannableString);
        loader.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loader.setIndeterminate(true);
        loader.setCancelable(false);
        loader.show();
    }

    public void HideSpinner() {
        if (loader != null) {
            loader.hide();
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
