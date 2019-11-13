package com.example.lolhelperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String[] names = new String[0];
    final String key = "RGAPI-bec70f8e-6691-400a-af8d-281d5ae5e611";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        for (final String singleName : names) {
            final MainActivity mainAct = this;
            LinearLayout lay = findViewById(R.id.faves);
            Button favoriteButton = new Button(MainActivity.this);
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
        name = (EditText) findViewById(R.id.editText);
        String nameOfSumm = name.getText().toString().trim();
        nameOfSumm = nameOfSumm.replaceAll("\\s+", "");
        String stringUrl = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nameOfSumm + "?api_key=" + key;

        UserSearchTask mUserTask = new UserSearchTask(this);
        mUserTask.execute(stringUrl);
    }

    private class UserSearchTask extends AsyncTask<String, String, String> {
        private String returner;
        public MainActivity mainer;

        public UserSearchTask(MainActivity a){
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
            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainer);
                alertDialogBuilder.setMessage("User could not be found, tap anywhere to continue.");
                AlertDialog shower = alertDialogBuilder.create();
                shower.show();
            }
        }
    }
}
