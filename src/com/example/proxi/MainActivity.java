package com.example.proxi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;



public class MainActivity extends Activity {

	Intent i;
	String tot_score;
	TextView txt;
	String username;

	
	private Handler mHandler = new Handler ();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        PieGraph pg = (PieGraph)findViewById(R.id.graph);
        
        //Cykel
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#00485B"));
        slice.setValue(10);
        pg.addSlice(slice);
        
        //Gång
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#A3CEDA"));
        slice.setValue(20);
        pg.addSlice(slice);
        
        //Löpning
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#0095A6"));
        slice.setValue(30);
        pg.addSlice(slice);
        
        int hole = 180;
        pg.setInnerCircleRatio(hole);
        int padding = 1;
        pg.setPadding(padding);
        
        final String Start_Lat = "58.394501";
        final String Start_Long = "15.561850";
        final String End_Lat = "58.400888";
        final String End_Long = "15.572107";
        final String User = "Johan";
        final String Start_Time = "2014-09-07 12:46";
        final String End_Time = "2014-09-07 12:47";

        txt = (TextView) findViewById(R.id.score_txt);
        
        Button btn = (Button) findViewById(R.id.post_btn);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new MyAsyncPost().execute(new String[]{Start_Lat, Start_Long, End_Lat, End_Long, User, Start_Time, End_Time});
            }

        });

        username = "Johan";
        String LongLat = HttpGet(username);
        
        txt.setText(tot_score);
        
        
        
    }
    
    private final Runnable mUpdateUITimerTask = new Runnable() {
        public void run() {
        	Log.e("bajs", "bärs");
            HttpGet(username);
            txt.setText(tot_score);
        }
    };
    
    
    
    private String HttpGet(String username) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        mHandler.postDelayed(mUpdateUITimerTask, 5000);

        String result = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("User",username));

        String sInfo = "";

        //http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.johanhenriksson.se/recieve.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result=sb.toString();

                Log.e("log_tag", result);

                // parse json data
                try {
                    JSONObject json_data = new JSONObject(result);
                    sInfo = "Score: " + json_data.getString("score");
                    tot_score = json_data.getString("score");
                    
                    Log.e("log_tag", sInfo);
                } catch (Exception e) {
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }

            }catch(Exception e){
                Log.e("log_tag", "Error converting result "+e.toString());
            }


        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
        return sInfo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class MyAsyncPost extends AsyncTask<String, Integer, Double>{
   	 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpPost(params);
			return null;
		}
 
		public void HttpPost(String [] params){
		    
	    	HttpClient client = new DefaultHttpClient();
	        
	  	  	HttpPost post = new HttpPost("http://johanhenriksson.se/report.php");
	      
	  	  try {
	        
	  		  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	  		  nameValuePairs.add(new BasicNameValuePair("Start_Lat", params[0]));
	  		  nameValuePairs.add(new BasicNameValuePair("Start_Long", params[1]));
	  		  nameValuePairs.add(new BasicNameValuePair("End_Lat", params[2]));
	  		  nameValuePairs.add(new BasicNameValuePair("End_Long", params[3]));
	  		  nameValuePairs.add(new BasicNameValuePair("User", params[4]));
	  		  nameValuePairs.add(new BasicNameValuePair("Start_Time", params[5]));
	  		  nameValuePairs.add(new BasicNameValuePair("End_Time", params[6]));
	  		  
	  		  //We need to encode our data into valid URL format before making HTTP request.		  
	  		  post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	   
	  		  client.execute(post);
 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}