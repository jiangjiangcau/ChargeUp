package com.example.chargeuplogin;

import java.util.HashMap;

import com.example.chargeuplogin.library.DatabaseHandler;

import android.support.v7.app.ActionBarActivity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConfirmLendingActivity extends ActionBarActivity {
	
	TextView borrower;
	String[] borrowers;
	String[] transactionids;
	String[] chargerids;
	ListView borrowersListView;
	ArrayAdapter<String> borrowersAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_lending);
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        HashMap<String,String> user = new HashMap<String, String>(); //////
        user = db.getUserDetails();
        final String sessionId = user.get("uid");
        final String username = user.get("uname");
		
		borrower = (TextView)findViewById(R.id.borrower);
		borrowersListView = (ListView)findViewById(R.id.borrowersListView);
		
//		borrower.setText("good");
//		borrower.setText(getIntent().getStringExtra("info"));
//		borrower.setText(getIntent().getStringExtra("borrower"));

		Bundle extras = getIntent().getExtras();
		String str_borrower = extras.getString("users");
		String str_transactionid = extras.getString("transactionids");
		String str_chargerid = extras.getString("chargerids");
		
//		borrower.setText(str_borrower);
		borrowers = str_borrower.split(",");
		transactionids = str_transactionid.split(",");
		chargerids = str_chargerid.split(",");
		
//		String aaa = "";
//		for(int i=0; i<transactionids.length; i++){
//			aaa = aaa + transactionids[i]+"\n";
//		}
//		borrower.setText(aaa);
		
		borrowersAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		borrowersListView.setAdapter(borrowersAdapter);
		for(int i=0; i<borrowers.length; i++){
			borrowersAdapter.add(borrowers[i]);
		}
		borrowersAdapter.notifyDataSetChanged();
		
		NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		NM.cancel(R.string.new_charger_request_exist);
		
		/*borrowersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Messaging.class);
				String friend_name = (String) parent.getAdapter().getItem(position);
				i.putExtra("friend_name",friend_name);
				i.putExtra("username", username);
				i.putExtra("sessionid", sessionId);
				startActivity(i);
			}
		});	*/	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm_lending, menu);
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
