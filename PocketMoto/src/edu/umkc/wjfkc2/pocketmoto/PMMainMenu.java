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
//TODO:SAVE PRECIOUS DATA AND DESIGN HIGH SCORE MENU!!!
public class PMMainMenu extends Activity implements OnClickListener {
	final PMGameEngine engine = new PMGameEngine(); //From J.F. DiMarzio.
	private boolean purchased;
	private int curPointer;
	static final private int MUTE_MUSIC = Menu.FIRST;
	//static final private int SETTINGS = Menu.FIRST; OMITTED FOR NOW!!!
	static final private int ABOUT = Menu.FIRST + 1;
	
	private static final String PREFS_NAME = "PrefsFile";
	private static final String MUTE_MUSIC_ATTRIBUTE_NAME = "musicMute";
	private static final String CREDITS_EARNED_ATTRIBUTE_NAME = "creditsEarned";
	private static final String NUM_PURCHASED_SUITS_ATT_NAME = "numPurchasedSuits";
	private static final String NUM_PURCHASED_BIKES_ATT_NAME = "numPurchasedBikes";
	private static final String RED_BIKE_PURCHASE_ATT_NAME = "redBikePurchased";
	private static final String PURP_BIKE_PURCHASE_ATT_NAME = "purpleBikePurchased";
	private static final String YELLOW_BIKE_PURCHASE_ATT_NAME = "yellowBikePurchased";
	private static final String GREEN_BIKE_PURCHASE_ATT_NAME = "greenBikePurchased";
	private static final String BLUE_SUIT_PURCHASE_ATT_NAME = "blueSuitPurchased";
	private static final String GREY_SUIT_PURCHASE_ATT_NAME = "greySuitPurchased";
	private static final String ORANGE_SUIT_PURCHASE_ATT_NAME = "orangeSuitPurchased";
	private static final String NEON_SUIT_PURCHASE_ATT_NAME = "neonSuitPurchased";
	private static final String CUR_PLAYER_BIKE_ATT_NAME = "currentPlayerBike";
	private static final String CUR_PLAYER_SUIT_ATT_NAME = "currentPlayerSuit";
	
