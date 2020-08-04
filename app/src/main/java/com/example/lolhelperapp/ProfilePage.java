package com.example.lolhelperapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.lolhelperapp.models.MatchList;
import com.example.lolhelperapp.models.Participant;
import com.example.lolhelperapp.models.ParticipantIdentity;
import com.example.lolhelperapp.models.SingleMatch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ProfilePage extends AppCompatActivity {

    final String key = "RGAPI-d548b3f3-42c9-4e23-8b3e-59346a21ae62";

    private String[] names = new String[0];

    private HashMap<Integer, String> champList = new HashMap<Integer, String>() {{
        put(226, "Aatrox");
        put(103, "Ahri");
        put(84, "Akali");
        put(12, "Alistar");
        put(32, "Amumu");
        put(34, "Anivia");
        put(1, "Annie");
        put(523, "Aphelios");
        put(22, "Ashe");
        put(136, "AurelionSol");
        put(268, "Azir");
        put(432, "Bard");
        put(53, "Blitzcrank");
        put(63, "Brand");
        put(201, "Braum");
        put(51, "Caitlyn");
        put(164, "Camille");
        put(69, "Cassiopeia");
        put(31, "Chogath");
        put(42, "Corki");
        put(122, "Darius");
        put(131, "Diana");
        put(119, "Draven");
        put(36, "DrMundo");
        put(245, "Ekko");
        put(60, "Elise");
        put(28, "Evelynn");
        put(81, "Ezreal");
        put(9, "Fiddlesticks");
        put(114, "Fiora");
        put(105, "Fizz");
        put(3, "Galio");
        put(41, "Gangplank");
        put(86, "Garen");
        put(150, "Gnar");
        put(79, "Gragas");
        put(104, "Graves");
        put(120, "Hecarim");
        put(74, "Heimerdinger");
        put(420, "Illaoi");
        put(39, "Irelia");
        put(427, "Ivern");
        put(40, "Janna");
        put(59, "JarvanIV");
        put(24, "Jax");
        put(126, "Jayce");
        put(202, "Jhin");
        put(222, "Jinx");
        put(145, "Kaisa");
        put(429, "Kalista");
        put(43, "Karma");
        put(30, "Karthus");
        put(38, "Kassadin");
        put(55, "Katarina");
        put(10, "Kayle");
        put(141, "Kayn");
        put(85, "Kennen");
        put(121, "Khazix");
        put(203, "Kindred");
        put(240, "Kled");
        put(96, "KogMaw");
        put(7, "Leblanc");
        put(64, "LeeSin");
        put(89, "Leona");
        put(876, "Lillia");
        put(127, "Lissandra");
        put(236, "Lucian");
        put(117, "Lulu");
        put(99, "Lux");
        put(54, "Malphite");
        put(90, "Malzahar");
        put(57, "Maokai");
        put(11, "MasterYi");
        put(21, "MissFortune");
        put(62, "MonkeyKing");
        put(82, "Mordekaiser");
        put(25, "Morgana");
        put(267, "Nami");
        put(75, "Nasus");
        put(111, "Nautilus");
        put(518, "Neeko");
        put(76, "Nidalee");
        put(56, "Nocturne");
        put(20, "Nunu");
        put(2, "Olaf");
        put(61, "Orianna");
        put(516, "Ornn");
        put(80, "Pantheon");
        put(78, "Poppy");
        put(555, "Pyke");
        put(246, "Qiyana");
        put(133, "Quinn");
        put(497, "Rakan");
        put(33, "Rammus");
        put(421, "RekSai");
        put(58, "Renekton");
        put(107, "Rengar");
        put(92, "Riven");
        put(68, "Rumble");
        put(13, "Ryze");
        put(113, "Sejuani");
        put(235, "Senna");
        put(875, "Sett");
        put(35, "Shaco");
        put(98, "Shen");
        put(102, "Shyvana");
        put(27, "Singed");
        put(14, "Sion");
        put(15, "Sivir");
        put(72, "Skarner");
        put(37, "Sona");
        put(16, "Soraka");
        put(50, "Swain");
        put(517, "Sylas");
        put(134, "Syndra");
        put(223, "TahmKench");
        put(163, "Taliyah");
        put(91, "Talon");
        put(44, "Taric");
        put(17, "Teemo");
        put(412, "Thresh");
        put(18, "Tristana");
        put(48, "Trundle");
        put(23, "Tryndamere");
        put(4, "TwistedFate");
        put(29, "Twitch");
        put(77, "Udyr");
        put(6, "Urgot");
        put(110, "Varus");
        put(67, "Vayne");
        put(45, "Veigar");
        put(161, "Velkoz");
        put(254, "Vi");
        put(112, "Viktor");
        put(8, "Vladimir");
        put(106, "Volibear");
        put(19, "Warwick");
        put(498, "Xayah");
        put(101, "Xerath");
        put(5, "XinZhao");
        put(157, "Yasuo");
        put(83, "Yorick");
        put(350, "Yuumi");
        put(154, "Zac");
        put(238, "Zed");
        put(115, "Ziggs");
        put(26, "Zilean");
        put(142, "Zoe");
        put(143, "Zyra");
    }};

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
        setTitle(bundle.getString("name") + "'s Profile");


        if (bundle != null) {
            summoner.setText(bundle.getString("name"));
            level.setText(bundle.getString("summonerLevel"));
            String ico = "http://ddragon.leagueoflegends.com/cdn/9.23.1/img/profileicon/" + bundle.getString("profileIconId") + ".png";
            System.out.println(ico);
            try {
                URL url = new URL(ico);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                profileIcon.setImageBitmap(bmp);
                profileIcon.getLayoutParams().height = 220;
                profileIcon.getLayoutParams().width = 220;
                profileIcon.requestLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
            accId = bundle.getString("accountId");
        } else {
            System.out.println("ACTUAL ERROR");
        }

        String stringUrl = "https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + accId + "?api_key=" + key;
        final MatchHistoryTask matches = new MatchHistoryTask();
        if (!accId.isEmpty()) {
            matches.execute(stringUrl, accId);
        }

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
        for (String n : names) {
            if (n.equals(bundle.getString("name"))) {
//                Button favoriteButton = findViewById(R.id.floatingActionButton);
//                favoriteButton.setBackgroundResource(R.drawable.btn_start_on);
            }
        }

        FloatingActionButton fab = findViewById(R.id.backer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matches.cancel(true);
                matches.singleMatch.cancel(true);
                Intent intent = new Intent(new Intent(getApplicationContext(), MainActivity.class));
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Simple function for adding an account to favorites
     *
     * @param view Passes current view
     */
    public void favoriteAccount(View view) {
        boolean nameWritten = false;
        Bundle bundle = getIntent().getExtras();
        String filename = "favorites.txt";
        String fileContents = bundle.getString("name");
        fileContents += "#";
        for (String n : names) {
            if (n.equals(bundle.getString("name"))) {
                nameWritten = true;
                break;
            }
        }
        if (nameWritten) {
            String nameHolder = "";
            for (String hold : names) {
                if (!hold.equals(bundle.getString("name"))) {
                    nameHolder += hold + "#";
                }
            }
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
                outputStreamWriter.write(nameHolder);
                outputStreamWriter.close();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Removed from Favorites");
                AlertDialog shower = alertDialogBuilder.create();
                shower.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_APPEND));
                outputStreamWriter.write(fileContents);
                outputStreamWriter.close();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Added to Favorites");
                AlertDialog shower = alertDialogBuilder.create();
                shower.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Moves on to the radar activity
     *
     * @param view Passes current view
     */
    public void toRadar(View view) {
        Intent intent = new Intent(new Intent(getApplicationContext(), RadarActivity.class));
        startActivity(intent);
    }

    /**
     * Working on images from the web
     *
     * @param url URL for image
     * @return Drawable grabbed from the URL
     */
    public static Drawable LoadImageFromWebOperations(String url) {
//        Drawable drawnIcon = null;
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable drawnIcon = Drawable.createFromStream(is, "src name");
            return drawnIcon;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * returns image as a bitmap
     *
     * @param src image source
     * @return the image
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches for a users match history using the api then calls the single match activity on each ID
     */
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
        private SingleMatchTask singleMatch;

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
                        singleMatch = new SingleMatchTask((i + 1));
                        singleMatch.execute(stringUrl, accId, String.valueOf(matches.getMatches().get(i).getTimestamp()), matches.getMatches().get(i).getRole(), matches.getMatches().get(i).getLane());

                    } else if (games < 150) {
                        games++;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Preforms a call to the API for a matches stats based on ID then fills the list
     */
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
        private int gameNumber;
        private String laneR;

        SingleMatchTask(int i) {
            gameNumber = i;
        }

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("DOING SINGLE MATCH");
            String current = "";
            accId = strings[1];
            roleR = strings[3];
            laneR = strings[4];
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
            LinearLayout lay = (LinearLayout) findViewById(R.id.layerout);
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
            long seconds = looker.getGameDuration() - (minutes * 60);
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
                aggScore = (((double) actualHolder.getStats().getKills() + (double) actualHolder.getStats().getAssists()) / (double) team1kills) * 10;
            } else if (team == 1 && team2kills != 0) {
                aggScore = (((double) actualHolder.getStats().getKills() + (double) actualHolder.getStats().getAssists()) / (double) team2kills) * 10;
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
                    superQueue = "Solo";
                    break;
                case 430:
                    superQueue = "Blind";
                    break;
                case 440:
                    superQueue = "Flex";
                    break;
                case 450:
                    superQueue = "ARAM";
                    break;
                default:
                    break;
            }

            System.out.println("SCORES: CS: " + csScore + " VERSATILITY: " + versScore + " CONSISTENCY: " + consScore + " OBJECTIVES: " + objecScore + " AGGRESSION: " + aggScore + " VISION: " + visScore);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("singleMatch");
            myRef.child(String.valueOf(looker.getGameId())).setValue(looker);
            ProgressBar loader = findViewById(R.id.gameProgress);
            TextView loaderText = findViewById(R.id.loadingText);

            int id = 0;
            switch (gameNumber) {
                case 1:
                    id = R.id.match1;
                    loader.setProgress(5);
                    break;
                case 2:
                    id = R.id.match2;
                    loader.setProgress(10);
                    break;
                case 3:
                    id = R.id.match3;
                    loader.setProgress(15);
                    break;
                case 4:
                    id = R.id.match4;
                    loader.setProgress(20);
                    break;
                case 5:
                    id = R.id.match5;
                    loader.setProgress(25);
                    break;
                case 6:
                    id = R.id.match6;
                    loader.setProgress(30);
                    break;
                case 7:
                    id = R.id.match7;
                    loader.setProgress(35);
                    break;
                case 8:
                    id = R.id.match8;
                    loader.setProgress(40);
                    break;
                case 9:
                    id = R.id.match9;
                    loader.setProgress(45);
                    break;
                case 10:
                    id = R.id.match10;
                    loader.setProgress(50);
                    break;
                case 11:
                    id = R.id.match11;
                    loader.setProgress(55);
                    break;
                case 12:
                    id = R.id.match12;
                    loader.setProgress(55);
                    break;
                case 13:
                    id = R.id.match13;
                    loader.setProgress(60);
                    break;
                case 14:
                    id = R.id.match14;
                    loader.setProgress(65);
                    break;
                case 15:
                    id = R.id.match15;
                    loader.setProgress(70);
                    break;
                case 16:
                    id = R.id.match16;
                    loader.setProgress(75);
                    break;
                case 17:
                    id = R.id.match17;
                    loader.setProgress(80);
                    break;
                case 18:
                    id = R.id.match18;
                    loader.setProgress(85);
                    break;
                case 19:
                    id = R.id.match19;
                    loader.setProgress(93);
                    break;
                case 20:
                    id = R.id.match20;
                    loaderText.setText("Done");
                    loader.setProgress(100);
                    break;
            }

            View tester = findViewById(id);
            TextView kdaText = (TextView) tester.findViewById(R.id.kdaText);
            TextView winText = tester.findViewById(R.id.wlText);
            TextView csTotalText = tester.findViewById(R.id.totalCs);
            TextView csPerMinuteText = tester.findViewById(R.id.csPerMin);
            TextView laneText = tester.findViewById(R.id.laneText);
            ImageView champIcon = tester.findViewById(R.id.characterIcon);
            TextView killRatio = tester.findViewById(R.id.killRateText);
            ImageView item1 = tester.findViewById(R.id.item1);
            ImageView item2 = tester.findViewById(R.id.item2);
            ImageView item3 = tester.findViewById(R.id.item3);
            ImageView item4 = tester.findViewById(R.id.item4);
            ImageView item5 = tester.findViewById(R.id.item5);
            ImageView item6 = tester.findViewById(R.id.item6);
            ImageView trinket = tester.findViewById(R.id.trinket);
            TextView visionStuff = tester.findViewById(R.id.visionText);
            TextView nameSpot = tester.findViewById(R.id.champName);
//            TextView rolerText = tester.findViewById(R.id.roleText);
            TextView timeStamp = tester.findViewById(R.id.gameTimestamp);
            TextView charLevel = tester.findViewById(R.id.charLevelText);


            DecimalFormat formatDecimal = new DecimalFormat("##.##");
            tester.setVisibility(View.VISIBLE);

            csTotalText.setText("Total CS: " + (actualHolder.getStats().getNeutralMinionsKilled() + actualHolder.getStats().getTotalMinionsKilled()));
            csPerMinuteText.setText("(" + csScore + "/Min)");
            laneText.setText(laneR);
            killRatio.setText("(" + formatDecimal.format(consScore) + ") KR");
            kdaText.setText(actualHolder.getStats().getKills() + "/" + actualHolder.getStats().getDeaths() + "/" + actualHolder.getStats().getAssists());

            String champName = "";
            for (int key : champList.keySet()) {
                if (key == actualHolder.getChampionId()) {
                    champName = champList.get(key);
                }
            }

            champIcon.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/champion/" + champName + ".png"));
            champIcon.getLayoutParams().height = 150;
            champIcon.getLayoutParams().width = 150;
            champIcon.requestLayout();

            item1.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/item/" + actualHolder.getStats().getItem0() + ".png"));
            item1.getLayoutParams().height = 90;
            item1.getLayoutParams().width = 90;
            item1.requestLayout();

            item2.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/item/" + actualHolder.getStats().getItem1() + ".png"));
            item2.getLayoutParams().height = 90;
            item2.getLayoutParams().width = 90;
            item2.requestLayout();

            item3.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/item/" + actualHolder.getStats().getItem2() + ".png"));
            item3.getLayoutParams().height = 90;
            item3.getLayoutParams().width = 90;
            item3.requestLayout();

            item4.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/item/" + actualHolder.getStats().getItem3() + ".png"));
            item4.getLayoutParams().height = 90;
            item4.getLayoutParams().width = 90;
            item4.requestLayout();

            item5.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/item/" + actualHolder.getStats().getItem4() + ".png"));
            item5.getLayoutParams().height = 90;
            item5.getLayoutParams().width = 90;
            item5.requestLayout();

            item6.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/item/" + actualHolder.getStats().getItem5() + ".png"));
            item6.getLayoutParams().height = 90;
            item6.getLayoutParams().width = 90;
            item6.requestLayout();

            trinket.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/9.23.1/img/item/" + actualHolder.getStats().getItem6() + ".png"));
            trinket.getLayoutParams().height = 90;
            trinket.getLayoutParams().width = 90;
            trinket.requestLayout();

            if (champName.equals("MonkeyKing")) {
                champName = "Wukong";
            }

            String str = String.format("%02d", seconds);
//            rolerText.setText(laneR);
            timeStamp.setText(String.valueOf(minutes) + "m " + str + "s");
//            System.out.println(champName);
//            String setter = getString(R.string.champ_and_level, champName);
            nameSpot.setText(champName);
            charLevel.setText("(" + actualHolder.getStats().getChampLevel() + ")");

            visionStuff.setText(String.valueOf(actualHolder.getStats().getVisionScore()));

            if (actualHolder.getStats().isWin()) {
                tester.setBackgroundColor(Color.rgb(124, 192, 217));
                String toSet = getString(R.string.wl_and_queue, "Win", superQueue);
                winText.setText(toSet);
            } else {
                tester.setBackgroundColor(Color.rgb(255, 96, 96));
                String toSet = getString(R.string.wl_and_queue, "Loss", superQueue);
                winText.setText(toSet);
            }
        }
    }

}
