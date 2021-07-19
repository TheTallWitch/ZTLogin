package com.gearback.zt.loginproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gearback.zt.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        OpenLogin();
    }


    public void OpenLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        Bundle args = new Bundle();
        args.putInt("header", R.drawable.ztapps);
        args.putInt("logo", R.drawable.icon_large);
        args.putString("privacy", "https://www.zodtond.com/apps/dhikr/fa/privacy-policy.htm");
        intent.putExtra("loginBundle", args);
        startActivityForResult(intent, 1000);
    }
}

