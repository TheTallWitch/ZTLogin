package com.gearback.zt.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.TextView;

import com.gearback.methods.Methods;
import com.spournasseh.calendartool.CalendarTool;

import java.util.Calendar;
import java.util.Date;

public class ExpireDialog extends DialogFragment {

    Methods methods = new Methods();
    TextView dateText, dialogText;
    Button extendBtn;

    public static ExpireDialog newInstance(int days, String userToken) {
        ExpireDialog f = new ExpireDialog();
        Bundle args = new Bundle();
        args.putInt("days", days);
        args.putString("userToken", userToken);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expire_dialog, container, false);
        dateText = view.findViewById(R.id.dateText);
        dialogText = view.findViewById(R.id.dialogText);
        extendBtn = view.findViewById(R.id.extendBtn);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.fade_animation;

        if (getArguments().getInt("days") == 0) {
            dialogText.setText(getString(R.string.expired_desc));
        }
        else {
            dialogText.setText(getString(R.string.expire_desc, methods.ReplaceNumber(String.valueOf(getArguments().getInt("days")))));
        }

        SharedPreferences mPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        String date = mPrefs.getString("noadsexpire", "");
        if (!date.equals("")) {
            Date dateObject = methods.StringDateToDate(date, Methods.DATEFORMAT2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObject);
            CalendarTool tool = new CalendarTool();
            tool.setGregorianDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            dateText.setText(methods.ReplaceNumber(tool.getIranianStringLong()));
        }

        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.ad_domain) + "app_upload/applications/appintro/pages/adremove.aspx?ud=" + getArguments().getString("userToken");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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
