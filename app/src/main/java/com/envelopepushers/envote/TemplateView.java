package com.envelopepushers.envote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class TemplateView extends AppCompatActivity {

    public TextView textSubject;
    public TextView textBody;
    public EcoIssue selectedIssue = new EcoIssue(EcoIssues.EMPTY);
    public Button btnSendEmail;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_view);

        //TextView setting objects
        textSubject = findViewById(R.id.text_email_subject);
        textBody = findViewById(R.id.text_email_body);

        Intent intent = getIntent();
        System.out.println(intent.getStringExtra("issue"));
        System.out.println(intent.getStringExtra("name"));
        System.out.println(intent.getStringExtra("email"));

        String selectedIssueKey = intent.getStringExtra("issue");

        selectedIssue = new EcoIssue(EcoIssues.valueOf(selectedIssueKey));

        EmailReceiver toSend = new EmailReceiver("contact@liberalparty.ca",
                "Justin Trudeau", EcoParty.LIBERAL);

        ArrayList<EmailReceiver> emailRecievers = new ArrayList<>();
        emailRecievers.add(toSend);

        String emailSubject = selectedIssue.getName() + " Issue";

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
                storeEmail();
            }
        });
    }

    private String getEmailStringFromTextFile(ArrayList<EmailReceiver> receivers) throws IOException {

        String string = "";
        StringBuilder stringBuilder = new StringBuilder();

        InputStream is = null;

        //Select the issue file based on issue
        switch (selectedIssue.getKey()) {
            case ("ELECTRIC"):
                is = this.getResources().openRawResource(R.raw.electric);
                break;

            case ("WATER"):
                is = this.getResources().openRawResource(R.raw.water);
                break;

            case ("TRASH"):
                is = this.getResources().openRawResource(R.raw.garbage);
                break;

            default:
                is = this.getResources().openRawResource(R.raw.air);
                break;
        }


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
        textBody.setText(emailBody + "\n"); //Give extra padding to the bottom
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

    private void storeEmail() {
        DatabaseReference myEmails = database.getReference("Emails");
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        String name = null;
        if (signInAccount != null) {
            name = signInAccount.getDisplayName();
        }
        EcoEmail email = new EcoEmail();
        email.addDeliveredTo(new EmailReceiver("bingbong@gmail.com", name, EcoParty.LIBERAL));
        email.setBody(textBody.toString());
        email.setDate(new Date());
        email.addEcoIssue(selectedIssue);
        myEmails.child("Emails").setValue(email);

    }
}