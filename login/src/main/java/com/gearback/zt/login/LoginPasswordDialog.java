package com.gearback.zt.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gearback.methods.HttpPostRequest;
import com.gearback.methods.Methods;
import com.gearback.zt.login.LoginActivity;
import com.gearback.zt.login.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class LoginPasswordDialog extends DialogFragment {
    Methods methods = new Methods();
    EditText emailValue;
    TextView continueBtn, emailEmpty, emailResult;

    public static LoginPasswordDialog newInstance() {
        LoginPasswordDialog f = new LoginPasswordDialog();
        Bundle args = new Bundle();

        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_password_dialog, container, false);
        continueBtn = view.findViewById(R.id.setDialogBtn);
        emailValue = view.findViewById(R.id.emailValue);
        emailEmpty = view.findViewById(R.id.emailEmpty);
        emailResult = view.findViewById(R.id.emailResult);

        emailEmpty.setVisibility(View.GONE);
        emailResult.setVisibility(View.GONE);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = String.valueOf(emailValue.getText());
                if (email.equals("")) {
                    emailEmpty.setVisibility(View.VISIBLE);
                }
                else {
                    emailEmpty.setVisibility(View.GONE);
                    boolean sendEmail = false;

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String lastTime = preferences.getString("PASSWORD_REFRESH_TIME", "");
                    if (lastTime.equals("")) {
                        sendEmail = true;
                    }
                    else {
                        Date date = methods.StringDateToDate(lastTime, Methods.DATEFORMAT2);
                        Calendar calendar = Calendar.getInstance();
                        Calendar nowCalendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.MINUTE, 15);
                        if (nowCalendar.getTime().after(calendar.getTime())) {
                            sendEmail = true;
                        }
                    }

                    if (sendEmail) {
                        ((LoginActivity)getActivity()).ShowCustomSpinner();
                        HttpPostRequest httpPostRequest = new HttpPostRequest(getActivity(), new HttpPostRequest.TaskListener() {
                            @Override
                            public void onFinished(String result) {
                                ((LoginActivity)getActivity()).HideSpinner();
                                try {
                                    JSONArray mainArray = new JSONArray(result);
                                    JSONObject mainObject = mainArray.getJSONObject(0);
                                    if (mainObject.getString("status").equals("1")) {
                                        emailResult.setVisibility(View.GONE);

                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("PASSWORD_REFRESH_TIME", methods.calendarToString(Calendar.getInstance(), Methods.DATEFORMAT2));
                                        editor.apply();

                                        LoginPasswordSentDialog loginPasswordSentDialog = LoginPasswordSentDialog.newInstance(email);
                                        loginPasswordSentDialog.show(getActivity().getSupportFragmentManager(), "loginPasswordSentDialog");

                                        dismiss();
                                    }
                                    else {
                                        emailResult.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        httpPostRequest.execute(getString(R.string.ad_domain) + "app_upload/applications/users/api/json/forget.aspx", "usermail=" + email);
                    }
                    else {
                        Toast.makeText(getActivity(), getString(R.string.password_sent_recently), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        view.findViewById(R.id.closeDialogBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.fade_animation;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window win = getDialog().getWindow();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.widthPixels * 0.99);
        if (methods.isTablet(getActivity())) {
            width = (int) (displaymetrics.widthPixels * 0.75);
        }
        win.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.CENTER);
    }
}
