package com.example.chargeuplogin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;

public class FragmentPageAdapter extends FragmentPagerAdapter{

	public FragmentPageAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
//	public ActionBarActivity getItem(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			return new WelcomeActivity();
		case 1:
			return new Login2Activity();
			
		case 2:
			return new ChargeUpFragment();
		case 3:
			return new HistoryFragment();
			//return new HistoryFragment();//MyDevicesFragment
//		case 3:
//			new Login2Activity();
//			return new HistoryFragment();

		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}
}
