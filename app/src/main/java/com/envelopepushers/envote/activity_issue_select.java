package com.envelopepushers.envote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class activity_issue_select extends AppCompatActivity {

    public LinearLayout issuesContainer;
    double userLat;
    double userLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_select);

        issuesContainer = findViewById(R.id.issues_container);

        ArrayList<EcoIssue> issuesInOrder = new ArrayList<>();

        issuesInOrder.add(new EcoIssue(EcoIssues.TRASH));
        issuesInOrder.add(new EcoIssue(EcoIssues.WATER));
        issuesInOrder.add(new EcoIssue(EcoIssues.ELECTRIC));
        issuesInOrder.add(new EcoIssue(EcoIssues.AIR));

        Intent intent = getIntent();
        userLat = intent.getDoubleExtra("lat", 0);
        userLon = intent.getDoubleExtra("lon", 0);

        generateIssueCards(issuesInOrder);
    }

    public void openRepActivity(String issueKey) {
        Intent intent = new Intent(this, RepresentativeSelectActivity.class);
        intent.putExtra("issue", issueKey);
        intent.putExtra("lat", userLat);
        intent.putExtra("lon", userLon);
        startActivity(intent);
        finish();
    }

    private void generateIssueCards(ArrayList<EcoIssue> issues) {

        for (EcoIssue currentIssue : issues) {

            //Create the individual card
            CardView issueCard = new CardView(this);

            //Set the Card params
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            issueCard.setRadius((int) getResources().getDimension(R.dimen.borderRadius_large));
            issueCard.setLayoutParams(cardLayoutParams);

            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) issueCard.getLayoutParams();
            cardViewMarginParams.setMargins(10, 30, 50, 20);
            issueCard.requestLayout();

            LinearLayout cardHolderLayout = new LinearLayout(this);
            cardHolderLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams cardHolderLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardHolderLayout.setPadding(
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    (int) getResources().getDimension(R.dimen.padding_small),
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    (int) getResources().getDimension(R.dimen.padding_medium));

            cardHolderLayout.setBackgroundColor(getColor(currentIssue.getColourDark()));
            cardHolderLayout.setLayoutParams(cardHolderLayoutParams);

            LinearLayout textHolderLayout = new LinearLayout(this);
            textHolderLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textHolderLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textHolderLayout.setPadding(
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    0,
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    0);

            //Set the email TO name as the title
            TextView cardTitle = new TextView(this);
            cardTitle.setText(currentIssue.getName());
            cardTitle.setTextSize(26);
//            cardTitle.setTextSize(getResources().getDimension(R.dimen.font_medium));
            cardTitle.setTextColor(getColor(currentIssue.getColourLight()));

            //Set the body as the first bit of the email
            TextView cardBody = new TextView(this);
            cardBody.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //TODO: Add Body Text about issue, make dropdown like Chris had
            //Format the email body
            cardBody.setText(getString(R.string.show_more));
            cardBody.setTextSize(18);
//            cardBody.setTextSize(getResources().getDimension(R.dimen.font_small));
            cardBody.setTextColor(getColor(currentIssue.getColourLight()));

            //Set the image as the icon for the issue
            ImageView cardIcon = new ImageView(this);
            LinearLayout.LayoutParams cardIconLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardIconLayoutParams.setMarginStart((int) getResources().getDimension(R.dimen.margin_medium));
            cardIcon.setLayoutParams(cardIconLayoutParams);

            //Get the first EcoIssue icon and color
            cardIcon.setImageIcon(Icon.createWithResource(this, currentIssue.getIcon()));
            cardIcon.setMinimumHeight((int) getResources().getDimension(R.dimen.imgSize_small));
            cardIcon.setMinimumWidth((int) getResources().getDimension(R.dimen.imgSize_small));
            cardIcon.setImageTintList(ColorStateList.valueOf(getColor(currentIssue.getColourLight())));

            //Add items in reverse order to the layout holder
            textHolderLayout.addView(cardTitle);
            textHolderLayout.addView(cardBody);
            cardHolderLayout.addView(textHolderLayout);
            cardHolderLayout.addView(cardIcon);

            //Add the filled Linear Layout to the full card
            issueCard.addView(cardHolderLayout);

            cardIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openRepActivity(currentIssue.getKey());
                }
            });

            issueCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cardBody.setText(getString(currentIssue.getDescription()));

                    issueCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openRepActivity(currentIssue.getKey());
                        }
                    });

                }
            });

            issuesContainer.addView(issueCard);
        }
    }
}