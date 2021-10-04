package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, TemplateView.class);
        startActivity(i);

        Intent i2 = new Intent(this, LocationSelectMap.class);
        startActivity(i2);

        Intent i3 = new Intent(this, HomeActivity.class);

        Intent i4 = new Intent(this, LoginActivity.class);
        Intent i5 = new Intent(this, SignUpActivity.class);
        startActivity(i5);
    }
}