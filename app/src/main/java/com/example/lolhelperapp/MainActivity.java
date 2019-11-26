package com.example.lolhelperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] names = new String[0];
    final String key = "RGAPI-90f853af-92b5-4527-be0e-663430e6ca63";
    private HashMap<Integer, String> champList = new HashMap<Integer, String>() {{
        put(226, "Aatrox");
        put(103, "Ahri");
        put(84, "Akali");
        put(12, "Alistar");
        put(32, "Amumu");
        put(34, "Anivia");
        put(1, "Annie");
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
        put(8, "Vladmir");
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        setTitle("Lol Helper App");
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText mainText = findViewById(R.id.summonerText);
        mainText.setBackgroundColor(Color.WHITE);

        Random r = new Random();
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

        ImageView back = findViewById(R.id.champBackground);
        List<Integer> keysAsArray = new ArrayList<Integer>(champList.keySet());
        back.setImageDrawable(LoadImageFromWebOperations("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/"+ champList.get(keysAsArray.get(r.nextInt(keysAsArray.size()))) +"_0.jpg"));

        for (final String singleName : names) {
            final MainActivity mainAct = this;
            LinearLayout lay = findViewById(R.id.faves);
            Button favoriteButton = new Button(MainActivity.this);
            favoriteButton.setBackgroundColor(Color.rgb(46, 38, 81));
            favoriteButton.setTextColor(Color.WHITE);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 400);
//            lp.bott

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameToSend = singleName.trim();
                    nameToSend = nameToSend.replaceAll("\\s+", "");
                    String stringUrl = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nameToSend + "?api_key=" + key;

                    UserSearchTask mUserTask = new UserSearchTask(mainAct);
                    mUserTask.execute(stringUrl);
                }
            });
            favoriteButton.setText(singleName);
            lay.addView(favoriteButton);
        }
    }

    public void searchName(View view) {
        EditText name;
        name = (EditText) findViewById(R.id.summonerText);
        String nameOfSumm = name.getText().toString().trim();
        nameOfSumm = nameOfSumm.replaceAll("\\s+", "");
        String stringUrl = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nameOfSumm + "?api_key=" + key;

        UserSearchTask mUserTask = new UserSearchTask(this);
        mUserTask.execute(stringUrl);
    }
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

    private class UserSearchTask extends AsyncTask<String, String, String> {
        private String returner;
        public MainActivity mainer;

        public UserSearchTask(MainActivity a) {
            this.mainer = a;
        }

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            System.out.println(strings[0]);
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
            Map<String, String> user = new HashMap<String, String>();
            if (null != returner && !returner.isEmpty()) {
                Bundle b = new Bundle();
                System.out.println("RAN ONCE");
                String[] params = returner.split(",");
                for (String s : params) {
                    b.putString(s.split(":")[0].trim().replaceAll("[{\"]", ""), s.split(":")[1].trim().replaceAll("[}\"]", ""));
                }
                System.out.println(user.values());
                Intent intent = new Intent(new Intent(getApplicationContext(), ProfilePage.class));
                intent.putExtras(b);
                startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainer);
                alertDialogBuilder.setMessage("User could not be found, tap anywhere to continue.");
                AlertDialog shower = alertDialogBuilder.create();
                shower.show();
            }
        }
    }
}
