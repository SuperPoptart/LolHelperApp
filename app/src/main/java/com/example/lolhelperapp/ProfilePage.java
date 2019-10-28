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
import com.merakianalytics.orianna.Orianna;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
        ImageView profileIcon = findViewById(R.id.profileIcon2);
        String accId = "";
        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            summoner.setText(bundle.getString("name"));
            level.setText(bundle.getString("summonerLevel"));
            String ico = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/" + bundle.getString("profileIconId") + ".png";
            profileIcon.setImageDrawable(LoadImageFromWebOperations(ico));
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
        try {
            inputStream = openFileInput(file.getName());
            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String reciever = "";
                StringBuilder sb = new StringBuilder();

                while ((reciever = bufferedReader.readLine()) != null) {
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

    public void favoriteAccount(View view) {
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

    public void toRadar(View view) {
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
        private List<Integer> totalCsScore = new ArrayList<>();
        private List<Integer> totalVersitilityScore = new ArrayList<>();
        private List<Integer> totalConsistancyScore = new ArrayList<>();
        private List<Integer> totalObjectivesScore = new ArrayList<>();
        private List<Integer> totalAggressionScore = new ArrayList<>();
        private List<Integer> totalVisionScore = new ArrayList<>();

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
                    if (matches.getMatches().get(i).getQueue() == 440 || matches.getMatches().get(i).getQueue() == 430 || matches.getMatches().get(i).getQueue() == 420 || matches.getMatches().get(i).getQueue() == 450) {
                        String stringUrl = "https://na1.api.riotgames.com/lol/match/v4/matches/" + matches.getMatches().get(i).getGameId() + "?api_key=" + key;
                        SingleMatchTask singleMatch = new SingleMatchTask();
                        singleMatch.execute(stringUrl, accId, String.valueOf(matches.getMatches().get(i).getTimestamp()));
//                        if (totalCsScore.size() != 5 && singleMatch.getStatus() == Status.FINISHED) {
//                            totalCsScore.add(singleMatch.csScore);
//                            System.out.println(singleMatch.csScore);
//                            totalVersitilityScore.add(singleMatch.versScore);
//                            System.out.println(singleMatch.versScore);
//                            totalConsistancyScore.add(singleMatch.consScore);
//                            System.out.println(singleMatch.consScore);
//                            totalObjectivesScore.add(singleMatch.objecScore);
//                            System.out.println(singleMatch.objecScore);
//                            totalAggressionScore.add(singleMatch.aggScore);
//                            System.out.println(singleMatch.aggScore);
//                            totalVisionScore.add(singleMatch.visScore);
//                            System.out.println(singleMatch.visScore);
//                        } else if (totalCsScore.size() == 5) {
//                            findViewById(R.id.toRadar).setVisibility(View.VISIBLE);
//                        } else {
//                            System.out.println("Nothin");
//                        }
                    } else {
                        games++;
                    }
                }
            }
        }
    }

    private class SingleMatchTask extends AsyncTask<String, String, String> {

        private String returner;
        private SingleMatch looker;
        private Gson gson = new Gson();
        private String accId;
        private long gameTime;
        private double csScore;
        private int versScore;
        private double consScore;
        private double objecScore;
        private double aggScore;
        private double visScore;

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
            int team1kills = 0;
            int team1deaths = 0;
            int team1assists = 0;
            int team2kills = 0;
            int team2deaths = 0;
            int team2assists = 0;

            ParticipantIdentity holder = new ParticipantIdentity();
            Participant actualHolder = new Participant();
            for (ParticipantIdentity j : looker.getParticipantIdentities()) {
                if (j.getPlayer().getAccountId().equals(accId)) {
                    holder = j;
                    break;
                }
            }
            for (Participant x : looker.getParticipants()) {
                if (x.getTeamId() == 100) {
                    team1kills += x.getStats().getKills();
                    team1deaths += x.getStats().getDeaths();
                    team1assists += x.getStats().getAssists();
                } else {
                    team2kills += x.getStats().getKills();
                    team2deaths += x.getStats().getDeaths();
                    team2assists += x.getStats().getAssists();
                }
                if (x.getParticipantId() == holder.getParticipantId()) {
                    actualHolder = x;

                }
            }
            TextView tv = new TextView(ProfilePage.this);
            Date current = new Date(gameTime);
            DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss:");
//            df.format(current)
            long minutes = TimeUnit.SECONDS.toMinutes(looker.getGameDuration());
            int team = 0;
            if (actualHolder.getTeamId() == 100) {
            } else {
                team = 1;
            }
            csScore = ((actualHolder.getStats().getNeutralMinionsKilled() + actualHolder.getStats().getTotalMinionsKilled()) / (int) minutes);
            versScore = actualHolder.getChampionId();
            if (actualHolder.getStats().getDeaths() == 0) {
                consScore = 2468;
            } else {
                consScore = (actualHolder.getStats().getKills() + actualHolder.getStats().getAssists()) / actualHolder.getStats().getDeaths();
            }
            objecScore = actualHolder.getStats().getTurretKills();
            if (team == 0) {
                aggScore = ((actualHolder.getStats().getKills() + actualHolder.getStats().getAssists()) / team1kills) * 10;
            } else {
                aggScore = ((actualHolder.getStats().getKills() + actualHolder.getStats().getAssists()) / team2kills) * 10;
            }
            visScore = (actualHolder.getStats().getVisionScore() / (int) minutes) * 10;

            if (actualHolder.getStats().isWin()) {
                tv.setBackgroundColor(Color.rgb(124, 192, 217));
                String toSet = actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists() + " Win Time: " + minutes + "m CS(" + csScore+"/min)";
                tv.setText(toSet);
                lay.addView(tv);
            } else {
                tv.setBackgroundColor(Color.rgb(255, 96, 96));
                String toSet = actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists() + " Loss Time: " + minutes+ "m CS(" + csScore+"/min)";
                tv.setText(toSet);
                lay.addView(tv);
            }

        }
    }

}
