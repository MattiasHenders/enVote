package com.envelopepushers.envote;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonFromWeb {

    /**
     * JSON object to return from web query
     */
    private JSONObject returnObj = null;

    /**
     * String returned from web query
     */
    private String returnString = null ;

    /**
     * Executes a web query.
     * @param url String
     */
    public JsonFromWeb(String url) {
        new JsonTask().execute(url);
    }

    /**
     * Gets the JSON string.
     * @return
     */
    public String getJSONString() {
        return returnString;
    }

    /**
     * Gets the JSON object.
     * @return JSONobject
     */
    public JSONObject getJSONObject() {
        return returnObj;
    }


    /**
     * Gets the JSON response from a web query. Does the heavy lifting
     */
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            // Attempt the query
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                // Read in the response to a string buffer
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);

                }

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // Creates and Returns a JSON Object
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                returnString = "null";
            } else {
                returnString = result;
            }
            // If the result is a JSON array make it a JSON object
            if (!result.trim().startsWith("{")) {
                result = "{\"data\":" + result + "}";
            }
            try {
                returnObj = new JSONObject(result);
            } catch (JSONException e) {
                try {
                    JSONArray arr = new JSONArray(result);
                    returnObj = arr.getJSONObject(0);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}
