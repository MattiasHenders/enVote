package com.envelopepushers.envote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
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

        String selectedIssueKey = intent.getStringExtra("issue");
        String receiverName = intent.getStringExtra("name");
        String partyName = intent.getStringExtra("party");
        String receiverEmail = intent.getStringExtra("email");

        selectedIssue = new EcoIssue(EcoIssues.valueOf(selectedIssueKey));

        EcoParty ecoParty;
        //Check for valid party
        try {
            ecoParty = EcoParty.valueOf(partyName);
        } catch (Exception e) {
            ecoParty = EcoParty.INDEPENDENT;
        }

        EmailReceiver toSend = new EmailReceiver(receiverEmail,
                receiverName, ecoParty);

        ArrayList<EmailReceiver> emailRecievers = new ArrayList<>();
        emailRecievers.add(toSend);

        String emailSubject = selectedIssue.getName() + " Issue";

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        String userName = "A concerned citizen";
        if (signInAccount != null) {
            userName = signInAccount.getDisplayName();
        }

        String rawEmailBody = "";
        try {
            rawEmailBody = getEmailStringFromTextFile(emailRecievers, userName);
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
                storeEmail(emailRecievers);
            }
        });
    }

    /**
     * Grabs the appropriate email template based on the issue.
     *
     * @param receivers as ArrayList<EmailReceiver>
     * @param userName as String
     * @return rawEmail as String
     * @throws IOException
     */
    private String getEmailStringFromTextFile(ArrayList<EmailReceiver> receivers, String userName) throws IOException {

        String string = "";
        StringBuilder stringBuilder = new StringBuilder();

        InputStream is = null;

        //Select the issue file based on issue
        switch (selectedIssue.getKey()) {
            case ("EMISSION"):
                is = this.getResources().openRawResource(R.raw.emissions);
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
        if (receivers.get(0).getParty() == EcoParty.INDEPENDENT) {
            rawEmail = rawEmail.replace("the [REP PARTY]", "you");

        } else {
            rawEmail = rawEmail.replace("[REP PARTY]", receivers.get(0).getParty().getPartyName());
        }
        rawEmail = rawEmail.replace("[SENDER NAME]", userName);

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

        finish();
    }

    /**
     * Writes the email into the firebase realtime database
     * @param receivers as ArrayList<EmailReceiver>
     */
    private void storeEmail(ArrayList<EmailReceiver> receivers) {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        String uid = signInAccount.getId();
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myEmails = database.getReference().child("users").child(user).child("emails");
        EcoEmail email = new EcoEmail();
        email.setDeliveredTo(receivers);
        email.setBody(textBody.getText().toString());
        email.setDate(new Date().toString());
        email.setIssue(selectedIssue);
        myEmails.push().setValue(email);
    }
}