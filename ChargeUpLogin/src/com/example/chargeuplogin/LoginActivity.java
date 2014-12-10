package com.example.chargeuplogin;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chargeuplogin.library.DatabaseHandler;
import com.example.chargeuplogin.library.UserFunctions;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//ActionBarActivity
//FragmentActivity
public class LoginActivity extends ActionBarActivity implements
		ActionBar.TabListener {
	ActionBar actionbar;
	ViewPager viewpager;
	FragmentPageAdapter ft; // FragmentPageAdapter.java

	double level = -1;

	Button btnLogout;
	//Button btnLogout0;
	Button changepas;
	Button btnUpdateinfo;
	Button btnRegisterCharger;
	Button btnDeleteCharger;
	Button btn_myaccount;
	Button btn_addtolist;
	TextView userPanel;
//	Button btn_findcharger;
	ImageButton btn_imageButton1;//find a charger
//	TextView txt;

	private Timer timer;
	private final int UPDATE_TIME_PERIOD = 10000;

	Context thiscontext;
	String sessionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		viewpager = (ViewPager) findViewById(R.id.pager);
		ft = new FragmentPageAdapter(getSupportFragmentManager());
		viewpager.setAdapter(ft);
		
//		txt = (TextView)findViewById(R.id.textView_Welcome);
//		txt.setText("good");

		// ////////////
		// battery level
		getBatteryLevel();
		// ///////////

		// actionbar = getActionBar();

		actionbar = getSupportActionBar();

		// if(actionbar == null){
		// System.err.println("actionbar is null");
		// }
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Adding Tabs:
		actionbar.addTab(actionbar.newTab().setText("Welcome").setTabListener(this));
		actionbar.addTab(actionbar.newTab().setText("Settings").setTabListener(this));
		actionbar.addTab(actionbar.newTab().setText("ChargeUp").setTabListener(this));
		actionbar.addTab(actionbar.newTab().setText("History").setTabListener(this));
		// actionbar.addTab(actionbar.newTab().setText("Login2").setTabListener(this));

		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionbar.setSelectedNavigationItem(arg0);
				
				if(arg0==0){ // welcome page
					//btnLogout0 = (Button)findViewById(R.id.logout0);
					
					/*btnLogout0.setOnClickListener(new View.OnClickListener() {						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// stop pushnotification
							NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
							NM.cancel(R.string.new_charger_request_exist);
							System.err.println("timer is stopped"); // zhijiang
							timer.cancel();

							UserFunctions logout = new UserFunctions();
							logout.logoutUser(getApplicationContext());

							Intent login = new Intent(getApplicationContext(),MainActivity.class);
							login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(login);
						}
					});*/
				}

				if (arg0 == 2) {
					btn_imageButton1 = (ImageButton)findViewById(R.id.imageButton1);
					
					
					// find a charger
					btn_imageButton1.setOnClickListener(new View.OnClickListener() {						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(getApplicationContext(),FindAChargerActivity.class);
							startActivity(i);
							
						}
					});
				}
				if (arg0 == 1) {
					// for original login activity
					changepas = (Button) findViewById(R.id.btchangepass);
					btnLogout = (Button) findViewById(R.id.logout);
					btnUpdateinfo = (Button) findViewById(R.id.updateUserinfo);
					btnRegisterCharger = (Button) findViewById(R.id.btnRegisterCharger);
					btnDeleteCharger = (Button) findViewById(R.id.btnDeleteCharger);
					btn_myaccount = (Button) findViewById(R.id.myaccount);
					btn_addtolist = (Button) findViewById(R.id.addtolist);
					userPanel = (TextView) findViewById(R.id.userPanel);
//					btn_findcharger = (Button) findViewById(R.id.findcharger);

					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					HashMap<String, String> user = new HashMap<String, String>(); // ////
					user = db.getUserDetails();

					final TextView login = (TextView) findViewById(R.id.textwelcome);
					// if(user.get("lname")!=""&&user.get("fname")!=""){
					if (!user.get("lname").isEmpty()&& !user.get("fname").isEmpty()) {
						login.setText("Welcome: " + user.get("lname") + ", "+ user.get("fname")+ "\nYou successfully login");
					} else {
						login.setText("Welcome: " + user.get("uname")+ "\nYou successfully login");
					}

					timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// new ProcessGettingMessage().execute();
									// Intent i1 = new
									Intent i1 = new Intent("GET_MESSAGES");

									Intent i2 = new Intent("REQUEST_CHARGER");

									Intent i3 = new Intent("CONFIRM");

									// System.err.println("jiang test1");
									
									DatabaseHandler db = new DatabaseHandler(getApplicationContext());
									HashMap<String, String> user = new HashMap<String, String>(); // ////
									user = db.getUserDetails();
									sessionId = user.get("uid");

									new process_getPendingNotifications().execute();

									sendBroadcast(i1);
									sendBroadcast(i2);
									sendBroadcast(i3);
								}
							});
						}
					}, new Date(), UPDATE_TIME_PERIOD);

					btnLogout.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							// stop pushnotification
							NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
							NM.cancel(R.string.new_charger_request_exist);
							System.err.println("timer is stopped"); // zhijiang
							timer.cancel();

							UserFunctions logout = new UserFunctions();
							logout.logoutUser(getApplicationContext());

							Intent login = new Intent(getApplicationContext(),MainActivity.class);
							login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(login);
							// finish();
						}
					});

					changepas.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent myIntent = new Intent(
									getApplicationContext(),
									ChangepasswordActivity.class);
							startActivity(myIntent);
							// finish();
						}
					});

					// update info: firstname + lastname
					btnUpdateinfo
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent myIntent = new Intent(
											getApplicationContext(),
											UpdateinfoActivity.class);
									myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(myIntent);
									// finish();
								}
							});

					// Register charger
					btnRegisterCharger
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent myIntent = new Intent(
											getApplicationContext(),
											RegisterChargerActivity.class);
									startActivity(myIntent);
									// finish();
								}
							});

					// get chargers, then delete the charger from the list
					btnDeleteCharger
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent myIntent = new Intent(
											getApplicationContext(),
											DeleteChargerActivity.class);
									startActivity(myIntent);
									// finish();
								}
							});

					// /////
					btn_myaccount
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									GetUserprofile();
								}
							});

					btn_addtolist
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent myIntent = new Intent(
											getApplicationContext(),
											AddAvailableChargerActivity.class);
									startActivity(myIntent);
									// finish();
								}
							});

