package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import com.gyagapen.cleverconnectivity.R;

public class AlarmReceiver extends BroadcastReceiver {

	private static final String DEBUG_TAG = "AlarmReceiver";

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	@Override
	public void onReceive(Context context, Intent intent) {

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// if service is active
		if (sharedPrefsEditor.isServiceActivated()) {

			Bundle bundle = intent.getExtras();
			boolean activateConnectivity = bundle
					.getBoolean(MainActivity.STR_ACTIVATE_CONNECTIVITY);

			Log.d(DEBUG_TAG, "Recurring alarm; activate  all connectivity : "
					+ String.valueOf(activateConnectivity));

			// if we have to desactivate connectivity, we save it in shared
			// preferences
			if (!activateConnectivity) {
				// is sleeping
				sharedPrefsEditor.setIsSleeping(true);

				// disable all connectivity if screen is off
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);

				if (!pm.isScreenOn()) {
					try {
						dataActivation
								.setConnectivityDisabled(sharedPrefsEditor);
						
						//cancel timers
						TimersSetUp timersSetUp = new TimersSetUp(context);
						timersSetUp.CancelTimerOn();
						timersSetUp.CancelTimeOff();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				// sleep off
				sharedPrefsEditor.setIsSleeping(false);

				// start service
				Intent i = new Intent(context, MainService.class);
				
				//if screen is off
				if(!dataActivation.isScreenIsOn())
				{
					
					//set first time on to false
					sharedPrefsEditor.setIsFirstTimeOn(false);
					
					//restart the cycle
					try {
						
						dataActivation.setConnectivityEnabled(sharedPrefsEditor);
					} catch (Exception e) {
						e.printStackTrace();
					}
					i.putExtra("screen_state", true);
				}
				
				context.startService(i);

			}

		}

	}

}
