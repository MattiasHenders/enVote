package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_issue_select extends AppCompatActivity {

    private Button btnSubmitLocation;
    TextView textView1;
    Button showMore1;
    TextView textView2;
    Button showMore2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_select);
        btnSubmitLocation = findViewById(R.id.nextPageButton);

        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });

        textView1=(TextView)findViewById(R.id.textView1);
        showMore1=(Button)findViewById(R.id.showMore1);
        showMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (showMore1.getText().toString().equalsIgnoreCase("Showmore..."))
                {
                    textView1.setMaxLines(Integer.MAX_VALUE);//your TextView
                    showMore1.setText("Showless");
                }
                else
                {
                    textView1.setMaxLines(3);//your TextView
                    showMore1.setText("Showmore...");
                }
            }
        });

        textView2=(TextView)findViewById(R.id.textView2);
        showMore2=(Button)findViewById(R.id.showMore2);
        showMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (showMore2.getText().toString().equalsIgnoreCase("Showmore..."))
                {
                    textView2.setMaxLines(Integer.MAX_VALUE);//your TextView
                    showMore2.setText("Showless");
                }
                else
                {
                    textView2.setMaxLines(3);//your TextView
                    showMore2.setText("Showmore...");
                }
            }
        });
    }
    public void openActivity() {
        Intent intent = new Intent(this, activity_representative_select.class);
        startActivity(intent);
    }
}