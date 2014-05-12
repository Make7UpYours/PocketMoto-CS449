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
	public static float playerBikeHandling;
	public static float playerBikeAcceleration;
	public static float playerBikeSpeed;
	public static int playerHP;
	public static int curPlayerHP;
	public static float curPlayerBikeAcceleration;
	public static float curPlayerBikeSpeed;
	
	//My own constants
	public static final int MOVEMENT_BUTTONS = R.drawable.bikercontrolbuttons;
	public static final int ENVIRONMENT_OBJECTS = R.drawable.environment;
	public static final int GAME_INFO = R.drawable.gameinfo;
	public static final int NUMBERS = R.drawable.numbers;
	public static final int NUM_SPRITESHEETS = 6;
	public static final float PLAYER_Y_POS = .275f;
	public static final int NUM_GAME_INFO_TEXTURES = 2;
	public static final int SCORE_GAME_INFO_INDEX = 0;
	public static final int GAME_STATUS_INDEX = 1;
	//Number display constants.
	public static final int NUM_NUMBERS = 8;
	public static final int SCORE_UNITS_NUMBERS_INDEX = 0;
	public static final int SCORE_TENS_NUMBERS_INDEX = 1;
	public static final int SCORE_HUNDS_NUMBERS_INDEX = 2;
	public static final int HP_NUMBERS_INDEX = 3;
	public static final int CREDIT_UNITS_NUMBERS_INDEX = 4;
	public static final int CREDIT_TENS_NUMBERS_INDEX = 5;
	public static final int CREDIT_HUNDS_NUMBERS_INDEX = 6;
	public static final int CREDIT_THOUSANDS_NUMBERS_INDEX = 7;
	
	//Keep track of sprite sheet indexes.
	public static final int BIKE_SPRITE_INDEX = 0;
	public static final int MOVEMENT_BUTTONS_INDEX = 1;
	public static final int ENVIRONMENT_SPRITE_INDEX = 2;
	public static final int SUIT_SPRITE_INDEX = 3;
	public static final int GAME_INFO_INDEX = 4;
	public static final int NUMBERS_INDEX = 5;
	
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
	public static int curPlayerSuit;
	public static int curPlayerBike;
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
	
	//Suit & bike stats.
	public static final float TIER_1_HANDLING = 0.025f;
	public static final float TIER_2_HANDLING = 0.035f;
	public static final float TIER_3_HANDLING = 0.045f;
	public static final float TIER_4_HANDLING = 0.055f;
	public static final float TIER_1_ACCELERATION = 0.0005f;
	public static final float TIER_2_ACCELERATION = 0.0010f;
	public static final float TIER_3_ACCELERATION = 0.00125f;
	public static final float TIER_4_ACCELERATION = 0.0015f;
	public static final float TIER_1_SPEED = 0.0325f;
	public static final float TIER_2_SPEED = 0.0425f;
	public static final float TIER_3_SPEED = 0.0525f;
	public static final float TIER_4_SPEED = 0.0625f;
	public static final int TIER_1_HP = 3;
	public static final int TIER_2_HP = 4;
	public static final int TIER_3_HP = 5;
	public static final int TIER_4_HP = 6;
	
	//Store variables and constants.
	public static final int TIER_1_BIKE_COST = 0;
	public static final int TIER_2_BIKE_COST = 1000;
	public static final int TIER_3_BIKE_COST = 3000;
	public static final int TIER_4_BIKE_COST = 6000;
	public static final int TIER_1_SUIT_COST = 0;
	public static final int TIER_2_SUIT_COST = 500;
	public static final int TIER_3_SUIT_COST = 1000;
	public static final int TIER_4_SUIT_COST = 2000;
	public static int playerEarnings;
	public static int[] purchasedBikes = new int[NUM_BIKES];
	public static int[] purchasedSuits = new int[NUM_SUITS];
	public static int numPurchasedBikes;
	public static int numPurchasedSuits;
	
	//Game state variables
	public static boolean gameOver = true;
	public static boolean creditsAwarded = false;
	public static int score = 0;
	public static int highScore;
	public static final float HP_NUM_X = 3.4125f;
	public static final float HP_NUM_Y = 3.0275f;
	public static final float SCORE_NUM_Y = 3.0275f;
	public static final float SCORE_HUNDS_X = 1.175f;
	public static final float SCORE_TENS_X = 1.28f;
	public static final float SCORE_UNITS_X = 1.385f;
	public static final float CREDITS_NUM_Y = 0.3825f;
	public static final float CREDITS_UNITS_NUM_X = 3.015f;
	public static final float CREDITS_TENS_NUM_X = 2.91f;
	public static final float CREDITS_HUNDS_NUM_X = 2.805f;
	public static final float CREDITS_THOUS_NUM_X = 2.7f;
	
	/** Kill the game and exit */
	public boolean onExit(View v) {
		try
		{
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
