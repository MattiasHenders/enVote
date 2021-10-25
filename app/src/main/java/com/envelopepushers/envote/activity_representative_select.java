package com.envelopepushers.envote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_representative_select extends AppCompatActivity {

    private Button btnSubmitLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_select);
        btnSubmitLocation = findViewById(R.id.nextPageButton);

        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmailActivity();
                finish();
            }
        });

        //setBottomNavBar();
    }


    private void setBottomNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_new) {
                    openMapActivity();
                    return true;
                }
                if (item.getItemId() == R.id.action_browse) {
                    openHomeActivity();
                    return true;
                }
                if (item.getItemId() == R.id.action_profile) {
                    openIssueActivity();
                    return true;
                }
                return false;
            }
        });
    }


    public void openEmailActivity() {
        Intent intent = new Intent(this, TemplateView.class);
        startActivity(intent);
        finish();
    }

    private void openMapActivity() {
        startActivity(new Intent(this, LocationSelectMap.class));
        finish();
    }

    private void openHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void openIssueActivity() {
        startActivity(new Intent(this, activity_issue_select.class));
        finish();
    }
}