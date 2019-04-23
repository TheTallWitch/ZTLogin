package com.gearback.zt.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gearback.methods.Methods;
import com.gearback.zt.login.R;
import com.spournasseh.calendartool.CalendarTool;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class ExtendDialog extends DialogFragment {

    Methods methods = new Methods();
    CalendarTool tool = new CalendarTool();
    TextView priceText, priceUnit, untilText;
    Button payBtn;

    ImageView accImage;
    TextView accName, accUsername;

    public static ExtendDialog newInstance(int type, String appToken, String userToken, String userName, String userMail, String userAvatar) {
        ExtendDialog f = new ExtendDialog();
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
        View view = inflater.inflate(R.layout.extend_dialog, container, false);
        priceText = view.findViewById(R.id.priceText);
        priceUnit = view.findViewById(R.id.priceUnit);
        untilText = view.findViewById(R.id.untilText);
        accImage = view.findViewById(R.id.accImage);
        accName = view.findViewById(R.id.accName);
        accUsername = view.findViewById(R.id.accUsername);
        payBtn = view.findViewById(R.id.payBtn);

        accName.setText(getArguments().getString("userName"));
        accUsername.setText(getArguments().getString("userMail"));
        if (!getArguments().getString("userAvatar").equals("")) {
            Picasso.with(getActivity()).load(getString(R.string.ad_domain) + "app_upload/uploads/usermedia/" + getArguments().getString("userAvatar")).into(accImage);
        }
        else {
            accImage.setImageResource(R.drawable.defaultavatar);
        }

        SharedPreferences mPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        String price = mPrefs.getString("price", "0");
        String currency = mPrefs.getString("currency", "ریال");
        priceText.setText(methods.ReplaceNumber(price));
        priceUnit.setText(currency);

        String date = mPrefs.getString("noadsexpire", "");
        if (!date.equals("")) {
            Date dateObject = methods.StringDateToDate(date, Methods.DATEFORMAT2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObject);
            tool.setGregorianDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            tool.nextDay(365);
            untilText.setText(methods.ReplaceNumber(tool.getIranianStringShort()));
        }

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.ad_domain) + "app_upload/applications/appintro/pages/adremove.aspx?ud=" + getArguments().getString("userToken") + "&token=" + getArguments().getString("appToken");
                Log.d("url", url);
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
