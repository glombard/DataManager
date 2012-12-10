package com.example.datamanager;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	static final String STR_ACTIVATE_CONNECTIVITY = "activateConnectivity";

	static final int ID_ALARM_TIME_ON = 1879;
	static final int ID_ALARM_TIME_OFF = 1899;

	// ui components
	private CheckBox cbData = null;
	private CheckBox cbDataMgr = null;
	private CheckBox cbWifi = null;
	private CheckBox cbWifiMgr = null;
	private CheckBox cbAutoSync = null;
	private CheckBox cbAutoWifiOff = null;
	private CheckBox cbSleepHours = null;
	private TextView edSleepHours = null;
	private EditText edTimeOn = null;
	private EditText edTimeOff = null;
	private EditText edInterval = null;
	private Button buttonSave = null;
	private Button buttonEditSleepHours = null;

	private int RETURN_CODE = 0;
	

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//initialize connectivity positions
		SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(getApplicationContext());
		connPrefs.saveAllConnectionSettingInSharedPrefs();
		
		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);


		try {
			sharedPrefsEditor.initializePreferences();

			setContentView(R.layout.activity_main);

			// init ui
			loadUiComponents();
			initializeUiComponentsData();

			// instanciate buttons
			buttonSave = (Button) findViewById(R.id.buttonSave);
			buttonSave.setOnClickListener(this);

			buttonEditSleepHours = (Button) findViewById(R.id.button_edit_sleep_hours);
			buttonEditSleepHours.setOnClickListener(this);

			cbData.setOnCheckedChangeListener(this);
			cbWifi.setOnCheckedChangeListener(this);
			cbAutoSync.setOnCheckedChangeListener(this);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {
		cbData = (CheckBox) findViewById(R.id.checkBoxData);
		cbDataMgr = (CheckBox) findViewById(R.id.checkBoxDataMgr);

		cbWifi = (CheckBox) findViewById(R.id.checkBoxWifi);
		cbWifiMgr = (CheckBox) findViewById(R.id.checkBoxWifiMgr);

		cbAutoSync = (CheckBox) findViewById(R.id.checkBoxAutoSync);
		cbAutoWifiOff = (CheckBox) findViewById(R.id.checkBoxAutoWifiOff);
		cbSleepHours = (CheckBox) findViewById(R.id.checkBoxSleepHours);

		edSleepHours = (TextView) findViewById(R.id.tvSleepHours);

		edTimeOn = (EditText) findViewById(R.id.editTextTimeOn);
		edTimeOff = (EditText) findViewById(R.id.editTextTimeOff);
		edInterval = (EditText) findViewById(R.id.editTextInterval);

	}

	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {
		int timeOn = sharedPrefsEditor.getTimeOn();
		edTimeOn.setText(String.valueOf(timeOn));

		int timeOff = sharedPrefsEditor.getTimeOff();
		edTimeOff.setText(String.valueOf(timeOff));

		int checkTime = sharedPrefsEditor.getIntervalCheck();
		edInterval.setText(String.valueOf(checkTime));

		boolean dataIsActivated = sharedPrefsEditor.isDataActivated();
		cbData.setChecked(dataIsActivated);

		boolean dataMgrIsActivated = sharedPrefsEditor.isDataMgrActivated();
		cbDataMgr.setChecked(dataMgrIsActivated);

		boolean wifiIsActivated = sharedPrefsEditor.isWifiActivated();
		cbWifi.setChecked(wifiIsActivated);

		boolean wifiMgrIsActivated = sharedPrefsEditor.isWifiManagerActivated();
		cbWifiMgr.setChecked(wifiMgrIsActivated);

		boolean autoSyncIsActivated = sharedPrefsEditor.isAutoSyncActivated();
		cbAutoSync.setChecked(autoSyncIsActivated);

		boolean autoWifiOffIsActivated = sharedPrefsEditor
				.isAutoWifiOffActivated();
		cbAutoWifiOff.setChecked(autoWifiOffIsActivated);

		boolean sleepHoursIsActivated = sharedPrefsEditor
				.isSleepHoursActivated();
		cbSleepHours.setChecked(sleepHoursIsActivated);

		String sleepTimeOn = sharedPrefsEditor.getSleepTimeOn();
		String sleepTimeOff = sharedPrefsEditor.getSleepTimeOff();

		edSleepHours.setText(sleepTimeOn + "-" + sleepTimeOff);

		// hide managers checkboxes if necessary

		if (!dataIsActivated) {
			cbDataMgr.setVisibility(View.INVISIBLE);
		}

		if (!wifiIsActivated) {
			cbWifiMgr.setVisibility(View.INVISIBLE);
		}

	}

	@TargetApi(11)
	public void onClick(View v) {

		// if saved button is click
		if (v == buttonSave) {
			// load ui compenents
			loadUiComponents();

			// get settings values
			int timeOn = Integer.parseInt(edTimeOn.getText().toString());
			int timeOff = Integer.parseInt(edTimeOff.getText().toString());
			int intervalCheck = Integer.parseInt(edInterval.getText()
					.toString());

			boolean dataIsActivated = cbData.isChecked();
			boolean dataMgrIsActivated = cbDataMgr.isChecked();

			boolean wifiIsActivated = cbWifi.isChecked();
			boolean wifiMgrIsActivated = cbWifiMgr.isChecked();

			boolean autoSyncIsActivated = cbAutoSync.isChecked();

			boolean autoWifiIsActivated = cbAutoWifiOff.isChecked();

			boolean sleepHoursIsActivated = cbSleepHours.isChecked();

			// save all these preferences
			sharedPrefsEditor.setAllValues(timeOn, timeOff, intervalCheck,
					dataIsActivated, dataMgrIsActivated, wifiIsActivated,
					wifiMgrIsActivated, autoSyncIsActivated,
					autoWifiIsActivated, sleepHoursIsActivated);

			try {
				// if data is disabled; data connection is stopped
				if (!dataIsActivated) {

					// keep auto sync (in case of wifi connection)
					dataActivation.setMobileDataEnabled(false);
					// dataActivation.setAutoSync(true);
				} else {
					// activate data
					dataActivation.setMobileDataEnabled(true);
					// dataActivation.setAutoSync(true);
				}

				// if wifi is disabled, wifi connection is stopped
				if (!wifiIsActivated) {
					dataActivation.setWifiConnectionEnabled(false);
					// dataActivation.setAutoSync(true);
				} else {
					dataActivation.setWifiConnectionEnabled(true);
					// dataActivation.setAutoSync(true);
				}

				// enable/disable autosync
				if (autoSyncIsActivated) {
					dataActivation.setAutoSync(true, sharedPrefsEditor);
				} else {
					dataActivation.setAutoSync(false, sharedPrefsEditor);
				}

				// if data manager and wifi manager are disabled, service is
				// stopped
				if ((!dataMgrIsActivated && !wifiMgrIsActivated)
						|| (!dataIsActivated && !wifiIsActivated)) {
					stopDataManagerService();
				}

				// if data and data manager enable or if wifi and wifi manager
				// enable, service is started
				if ((dataIsActivated && dataMgrIsActivated)
						|| (wifiIsActivated && wifiMgrIsActivated)) {
					// Toast.makeText(this,
					// "service is started : "+sharedPrefsEditor.isDataMgrActivated(),
					// Toast.LENGTH_SHORT).show();
					StartDataManagerService();
				}

				// sleeping hours
				manageSleepingHours(sharedPrefsEditor.getSleepTimeOff(),
						sharedPrefsEditor.getSleepTimeOn());

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (v == buttonEditSleepHours) // if edit sleep hours button
		{
			// open new activity
			Intent sleepTimePickerIntent = new Intent(this,
					SleepTimerPickerActivity.class);
			startActivityForResult(sleepTimePickerIntent, RETURN_CODE);
		}

	}

	/**
	 * Stop data manager service
	 */
	public void stopDataManagerService() {
		// if service is started
		// if (sharedPrefsEditor.isServiceActivated()) {
		stopService(new Intent(this, MainService.class));
		// }
	}

	public void StartDataManagerService() {
		// if service is not started
		// if (!sharedPrefsEditor.isServiceActivated()) {
		Intent serviceIntent = new Intent(this, MainService.class);
		startService(serviceIntent);
		// }
	}

	/**
	 * whenever a checkbox is checked/unchecked
	 */

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (buttonView == cbData) {
			if (isChecked) {
				// enable dataManager checkBox
				cbDataMgr.setVisibility(View.VISIBLE);

			} else {
				// disable dataManager checkBoc
				cbDataMgr.setVisibility(View.INVISIBLE);
			}

		} else if (buttonView == cbWifi) {
			if (isChecked) {
				// enable dataManager checkBox
				cbWifiMgr.setVisibility(View.VISIBLE);

			} else {
				// disable dataManager checkBoc
				cbWifiMgr.setVisibility(View.INVISIBLE);
			}
		}

	}

	// manage result from other activity
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// verify return code

		if (requestCode == RETURN_CODE) {

			// refresh sleep hours time off/on
			if (resultCode == RESULT_OK) {
				String sleepTimeOn = sharedPrefsEditor.getSleepTimeOn();
				String sleepTimeOff = sharedPrefsEditor.getSleepTimeOff();

				edSleepHours.setText(sleepTimeOn + "-" + sleepTimeOff);

				manageSleepingHours(sleepTimeOff, sleepTimeOn);

			}
		}
	}

	public void manageSleepingHours(String sleepTimeOff, String sleepTimeOn) {

		// decides wether to activate/desactivate sleeping hours now
		if (cbSleepHours.isChecked()) {

			// schedule alarms
			setUpAlarm(sleepTimeOff, getBaseContext(), true);
			setUpAlarm(sleepTimeOn, getBaseContext(), false);

			
			if (time1IsAftertimer2(sleepTimeOff, sleepTimeOn)) {
				// if time sleep on has passed and not time sleep off then
				// activate sleeping
				if (timeIsPassed(sleepTimeOn) && !timeIsPassed(sleepTimeOff)) {
					Log.i("TimePassed", sleepTimeOn + " passed and "
							+ sleepTimeOff + " not passed");
					sharedPrefsEditor.setIsSleeping(true);
				} else {
					sharedPrefsEditor.setIsSleeping(false);
				}
			} else {
				Log.i("Timer", sleepTimeOff+" is before "+sleepTimeOn);
				if (timeIsPassed(sleepTimeOn)) {
					Log.i("Timer", sleepTimeOn+" is passed");
					sharedPrefsEditor.setIsSleeping(true);
				} else {
					Log.i("Timer", sleepTimeOn+" is NOT passed");
					sharedPrefsEditor.setIsSleeping(false);
				}
			}
		} else {
			// cancel alarms
			Intent intent = new Intent(this, AlarmReceiver.class);
			PendingIntent timeOn = PendingIntent.getBroadcast(this,
					ID_ALARM_TIME_ON, intent, 0);
			PendingIntent timeOff = PendingIntent.getBroadcast(this,
					ID_ALARM_TIME_OFF, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

			alarmManager.cancel(timeOn);
			alarmManager.cancel(timeOff);
			
			//set sleeping to false
			sharedPrefsEditor.setIsSleeping(false);
		}

	}

	public void setUpAlarm(String time, Context context,
			boolean activateConnectivity) {

		// get hour and minute from time
		String[] splittedTime = time.split(":");
		int hour = Integer.valueOf(splittedTime[0]);
		int minute = Integer.valueOf(splittedTime[1]);

		// setting alarm
		Calendar updateTime = Calendar.getInstance();
		updateTime.set(Calendar.HOUR_OF_DAY, hour);
		updateTime.set(Calendar.MINUTE, minute);

		Intent alarmLauncher = new Intent(context, AlarmReceiver.class);
		alarmLauncher.putExtra(STR_ACTIVATE_CONNECTIVITY, activateConnectivity);

		PendingIntent recurringAlarm = null;

		if (activateConnectivity) {
			recurringAlarm = PendingIntent.getBroadcast(context,
					ID_ALARM_TIME_OFF, alarmLauncher,
					PendingIntent.FLAG_CANCEL_CURRENT);
		} else {
			recurringAlarm = PendingIntent.getBroadcast(context,
					ID_ALARM_TIME_ON, alarmLauncher,
					PendingIntent.FLAG_CANCEL_CURRENT);
		}

		AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				recurringAlarm);
	}

	public boolean timeIsPassed(String time) {
		// get hour and minute from time
		String[] splittedTime = time.split(":");
		int hour = Integer.valueOf(splittedTime[0]);
		int minute = Integer.valueOf(splittedTime[1]);

		// get current time
		Calendar currentTime = Calendar.getInstance();
		//currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));

		Log.i("current hour", currentTime.toString());

		// setting alarm time
		Calendar instanceTime = Calendar.getInstance();
		//instanceTime.setTimeZone(TimeZone.getTimeZone("GMT"));
		instanceTime.set(Calendar.HOUR_OF_DAY, hour);
		instanceTime.set(Calendar.MINUTE, minute);
		
		Log.i("alarm hour", instanceTime.toString());

		return currentTime.after(instanceTime);
	}

	public boolean time1IsAftertimer2(String time1, String time2) {
		// building time1

		String[] splittedTime1 = time1.split(":");
		int hour1 = Integer.valueOf(splittedTime1[0]);
		int minute1 = Integer.valueOf(splittedTime1[1]);

		Calendar calTime1 = Calendar.getInstance();
		calTime1.setTimeZone(TimeZone.getTimeZone("GMT"));
		calTime1.set(Calendar.HOUR_OF_DAY, hour1);
		calTime1.set(Calendar.MINUTE, minute1);

		// building time2
		String[] splittedTime2 = time2.split(":");
		int hour2 = Integer.valueOf(splittedTime2[0]);
		int minute2 = Integer.valueOf(splittedTime2[1]);

		Calendar calTime2 = Calendar.getInstance();
		calTime2.setTimeZone(TimeZone.getTimeZone("GMT"));
		calTime2.set(Calendar.HOUR_OF_DAY, hour2);
		calTime2.set(Calendar.MINUTE, minute2);

		return calTime1.after(calTime2);

	}
	


}
