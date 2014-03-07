package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
	static final private int MUTE_MUSIC = Menu.FIRST;
	//static final private int SETTINGS = Menu.FIRST; OMITTED FOR NOW!!!
	static final private int ABOUT = Menu.FIRST + 1;
	private static final String PREFS_NAME = "PrefsFile";
	private static final String MUTE_MUSIC_ATTRIBUTE_NAME = "musicMute";
	private boolean muteMusic;
	
	/** Called when the activity if first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen);
	    
	  //Preserve muteMusic
        if (savedInstanceState != null) {
			muteMusic = savedInstanceState.getBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, false);
		}
		else {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			muteMusic = settings.getBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, false);
		}
	    
	    //OMITTED FOR THE TIME BEING!!!
		//if ((FragmentPreferenceActivity.getMusicSetting(this))) {
			//PMMusic.player.setVolume(0.0f, 0.0f);
		//}
		
		//Set menu button options.
		View startButton = findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
		
		View exitButton = findViewById(R.id.exitButton);
		exitButton.setOnClickListener(this);
		
		checkMute();
	}
	
	/** Preserves app data when paused. */
    @Override
    protected void onPause() {
		super.onPause();

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, muteMusic);

		// Commit the edits
		editor.commit();
    }
	/** Creates options menu. */
    @SuppressWarnings("unused")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create settings and about menu items.
		MenuItem itemAbout = menu.add(Menu.NONE, ABOUT, Menu.NONE, "App Info");
		MenuItem itemMute = menu.add(Menu.NONE, MUTE_MUSIC, Menu.NONE, "Mute/Play Music");
		//SETTINGS HAVE BEEN OMITTED FOR NOW!!!
		//MenuItem itemSettings = menu.add(Menu.NONE, SETTINGS, Menu.NONE, "Settings");
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
		/*CODE OMITTED FOR TIME BEING!!!
		case (SETTINGS): {
			//Allows user to change app settings.
			Intent i = new Intent(this, FragmentPreferenceActivity.class);
	    	startActivity(i);
		    return true;
		}*/
		case (MUTE_MUSIC): {
			//Invert muteMusic.
			if (muteMusic){
				muteMusic = false;
			}else{
				muteMusic = true;
			}
			//Check muteMusic & musicThread status.
			if (muteMusic && PMMusic.isRunning){
				PMMusic.player.pause();
			}else if (PMMusic.isRunning && !muteMusic){
				PMMusic.player.start();
			}else if (!muteMusic){
				PMGameEngine.musicThread.start();
			}
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
				//Save muteMusic prefs. This is my code.
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, muteMusic);

				// Commit the edits
				editor.commit();
				
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
			}
		}
	}
	/** Determines if the music should be played. */
	private void checkMute() {
		//Check muteMusic & musicThread status.
		if (muteMusic && PMMusic.isRunning){
			PMMusic.player.pause();
		}else if (PMMusic.isRunning && !muteMusic){
			PMMusic.player.start();
		}else if (!muteMusic){
			PMGameEngine.musicThread.start();
		}
	}
}