	private boolean muteMusic;
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen);
		//Preserve data.
        if (savedInstanceState != null) {
        	curPointer = 0;
        	purchased = false;
        	
			muteMusic = savedInstanceState.getBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, false);
			PMGameEngine.playerEarnings = savedInstanceState.getInt(CREDITS_EARNED_ATTRIBUTE_NAME, 500000); //CHANGE THIS VALUE TO ADD MORE DEFAULT CREDITS
			PMGameEngine.numPurchasedBikes = savedInstanceState.getInt(NUM_PURCHASED_BIKES_ATT_NAME, 1);
			PMGameEngine.numPurchasedSuits = savedInstanceState.getInt(NUM_PURCHASED_SUITS_ATT_NAME, 1);
			PMGameEngine.curPlayerBike = savedInstanceState.getInt(CUR_PLAYER_BIKE_ATT_NAME, PMGameEngine.RED_BIKE);
			PMGameEngine.curPlayerSuit = savedInstanceState.getInt(CUR_PLAYER_SUIT_ATT_NAME, PMGameEngine.BLUE_SUIT);
			//Load bike purchases.
			purchased = savedInstanceState.getBoolean(RED_BIKE_PURCHASE_ATT_NAME, true);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.RED_BIKE;
				curPointer++;
			}
			purchased = savedInstanceState.getBoolean(PURP_BIKE_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.PURPLE_BIKE;
				curPointer++;
			}
			purchased = savedInstanceState.getBoolean(YELLOW_BIKE_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.YELLOW_BIKE;
				curPointer++;
			}
			purchased = savedInstanceState.getBoolean(GREEN_BIKE_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.GREEN_BIKE;
			}
			//Load suit purchases.
			curPointer = 0;
			purchased = savedInstanceState.getBoolean(BLUE_SUIT_PURCHASE_ATT_NAME, true);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.BLUE_SUIT;
				curPointer++;
			}
			purchased = savedInstanceState.getBoolean(GREY_SUIT_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.GREY_SUIT;
				curPointer++;
			}
			purchased = savedInstanceState.getBoolean(ORANGE_SUIT_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.ORANGE_SUIT;
				curPointer++;
			}
			purchased = savedInstanceState.getBoolean(NEON_SUIT_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.NEON_SUIT;
			}
		}
		else {
			curPointer = 0;
			purchased = false;
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			muteMusic = settings.getBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, false);
			PMGameEngine.playerEarnings = settings.getInt(CREDITS_EARNED_ATTRIBUTE_NAME, 500000);//CHANGE THIS VALUE TO ADD MORE DEFAULT CREDITS
			PMGameEngine.numPurchasedBikes = settings.getInt(NUM_PURCHASED_BIKES_ATT_NAME, 1);
			PMGameEngine.numPurchasedSuits = settings.getInt(NUM_PURCHASED_SUITS_ATT_NAME, 1);
			PMGameEngine.curPlayerBike = settings.getInt(CUR_PLAYER_BIKE_ATT_NAME, PMGameEngine.RED_BIKE);
			PMGameEngine.curPlayerSuit = settings.getInt(CUR_PLAYER_SUIT_ATT_NAME, PMGameEngine.BLUE_SUIT);
			//Load bike purchases.
			purchased = settings.getBoolean(RED_BIKE_PURCHASE_ATT_NAME, true);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.RED_BIKE;
				curPointer++;
			}
			purchased = settings.getBoolean(PURP_BIKE_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.PURPLE_BIKE;
				curPointer++;
			}
			purchased = settings.getBoolean(YELLOW_BIKE_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.YELLOW_BIKE;
				curPointer++;
			}
			purchased = settings.getBoolean(GREEN_BIKE_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedBikes[curPointer] = PMGameEngine.GREEN_BIKE;
			}
			//Load suit purchases.
			curPointer = 0;
			purchased = settings.getBoolean(BLUE_SUIT_PURCHASE_ATT_NAME, true);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.BLUE_SUIT;
				curPointer++;
			}
			purchased = settings.getBoolean(GREY_SUIT_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.GREY_SUIT;
				curPointer++;
			}
			purchased = settings.getBoolean(ORANGE_SUIT_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.ORANGE_SUIT;
				curPointer++;
			}
			purchased = settings.getBoolean(NEON_SUIT_PURCHASE_ATT_NAME, false);
			if(purchased){
				PMGameEngine.purchasedSuits[curPointer] = PMGameEngine.NEON_SUIT;
			}
		}
	    
	    //OMITTED FOR THE TIME BEING!!!
		//if ((FragmentPreferenceActivity.getMusicSetting(this))) {
			//PMMusic.player.setVolume(0.0f, 0.0f);
		//}
		
		//Set menu button options.
		View startButton = findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
		
		View suitStoreButton = findViewById(R.id.suitStoreButton);
		suitStoreButton.setOnClickListener(this);
		
		View bikeStoreButton = findViewById(R.id.bikeStoreButton);
		bikeStoreButton.setOnClickListener(this);
		
		View exitButton = findViewById(R.id.exitButton);
		exitButton.setOnClickListener(this);
		
		try{
			checkMute();	
		}
		catch(NullPointerException ex){
			System.out.println("PMGameEngine.musicThread failed to start.");
		}
	}
	
	/** Preserves app data when paused. */
    @Override
    protected void onPause() {
		super.onPause();
		//TODO:REFACTOR!!! CREATE METHOD!!!
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, muteMusic);
		editor.putInt(CREDITS_EARNED_ATTRIBUTE_NAME, PMGameEngine.playerEarnings);
		editor.putInt(NUM_PURCHASED_BIKES_ATT_NAME, PMGameEngine.numPurchasedBikes);
		editor.putInt(NUM_PURCHASED_SUITS_ATT_NAME, PMGameEngine.numPurchasedSuits);
		editor.putInt(CUR_PLAYER_BIKE_ATT_NAME, PMGameEngine.curPlayerBike);
		editor.putInt(CUR_PLAYER_SUIT_ATT_NAME, PMGameEngine.curPlayerSuit);
		//Save bike purchases.
		for (int index = 0; index < PMGameEngine.numPurchasedBikes; index++){
			int bike = PMGameEngine.purchasedBikes[index];
			if(bike == PMGameEngine.RED_BIKE){
				editor.putBoolean(RED_BIKE_PURCHASE_ATT_NAME, true);
			}
			else if(bike == PMGameEngine.PURPLE_BIKE){
				editor.putBoolean(PURP_BIKE_PURCHASE_ATT_NAME, true);
			}
			else if(bike == PMGameEngine.YELLOW_BIKE){
				editor.putBoolean(YELLOW_BIKE_PURCHASE_ATT_NAME, true);
			}
			else if(bike == PMGameEngine.GREEN_BIKE){
				editor.putBoolean(GREEN_BIKE_PURCHASE_ATT_NAME, true);
			}
		}
		//Save suit purchases.
		for (int index = 0; index < PMGameEngine.numPurchasedSuits; index++){
			int suit = PMGameEngine.purchasedSuits[index];
			if(suit == PMGameEngine.BLUE_SUIT){
				editor.putBoolean(BLUE_SUIT_PURCHASE_ATT_NAME, true);
			}
			else if(suit == PMGameEngine.GREY_SUIT){
				editor.putBoolean(GREY_SUIT_PURCHASE_ATT_NAME, true);
			}
			else if(suit == PMGameEngine.ORANGE_SUIT){
				editor.putBoolean(ORANGE_SUIT_PURCHASE_ATT_NAME, true);
			}
			else if (suit == PMGameEngine.NEON_SUIT){
				editor.putBoolean(NEON_SUIT_PURCHASE_ATT_NAME, true);
			}
		}

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
		case R.id.bikeStoreButton:
			Intent bikeStore = new Intent(getApplicationContext(),PMBikeStore.class);
			PMMainMenu.this.startActivity(bikeStore);
			break;
		case R.id.suitStoreButton:
			Intent suitStore = new Intent(getApplicationContext(),PMSuitStore.class);
			PMMainMenu.this.startActivity(suitStore);
			break;
		case R.id.exitButton:
			/* Exit Game, ensure exit is clean.
			 * Code was from J.F. DiMarzio.
			 */
			boolean clean = false;
			clean = engine.onExit(v);
			if (clean)
			{
				//TODO:REFACTOR!!! CREATE METHOD!!!
				//Save precious data. This is my code.
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean(MUTE_MUSIC_ATTRIBUTE_NAME, muteMusic);
				editor.putInt(CREDITS_EARNED_ATTRIBUTE_NAME, PMGameEngine.playerEarnings);
				editor.putInt(NUM_PURCHASED_BIKES_ATT_NAME, PMGameEngine.numPurchasedBikes);
				editor.putInt(NUM_PURCHASED_SUITS_ATT_NAME, PMGameEngine.numPurchasedSuits);
				editor.putInt(CUR_PLAYER_BIKE_ATT_NAME, PMGameEngine.curPlayerBike);
				editor.putInt(CUR_PLAYER_SUIT_ATT_NAME, PMGameEngine.curPlayerSuit);
				//Save bike purchases.
				for (int index = 0; index < PMGameEngine.numPurchasedBikes; index++){
					int bike = PMGameEngine.purchasedBikes[index];
					if(bike == PMGameEngine.RED_BIKE){
						editor.putBoolean(RED_BIKE_PURCHASE_ATT_NAME, true);
					}
					else if(bike == PMGameEngine.PURPLE_BIKE){
						editor.putBoolean(PURP_BIKE_PURCHASE_ATT_NAME, true);
					}
					else if(bike == PMGameEngine.YELLOW_BIKE){
						editor.putBoolean(YELLOW_BIKE_PURCHASE_ATT_NAME, true);
					}
					else if(bike == PMGameEngine.GREEN_BIKE){
						editor.putBoolean(GREEN_BIKE_PURCHASE_ATT_NAME, true);
					}
				}
				//Save suit purchases.
				for (int index = 0; index < PMGameEngine.numPurchasedSuits; index++){
					int suit = PMGameEngine.purchasedSuits[index];
					if(suit == PMGameEngine.BLUE_SUIT){
						editor.putBoolean(BLUE_SUIT_PURCHASE_ATT_NAME, true);
					}
					else if(suit == PMGameEngine.GREY_SUIT){
						editor.putBoolean(GREY_SUIT_PURCHASE_ATT_NAME, true);
					}
					else if(suit == PMGameEngine.ORANGE_SUIT){
						editor.putBoolean(ORANGE_SUIT_PURCHASE_ATT_NAME, true);
					}
					else if (suit == PMGameEngine.NEON_SUIT){
						editor.putBoolean(NEON_SUIT_PURCHASE_ATT_NAME, true);
					}
				}

				// Commit the edits
				editor.commit();
				
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
			}
		}
	}
	/** Determines if the music should be played. */
	private void checkMute() throws NullPointerException {
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
