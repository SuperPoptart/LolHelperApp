package com.example.lolhelperapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.lolhelperapp.models.MatchList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String key = "RGAPI-19474d33-5c79-4d45-8e50-a8735bc0b977";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView summoner = findViewById(R.id.sumName);
        TextView level = findViewById(R.id.level);
        ImageView profileIcon = findViewById(R.id.profileIcon);
        String accId = "";


        Bundle b = getIntent().getExtras();
        if (b != null) {
            summoner.setText(b.getString("name"));
            level.setText(b.getString("summonerLevel"));
            String ico = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/" + b.getString("profileIconId") + ".png";
            LoadImageFromWebOperations(ico, profileIcon);
            accId = b.getString("accountId");
        } else {
            System.out.println("ACTUAL ERROR");
        }

        String stringUrl = "https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + accId + "?api_key=" + key;
        if (!accId.isEmpty()) {
            MatchHistoryTask matches = new MatchHistoryTask();
            matches.execute(stringUrl);
        }

        FloatingActionButton fab = findViewById(R.id.backer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(getApplicationContext(), MainActivity.class));
                startActivity(intent);
                finish();
            }
        });
    }

    public static Drawable LoadImageFromWebOperations(String url, ImageView iv) {
        Drawable ugh = null;
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            iv.setImageDrawable(d);
            return d;
        } catch (Exception e) {
        }
        return ugh;
    }

    private class MatchHistoryTask extends AsyncTask<String, String, String> {

        private String returner;

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            if (!strings[0].isEmpty()) {
                String stringUrl = strings[0];
                System.out.println(stringUrl);
                try {
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL(stringUrl);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream in = urlConnection.getInputStream();

                        InputStreamReader isw = new InputStreamReader(in);

                        int data = isw.read();
                        while (data != -1) {
                            current += (char) data;
                            data = isw.read();
                        }
                        System.out.println(current);
                        returner = current;
                        return current;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                }
            }
            System.out.println("EMPTY");
            System.out.println(current);
            returner = current;
            return current;
        }

        @Override
        protected void onPostExecute(String p) {
            Gson gson = new Gson();
            if (null != returner) {
                MatchList matches = gson.fromJson(returner, MatchList.class);
                LinearLayout lay = findViewById(R.id.layerout);
                for(int i = 0; i < 20 ; i ++){
                    TextView tv = new TextView(ProfilePage.this);
                    String toSet = matches.getMatches().get(i).getLane() + ": " + matches.getMatches().get(i).getRole();
                    tv.setText(toSet);
                    lay.addView(tv);
                }
            }
        }
    }
}
