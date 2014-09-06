package com.example.proxi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        PieGraph pg = (PieGraph)findViewById(R.id.graph);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#00485B"));
        slice.setValue(2);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#A3CEDA"));
        slice.setValue(3);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#0095A6"));
        slice.setValue(8);
        pg.addSlice(slice);
        
        int hole = 180;
        pg.setInnerCircleRatio(hole);
        int padding = 1;
        pg.setPadding(padding);
        
        Button btn = (Button) findViewById(R.id.post_btn);
        btn.setOnClickListener(new OnClickListener( ) {
        	
        	@Override
        	public void onClick(View v)
        	{
				new MyAsyncTask().execute();		
        	}
       
        });
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
    	 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpPost();
			return null;
		}
 
		public void HttpPost(){
		    
	    	HttpClient client = new DefaultHttpClient();
	        
	  	  	HttpPost post = new HttpPost("http://johanhenriksson.se/report.php");
	      
	  	  try {
	        
	  		  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	  		  nameValuePairs.add(new BasicNameValuePair("Start_Lat","123456789"));
	  		  nameValuePairs.add(new BasicNameValuePair("Start_Long","123456789"));
	  		  nameValuePairs.add(new BasicNameValuePair("End_Lat","123456789"));
	  		  nameValuePairs.add(new BasicNameValuePair("End_Long","123456789"));
	  		  nameValuePairs.add(new BasicNameValuePair("User","hej"));
	  		  
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