package com.locus.locusdemo.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.locus.locusdemo.R;
import com.locus.locusdemo.db.ValueDAO;
import com.locus.locusdemo.util.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    PrefUtils  prefUtils = new PrefUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        new Handler().postDelayed(() -> {
            if (!PrefUtils.getDataSaved(SplashActivity.this)) {
                ValueDAO.getInstance().deleteData();
                ValueDAO.getInstance().saveUserData();
            }
            Intent i = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(i);

            // close this activity
            finish();
        }, 3000);

    }
}
