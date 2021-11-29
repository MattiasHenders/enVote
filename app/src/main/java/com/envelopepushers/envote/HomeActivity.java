package com.envelopepushers.envote;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //Email card variables
    public ArrayList<EcoEmail> pastEmails = new ArrayList<>();
    final int MAX_BODY_PREVIEW = 100;

    Button logout;

    //Firebase variables
    DatabaseReference database;

    /**
     * Called on creation of the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Connect to Firebase
        String user;
        try {
            user = FirebaseAuth.getInstance().getCurrentUser().getUid();
            database = FirebaseDatabase.getInstance().getReference("users").child(user).child("emails");
        } catch (NullPointerException e) {
            Toast.makeText(this,"No current user signed in", Toast.LENGTH_SHORT).show();
        }

        //Get the past emails
        getPastEmails();

        //Generate the bottom navbar
        setBottomNavBar();
    }

    /**
     * Show the bottom navbar.
     */
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
            return false;
        });
    }

    /**
     * Access the firebase to find past emails
     */
    private void getPastEmails() {

        //If database is null
        CardView noPastEmailsButton = findViewById(R.id.no_past_emails_button);
        noPastEmailsButton.setVisibility(View.VISIBLE);
        noPastEmailsButton.setOnClickListener(view -> openMapActivity());

        // Read from the database
        database.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pastEmails.add(snapshot.getValue(EcoEmail.class));



                if (pastEmails.isEmpty()) {
                    noPastEmailsButton.setVisibility(View.VISIBLE);

                    noPastEmailsButton.setOnClickListener(view -> openMapActivity());
                } else {
                    noPastEmailsButton.setVisibility(View.GONE);

                    ScrollView scrollView = findViewById(R.id.scroller);
                    scrollView.getLayoutParams().height = (int)getResources().getDimension(R.dimen.scrollHeight);

                    //Add cards if emails are found
                    generatePastEmailCards();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Opens the Map activity after checking for permissions
     */
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

    /**
     * Generates the email cards dynamically
     */
    private void generatePastEmailCards() {

        //Remove child views first
        LinearLayout view = findViewById(R.id.container_past_emails);
        view.removeAllViews();

        //Loop through all emails
        for (EcoEmail pastEmail : pastEmails) {

            EcoIssue currentTopIssue = pastEmail.getIssue();

            LinearLayout cardHolder = new LinearLayout(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 0);
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

            ViewGroup.MarginLayoutParams cardViewMarginParams =
                    (ViewGroup.MarginLayoutParams) pastEmailCard.getLayoutParams();

            cardViewMarginParams.setMargins(
                    (int)getResources().getDimension(R.dimen.margin_small),
                    (int)getResources().getDimension(R.dimen.margin_small),
                    (int)getResources().getDimension(R.dimen.margin_small),
                    (int)getResources().getDimension(R.dimen.margin_small));

            pastEmailCard.requestLayout();

            LinearLayout cardHolderLayout = new LinearLayout(this);
            cardHolderLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams cardHolderLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardHolderLayout.setPadding(
                    (int) getResources().getDimension(R.dimen.padding_small),
                    (int) getResources().getDimension(R.dimen.padding_small),
                    (int) getResources().getDimension(R.dimen.padding_small),
                    (int) getResources().getDimension(R.dimen.padding_small));

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
            cardTitle.setTextSize((int)getResources().getDimension(R.dimen.dynamic_font_large));
            cardTitle.setTextColor(getColor(currentTopIssue.getColourLight()));

            //Set the sub-header as the date the email was sent
            TextView cardSubHeader = new TextView(this);
            cardSubHeader.setText(pastEmail.getDate());
            cardSubHeader.setTextSize((int)getResources().getDimension(R.dimen.dynamic_font_medium));
            cardSubHeader.setTextColor(getColor(currentTopIssue.getColourLight()));

            //Set the body as the first bit of the email
            TextView cardBody = new TextView(this);
            cardBody.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //Format the email body
            String body = pastEmail.getBody();
            if (body.length() > MAX_BODY_PREVIEW) {
                body = body.substring(0, MAX_BODY_PREVIEW);
                body += "...";
            }
            cardBody.setText(body);
            cardBody.setTextSize((int)getResources().getDimension(R.dimen.dynamic_font_small));
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
            LinearLayout view2 = findViewById(R.id.container_past_emails);
            view2.addView(cardHolder, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

        }
    }

    /**
     * Uses Google Auth to sign the user out
     */
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
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }
}