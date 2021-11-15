package com.envelopepushers.envote;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    public ArrayList<EcoEmail> pastEmails = new ArrayList<>();
    final int MAX_BODY_PREVIEW = 50;

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        logout = findViewById(R.id.btnLogout);
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount != null) {
//            name.setText(signInAccount.getDisplayName());
//        }

        CardView noPastEmailsButton = findViewById(R.id.no_past_emails_button);

        EcoEmail testEmail0 = new EcoEmail();
        testEmail0.addDeliveredTo(
                new EmailReceiver("mattias@gmail.com", "Mattias Henders", EcoParty.NDP));
        testEmail0.setBody("According to all known laws of aviation there is no way a bee should be able to fly.");
        testEmail0.setDate(new Date());
        testEmail0.addEcoIssue(new EcoIssue(EcoIssues.WATER));

        EcoEmail testEmail1 = new EcoEmail();
        testEmail1.addDeliveredTo(
                new EmailReceiver("mattias@gmail.com", "Mattias Henders", EcoParty.NDP));
        testEmail1.setBody("According to all known laws of aviation there is no way a bee should be able to fly.");
        testEmail1.setDate(new Date());
        testEmail1.addEcoIssue(new EcoIssue(EcoIssues.AIR));

        EcoEmail testEmail2 = new EcoEmail();
        testEmail2.addDeliveredTo(
                new EmailReceiver("mattias@gmail.com", "Mattias Henders", EcoParty.NDP));
        testEmail2.setBody("According to all known laws of aviation there is no way a bee should be able to fly.");
        testEmail2.setDate(new Date());
        testEmail2.addEcoIssue(new EcoIssue(EcoIssues.TRASH));

        EcoEmail testEmail3 = new EcoEmail();
        testEmail3.addDeliveredTo(
                new EmailReceiver("mattias@gmail.com", "Mattias Henders", EcoParty.NDP));
        testEmail3.setBody("According to all known laws of aviation there is no way a bee should be able to fly.");
        testEmail3.setDate(new Date());
        testEmail3.addEcoIssue(new EcoIssue(EcoIssues.ELECTRIC));
        pastEmails.add(testEmail0);
        pastEmails.add(testEmail1);
        pastEmails.add(testEmail2);
        pastEmails.add(testEmail3);

        setBottomNavBar();

        if (pastEmails.size() == 0) {
            noPastEmailsButton.setVisibility(View.VISIBLE);

            noPastEmailsButton.setOnClickListener(view -> openMapActivity());
        } else {
            generagePastEmailCards();
        }

    }

    private void setBottomNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_new) {
                openMapActivity();
                return true;
            }
            if (item.getItemId() == R.id.action_browse) {
                signOut();
                return true;
            }
            if (item.getItemId() == R.id.action_profile) {
                openProfileActivity();
                return true;
            }
            return false;
        });
    }

    private void openMapActivity() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(this, LocationSelectMap.class));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 8);
            Toast.makeText(this, "Can't access map", Toast.LENGTH_LONG).show();
        }
    }

    private void openHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void openProfileActivity() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void openIssueActivity() {
        startActivity(new Intent(this, activity_issue_select.class));
    }

    private void generagePastEmailCards() {

        for (EcoEmail pastEmail : pastEmails) {

            EcoIssue currentTopIssue = pastEmail.getEcoIssues().get(0);

            LinearLayout cardHolder = new LinearLayout(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 30);
            cardHolder.setLayoutParams(layoutParams);

            //Create the individual card
            CardView pastEmailCard = new CardView(this);

            //Set the Card params
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            pastEmailCard.setRadius(
                    (int) getResources().getDimension(R.dimen.borderRadius_medium));
            pastEmailCard.setLayoutParams(cardLayoutParams);

            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) pastEmailCard.getLayoutParams();
            cardViewMarginParams.setMargins(10, 30, 50, 20);
            pastEmailCard.requestLayout();

            LinearLayout cardHolderLayout = new LinearLayout(this);
            cardHolderLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams cardHolderLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardHolderLayout.setPadding(
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    (int) getResources().getDimension(R.dimen.padding_medium));

            cardHolderLayout.setBackgroundColor(getColor(currentTopIssue.getColourDark()));
            cardHolderLayout.setLayoutParams(cardHolderLayoutParams);

            //Set the Header Layout params and layout
            LinearLayout headerLayout = new LinearLayout(this);
            LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);
            headerLayout.setOrientation(LinearLayout.HORIZONTAL);
            headerLayout.setLayoutParams(headerLayoutParams);

            //Set the Header Text layout and params
            LinearLayout headerTextLayout = new LinearLayout(this);
            LinearLayout.LayoutParams headerTextLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            headerTextLayout.setOrientation(LinearLayout.VERTICAL);
            headerTextLayout.setLayoutParams(headerTextLayoutParams);

            //Set the email TO name as the title
            TextView cardTitle = new TextView(this);
            cardTitle.setText(pastEmail.getDeliveredTo().get(0).getFullName());
            cardTitle.setTextSize(24);
            cardTitle.setTextColor(getColor(currentTopIssue.getColourLight()));

            //Set the sub-header as the date the email was sent
            TextView cardSubHeader = new TextView(this);
            cardSubHeader.setText(pastEmail.getDate());
            cardSubHeader.setTextSize(22);
            cardSubHeader.setTextColor(getColor(currentTopIssue.getColourLight()));

            //Set the body as the first bit of the email
            TextView cardBody = new TextView(this);
            cardBody.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //Format the email body
            StringBuilder body = new StringBuilder(pastEmail.getBody());
            body.append("...");
            while (body.length() <= MAX_BODY_PREVIEW) {
                body.append(" ");
            }
            cardBody.setText(body.substring(0, MAX_BODY_PREVIEW));
            cardBody.setText(body);
            cardBody.setTextSize(14);
            cardBody.setTextColor(getColor(currentTopIssue.getColourLight()));

            //Set the image as the icon for the issue
            ImageView cardIcon = new ImageView(this);
            LinearLayout.LayoutParams cardIconLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardIconLayoutParams.setMarginStart((int) getResources().getDimension(R.dimen.margin_xxxLarge));
            cardIcon.setLayoutParams(cardIconLayoutParams);

            //Get the first EcoIssue icon and color
            cardIcon.setImageIcon(Icon.createWithResource(this, currentTopIssue.getIcon()));
            cardIcon.setMinimumHeight((int) getResources().getDimension(R.dimen.imgSize_small));
            cardIcon.setMinimumWidth((int) getResources().getDimension(R.dimen.imgSize_small));
            cardIcon.setImageTintList(ColorStateList.valueOf(getColor(currentTopIssue.getColourLight())));

            //Add items in reverse order to the layout holder
            headerTextLayout.addView(cardTitle);
            headerTextLayout.addView(cardSubHeader);
            headerLayout.addView(headerTextLayout);
            headerLayout.addView(cardIcon);
            cardHolderLayout.addView(headerLayout);
            cardHolderLayout.addView(cardBody);

            //Add the filled Linear Layout to the full card
            pastEmailCard.addView(cardHolderLayout);

            cardHolder.addView(pastEmailCard);

            //Add card to the layout that holds the past trips
            LinearLayout view = findViewById(R.id.container_past_emails);
            view.addView(cardHolder, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

        }
    }
    private void signOut() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }
}