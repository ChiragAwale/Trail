package com.chiragawale.trail.data;


import android.os.AsyncTask;
import android.util.Log;

import com.chiragawale.trail.data.model.LoggedInUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private final String LOGIN_API_URL = "https://drt-trail-app.herokuapp.com/api_user_login/";
    public Result<LoggedInUser> login(String username, String password) {
        String res = "";
        try {
            // TODO: handle loggedInUser authentication
            Log.e("MSG rep", "here");
            LoginTask loginTask = new LoginTask();
            try {
                res = loginTask.execute(username, password).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JSONObject json = new JSONObject(res);
            if(json.getString("Message").equalsIgnoreCase("Successful Authentication 200 OK")){
                LoggedInUser user =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                 json.getString("username"));
                return new Result.Success<>(user);
            } else{
                return new Result.Error(new IOException("Error logging in"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            return api_login(params[0],params[1]);
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }

        public String api_login(String username, String password)  {
            URL url = null;
            String resp = "";
            String inputLine = "";
            Log.e("Username",username);
            Log.e("Password", password);
            //List<Long> uploadedEntries = new ArrayList<>();
            try {
                url = new URL(LOGIN_API_URL);
            } catch (MalformedURLException e) {
                Log.e("Login Data Source", e.getMessage());
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();
                Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                String json = "{ \"username\":\"" + username + "\",";
                json += "\"password\":\"" + password + "\"}";
                Log.e("Credentials",json);
                writer.write(json);
                writer.flush();
                Log.e("STATUS", String.valueOf(conn.getResponseCode()));
                Log.e("MSG", conn.getResponseMessage());
                InputStreamReader streamReader = new
                        InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                resp = stringBuilder.toString();
                reader.close();
                streamReader.close();
                writer.close();
                conn.disconnect();
                Log.e("MSG",resp);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Login Data Source", e.getMessage());
            }
            return resp;
        }
    }
}
