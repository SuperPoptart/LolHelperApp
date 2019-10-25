package com.example.lolhelperapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.lolhelperapp.models.MatchList;
import com.example.lolhelperapp.models.Participant;
import com.example.lolhelperapp.models.ParticipantIdentity;
import com.example.lolhelperapp.models.SingleMatch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ProfilePage extends AppCompatActivity {

    final String key = "RGAPI-46084dd2-9207-4edd-b1fb-9bba018d0a1a";
    private String[] names = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView summoner = findViewById(R.id.sumName);
        TextView level = findViewById(R.id.level);
        ImageView profileIcon = findViewById(R.id.profileIcon);
        String accId = "";
        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            summoner.setText(bundle.getString("name"));
            level.setText(bundle.getString("summonerLevel"));
            String ico = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/" + bundle.getString("profileIconId") + ".png";
//            profileIcon.setImageDrawable(LoadImageFromWebOperations(ico));
            accId = bundle.getString("accountId");
        } else {
            System.out.println("ACTUAL ERROR");
        }

        String stringUrl = "https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + accId + "?api_key=" + key;
        if (!accId.isEmpty()) {
            MatchHistoryTask matches = new MatchHistoryTask();
            matches.execute(stringUrl, accId);
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

//        //GRABBING FAV
        String filename = "favorites.txt";
        FileInputStream inputStream;
        File file = new File(getFilesDir(), filename);
        String holder = "";
        try{
            inputStream = openFileInput(file.getName());
            if( inputStream != null){
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String reciever = "";
                StringBuilder sb = new StringBuilder();

                while( (reciever = bufferedReader.readLine()) != null) {
                    sb.append(reciever);
                }

                inputStream.close();
                holder = sb.toString();
                System.out.println(holder);
                names = new String[holder.split("#").length];
                names = holder.split("#");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void favoriteAccount(View view){
        Bundle bundle = getIntent().getExtras();
        String filename = "favorites.txt";
        String fileContents = bundle.getString("name");
        fileContents += "#";

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_APPEND));
            outputStreamWriter.write(fileContents);
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toRadar(View view){
        Intent intent = new Intent(new Intent(getApplicationContext(), RadarActivity.class));
        startActivity(intent);
        finish();
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        Drawable drawnIcon = null;
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            drawnIcon = Drawable.createFromStream(is, "ddragon");
            return drawnIcon;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawnIcon;
    }

    private class MatchHistoryTask extends AsyncTask<String, String, String> {

        private String returner;
        private String accId;

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            accId = strings[1];
            if (!strings[0].isEmpty()) {
                String stringUrl = strings[0];
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
            returner = current;
            return current;
        }

        @Override
        protected void onPostExecute(String p) {
            Gson gson = new Gson();
            if (null != returner) {
                MatchList matches = gson.fromJson(returner, MatchList.class);
                int games = 20;
                for (int i = 0; i < games; i++) {
                    if(matches.getMatches().get(i).getQueue() == 440 || matches.getMatches().get(i).getQueue() == 430 || matches.getMatches().get(i).getQueue() == 420 || matches.getMatches().get(i).getQueue() == 450) {
                        String stringUrl = "https://na1.api.riotgames.com/lol/match/v4/matches/" + matches.getMatches().get(i).getGameId() + "?api_key=" + key;
                        SingleMatchTask singleMatch = new SingleMatchTask();
                        singleMatch.execute(stringUrl, accId, String.valueOf(matches.getMatches().get(i).getTimestamp()));
                    }else{
                        games++;
                    }
                }
            }
        }
    }

    private class SingleMatchTask extends AsyncTask<String, String, String> {

        private String  returner;
        private SingleMatch looker;
        private Gson gson = new Gson();
        private String accId;
        private long gameTime;

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            accId = strings[1];
            gameTime = Long.parseLong(strings[2]);
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
                        looker = gson.fromJson(returner, SingleMatch.class);
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
        protected void onPostExecute(String s) {
            LinearLayout lay = findViewById(R.id.layerout);

            ParticipantIdentity holder = new ParticipantIdentity();
            Participant actualHolder = new Participant();
            for(ParticipantIdentity j : looker.getParticipantIdentities()){
                if(j.getPlayer().getAccountId().equals(accId)){
                    holder = j;
                    break;
                }
            }
            for(Participant x : looker.getParticipants()){
                if(x.getParticipantId() == holder.getParticipantId()){
                    actualHolder = x;
                    break;
                }
            }
            TextView tv = new TextView(ProfilePage.this);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(gameTime);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(gameTime);


            if(actualHolder.getStats().isWin()) {
                tv.setBackgroundColor(Color.rgb(124, 192, 217));
                String toSet = actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists() + " Win Time: " + minutes;
                tv.setText(toSet);
                lay.addView(tv);
            }else{
                tv.setBackgroundColor(Color.rgb(255, 96, 96));
                String toSet = actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists() + " Loss Time: " + minutes;
                tv.setText(toSet);
                lay.addView(tv);
            }

        }
    }

}
