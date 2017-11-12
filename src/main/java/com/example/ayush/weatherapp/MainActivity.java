package com.example.ayush.weatherapp;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isThereInternet()){
            //starts web service
            getInfo info = new getInfo();
            info.execute();
        }else{
            Toast.makeText(this, "Internet not enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean isThereInternet(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //check if internet has been enabled
        return manager.getActiveNetworkInfo().isConnected();
    }

    class getInfo extends AsyncTask{

        StringBuilder builder = new StringBuilder();
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        String Weatherurl = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL(Weatherurl);
                URLConnection connection = url.openConnection();
                connection.connect();
                //opens the connection
                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String info = "";
                while ((info = reader.readLine()) != null){
                    builder.append(info);
                    //appends input into a stringBuilder
                }


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            tv1 = (TextView) findViewById(R.id.temperature);
            tv2 = (TextView) findViewById(R.id.kindOfweather_title);
            tv3 = (TextView) findViewById(R.id.kindOfweather_desc);
            tv4 = (TextView) findViewById(R.id.temp_min);
            tv5 = (TextView) findViewById(R.id.temp_max);
            try {
                //gets all the information and formats it
                JSONObject object = new JSONObject(builder.toString());
                JSONArray weather =  object.getJSONArray("weather");
                JSONObject main = object.getJSONObject("main");
                String mainWeather = weather.getJSONObject(0).getString("main");
                String mainWeather_desc = weather.getJSONObject(0).getString("description");
                int temp_min = main.getInt("temp_min");
                int temp_max = main.getInt("temp_max");
                int temperature = main.getInt("temp");
                int temp_celcius = ((temperature - 32) * 5)/9;
                //sets text to the textviews
                tv1.setText("Temperature: " + String.valueOf(temperature)  + " Degrees Farheinheit" + "\n(" + String.valueOf(temp_celcius) + " Degrees Celsius) ");
                tv2.setText(mainWeather);
                tv3.setText(mainWeather_desc);
                tv4.setText("Minimum: " + temp_min + " Degrees Farheinheit");
                tv5.setText("Maximum: " + temp_max + " Degrees Farheinheit");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
