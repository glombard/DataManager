package com.example.datamanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.TimePicker;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.gyagapen.cleverconnectivity.R;

public class SleepTimerPickerActivity extends Activity {

	// time pickers
	private TimePicker timePickerSleepOn = null;
	private TimePicker timePickerSleepOff = null;
	private CheckBox cbSleepHours = null;


	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleeptimepicker);

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// initialize ui components
		initUiComponents();

		//load data componenets 
		loadTimePickerData();


	}

	private void initUiComponents() {

		timePickerSleepOff = (TimePicker) findViewById(R.id.timePickerSleepOff);
		timePickerSleepOn = (TimePicker) findViewById(R.id.timerPickerSleepOn);
		cbSleepHours = (CheckBox) findViewById(R.id.checkBoxSleepHours);
		
	}

	private void loadTimePickerData() {
		// sleep off
		String[] timeSleepOff = sharedPrefsEditor.getSleepTimeOff().split(":");
		int hourSleepOff = Integer.valueOf(timeSleepOff[0]);
		int minuteSleepOff = Integer.valueOf(timeSleepOff[1]);

		timePickerSleepOff.setCurrentHour(hourSleepOff);
		timePickerSleepOff.setCurrentMinute(minuteSleepOff);

		// sleep on
		String[] timeSleepOn = sharedPrefsEditor.getSleepTimeOn().split(":");
		int hourSleepOn = Integer.valueOf(timeSleepOn[0]);
		int minuteSleepOn = Integer.valueOf(timeSleepOn[1]);

		timePickerSleepOn.setCurrentHour(hourSleepOn);
		timePickerSleepOn.setCurrentMinute(minuteSleepOn);
		
		boolean sleepHoursIsActivated = sharedPrefsEditor
				.isSleepHoursActivated();
		cbSleepHours.setChecked(sleepHoursIsActivated);
		
		
	}

	/*public void onClick(View v) {

		//if save button is clicked
		if(v == buttonSave)
		{
			// force the timepickers to loose focus and the typed value is available !
			timePickerSleepOn.clearFocus();
			timePickerSleepOff.clearFocus();

			//getting value from sleep time on
			int hourSleepOn = timePickerSleepOn.getCurrentHour();
			int minuteSleepOn = timePickerSleepOn.getCurrentMinute();
			String timeOn = String.format("%02d:%02d", hourSleepOn, minuteSleepOn);

			//getting value from sleep time off
			int hourSleepOff = timePickerSleepOff.getCurrentHour();
			int minuteSleepOff = timePickerSleepOff.getCurrentMinute();
			String timeOff = String.format("%02d:%02d", hourSleepOff, minuteSleepOff);


			//saving in preferences
			sharedPrefsEditor.setSleepTimeOn(timeOn);
			sharedPrefsEditor.setSleepTimeOff(timeOff);

			//create the intent
			Intent intent = new Intent();

			//return result with intent
			setResult(RESULT_OK, intent);

			//terminate activity
			finish();

		}

	}*/



	private void applySettings()
	{
		boolean sleepHoursIsActivated = cbSleepHours.isChecked();
		sharedPrefsEditor.setSleepHoursActivation(sleepHoursIsActivated);
		
		// force the timepickers to loose focus and the typed value is available !
		timePickerSleepOn.clearFocus();
		timePickerSleepOff.clearFocus();

		//getting value from sleep time on
		int hourSleepOn = timePickerSleepOn.getCurrentHour();
		int minuteSleepOn = timePickerSleepOn.getCurrentMinute();
		String timeOn = String.format("%02d:%02d", hourSleepOn, minuteSleepOn);

		//getting value from sleep time off
		int hourSleepOff = timePickerSleepOff.getCurrentHour();
		int minuteSleepOff = timePickerSleepOff.getCurrentMinute();
		String timeOff = String.format("%02d:%02d", hourSleepOff, minuteSleepOff);


		//saving in preferences
		sharedPrefsEditor.setSleepTimeOn(timeOn);
		sharedPrefsEditor.setSleepTimeOff(timeOff);

		//setting sleeping hours
		AlarmMgr alarmMgr = new AlarmMgr(this, sharedPrefsEditor);
		alarmMgr.manageSleepingHours(timeOff,
				timeOn);

	}

	protected void onDestroy() {

		applySettings();
		super.onDestroy();
	}


}
