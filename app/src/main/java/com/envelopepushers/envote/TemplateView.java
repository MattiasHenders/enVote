package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TemplateView extends AppCompatActivity {

    public TextView textEmailTo;
    public TextView textSubject;
    public TextView textBody;

    public Button btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_view);

        //TextView setting objects
        textEmailTo = findViewById(R.id.text_email_to_line);
        textSubject = findViewById(R.id.text_email_subject);
        textBody = findViewById(R.id.text_email_body);

        setEmailTemplate();

        btnSendEmail = findViewById(R.id.btn_send_email);


        //OnClickListeners Set
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Sets the content in the email template.
     */
    private void setEmailTemplate() {

        textEmailTo.setText(getString(R.string.email_to) + " PM Mattias");
        textSubject.setText(getString(R.string.email_subject) + " Fix the Water!");
        textBody.setText(getString(R.string.filler_text) + getString(R.string.filler_text));

    }
}