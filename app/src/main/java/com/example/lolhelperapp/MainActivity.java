package com.example.lolhelperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchName(View view) {
        String key = "RGAPI-19474d33-5c79-4d45-8e50-a8735bc0b977";
        EditText name;
        name = (EditText) findViewById(R.id.editText);
        String nameOfSumm = name.getText().toString().trim();
        nameOfSumm = nameOfSumm.replaceAll("\\s+", "");
        String stringUrl = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nameOfSumm + "?api_key=" + key;

        UserSearchTask mUserTask = new UserSearchTask();
        mUserTask.execute(stringUrl);
    }

    private class UserSearchTask extends AsyncTask<String, String, String> {
        private String returner;

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

        @Override
        protected void onPostExecute(String p) {
            Map<String, String> user = new HashMap<String, String>();
            if(null != returner) {
                Bundle b = new Bundle();
                System.out.println("RAN ONCE");
                String[] params = returner.split(",");
                for (String s : params) {
                    user.put(s.split(":")[0].trim().replaceAll("[{\"]", ""), s.split(":")[1].trim().replaceAll("[}\"]", ""));
                    b.putString(s.split(":")[0].trim().replaceAll("[{\"]", ""), s.split(":")[1].trim().replaceAll("[}\"]", ""));
                }
                System.out.println(user.values());
                Intent intent = new Intent(new Intent(getApplicationContext(), ProfilePage.class));
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        }
    }
}
