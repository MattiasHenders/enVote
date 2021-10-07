package com.envelopepushers.envote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.j256.ormlite.stmt.query.In;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    public ArrayList<EcoEmail> pastEmails = new ArrayList<>();
    final int MAX_BODY_PREVIEW = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CardView noPastEmailsButton = findViewById(R.id.no_past_emails_button);

        EcoEmail testEmail = new EcoEmail();
        testEmail.addDeliveredTo(
                new EmailReceiver("mattias@gmail.com", "Mattias Henders", "M Party"));
        testEmail.setBody("According to all known laws of aviation there is no way a bee should be able to fly.");
        testEmail.setDate(new Date());
        pastEmails.add(testEmail);
        pastEmails.add(testEmail);
        pastEmails.add(testEmail);
        pastEmails.add(testEmail);
        pastEmails.add(testEmail);

        if (pastEmails.size() == 0) {
            noPastEmailsButton.setVisibility(View.VISIBLE);

            noPastEmailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openMapActivity();
                }
            });
        } else {
            generagePastEmailCards();
        }

        setBottomNavBar();
    }

    private void setBottomNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_new) {
                    openMapActivity();
                    return true;
                }
                if (item.getItemId() == R.id.action_browse) {
                    openHomeActivity();
                    return true;
                }
                if (item.getItemId() == R.id.action_profile) {
                    openIssueActivity();
                    return true;
                }
                return false;
            }
        });
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

    private void generagePastEmailCards() {

        for (EcoEmail pastEmail : pastEmails) {


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

            cardHolderLayout.setBackgroundColor(getColor(R.color.dull_grey));
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
            cardTitle.setTextColor(getColor(R.color.dark_grey));

            //Set the sub-header as the date the email was sent
            TextView cardSubHeader = new TextView(this);
            cardSubHeader.setText(pastEmail.getDate());
            cardSubHeader.setTextSize(22);
            cardSubHeader.setTextColor(getColor(R.color.dark_grey));

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
            cardBody.setTextColor(getColor(R.color.dark_grey));

            //Set the image as the icon for the issue
            ImageView cardIcon = new ImageView(this);
            LinearLayout.LayoutParams cardIconLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardIconLayoutParams.setMarginStart((int) getResources().getDimension(R.dimen.margin_xxxLarge));
            cardIcon.setLayoutParams(cardIconLayoutParams);

            cardIcon.setImageIcon(Icon.createWithResource(this, R.drawable.ic_baseline_eco_24));
            cardIcon.setMinimumHeight((int) getResources().getDimension(R.dimen.imgSize_small));
            cardIcon.setMinimumWidth((int) getResources().getDimension(R.dimen.imgSize_small));
            cardIcon.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_grey)));

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
}