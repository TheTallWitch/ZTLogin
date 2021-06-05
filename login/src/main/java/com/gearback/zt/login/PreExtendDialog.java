package com.gearback.zt.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gearback.methods.Methods;
import com.spournasseh.calendartool.CalendarTool;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class PreExtendDialog extends DialogFragment {

    Methods methods = new Methods();
    TextView dateText, dialogText;
    Button extendBtn;

    ImageView accImage;
    TextView accName, accUsername;

    CalendarTool tool = new CalendarTool();

    public static PreExtendDialog newInstance(int type, String appToken, String userToken, String userName, String userMail, String userAvatar) {
        PreExtendDialog f = new PreExtendDialog();
        Bundle args = new Bundle();
        args.putInt("type", type);
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
        View view = inflater.inflate(R.layout.pre_extend_dialog, container, false);
        dateText = view.findViewById(R.id.dateText);
        dialogText = view.findViewById(R.id.dialogText);
        extendBtn = view.findViewById(R.id.extendBtn);
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

        SharedPreferences mPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        String date = mPrefs.getString("noadsexpire", "");
        if (!date.equals("")) {
            Date dateObject = methods.StringDateToDate(date, Methods.DATEFORMAT2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObject);
            tool.setGregorianDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            dateText.setText(methods.ReplaceNumber(tool.getIranianStringLong()));
        }

        if (getArguments().getInt("type") == 0) {
            CalendarTool nowTool = new CalendarTool();
            dialogText.setText(getString(R.string.extend_desc, methods.ReplaceNumber(String.valueOf(tool.dateDifferenceInDays(tool.getIranianDate(), nowTool.getIranianDate())))));
        }
        else {
            dialogText.setText(getString(R.string.removed_desc));
        }

        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExtendDialog extendDialog = ExtendDialog.newInstance(getArguments().getInt("type"), getArguments().getString("appToken"), getArguments().getString("userToken"), getArguments().getString("userName"), getArguments().getString("userMail"), getArguments().getString("userAvatar"));
                extendDialog.show(getActivity().getSupportFragmentManager(), "extendDialog");
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
