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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.j256.ormlite.stmt.query.In;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        generagePastEmailCards();

        setBottomNavBar();
    }

    private void generagePastEmailCards() {

        //Create the individual card
        CardView pastEmailCard = new CardView(this);

        //Set the Card params
        CardView.LayoutParams cardLayoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.MATCH_PARENT);
        cardLayoutParams.leftMargin = 50;
        pastEmailCard.setRadius(
                (int)getResources().getDimension(R.dimen.borderRadius_medium));

        pastEmailCard.setLayoutParams(cardLayoutParams);

        ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) pastEmailCard.getLayoutParams();
        cardViewMarginParams.setMargins(50, 30, 50, 30);
        pastEmailCard.requestLayout();

        LinearLayout cardHolderLayout = new LinearLayout(this);
        cardHolderLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams cardHolderLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardHolderLayout.setPadding(
                (int)getResources().getDimension(R.dimen.padding_medium),
                (int)getResources().getDimension(R.dimen.padding_medium),
                (int)getResources().getDimension(R.dimen.padding_medium),
                (int)getResources().getDimension(R.dimen.padding_medium));

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
        cardTitle.setText(getString(R.string.home_test_name));
        //cardTitle.setTextSize(getResources().getDimension(R.dimen.font_small));
        cardTitle.setTextSize(24);
        cardTitle.setTextColor(getColor(R.color.dark_grey));

        //Set the sub-header as the date the email was sent
        TextView cardSubHeader = new TextView(this);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd yyyy");
        cardSubHeader.setText(formatter.format(new Date()));
        //cardSubHeader.setTextSize(getResources().getDimension(R.dimen.font_small));
        cardSubHeader.setTextSize(22);
        cardSubHeader.setTextColor(getColor(R.color.dark_grey));

        //Set the body as the first bit of the email
        TextView cardBody = new TextView(this);
        cardBody.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        cardBody.setText(getString(R.string.home_test_description));
        //cardBody.setTextSize(getResources().getDimension(R.dimen.font_small));
        cardBody.setTextSize(14);
        cardBody.setTextColor(getColor(R.color.dark_grey));

        //Set the image as the icon for the issue
        ImageView cardIcon = new ImageView(this);
        LinearLayout.LayoutParams cardIconLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardIconLayoutParams.setMarginStart((int)getResources().getDimension(R.dimen.margin_xxxLarge));
        cardIcon.setLayoutParams(cardIconLayoutParams);
        cardIcon.setImageIcon(Icon.createWithResource(this, R.drawable.ic_baseline_eco_24));
        cardIcon.setMinimumHeight((int)getResources().getDimension(R.dimen.imgSize_small));
        cardIcon.setMinimumWidth((int)getResources().getDimension(R.dimen.imgSize_small));
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

        //Add card to the layout that holds the past trips
        LinearLayout view = findViewById(R.id.container_past_emails);
        view.addView(pastEmailCard, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
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
}