package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiConnectionReceiver extends BroadcastReceiver {



	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation = null;
	private LogsProvider logsProvider = null;

	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context, this.getClass());
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);


		if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if(networkInfo.isConnected()) {

				// Wifi is connected
				logsProvider.info("Wifi is connected, getcheckconnwifi: "+sharedPrefsEditor.getCheckNetConnectionWifi()+" netHasToBeChecked: "+sharedPrefsEditor.getNetHasToBeChecked());

				//verify internet connection
				//checking if internet connection is availaible
				if(sharedPrefsEditor.getCheckNetConnectionWifi() && sharedPrefsEditor.getNetHasToBeChecked())
				{
					if(!dataActivation.isInternetConnectionAvailable())
					{
						//shut down wifi
						dataActivation.setWifiConnectionEnabled(false, false, sharedPrefsEditor);
					}
					
					sharedPrefsEditor.setNetConnHasToBeChecked(false);
				}

			}
			else if(!networkInfo.isConnected())
			{
				// Wifi is connected
				logsProvider.info("Wifi is disconnected: ");
			}

		}
		
		

	}
}
