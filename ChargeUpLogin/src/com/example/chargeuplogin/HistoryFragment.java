package com.example.chargeuplogin;

//import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

public class HistoryFragment extends Fragment{
	Context thiscontext;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		thiscontext = container.getContext();
		return inflater.inflate(R.layout.history_fragment_layout, container,false);
	}
	
	public void test(){
		NotificationManager NM = (NotificationManager) thiscontext.getSystemService(Context.NOTIFICATION_SERVICE);
	}

}
