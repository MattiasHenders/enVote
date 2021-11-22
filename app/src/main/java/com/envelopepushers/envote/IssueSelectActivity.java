package com.envelopepushers.envote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class IssueSelectActivity extends AppCompatActivity {

    public LinearLayout issuesContainer;
    double userLat;
    double userLon;
    int aqi = 0;
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
        aqi = intent.getIntExtra("aqi", 0);
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

            LinearLayout cardVerticalLayout = new LinearLayout(this);
            cardVerticalLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams cardVerticalLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardVerticalLayout.setLayoutParams(cardVerticalLayoutParams);
            cardVerticalLayout.setBackgroundColor(getColor(currentIssue.getColourDark()));

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

            //Format the email body
            cardBody.setText(getString(R.string.show_more));
            cardBody.setTextSize(18);
//            cardBody.setTextSize(getResources().getDimension(R.dimen.font_small));
            cardBody.setTextColor(getColor(currentIssue.getColourLight()));

            //Set the image as the icon for the issue
            ImageView cardIcon = new ImageView(this);
            LinearLayout.LayoutParams cardIconLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardIconLayoutParams.setMarginStart((int) getResources().getDimension(R.dimen.margin_medium));
            cardIcon.setLayoutParams(cardIconLayoutParams);

            //Add the button
            CardView selectButton = new CardView(this);
            LinearLayout.LayoutParams cardButtonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardButtonParams.gravity = Gravity.CENTER;
            cardButtonParams.bottomMargin = (int) getResources().getDimension(R.dimen.margin_medium);

            selectButton.setLayoutParams(cardButtonParams);
            selectButton.setRadius((int) getResources().getDimension(R.dimen.borderRadius_medium));

            LinearLayout textButtonLayout = new LinearLayout(this);
            LinearLayout.LayoutParams cardButtonTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardButtonParams.gravity = Gravity.CENTER;
            textButtonLayout.setPadding(
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    (int) getResources().getDimension(R.dimen.padding_small),
                    (int) getResources().getDimension(R.dimen.padding_medium),
                    (int) getResources().getDimension(R.dimen.padding_small));

            textButtonLayout.setLayoutParams(cardButtonTextParams);
            textButtonLayout.setBackgroundColor(getColor(currentIssue.getColourLight()));
            TextView buttonText = new TextView(this);
            buttonText.setText(R.string.select_issue_text);
            buttonText.setTextColor(getColor(currentIssue.getColourDark()));

            textButtonLayout.addView(buttonText);
            selectButton.addView(textButtonLayout);

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

            cardVerticalLayout.addView(cardHolderLayout);
            cardVerticalLayout.addView(selectButton);

            //Add the filled Linear Layout to the full card
            issueCard.addView(cardVerticalLayout);

            cardIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openRepActivity(currentIssue.getKey());
                }
            });


            if (currentIssue.getKey().equals(EcoIssues.WATER.getKey())) {
                cardBody.setId(R.id.waterIssueDescription);
                selectButton.setId(R.id.waterButton);
            } else if (currentIssue.getKey().equals(EcoIssues.ELECTRIC.getKey())) {
                cardBody.setId(R.id.electricIssueDescription);
                selectButton.setId(R.id.electricButton);
            } else if (currentIssue.getKey().equals(EcoIssues.TRASH.getKey())) {
                cardBody.setId(R.id.trashIssueDescription);
                selectButton.setId(R.id.trashButton);
            } else {
                cardBody.setId(R.id.airIssueDescription);
                selectButton.setId(R.id.airButton);
            }

            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openRepActivity(currentIssue.getKey());
                }
            });
            selectButton.setVisibility(View.GONE);

            issueCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentIssue.getKey().equals("AIR")) {
                        cardBody.setText("The current AQI is: " + aqi + ". An AQI over 80 is typically considered harmful.\n" + getString(currentIssue.getDescription()));

                    } else {
                        cardBody.setText(getString(currentIssue.getDescription()));
                    }

                    //Close the other cards
                    closeOtherCards(currentIssue.getKey());
                }
            });

            issuesContainer.addView(issueCard);
        }
    }

    private void closeOtherCards(String issue) {

        TextView airText = findViewById(R.id.airIssueDescription);
        TextView waterText = findViewById(R.id.waterIssueDescription);
        TextView trashText = findViewById(R.id.trashIssueDescription);
        TextView electricText = findViewById(R.id.electricIssueDescription);

        CardView airButton = findViewById(R.id.airButton);
        CardView waterButton = findViewById(R.id.waterButton);
        CardView trashButton = findViewById(R.id.trashButton);
        CardView electricButton = findViewById(R.id.electricButton);

        switch (issue) {
            case "AIR":
                waterText.setText(R.string.show_more);
                trashText.setText(R.string.show_more);
                electricText.setText(R.string.show_more);

                airButton.setVisibility(View.VISIBLE);
                waterButton.setVisibility(View.GONE);
                trashButton.setVisibility(View.GONE);
                electricButton.setVisibility(View.GONE);
                break;

            case "WATER":
                airText.setText(R.string.show_more);
                trashText.setText(R.string.show_more);
                electricText.setText(R.string.show_more);

                airButton.setVisibility(View.GONE);
                waterButton.setVisibility(View.VISIBLE);
                trashButton.setVisibility(View.GONE);
                electricButton.setVisibility(View.GONE);
                break;

            case "TRASH":
                airText.setText(R.string.show_more);
                waterText.setText(R.string.show_more);
                electricText.setText(R.string.show_more);

                airButton.setVisibility(View.GONE);
                waterButton.setVisibility(View.GONE);
                trashButton.setVisibility(View.VISIBLE);
                electricButton.setVisibility(View.GONE);
                break;

            default:
                airText.setText(R.string.show_more);
                waterText.setText(R.string.show_more);
                trashText.setText(R.string.show_more);

                airButton.setVisibility(View.GONE);
                waterButton.setVisibility(View.GONE);
                trashButton.setVisibility(View.GONE);
                electricButton.setVisibility(View.VISIBLE);
                break;
        }
    }
}