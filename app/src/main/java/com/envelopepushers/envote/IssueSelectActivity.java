//package com.envelopepushers.envote;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.sql.SQLOutput;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class IssueSelectActivity extends AppCompatActivity {
//
//    private Button btnSubmitLocation;
//    TextView textView1;
//    Button showMore1;
//    TextView textView2;
//    Button showMore2;
//    double userLat;
//    double userLon;
//    boolean airIssue = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_issue_select);
//
//        Intent intent = getIntent();
//        btnSubmitLocation = findViewById(R.id.nextPageButton);
//
//        userLat = intent.getDoubleExtra("lat", 0);
//        userLon = intent.getDoubleExtra("lon", 0);
//        getAirIssue();
//        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openRepActivity();
//            }
//        });
//
//        textView1=(TextView)findViewById(R.id.textView1);
//        showMore1=(Button)findViewById(R.id.showMore1);
//        showMore1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (showMore1.getText().toString().equalsIgnoreCase("Showmore..."))
//                {
//                    textView1.setMaxLines(Integer.MAX_VALUE);//your TextView
//                    showMore1.setText("Showless");
//                }
//                else
//                {
//                    textView1.setMaxLines(3);//your TextView
//                    showMore1.setText("Showmore...");
//                }
//            }
//        });
//
//        textView2=(TextView)findViewById(R.id.textView2);
//        showMore2=(Button)findViewById(R.id.showMore2);
//        showMore2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (showMore2.getText().toString().equalsIgnoreCase("Showmore..."))
//                {
//                    textView2.setMaxLines(Integer.MAX_VALUE);//your TextView
//                    showMore2.setText("Showless");
//
//                }
//                else
//                {
//                    textView2.setMaxLines(3);//your TextView
//                    showMore2.setText("Showmore...");
//                }
//            }
//        });
//
//        //setBottomNavBar();
//    }
//    public void openRepActivity() {
//        Intent intent = new Intent(this, RepresentativeSelectActivity.class);
//        intent.putExtra("lat", userLat);
//        intent.putExtra("lon", userLon);
//        // TODO add user's selection.
//        startActivity(intent);
//        finish();
//    }
//
////    private void getLocalReps() {
////
////        String urlCall = "https://represent.opennorth.ca/representatives/?point=" +
////                userLat + "%2C" +
////                userLon;
////
////        JsonFromWeb reps = new JsonFromWeb(urlCall);
////        final JSONObject[] repsJSONHolder = {null};
////
////        //Use new timer to get response
////        new Timer().schedule(new TimerTask() {
////            @Override
////            public void run() {
////                while (reps.getJSONObject() == null) {
////                    //Wait for response on separate thread
////                }
////                repsJSONHolder[0] = reps.getJSONObject();
////                try {
////                    localReps = repsJSONHolder[0].getJSONArray("objects");
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////                Log.i("REPS", "Got reps!");
////            }
////        }, 100);
////    }
//
////    private void getLocalIssues() {
////
////        getAirIssue();
////    }
//
//    private void getAirIssue() {
//
//        String urlCall = "https://api.waqi.info/feed/geo:" +
//                userLat + ";" +
//                userLon +
//                "/?token=demo";
//
//        JsonFromWeb airQuality = new JsonFromWeb(urlCall);
//        final JSONObject[] airQualityJSONHolder = {null};
//        final int[] AQIScore = new int[1];
//        //Use new timer to get response
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                while (airQuality.getJSONObject() == null) {
//                    //Wait for response on separate thread
//                }
//                airQualityJSONHolder[0] = airQuality.getJSONObject();
//                try {
//                    if (airQualityJSONHolder[0]
//                            .getJSONObject("data")
//                            .getInt("aqi") > 80) {
//                        airIssue = true;
//                    }
//                    AQIScore[0] = airQualityJSONHolder[0].getJSONObject("data").getInt("aqi");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Log.i("AIR", "Air API call done, air is an issue=" + airIssue);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if ( AQIScore[0] > 150 ) {
//                            textView1.setText("The Air quality is: " + AQIScore[0] + ". This is an unhealthy air quality level.");
//                        } else if ( AQIScore[0] > 80 ) {
//                            textView1.setText("The Air quality is: " + AQIScore[0] + ". This is a moderate air quality level that may be harmful to sensitive individuals");
//                        } else {
//                            textView1.setText("The Air quality is: " + AQIScore[0] + ". This is a safe air quality level for most individuals.");
//                        }
//                    }
//                });
//
//            }
//        }, 100);
//    }
//
////    private void getWaterIssue() {
////
////        String urlCall = "https://api.waqi.info/feed/geo:" +
////                userLat + ";" +
////                userLon +
////                "/?token=demo";
////
////        JsonFromWeb airQuality = new JsonFromWeb(urlCall);
////        final JSONObject[] airQualityJSONHolder = {null};
////
////        //Use new timer to get response
////        new Timer().schedule(new TimerTask() {
////            @Override
////            public void run() {
////                while (airQuality.getJSONObject() == null) {
////                    //Wait for response on separate thread
////                }
////                airQualityJSONHolder[0] = airQuality.getJSONObject();
////                try {
////                    if (airQualityJSONHolder[0]
////                            .getJSONObject("data")
////                            .getInt("aqi") > 80) {
////                        airIssue = true;
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////                Log.i("AIR", "Air API call done, air is an issue=" + airIssue);
////            }
////        }, 100);
////    }
//
//    private void setBottomNavBar() {
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() == R.id.action_new) {
//                    openMapActivity();
//                    return true;
//                }
//                if (item.getItemId() == R.id.action_browse) {
//                    openHomeActivity();
//                    return true;
//                }
//                if (item.getItemId() == R.id.action_profile) {
//                    openIssueActivity();
//                    return true;
//                }
//                return false;
//            }
//        });
//    }
//
//    public void openEmailActivity() {
//        Intent intent = new Intent(this, TemplateView.class);
//        startActivity(intent);
//        finish();
//    }
//
//    private void openMapActivity() {
//        startActivity(new Intent(this, LocationSelectMap.class));
//        finish();
//    }
//
//    private void openHomeActivity() {
//        startActivity(new Intent(this, HomeActivity.class));
//        finish();
//    }
//
//    private void openIssueActivity() {
//        startActivity(new Intent(this, IssueSelectActivity.class));
//        finish();
//    }
//}