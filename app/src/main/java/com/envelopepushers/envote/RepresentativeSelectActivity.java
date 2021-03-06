package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RepresentativeSelectActivity extends AppCompatActivity {

    private static final String SERVICE_URL = "https://represent.opennorth.ca/representatives/?point=";
    private ArrayList<Representative> _repsList;
    private RecyclerView _recyclerView;
    private RecyclerAdapter _recyclerAdapter;
    private RequestQueue _requestQueue;
    double userLat;
    double userLon;
    public String selectedIssue;

    String issue = "issue";
    String lat = "lat";
    String lon = "lon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_select);
        Intent intent = getIntent();
        _recyclerView = findViewById(R.id.recycler_view);
        _recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(lm);

        selectedIssue = intent.getStringExtra(issue);

        userLat = intent.getDoubleExtra(lat, 0);
        userLon = intent.getDoubleExtra(lon, 0);
        _repsList = new ArrayList<Representative>();
        _requestQueue = Volley.newRequestQueue(this);
        queueParseJSON();
    }

    public void onResponse(JSONArray response) {
        String jsonStr = response.toString();
        Gson gson = new Gson();
        BaseReps baseToon = gson.fromJson(jsonStr, BaseReps.class);
        _repsList = baseToon.getReps();
        _recyclerAdapter = new RecyclerAdapter(RepresentativeSelectActivity.this, _repsList);
        _recyclerView.setAdapter(_recyclerAdapter);
    }

    private void queueParseJSON() {
        String url = SERVICE_URL + userLat + "," + userLon;
        System.out.println(url);
        JsonFromWeb reps = new JsonFromWeb(url);
        final JSONObject[] repJSONHolder = {null};
        final JSONArray[] jsonData = {null};

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (reps.getJSONObject() == null) {

                }
                repJSONHolder[0] = reps.getJSONObject();
                try {
                    jsonData[0] = repJSONHolder[0].getJSONArray("objects");
                    for (int ndx = 0; ndx < jsonData[0].length(); ndx++) {
                        JSONObject obj = jsonData[0].getJSONObject(ndx);
                        String name = obj.getString("name");
                        String email = obj.getString("email");
                        String party = obj.getString("party_name");
                        String govLevel = obj.getString("representative_set_name");
                        String pictureUrl = obj.getString("photo_url");

                        _repsList.add(new Representative(name, party, email, govLevel, pictureUrl));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _recyclerAdapter = new RecyclerAdapter(RepresentativeSelectActivity.this, _repsList);
                            _recyclerView.setAdapter(_recyclerAdapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }
}