//					btn_findcharger.setOnClickListener(new View.OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									Intent i = new Intent(getApplicationContext(),FindAChargerActivity.class);
//									startActivity(i);
//								}
//					});

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}// end of onCreate
		// ////////////////////////////////////////////////////////////////////////////////////

	public void GetUserprofile() {
		new process_getUserprofile().execute();
	}

	private class process_getUserprofile extends
			AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		String sessionId;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			HashMap<String, String> user = new HashMap<String, String>(); // ////
			user = db.getUserDetails();
			sessionId = user.get("uid");

			pDialog = new ProgressDialog(LoginActivity.this);
			// pDialog.setTitle("Contacting Servers");
			// pDialog.setMessage("Logging in ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			// pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getUserprofile(sessionId); // ////////
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString("status") != null) {
					String status = json.getString("status");
					if (status.equals("success")) {
						// pDialog.setMessage("Loading...");
						// pDialog.setTitle("Getting Data");
						pDialog.dismiss();

						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						HashMap<String, String> user = new HashMap<String, String>(); // ////
						user = db.getUserDetails();

						JSONObject userprofile = (JSONObject) json
								.getJSONObject("userprofile");
						userPanel.setText("");
						userPanel
								.setText("User's Info" + "\nUsername:\t"
										+ user.get("uname") + "\nEmail:\t"
										+ user.get("email") + "\nAvatar:\t"
										+ userprofile.getString("avatar")
										+ "\nFirst name:\t"
										+ userprofile.getString("firstname")
										+ "\nLast name:\t"
										+ userprofile.getString("lastname")
										+ "\nBio:\t"
										+ userprofile.getString("bio")
										+ "\nMajor:\t"
										+ userprofile.getString("major")
										+ "\nScore:\t"
										+ userprofile.getString("score"));

					} else {
						pDialog.dismiss();
						// errormsg.setText(json.getString("error"));
						Toast.makeText(getApplicationContext(),
								"getUserprofile:" + json.getString("error"),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					pDialog.dismiss();
					Toast.makeText(getApplicationContext(),
							"Error occured in geting user's profile",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// get pending notifications
	private class process_getPendingNotifications extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		String sessionId;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			HashMap<String, String> user = new HashMap<String, String>(); // ////
			user = db.getUserDetails();
			sessionId = user.get("uid");

			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getPendingNotifications(sessionId); // ////////
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString("status") != null) {
					String status = json.getString("status");
					if (status.equals("success")) {

						pDialog.dismiss();

						// System.err.println("getPendingNotifications success");

						JSONArray messages = (JSONArray) json
								.getJSONArray("notifications");
						/*
						 * for(int i=0; i<messages.length(); i++){ JSONObject
						 * msg_i = (JSONObject)messages.get(i);
						 * if(msg_i.getInt("type")==2){ String borrower =
						 * msg_i.getString("borrower"); String transactionid =
						 * msg_i.getString("transactionid"); String chargerid =
						 * msg_i.getString("chargerid");
						 * 
						 * NotificationManager NM = (NotificationManager)
						 * getSystemService(NOTIFICATION_SERVICE);
						 * NotificationCompat.Builder mBuilder = new
						 * NotificationCompat.Builder(getApplicationContext())
						 * .setSmallIcon(R.drawable.stat_sample)
						 * .setContentTitle("new charger request");
						 * 
						 * Intent intent = new Intent(getApplicationContext(),
						 * ConfirmLendingActivity.class);
						 * 
						 * Bundle extras = new Bundle();
						 * extras.putString("borrower", borrower);
						 * extras.putString("transactionid", transactionid);
						 * extras.putString("chargerid", chargerid);
						 * intent.putExtras(extras); PendingIntent contentIntent
						 * = PendingIntent.getActivity(getApplicationContext(),
						 * 0, intent, 0);
						 * mBuilder.setContentText("You have new charger request(s)"
						 * ); mBuilder.setContentIntent(contentIntent);
						 * NM.notify(R.string.new_charger_request_exist,
						 * mBuilder.build()); } }
						 */

						String users = "", transactionids = "", chargerids = "";
						int count = 0;
						for (int i = 0; i < messages.length(); i++) {
							JSONObject msg_i = (JSONObject) messages.get(i);
							if (msg_i.getInt("type") == 2) {
								count = count + 1;
								String borrower = msg_i.getString("borrower");
								String transactionid = msg_i
										.getString("transactionid");
								String chargerid = msg_i.getString("chargerid");

								users = users + borrower + ",";
								transactionids = transactionids + transactionid
										+ ",";
								chargerids = chargerids + chargerid + ";";
							}
						}
						// remove last ","
						if (!users.equals("")) {
							users = users.substring(0, users.length() - 1);
							transactionids = transactionids.substring(0,
									transactionids.length() - 1);
							chargerids = chargerids.substring(0,
									chargerids.length() - 1);
						}

						if (count > 0) {
							NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
									getApplicationContext()).setSmallIcon(
									R.drawable.stat_sample).setContentTitle(
									"new charger request");

							Intent intent = new Intent(getApplicationContext(),
									ConfirmLendingActivity.class);

							Bundle extras = new Bundle();

							extras.putString("users", users);
							extras.putString("transactionids", transactionids);
							extras.putString("chargerids", chargerids);

							intent.putExtras(extras);

							PendingIntent contentIntent = PendingIntent
									.getActivity(getApplicationContext(), 0,
											intent, 0);
							mBuilder.setContentText("You have new charger request(s)");
							mBuilder.setContentIntent(contentIntent);
							NM.notify(R.string.new_charger_request_exist,
									mBuilder.build());
						}

					} else {
						pDialog.dismiss();
						// errormsg.setText(json.getString("error"));
//						Toast.makeText(getApplicationContext(),"getPendingNotifications:"+ json.getString("error"),
//								Toast.LENGTH_SHORT).show();
					}
				} else {
					pDialog.dismiss();
					Toast.makeText(getApplicationContext(),"Error occured in getPendingNotifications",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onTabReselected(android.support.v7.app.ActionBar.Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(android.support.v7.app.ActionBar.Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(android.support.v7.app.ActionBar.Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	private void getBatteryLevel() {
		BroadcastReceiver bc = new BroadcastReceiver() {

			@SuppressLint("NewApi")
			@Override
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				float batteryLevel = intent.getIntExtra(
						BatteryManager.EXTRA_LEVEL, -1);
				float batteryCapacity = intent.getIntExtra(
						BatteryManager.EXTRA_SCALE, -1);
				if (batteryLevel > 0 && batteryCapacity > 0) {
					level = (int) (batteryLevel * 100) / batteryCapacity;

				}

				if (level < 35) {
					NotificationManager notificationManager = (NotificationManager) getApplicationContext()
							.getSystemService(Context.NOTIFICATION_SERVICE);
					// NotificationCompat.Builder notif=new
					// NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Battery notification").setContentText(button.getText().toString());
					// notificationManager.notify(1000, notif.build());

					Notification notif = new Notification.Builder(
							getApplicationContext())
							.setContentTitle("Battery Level low. Use Chargeup?")
							.setSmallIcon(R.drawable.ic_launcher).build();

					notif.defaults |= notif.DEFAULT_VIBRATE;
					notificationManager.notify(1000, notif);
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(bc, intentFilter);
	}
}
