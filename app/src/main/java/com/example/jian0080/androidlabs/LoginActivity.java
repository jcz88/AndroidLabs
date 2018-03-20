package com.example.jian0080.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

            SharedPreferences prefs = getApplicationContext().getSharedPreferences("login_email_file", MODE_PRIVATE);
            final SharedPreferences.Editor edit = prefs.edit();
            final EditText loginName = findViewById(R.id.login_text_input);
            loginName.setText(prefs.getString("DefaultEmail", "email@domain.com"), TextView.BufferType.NORMAL);
            final Button loginButton = findViewById(R.id.button_login);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edit.putString("DefaultEmail",loginName.getText().toString());
                    edit.commit();
                    Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                    startActivity(intent);
                }
            });
        Log.i(ACTIVITY_NAME, "In onCreate()");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
