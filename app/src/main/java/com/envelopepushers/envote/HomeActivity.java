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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    public ArrayList<EcoEmail> pastEmails = new ArrayList<>();
    final int MAX_BODY_PREVIEW = 50;

    Button logout;

    //Firebase
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Connect to Firebase
        database = FirebaseDatabase.getInstance().getReference("Emails");

//        logout = findViewById(R.id.btnLogout);
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount != null) {
//            name.setText(signInAccount.getDisplayName());
//        }
        getPastEmails();
        setBottomNavBar();
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
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void getPastEmails() {

        System.out.println("Starting firebase EcoEmails");

        // Read from the database
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pastEmails.add(snapshot.getValue(EcoEmail.class));
                pastEmails.add(snapshot.getValue(EcoEmail.class));
                System.out.println("Added Child");

                CardView noPastEmailsButton = findViewById(R.id.no_past_emails_button);

                if (pastEmails.isEmpty()) {
                    System.out.println("Setting NO emails");
                    noPastEmailsButton.setVisibility(View.VISIBLE);

                    noPastEmailsButton.setOnClickListener(view -> openMapActivity());
                } else {
                    System.out.println("Setting past emails");
                    ScrollView scrollView = findViewById(R.id.scroller);
                    scrollView.getLayoutParams().height = (int) convertDpToPixel(350, getApplicationContext());
                    generagePastEmailCards();
                }

                generagePastEmailCards();
                setBottomNavBar();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                pastEmails.add(snapshot.getValue(EcoEmail.class));
                System.out.println("Changed Child");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pastEmails.add(snapshot.getValue(EcoEmail.class));
                System.out.println("Moved Child");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get the EcoEmail objects

        //Set the objects to pastEmails

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
        startActivity(new Intent(this, IssueSelectActivity.class));
    }

    private void generagePastEmailCards() {

        for (EcoEmail pastEmail : pastEmails) {

            EcoIssue currentTopIssue = pastEmail.getIssue();

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
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }
}