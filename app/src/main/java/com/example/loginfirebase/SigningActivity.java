package com.example.loginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SigningActivity extends AppCompatActivity {

    public String emailText, passwordText;
    private EditText email, password;
    private Button login;
    private FirebaseAuth auth = null;
    boolean loging = false;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);

        auth = FirebaseAuth.getInstance();
    }

    public void logIn(View view) throws Exception {
        emailText = email.getText().toString();
        passwordText = password.getText().toString();

        if (checkText() == false) {
            String encryptPassword = encrypt(emailText, passwordText);
            loginUser(emailText, encryptPassword);
        }
    }

    public boolean checkText() {
        boolean empty = false;
        emailText = email.getText().toString();
        passwordText = password.getText().toString();
        if (emailText.equals("") || passwordText.equals("")
                || passwordText.length() < 6 || emailText.contains("@") || emailText.length() < 15) {
            if (emailText.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter Your Email!", Toast.LENGTH_LONG).show();
                empty = true;
            } else if (passwordText.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter Password!", Toast.LENGTH_LONG).show();
                empty = true;
            }
        } else if (passwordText.length() < 6) {
            Toast.makeText(getApplicationContext(), "Please Password must be at least 6 letters!", Toast.LENGTH_LONG).show();
            empty = true;
        } else if (emailText.contains("@") || emailText.length() < 15) {
            Toast.makeText(getApplicationContext(), "Please Enter Correct Email!", Toast.LENGTH_LONG).show();
            empty = true;

        }
        return empty;
    }

    public void welcomeActivity() {
        Intent intent = new Intent(SigningActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verifyUser();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed : " + task.getException().getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void verifyUser() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser.isEmailVerified()) {
            welcomeActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Please verify Your Account", Toast.LENGTH_LONG).show();
        }
    }

    private String encrypt(String username, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(username.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes();
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
