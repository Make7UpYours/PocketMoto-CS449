package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/** The main menu for PocketMoto.
 *  Some code was used from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where his code is used. 
 */
public class PMMainMenu extends Activity implements OnClickListener {
	final PMGameEngine engine = new PMGameEngine(); //From J.F. DiMarzio.
	static final private int SETTINGS = Menu.FIRST;
	static final private int ABOUT = Menu.FIRST + 1;
	/** Called when the activity if first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen); //Temp set to splash.
		
		//TODO FIND A WAY TO STOP THE MUSIC WITHOUT HAVING TO RESTART APP!!!
		if (!(FragmentPreferenceActivity.getMusicSetting(this))) {
			/* Fire up background music.
			 * Code was from J.F. DiMarzio.
			 */
		    PMGameEngine.musicThread = new Thread(){
		    	public void run(){
		    		Intent bgmusic = new Intent(getApplicationContext(), PMMusic.class);
		        	startService(bgmusic);
		        }
		    };
		    PMGameEngine.musicThread.start();
		}
		
		//Set menu button options.
		View startButton = findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
		
		View exitButton = findViewById(R.id.exitButton);
		exitButton.setOnClickListener(this);
	}
	
	/** Creates options menu. */
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create settings and about menu items.
		MenuItem itemAbout = menu.add(Menu.NONE, ABOUT, Menu.NONE, "App Info");
		MenuItem itemSettings = menu.add(Menu.NONE, SETTINGS, Menu.NONE, "Settings");
		return true;
	}
    
    /** Performs menu operations. */
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case (ABOUT): {
			//Display information about the app.
			Intent i = new Intent(this, PMAbout.class);
	    	startActivity(i);
		    return true;
		}
		case (SETTINGS): {
			//Allows user to change app settings.
			Intent i = new Intent(this, FragmentPreferenceActivity.class);
	    	startActivity(i);
		    return true;
		}
		}
		return false;
	}
	
	/** Code is run when user clicks on menu buttons. */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.startButton:
			/* Start Game!!! 
			 * Code was from J.F. DiMarzio.
			 */
			Intent game = new Intent(getApplicationContext(),PMGame.class);
			PMMainMenu.this.startActivity(game);
			break;
		case R.id.exitButton:
			/* Exit Game, ensure exit is clean.
			 * Code was from J.F. DiMarzio.
			 */
			boolean clean = false;
			clean = engine.onExit(v);
			if (clean)
			{
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
			}
		}
	}

}
