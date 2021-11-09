package com.envelopepushers.envote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_issue_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_select);

        //textView2.setMaxLines(3);//your TextView




    }

    public void openRepActivity() {
        Intent intent = new Intent(this, activity_representative_select.class);
        startActivity(intent);
        finish();
    }
}