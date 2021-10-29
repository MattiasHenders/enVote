package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;

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

        EcoEmail newEmail = getIntent().getParcelableExtra("ecoEmail");

        //Get the String from the email Object and pull info to the template
        ArrayList<EmailReceiver> emailRecievers = newEmail.getDeliveredTo();
        String emailTo = emailRecievers.get(0).getEmail();
        String emailSubject = newEmail.getSubject();
        String emailBody = newEmail.getBody();

        setEmailTemplate(emailTo, emailSubject, emailBody);

        btnSendEmail = findViewById(R.id.btn_send_email);

        //OnClickListeners Set
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmailIntent(emailRecievers, emailSubject, emailBody);
            }
        });
    }

    /**
     * Sets the content in the email template.
     */
    private void setEmailTemplate(String emailTo, String emailSubject, String emailBody) {

        textEmailTo.setText(emailTo);
        textSubject.setText(emailSubject);
        textBody.setText(emailBody);
    }

    private void startEmailIntent(ArrayList<EmailReceiver> receivers, String subject, String body) {

        //Get String array of who to send to
        String[] to = new String[receivers.size()];
        for (int i = 0; i < receivers.size(); i++) {
            to[i] = receivers.get(i).getEmail();
        }

        //Send the email using local browser
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, to);
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, body);

        //Need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
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
        startActivity(new Intent(this, IssueSelectActivity.class));
        finish();
    }
}