package com.example.chargeuplogin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chargeuplogin.chatting.AwesomeAdapter;
import com.example.chargeuplogin.chatting.Message;
import com.example.chargeuplogin.library.UserFunctions;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Messaging extends ActionBarActivity {
	
	EditText message;
	EditText messageHistory;
	Button btn_sendMessageButton;
	//Button btn_getMessageButton;
	
	String friend_name;
	String username;
	String sessionid;

	
	ArrayList<Message> messages;
	AwesomeAdapter adapter;
	ListView list;
	
	private Timer timer;
	private final int UPDATE_TIME_PERIOD = 10000; //15000
	
	
	public class MessageReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle extra = intent.getExtras();
			
//			System.err.println("jiang MessageReceiver");
			
			
//			if (extra == null)
			{
				System.err.println("intent.getAction():"+intent.getAction());
//				String action = intent.getAction();
//				if(action.equals("GET_MESSAGES")){
					//checkRequest();
					new ProcessGettingMessage().execute();
//					System.err.println("GET_MESSAGES intent is called");
//				}				
			}
		}
		
	};
	public MessageReceiver messageReceiver = new MessageReceiver();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messaging_screen);
		
		list = (ListView)findViewById(R.id.list);
		/////////////////////////////
		messages = new ArrayList<Message>();		
		adapter = new AwesomeAdapter(this, messages);
		list.setAdapter(adapter);
		// send chatting history
		
		//adapter.notifyDataSetChanged();
		////////////////////////////
		

		IntentFilter i = new IntentFilter();
		i.addAction("GET_MESSAGES");
		registerReceiver(messageReceiver, i);
		
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			friend_name = extras.getString("friend_name");
			username = extras.getString("username");
			sessionid = extras.getString("sessionid");
			setTitle("Chatting with " + friend_name);
		}
		
		System.err.println("jiang username: "+username);
		new ProcessGettingMessage().execute();
		
		message = (EditText)findViewById(R.id.message);
//		messageHistory = (EditText)findViewById(R.id.messageHistory);
		btn_sendMessageButton = (Button)findViewById(R.id.sendMessageButton);
		//btn_getMessageButton = (Button)findViewById(R.id.getMessageButton);
		
		
		
		btn_sendMessageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
		
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      new ProcessGettingMessage().execute(); 
                  }
              });
			}
		}, new Date(), UPDATE_TIME_PERIOD);

		
	}
	
	
//	protected void onDestroy(Bundle savedInstanceState) {
//		timer.cancel();
//	}
	
	private class ProcessSendingMessage extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

//        String sessionId, deviceId;
        String text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            text = message.getText().toString();
            
            pDialog = new ProgressDialog(Messaging.this);
            //pDialog.setTitle("Contacting Servers");
            //pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.sendMessage(sessionid, friend_name, text);
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
            			
            			message.setText(null);
            			
            			// let sender immediately see the sent message
            			messages.add(new Message(text,true));        				
        				adapter.notifyDataSetChanged();
        				// android listview auto scroll to bottom
        				if(messages.size()>=1){
        					list.setSelection(messages.size()-1);
        				}
            			
            			pDialog.dismiss();
            			//updateErrorMsg.setText("Update Success");
            		}else{
            			pDialog.dismiss();
            			//updateErrorMsg.setText(json.getString("error"));
            			Toast.makeText(Messaging.this, json.getString("error"), Toast.LENGTH_SHORT).show();
            		}
            	}
            	else{
                    pDialog.dismiss();
                    //updateErrorMsg.setText("Error occured in Update");
                    Toast.makeText(Messaging.this, "Error occured in sending message", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }

	
	
	private class ProcessGettingMessage extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

//        String sessionId, deviceId;
        //String text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            //text = message.getText().toString();
            
            pDialog = new ProgressDialog(Messaging.this);
            //pDialog.setTitle("Contacting Servers");
            //pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getMessage(sessionid, friend_name);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	if (json.getString("status") != null){
            		String status = json.getString("status"); 
            		if(status.equals("success")){           			
            			messages.clear();
            			JSONArray msg = json.getJSONArray("messages");
            			for(int i=0; i<msg.length(); i++){
            				JSONObject tmp = (JSONObject)msg.get(i);            				
            				//messages.add(new Message("Hello", false));
            				String msg1 = tmp.getString("text");
            				String sender = tmp.getString("sender");
            				boolean flag = false;
            				if(sender.equals(username)){
            					flag = true;
            				}else{
            					flag = false;
            				}
            				messages.add(new Message(msg1,flag));
            				
            				adapter.notifyDataSetChanged();
            				if(messages.size()>=1){
            					list.setSelection(messages.size()-1);
            				}            				
            			}
            			
            			pDialog.dismiss();
            		}else{
            			pDialog.dismiss();
            			//System.err.println("jiangge:"+json.getString("error"));
            			Toast.makeText(Messaging.this, json.getString("error"), Toast.LENGTH_SHORT).show();
            		}
            	}
            	else{
                    pDialog.dismiss();
                    Toast.makeText(Messaging.this, "Error occured in getting message", Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.messaging, menu);
		System.err.println("timer is canceled in Messaging.java");
		timer.cancel();
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
	
		
}
