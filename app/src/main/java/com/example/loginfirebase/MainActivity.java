package com.example.loginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (auth.getCurrentUser() != null){
            welcomeActivity();
        }
    }

    public void welcomeActivity() {
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void registerActivity() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void signingActivity() {
        Intent intent = new Intent(MainActivity.this, SigningActivity.class);
        startActivity(intent);
    }

    public void onClickSignUp(View view) {
        registerActivity();
    }

    public void onClickSignIn(View view) {
        signingActivity();
    }
}
