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
	public static final int BACKGROUND_MUSIC = R.raw.drivenedit;
	public static final int PLAYER_BIKE_SHEET = R.drawable.bikes;
	public static final int PLAYER_SUIT_SHEET = R.drawable.biker;
	public static final int PLAYER_FRAMES_BETWEEN_ANI = 9; //Draw player every 9 gameloop iterations.
	public static final float OBJECT_SCALE = 0.25f;
	
	//Game control variables
	public static Context context;
	public static Thread musicThread;
	public static Display display;
	
	//0.1375 is max scroll speed before screen bugs out.
	public static final float MAX_BIKE_SPEED = 0.1375f;
	public static final float BRAKE_SPEED_MODIFIER = 0.0525f;
	public static float curPlayerPosX = 2f; //Keep track of the player's current x coord.
	public static int playerBikeAction = 0;
	public static float playerBikeHandling = 0.025f;
	public static float playerBikeAcceleration = 0.0015f;
	public static float playerBikeSpeed = 0.0625f;
	public static float curPlayerBikeAcceleration;
	public static float curPlayerBikeSpeed;
	
	//My own constants
	public static final int MOVEMENT_BUTTONS = R.drawable.bikercontrolbuttons;
	public static final int ENVIRONMENT_OBJECTS = R.drawable.environment;
	public static final int NUM_SPRITESHEETS = 4;
	public static final float PLAYER_Y_POS = .275f;
	
	//Keep track of sprite sheet indexes.
	public static final int BIKE_SPRITE_INDEX = 0;
	public static final int MOVEMENT_BUTTONS_INDEX = 1;
	public static final int ENVIRONMENT_SPRITE_INDEX = 2;
	public static final int SUIT_SPRITE_INDEX = 3;
	
	//EnvironmentObject constants.
	public static final int MAX_ENVIRO_OBJECTS = 4;
	public static final int NUM_ENVIRO_TYPES = 3;
	public static final int OBJ_TYPE_ROCK = 0;
	public static final int OBJ_TYPE_UPWRD_CAR = 1;
	public static final int OBJ_TYPE_DWNWRD_CAR = 2;
	public static final float CAR_SPEED = 0.01f;
	public static final int YELLOW_CAR = 0;
	public static final int GREY_CAR = 1;
	public static final int RED_CAR = 2;
	public static final int GREEN_CAR = 3;
	
	//Constants to keep track of player actions for vertical movement.
	public static final int PLAYER_THROTTLE = 1;
	public static final int PLAYER_BRAKE = 2;
	public static final int PLAYER_RELEASE = 3;
	
	public static float backgroundScrollSpeed = 0; //Don't scroll at game start.
	
	//Screen rotation constants.
	public static final float START_ROT_MOVEMENT = 0.075f;
	
	//Constants & vars to keep track of player actions for lateral movement.
	public static int playerBikeLatAction = 0;
	public static float playerBikeTurnRate = 0;
	public static final int SCREEN_ROT_LEFT = 1;
	public static final int SCREEN_ROT_RIGHT = 2;
	public static final int SCREEN_NO_ROT = 0;
	
	//Bike & player selection variables & constants.
	public static int curPlayerSuit = 3;
	public static int curPlayerBike = 3;
	public static final int NUM_BIKES = 4;
	public static final int NUM_SUITS = 4;
	public static final int RED_BIKE = 0;
	public static final int PURPLE_BIKE = 1;
	public static final int YELLOW_BIKE = 2;
	public static final int GREEN_BIKE = 3;
	public static final int BLUE_SUIT = 0;
	public static final int GREY_SUIT = 1;
	public static final int ORANGE_SUIT = 2;
	public static final int NEON_SUIT = 3;
	
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
