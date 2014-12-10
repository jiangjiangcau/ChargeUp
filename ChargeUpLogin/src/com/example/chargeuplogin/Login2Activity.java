package com.example.chargeuplogin;


import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chargeuplogin.library.DatabaseHandler;
import com.example.chargeuplogin.library.UserFunctions;

import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.ActionBarActivity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//ActionBarActivity
//Fragment
public class Login2Activity extends Fragment {
	
//	Button btnLogout;
//    Button changepas;
//    Button btnUpdateinfo;
//    Button btnRegisterCharger;
//    Button btnDeleteCharger;
//    Button btn_myaccount;
//    Button btn_addtolist;
//    TextView userPanel;
//    Button btn_findcharger;
//
//    private Timer timer;
//    private final int UPDATE_TIME_PERIOD = 10000;
//    
//    Context thiscontext;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		thiscontext = container.getContext();
		
		View rootView = inflater.inflate(R.layout.activity_login2, container,false);
        
//		return inflater.inflate(R.layout.activity_login2, container,false);
		return rootView;
	}

}
