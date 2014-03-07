package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

/** Displays the PocketMoto Splash Screen.
 *  Some code was used from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where his code is used. 
 */
public class PMSplashScreen extends Activity {

	/** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//Next line obtained from J.F. DiMarzio.
    	PMGameEngine.display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        PMGameEngine.context = this; //Code obtained from J.F. DiMarzio.
        /* Fire up background music.
		 * Code was from J.F. DiMarzio.
		 */
	    PMGameEngine.musicThread = new Thread(){
	    	public void run(){
	    		Intent bgmusic = new Intent(getApplicationContext(), PMMusic.class);
	        	startService(bgmusic);
	        }
	    };
        //Start up splash screen transition to main menu.
        new Handler().postDelayed(new Runnable() {
        	@Override
        	public void run() {
        		Intent mainMenu = new Intent(PMSplashScreen.this, PMMainMenu.class);
        		PMSplashScreen.this.startActivity(mainMenu);
        		PMSplashScreen.this.finish();
        		overridePendingTransition(R.layout.fadein, R.layout.fadeout); //Code obtained from J.F. DiMarzio.
        	}
        }, PMGameEngine.SPLASH_DELAY);
    }    
}
