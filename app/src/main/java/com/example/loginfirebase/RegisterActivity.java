package com.example.loginfirebase;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    public String nameText, emailText, passwordText;
    private Button facebook, twitter, register;
    private ImageButton visibility;
    private EditText name, email, password;
    private TextView singin;
    private FirebaseAuth auth = null;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        facebook = findViewById(R.id.facebook_button);
        twitter = findViewById(R.id.twitter_button);
        register = findViewById(R.id.register_button);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        singin = findViewById(R.id.sign_in_editText);
//        visibility = findViewById(R.id.visibility);

        auth = FirebaseAuth.getInstance();

    }


    public void onClickSignIn(View view) {
        Intent intent = new Intent(RegisterActivity.this, SigningActivity.class);
        startActivity(intent);
    }

    public void registerPerson(View view) throws Exception {
        nameText = name.getText().toString();
        emailText = email.getText().toString();
        passwordText = password.getText().toString();

        if (checkText() == false) {
            String encryptPassword = encrypt(emailText, passwordText);
            addUser(emailText, encryptPassword);
        }

    }

    public boolean checkText() {
        boolean empty = false;
        nameText = name.getText().toString();
        emailText = email.getText().toString();
        passwordText = password.getText().toString();
        if (nameText.equals("") || emailText.equals("") || passwordText.equals("")
                || passwordText.length() < 6 || emailText.contentEquals("@") || emailText.length() < 15 || nameText.length() < 3) {
            if (nameText.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter Your Name!", Toast.LENGTH_LONG).show();
                empty = true;
            } else if (emailText.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter Your Email!", Toast.LENGTH_LONG).show();
                empty = true;
            } else if (passwordText.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter Password!", Toast.LENGTH_LONG).show();
                empty = true;
            } else if (passwordText.length() < 6) {
                Toast.makeText(getApplicationContext(), "Please Password must be at least 6 letters!", Toast.LENGTH_LONG).show();
                empty = true;
            } else if (emailText.contains("@") || emailText.length() < 15) {
                Toast.makeText(getApplicationContext(), "Please Enter Correct Email!", Toast.LENGTH_LONG).show();
                empty = true;
            } else if (nameText.length() < 3) {
                Toast.makeText(getApplicationContext(), "Please Name must be at least 3 letters!", Toast.LENGTH_LONG).show();
                empty = true;
            }
        }
        return empty;
    }

    public void addUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_LONG).show();
                            sendEmailVerification();
                        } else {
                            Toast.makeText(getApplicationContext(), "Register Failed : " + task.getException().getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void sendEmailVerification() {
        FirebaseUser currentUser = auth.getCurrentUser();
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Check Your Email For Verification",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Register Failed : " + task.getException().getMessage()
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
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
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
        return secretKeySpec;
    }

//    public void visibility(View view) {
//        Drawable drawable = visibility.getDrawable().getCurrent();
//        if (drawable == getResources().getDrawable(R.drawable.ic_visibility)) {
//            visibility.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off));
//        }if (drawable == getResources().getDrawable(R.drawable.ic_visibility_off)){
//            visibility.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility));
//        }
//
//    }
}
