package mota.dev.happytesting.Views.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mota.dev.happytesting.R;

public class SplashActivity extends AppCompatActivity {

    private static final long TIME_IN_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTheme(R.style.AppTheme);
        initDelay();
    }

    private void initDelay() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                goToLoginActivity();
            }
        }, TIME_IN_SPLASH);
    }

    private void goToLoginActivity()
    {
        Intent i = new Intent(SplashActivity.this,LoginActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(SplashActivity.this,
                                                                      android.R.anim.fade_in,
                                                                      android.R.anim.fade_out);
        startActivity(i,options.toBundle());
        finish();
    }
}
