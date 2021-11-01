package com.envelopepushers.envote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText editEmail, editPassword;
    Button btnRegister;
    TextView signUp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);
        signUp = findViewById(R.id.signinLink);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editEmail.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    editEmail.setError("Password must be >= 6 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register user
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(SignUpActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignInActivity();
            }
        });
    }

    private void openSignInActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}