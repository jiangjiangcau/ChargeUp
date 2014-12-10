package com.example.chargeuplogin;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chargeuplogin.library.DatabaseHandler;
import com.example.chargeuplogin.library.UserFunctions;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FindAChargerActivity extends ActionBarActivity {
	
	
	
	
	
	public void checkRequest(){
		// check pendingmessage on web-server
		// if type==2
		String type="";
		if(type.equals("2")){
			NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
	    	.setSmallIcon(R.drawable.stat_sample)
	    	.setContentTitle("New charger request");			
			
			//Someone wants to borrow your charger
		}
	}
	
	ListView usersListView;
	private ArrayAdapter<String> usersAdapter;
	String username;
	
	Button btnCancel_find;
	//Button btn_findACharger;
	//ListView chargersListView;
	Spinner mySpinner;
	private ArrayAdapter<String> chargersAdapter;
	
	private JSONArray devices;
	String deviceId="";
	
	@SuppressLint("UseSparseArrays")
	HashMap<Integer,String> chargersid = new HashMap<Integer,String>();
	String sessionId,chargerId;
	
	HashMap<String,Integer> map = new HashMap<String,Integer>();
	HashMap<Integer,String> device_charger = new HashMap<Integer,String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_a_charger);
		
		//////////////////
		btnCancel_find = (Button)findViewById(R.id.btnCancel_find);
		//btn_findACharger = (Button)findViewById(R.id.findACharger);
		
		usersListView = (ListView)findViewById(R.id.userListView);
		usersAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		usersListView.setAdapter(usersAdapter);
		
		//chargersListView = (ListView)findViewById(R.id.chargesListView);
		mySpinner = (Spinner)findViewById(R.id.spinner_find);
		
		chargersAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		//chargersListView.setAdapter(chargersAdapter);
		mySpinner.setAdapter(chargersAdapter);
		
			
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        HashMap<String,String> user = new HashMap<String, String>(); //////
        user = db.getUserDetails();
        username = user.get("uname");
        sessionId = user.get("uid");
        //System.err.println("jiang 1: "+username+", "+sessionId);
		
		// get devices from server
		new ProcessGetDevices().execute();
		
		// Get Chargers
		
		getChargers();
		
		mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position>0){
					deviceId = device_charger.get(position-1);
					//EditText  deviceid_find = (EditText)findViewById(R.id.deviceid_find);
					//deviceid_find.setText(deviceId);
					dialog();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
		}});

		btnCancel_find.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(v.getContext(),LoginActivity.class);
				startActivity(myIntent);
				finish();
			}
		});		
		////////////////////
		

		
