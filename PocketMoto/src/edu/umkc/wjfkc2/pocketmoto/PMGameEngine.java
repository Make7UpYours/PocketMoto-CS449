package edu.umkc.wjfkc2.pocketmoto;

import android.content.Context;
import android.view.Display;
import android.view.View;

/** Houses the game data and various back-end functions.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMGameEngine {
	/* Constants that will be used in the game */
	public static final int SPLASH_DELAY = 4000;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);
	public static final int BACKGROUND_LAYER_ONE = R.drawable.grass;
	public static final int BACKGROUND_LAYER_TWO = R.drawable.road;
	public static final int BACKGROUND_MUSIC = R.raw.warfieldedit; //TODO: CHANGE FROM DiMarzio's MUSIC!!!
	public static final int PLAYER_BIKE = R.drawable.biker;
	public static final int PLAYER_FRAMES_BETWEEN_ANI = 9; //Draw player every 9 gameloop iterations.
	public static final int PLAYER_RELEASE = 3;
	//Game control variables
	public static Context context;
	public static Thread musicThread;
	public static Display display;
	public static float curPlayerPosX = 2.0f; //Keep track of the player's current x coord.
	public static int playerBikeAction = 0;
	//MY own constants
	public static final int MOVEMENT_BUTTONS = R.drawable.bikercontrolbuttons;
	//0.1375 is max scroll speed before screen bugs out.
	public static final float MAX_BIKE_SPEED = 0.1375f;
	//Constants to keep track of player actions.
	public static final int PLAYER_THROTTLE = 1;
	public static final int PLAYER_BRAKE = 2;
	//My own variables.
	public static float backgroundScrollSpeed = 0; //Don't scroll at game start.
	
	/** Kill the game and exit */
	public boolean onExit(View v) {
		try
		{
			/* ISSUES WITH EXITING WITH MUSIC THREAD...
			Intent bgmusic = new Intent(context, SFMusic.class);
			context.stopService(bgmusic);
			musicThread.stop();*/
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
