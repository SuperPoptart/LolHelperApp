package com.example.lolhelperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private String key = "RGAPI-db34f005-bf2b-4dbb-9273-34a52680e9cb";
    //    private String stringUrl = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+Sname+"?api_key=" + key;
    private String lameUrl = "https://u.gg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchName(View view) {
        name = (EditText) findViewById(R.id.editText);
        String nameOfSumm = name.getText().toString().trim();
        nameOfSumm = nameOfSumm.replaceAll("\\s+", "");
        String stringUrl = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nameOfSumm + "?api_key=" + key;

        UserSearchTask mUserTask = new UserSearchTask();
        Map<String, String> user = new HashMap<String, String>();
        mUserTask.execute(stringUrl);
        if(null != mUserTask.returner) {
            String[] params = mUserTask.returner.split(",");
            for (String s : params) {
                user.put(s.split(":")[0].trim().replaceAll("[{\"]", ""), s.split(":")[1].trim().replaceAll("[}\"]", ""));
            }
            String ico = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/" + (String) user.get("profileIconId") + ".png ";
            System.out.println(user.values());
        }
    }

    private class UserSearchTask extends AsyncTask<String, String, String> {
        public String returner;

        public UserSearchTask() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
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
    }
}
