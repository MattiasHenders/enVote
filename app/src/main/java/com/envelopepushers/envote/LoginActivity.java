package com.envelopepushers.envote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button submitLogin = findViewById(R.id.login);
        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });
        TextView signUp = findViewById(R.id.signupLink);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openSignUpActivity();}
        });
    }

    private void openHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void openSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
}