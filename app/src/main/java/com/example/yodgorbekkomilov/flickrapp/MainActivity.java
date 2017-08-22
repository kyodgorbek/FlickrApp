package com.example.yodgorbekkomilov.flickrapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MainActivity extends AppCompatActivity {


    String FlickrQuery_url = "http://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_per_page = "&per_page=1";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=";

    String FlickrApiKey = "962f0e430ed4b39579e9b70ef5928446";
    EditText searchText;
    Button searchButton;
    TextView textQueryResult, textJsonResult;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = (EditText)findViewById(R.id.searchtext);
        searchButton = (Button)findViewById(R.id.searchbutton);
        textQueryResult = (TextView)findViewById(R.id.queryresult);
        textJsonResult = (TextView)findViewById(R.id.jsonresult);
        searchButton.setOnClickListener(searchButtonOnClickListener);

    }
    private Button.OnClickListener searchButtonOnClickListener
            = new Button.OnClickListener(){
        public void onClick(View arg0) {
            String searchQ = searchText.getText().toString();
            String searchResult = QueryFlickr(searchQ);
            textQueryResult.setText(searchResult);
            String jsonResult = ParseJSON(searchResult);
            textJsonResult.setText(jsonResult);

        }};
    private String QueryFlickr(String q){
        String qResult = null;
        String qString =FlickrQuery_url + FlickrQuery_per_page+ FlickrQuery_nojsoncallback+ FlickrQuery_format+ FlickrQuery_tag + q + FlickrQuery_key + FlickrApiKey;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);
        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
            if (httpEntity != null){
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();
                String stringReadLine = null;
                while ((stringReadLine = bufferedreader.readLine()) != null)                     {
                    stringBuilder.append(stringReadLine + "\n");
                }
                qResult = stringBuilder.toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return qResult;
    }

    private String ParseJSON(String json){

        String jResult = null;
        try {

            JSONObject JsonObject = new JSONObject(json);
            JSONObject Json_photos = JsonObject.getJSONObject("photos");
            JSONArray JsonArray_photo = Json_photos.getJSONArray("photo");
            //We have only one photo in this exercise
            JSONObject FlickrPhoto = JsonArray_photo.getJSONObject(0);
            jResult = "\nid: " + FlickrPhoto.getString("id") + "\n"

                    + "owner: " + FlickrPhoto.getString("owner") + "\n"
                    + "secret: " + FlickrPhoto.getString("secret") + "\n"
                    + "server: " + FlickrPhoto.getString("server") + "\n"
                    + "farm: " + FlickrPhoto.getString("farm") + "\n"
                    + "title: " + FlickrPhoto.getString("title") + "\n";
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return jResult;
    }
}