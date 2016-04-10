package com.usingrestapi.toinews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static String url = "http://timesofindia.indiatimes.com/feeds/newsdefaultfeeds.cms?feedtype=sjson";

    //Json Node Names

    private static final String TAG_NEWSITEM = "NewsItem";
    private static final String TAG_HEAD_LINE= "HeadLine";
    JSONArray cars = null;
    JSONObject c;
    TextView mtextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtextview = (TextView)findViewById(R.id.mytext);
        new NiksAynctask().execute(url);
    }

    class NiksAynctask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader niksreader = null;
            try {
                URL myurl = new URL(urls[0]);
                connection = (HttpURLConnection) myurl.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                niksreader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                 String line = "";
                 while((line = niksreader.readLine())!=null)
                 {
                     buffer.append(line);
                 }
              String niksFinalJson = buffer.toString();

                JSONObject niksJSONObject = new JSONObject(niksFinalJson);
                JSONArray niksJSONArray = niksJSONObject.getJSONArray("NewsItem");
                StringBuffer finalBufferedData = new StringBuffer();
                for(int i=0; i<niksJSONArray.length();i++) {
                    JSONObject finalObject = niksJSONArray.getJSONObject(i);

                    String Headline = finalObject.getString("HeadLine");

                    finalBufferedData.append(i+1+". "+Headline+"\n\n");
                }
                return finalBufferedData.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                {
                    connection.disconnect();
                }
                if (niksreader!=null)
                {
                    try {
                        niksreader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mtextview.setText(s);
        }
    }

}
