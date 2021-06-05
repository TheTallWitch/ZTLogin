package com.gearback.zt.login;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gearback.methods.Methods;

public class LoginPasswordSentDialog extends DialogFragment {
    Methods methods = new Methods();
    TextView continueBtn, emailValue;

    public static LoginPasswordSentDialog newInstance(String email) {
        LoginPasswordSentDialog f = new LoginPasswordSentDialog();
        Bundle args = new Bundle();
        args.putString("email", email);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_password_sent_dialog, container, false);
        continueBtn = view.findViewById(R.id.setDialogBtn);
        emailValue = view.findViewById(R.id.emailValue);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.fade_animation;

        emailValue.setText(getArguments().getString("email"));

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
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
