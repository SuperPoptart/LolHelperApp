package com.example.lolhelperapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.lolhelperapp.models.Match;
import com.example.lolhelperapp.models.MatchList;
import com.example.lolhelperapp.models.Participant;
import com.example.lolhelperapp.models.ParticipantIdentity;
import com.example.lolhelperapp.models.SingleMatch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.OrientationHelper;

import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ProfilePage extends AppCompatActivity {

    final String key = "RGAPI-866615d8-0fd8-4081-824d-0d148632bb28";

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
        MatchHistoryTask matches = new MatchHistoryTask();
        if (!accId.isEmpty()) {
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
        private List<String> gameIds = new ArrayList<>();
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
            MatchList matches = gson.fromJson(returner, MatchList.class);
            List<SingleMatch> allMatches = new ArrayList<>();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("singleMatch");

            if (null != returner) {
                int games = 20;
                for (int i = 0; i < games; i++) {
                    if (matches.getMatches().get(i).getQueue() == 440 || matches.getMatches().get(i).getQueue() == 430 || matches.getMatches().get(i).getQueue() == 420 || matches.getMatches().get(i).getQueue() == 450 || matches.getMatches().get(i).getQueue() == 400) {
                        String stringUrl = "https://na1.api.riotgames.com/lol/match/v4/matches/" + matches.getMatches().get(i).getGameId() + "?api_key=" + key;
//                        gameIds.add(stringUrl);
                        System.out.println(matches.getMatches().get(i).getGameId());
                        SingleMatchTask singleMatch = new SingleMatchTask();
                        singleMatch.execute(stringUrl, accId, String.valueOf(matches.getMatches().get(i).getTimestamp()), matches.getMatches().get(i).getRole());

                    } else if (games < 150) {
                        games++;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private class SingleMatchTask extends AsyncTask<String, String, String> {

        private String returner;
        private String roleR;
        private SingleMatch looker = new SingleMatch();
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
            System.out.println("DOING SINGLE MATCH");
            String current = "";
            accId = strings[1];
            roleR = strings[3];
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
            if (looker.getParticipantIdentities().isEmpty()) {
                cancel(true);
            }
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
            Date current = new Date(gameTime);
            DateFormat df = new SimpleDateFormat("dd:MM:yy", Locale.US);
            String gamesDate = df.format(current);

            long minutes = TimeUnit.SECONDS.toMinutes(looker.getGameDuration());
            int team = 0;
            if (actualHolder.getTeamId() == 100) {
            } else {
                team = 1;
            }
            if ((int) minutes != 0) {
                csScore = ((actualHolder.getStats().getNeutralMinionsKilled() + actualHolder.getStats().getTotalMinionsKilled()) / (int) minutes);
            } else {
                csScore = 0;
            }
            versScore = actualHolder.getChampionId();
            if (actualHolder.getStats().getDeaths() == 0) {
                consScore = 2468;
            } else {
                consScore = ((double) actualHolder.getStats().getKills() + (double) actualHolder.getStats().getAssists()) / (double) actualHolder.getStats().getDeaths();
            }
            objecScore = actualHolder.getStats().getTurretKills();
            if (team == 0 && team1kills != 0) {
                aggScore = ((actualHolder.getStats().getKills() + actualHolder.getStats().getAssists()) / team1kills) * 10;
            } else if (team == 1 && team2kills != 0) {
                aggScore = ((actualHolder.getStats().getKills() + actualHolder.getStats().getAssists()) / team2kills) * 10;
            } else {
                aggScore = 10;
            }
            if ((int) minutes != 0) {
                visScore = (actualHolder.getStats().getVisionScore() / (int) minutes) * 10;
            } else {
                visScore = 0;
            }

            String superQueue = "";

            switch (looker.getQueueId()) {
                case 400:
                    superQueue = "Normal";
                    break;
                case 420:
                    superQueue = "Ranked Solo";
                    break;
                case 430:
                    superQueue = "Blind";
                    break;
                case 440:
                    superQueue = "Flex";
                    break;
                case 450:
                    superQueue = "Aram";
                    break;
                default:
                    break;
            }

            System.out.println("SCORES: CS: " + csScore + " VERSATILITY: " + versScore + " CONSISTENCY: " + consScore + " OBJECTIVES: " + objecScore + " AGGRESSION: " + aggScore + " VISION: " + visScore);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("singleMatch");
            myRef.child(String.valueOf(looker.getGameId())).setValue(looker);


            HorizontalScrollView hSv = new HorizontalScrollView(ProfilePage.this);
            LinearLayout horoLay = new LinearLayout(ProfilePage.this);
            horoLay.setOrientation(LinearLayout.HORIZONTAL);

            ScrollView firstSection = new ScrollView(ProfilePage.this);
            LinearLayout firstLay = new LinearLayout(ProfilePage.this);
            ScrollView secondSection = new ScrollView(ProfilePage.this);
            LinearLayout secondLay = new LinearLayout(ProfilePage.this);
            ScrollView fourthSection = new ScrollView(ProfilePage.this);
            LinearLayout fourthLay = new LinearLayout(ProfilePage.this);

            HorizontalScrollView thirdSection = new HorizontalScrollView(ProfilePage.this);
            LinearLayout forThree = new LinearLayout(ProfilePage.this);
            forThree.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout thirdOne = new LinearLayout(ProfilePage.this);
            LinearLayout thirdTwo = new LinearLayout(ProfilePage.this);
            LinearLayout thirdThree = new LinearLayout(ProfilePage.this);

            TextView winOrLoss = new TextView(ProfilePage.this);
            if (actualHolder.getStats().isWin()) {
                hSv.setBackgroundColor(Color.rgb(124, 192, 217));
//                String toSet = actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists() + " Win Time: " + minutes + "m CS(" + csScore + "/min) " + superQueue;
                winOrLoss.setText("Win");
                firstLay.addView(winOrLoss);
            } else {
                hSv.setBackgroundColor(Color.rgb(255, 96, 96));
//                String toSet = actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists() + " Loss Time: " + minutes + "m CS(" + csScore + "/min) " + superQueue;
                winOrLoss.setText("Loss");
                firstLay.addView(winOrLoss);
            }
            ImageView champIcon = new ImageView(ProfilePage.this);
            champIcon.setImageDrawable(Drawable.createFromPath("C:\\Users\\Richard Sanchez\\AndroidStudioProjects\\LolHelperApp\\app\\src\\main\\res\\drawable\\lolhelpericon.png"));
            firstLay.addView(champIcon);
            TextView lane = new TextView(ProfilePage.this);
            lane.setText(roleR);
            firstLay.addView(lane);
            firstLay.setOrientation(LinearLayout.VERTICAL);
            firstSection.addView(firstLay);

            TextView kDA = new TextView(ProfilePage.this);
            String kdaText = actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists();
            kDA.setText(kdaText);
            secondLay.addView(kDA);
            TextView kR = new TextView(ProfilePage.this);
            if (consScore == 2468) {
                kR.setText("Perfect");
            } else {
                kR.setText(String.valueOf(consScore));
            }
            secondLay.addView(kR);
            secondLay.setOrientation(LinearLayout.VERTICAL);
            secondSection.addView(secondLay);

            ImageView item0 = new ImageView(ProfilePage.this);
            ImageView item1 = new ImageView(ProfilePage.this);
            ImageView item2 = new ImageView(ProfilePage.this);
            ImageView item3 = new ImageView(ProfilePage.this);
            ImageView item4 = new ImageView(ProfilePage.this);
            ImageView item5 = new ImageView(ProfilePage.this);
            thirdOne.addView(item0);
            thirdOne.addView(item1);
            thirdTwo.addView(item2);
            thirdTwo.addView(item3);
            thirdThree.addView(item4);
            thirdThree.addView(item5);
            forThree.addView(thirdOne);
            forThree.addView(thirdTwo);
            forThree.addView(thirdThree);
            thirdSection.addView(forThree);

            ImageView trinket = new ImageView(ProfilePage.this);
            fourthLay.addView(trinket);
            TextView dater = new TextView(ProfilePage.this);
            fourthLay.addView(dater);
            fourthSection.addView(fourthLay);

            horoLay.addView(firstSection);
            horoLay.addView(secondSection);
            horoLay.addView(thirdSection);
            horoLay.addView(fourthSection);

            hSv.addView(horoLay);
            lay.addView(hSv);
        }
    }

}
