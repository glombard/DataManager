package com.example.threadtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.RecoverySystem.ProgressListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button monBouton = null;
	Button monBoutonBar = null;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        monBouton = (Button)findViewById(R.id.MonBouton);
        monBouton.setOnClickListener(this);
        
        monBoutonBar = (Button)findViewById(R.id.MonBoutonBar);
        monBoutonBar.setOnClickListener(this);
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onClick(View v) {
		
		//new handler
		AHandler handler = new AHandler(this);
		
		if(v == monBouton)
		{

			//create new thread
			Thread processThread = new Thread(new ARunnable(handler));
			processThread.start();
			
		}
		else if(v == monBoutonBar)
		{

			//create new thread
			Thread processThread = new Thread(new ARunnableBar(handler));
			processThread.start();
			
		}

		
	}
	

}
