package com.gearback.zt.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import com.gearback.methods.Methods;

public class LoginRegisterDialog extends DialogFragment {

    private static int LOGIN_CODE = 77;
    Methods methods = new Methods();
    Button loginBtn;

    public static LoginRegisterDialog newInstance(int header, int logo) {
        LoginRegisterDialog f = new LoginRegisterDialog();
        Bundle args = new Bundle();
        args.putInt("header", header);
        args.putInt("logo", logo);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_register_dialog, container, false);
        loginBtn = view.findViewById(R.id.loginBtn);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.fade_animation;

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                OpenLoginActivity();
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

    public void OpenLoginActivity() {
        if (methods.isInternetAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            Bundle args = new Bundle();
            args.putInt("header", getArguments().getInt("header"));
            args.putInt("logo", getArguments().getInt("logo"));
            intent.putExtra("loginBundle", args);
            startActivityForResult(intent, LOGIN_CODE);
        }
        else {
            ConsentDialog consentDialog = ConsentDialog.newInstance(getString(R.string.no_internet), getString(R.string.no_internet_desc), getString(R.string.try_again), "");
            consentDialog.SetListener(new ConsentDialog.OnSetClickListener() {
                @Override
                public void onAccept() {
                    OpenLoginActivity();
                }

                @Override
                public void onReject() {

                }
            });
            consentDialog.show(getActivity().getSupportFragmentManager(), "consentDialog");
        }
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