//		btn_findACharger.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				new ProcessFindACharger().execute();
//			}
//		});
		
		/*usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Messaging.class);
				String friend_name = (String) parent.getAdapter().getItem(position);
				i.putExtra("friend_name",friend_name);
				i.putExtra("username", username);
				i.putExtra("sessionid", sessionId);
				//System.err.println("friend_name: "+friend_name+",username: "+username+",sessionId: "+username);
				startActivity(i);
			}
		});*/
		
	}
	
	protected void dialog(){
		AlertDialog.Builder builder = new Builder(FindAChargerActivity.this);
		builder.setMessage("Find this charger?");
		builder.setTitle("Warning");
		builder.setPositiveButton("Yes", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				new ProcessFindACharger().execute();	
			}			
		});
		builder.setNegativeButton("No", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}			
		});
		builder.create().show();
	}
	
	
	private class ProcessFindACharger extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        
        //String deviceId;/////
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            //EditText  deviceid_find = (EditText)findViewById(R.id.deviceid_find);////
            //deviceId = deviceid_find.getText().toString();////
            
            
            pDialog = new ProgressDialog(FindAChargerActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.findACharger(sessionId,deviceId);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	if (json.getString("status") != null){
            		String status = json.getString("status"); 
            		if(status.equals("success")){
            			//pDialog.setMessage("Loading...");
            			//pDialog.setTitle("Getting Data");
            			
            			JSONArray users = json.getJSONArray("users");
            			usersAdapter.clear();
            			for(int i=0; i<users.length(); i++){
            				JSONObject userObj = (JSONObject)users.get(i);
            				String user = userObj.getString("username");
            				usersAdapter.add(user);
            				usersAdapter.notifyDataSetChanged();
            			}            			
            			if(users.length()==0){
            				usersAdapter.add("no one has this charger now");
            				usersAdapter.notifyDataSetChanged();
            			}
            			pDialog.dismiss();
            			//updateErrorMsg.setText("Update Success");
            		}else{
            			pDialog.dismiss();
            			//updateErrorMsg.setText(json.getString("error"));
            			String tmp = json.getString("error");
            			if(tmp.equals("Device id is invalid.")){
            				usersAdapter.clear();
            				usersAdapter.add("no one has this charger now");
            				usersAdapter.notifyDataSetChanged();
            				Toast.makeText(FindAChargerActivity.this, "no one has this charger now", Toast.LENGTH_SHORT).show();
            			}else{
            				usersAdapter.clear();
            				usersAdapter.add(json.getString("error"));
            				usersAdapter.notifyDataSetChanged();
            				Toast.makeText(FindAChargerActivity.this, json.getString("error"), Toast.LENGTH_SHORT).show();
            			}
            		}
            	}
            	else{
                    pDialog.dismiss();
                    usersAdapter.clear();
    				usersAdapter.add("Error occured in findACharger");
    				usersAdapter.notifyDataSetChanged();
                    //updateErrorMsg.setText("Error occured in Update");
                    Toast.makeText(FindAChargerActivity.this, "Error occured in findACharger", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_acharger, menu);
		return true;
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
	
	///////////////////////////////////
		
	public void getChargers(){
        new process_getChargers().execute();
    }
	
	private class process_getChargers extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

//        String sessionId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
//            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//            HashMap<String,String> user = new HashMap<String, String>(); //////
//            user = db.getUserDetails();
//            sessionId = user.get("uid");
            
            pDialog = new ProgressDialog(FindAChargerActivity.this);
            //pDialog.setTitle("Contacting Servers");
            //pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getChargers(sessionId); //////////
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	if (json.getString("status") != null){
            		String status = json.getString("status"); 
            		if(status.equals("success")){
            			//pDialog.setMessage("Loading...");
            			//pDialog.setTitle("Getting Data");            			
            			pDialog.dismiss();
            			
            			JSONArray chargers = json.getJSONArray("chargers");
            			//{"chargers":[{"chargerid":"15","deviceid":"1"},{"chargerid":"16","deviceid":"1"}],"status":"success"}
            			
            			
            			//HashMap<String,Integer> map = new HashMap<String,Integer>();
            			for(int i=0; i<devices.length(); i++){
            				JSONObject dev = (JSONObject)devices.get(i);
            				String key = dev.getString("deviceid");
            				if(!map.containsKey(key)){
            					map.put(key, i);
            				}
            			}
            			
            			chargersAdapter.clear();
            			chargersAdapter.add("Choose one charger:");
            			chargersid.clear();
            			device_charger.clear();
            			if(chargers.length()==0){
            				chargersAdapter.clear();
            				chargersAdapter.add("You have no any registered charger");
            				chargersAdapter.notifyDataSetChanged();
            			}
            			for(int i=0; i<chargers.length(); i++){
            				JSONObject ch = (JSONObject)chargers.get(i);
            				if(!chargersid.containsKey(i)){
            					chargersid.put(i, ch.getString("chargerid"));
            				}
            				//System.err.println("ch "+i+": "+ch.getString("deviceid"));
            				int idx = map.get(ch.getString("deviceid"));
            				device_charger.put(i, ch.getString("deviceid"));
            				
            				System.err.println("idx = "+idx);//zhijiang
            				
            				JSONObject dev = (JSONObject)devices.get(idx);
            				String type;
            				if(dev.getString("type").equals("1")){
            					type = "smartphone";
            				}else if(dev.getString("type").equals("2")){
            					type = "tablet";
            				}else{
            					type = "laptop";
            				}
            				chargersAdapter.add("Charger "+(i+1)+": \n"+type+","+
            						dev.getString("brand")+","+dev.getString("model"));
            				chargersAdapter.notifyDataSetChanged();
            				
            			}
            			
            			//errormsg_getChargers.setText("You have "+chargers.length()+" kinds of Chargers");
            			
            		}else{
            			pDialog.dismiss();
            			//errormsg_getChargers.setText(json.getString("error"));
            		}
            	}
            	else{
                    pDialog.dismiss();
                    //errormsg_getChargers.setText("Error occured in getChargers");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
       }
	}
	
	
    // getDevices
    private class ProcessGetDevices extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String sessionId, type;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            type = "0";
            
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            HashMap<String,String> user = new HashMap<String, String>(); //////
            user = db.getUserDetails();
            sessionId = user.get("uid");
            
            pDialog = new ProgressDialog(FindAChargerActivity.this);
//            pDialog.setTitle("Contacting Servers");
//            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
//            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getDevices(sessionId, type); //////////
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	if (json.getString("status") != null){
            		String status = json.getString("status"); 
            		if(status.equals("success")){
            			pDialog.setMessage("Loading...");
            			pDialog.setTitle("Getting Data");
            			
            			pDialog.dismiss();
            			
            			//errormsg_getChargers.setText("get devices successfully!");
            			
            			devices = json.getJSONArray("devices");                    			
            		}else{
            			pDialog.dismiss();
            			//errormsg_getChargers.setText(json.getString("error"));
            		}
            	}
            	else{
                    pDialog.dismiss();
                    //errormsg_getChargers.setText("Error occured in getting devices");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }
    


}
