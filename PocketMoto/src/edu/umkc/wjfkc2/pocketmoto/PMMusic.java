package edu.umkc.wjfkc2.pocketmoto;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/** Controls the background music service.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMMusic extends Service {
	public static boolean isRunning = false;
	static MediaPlayer player;
	private static Context context; 
	@Override
	public IBinder onBind(Intent arg0) {          
		return null;     
	}     
	@Override     
	public void onCreate() {         
		super.onCreate();
		context = this;
		setMusicOptions(PMGameEngine.LOOP_BACKGROUND_MUSIC,PMGameEngine.R_VOLUME,
				PMGameEngine.L_VOLUME,PMGameEngine.BACKGROUND_MUSIC);
	}
	public static void setMusicOptions(boolean isLooped, int rVolume, int lVolume, int soundFile){
		player = MediaPlayer.create(context, soundFile);  
		player.setLooping(isLooped);          
		player.setVolume(rVolume,lVolume);
	}
	public int onStartCommand(Intent intent, int flags, int startId) {           
		try
		{
			player.start(); 
			isRunning = true;
		}catch(Exception e){
			isRunning = false;
			player.stop();
		}

		return 1;     
	}      
	public void onStart(Intent intent, int startId) {         
		// TODO        
	}    
	public IBinder onUnBind(Intent arg0) {         
		// TODO Auto-generated method stub          
		return null;     
	}      
	public void onStop() {
		   isRunning = false;
	}     
	public void onPause() {      }     
	@Override     
	public void onDestroy() {          
		   player.stop();         
		   player.release();
	}      
	@Override     
	public void onLowMemory() {
		   player.stop();
	}
}
