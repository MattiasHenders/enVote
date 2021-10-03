package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_representative_and_issue_confirmation extends AppCompatActivity {
    private Button btnSubmitLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_and_issue_confirmation);
        btnSubmitLocation = findViewById(R.id.nextPageButton);

        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });
    }
    public void openActivity() {
        Intent intent = new Intent(this, LocationSelectMap.class);
        startActivity(intent);
    }
}