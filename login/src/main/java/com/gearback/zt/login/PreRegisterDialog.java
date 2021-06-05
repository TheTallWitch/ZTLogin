package com.gearback.zt.login;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.gearback.methods.Methods;
import com.squareup.picasso.Picasso;

public class PreRegisterDialog extends DialogFragment {

    Methods methods = new Methods();
    ImageView accImage;
    TextView accName, accUsername;

    public static PreRegisterDialog newInstance(String appToken, String userToken, String userName, String userMail, String userAvatar) {
        PreRegisterDialog f = new PreRegisterDialog();
        Bundle args = new Bundle();
        args.putString("appToken", appToken);
        args.putString("userToken", userToken);
        args.putString("userName", userName);
        args.putString("userMail", userMail);
        args.putString("userAvatar", userAvatar);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pre_register_dialog, container, false);
        accImage = view.findViewById(R.id.accImage);
        accName = view.findViewById(R.id.accName);
        accUsername = view.findViewById(R.id.accUsername);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.fade_animation;

        accName.setText(getArguments().getString("userName"));
        accUsername.setText(getArguments().getString("userMail"));
        if (!getArguments().getString("userAvatar").equals("")) {
            Picasso.with(getActivity()).load(getString(R.string.ad_domain) + "app_upload/uploads/usermedia/" + getArguments().getString("userAvatar")).into(accImage);
        }
        else {
            accImage.setImageResource(R.drawable.defaultavatar);
        }

        view.findViewById(R.id.setDialogBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDialog registerDialog = RegisterDialog.newInstance(getArguments().getString("appToken"), getArguments().getString("userToken"), getArguments().getString("userName"), getArguments().getString("userMail"), getArguments().getString("userAvatar"));
                registerDialog.show(getActivity().getSupportFragmentManager(), "registerDialog");
                dismiss();
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
