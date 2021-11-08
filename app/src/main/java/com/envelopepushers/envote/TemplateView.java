package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.stmt.query.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TemplateView extends AppCompatActivity {

    public TextView textSubject;
    public TextView textBody;

    public Button btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_view);



        //TextView setting objects
        textSubject = findViewById(R.id.text_email_subject);
        textBody = findViewById(R.id.text_email_body);

        //EcoEmail newEmail = getIntent().getParcelableExtra("ecoEmail");

        //Get the String from the email Object and pull info to the template
//        ArrayList<EmailReceiver> emailRecievers = newEmail.getDeliveredTo();

        EmailReceiver toSend = new EmailReceiver("contact@liberalparty.ca",
                "Justin Trudeau", EcoParty.LIBERAL);

        ArrayList<EmailReceiver> emailRecievers = new ArrayList<>();
        emailRecievers.add(toSend);

        String emailSubject = "Air Quality Issue";

//        String emailTo = emailRecievers.get(0).getEmail();
//        String emailSubject = newEmail.getSubject();
//        String emailBody = newEmail.getBody();

        String rawEmailBody = "";
        try {
            rawEmailBody = getEmailStringFromTextFile(emailRecievers);
        } catch (IOException ioe) {
            System.out.println("ERROR: IO");
        }

        final String emailBody = rawEmailBody;

        setEmailTemplate(emailRecievers.get(0).getEmail(), emailSubject, emailBody);

        btnSendEmail = findViewById(R.id.btn_send_email);

        //OnClickListeners Set
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmailIntent(emailRecievers, emailSubject, emailBody);
            }
        });
    }

    private String getEmailStringFromTextFile(ArrayList<EmailReceiver> receivers) throws IOException {

        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStream is = this.getResources().openRawResource(R.raw.air);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(string).append("\n");
        }
        is.close();

        String rawEmail = stringBuilder.toString();

        rawEmail = rawEmail.replace("[REP NAME]", receivers.get(0).getFullName());
        rawEmail = rawEmail.replace("[REP PARTY]", receivers.get(0).getParty().getPartyName());
        rawEmail = rawEmail.replace("[SENDER NAME]", "Mattias Henders");

        return rawEmail;
    }

    /**
     * Sets the content in the email template.
     */
    private void setEmailTemplate(String emailTo, String emailSubject, String emailBody) {

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
        startActivity(new Intent(this, activity_issue_select.class));
        finish();
    }
}