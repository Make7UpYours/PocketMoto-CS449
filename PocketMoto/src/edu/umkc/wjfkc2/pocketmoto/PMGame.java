package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

/** PocketMoto game Activity.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMGame extends Activity {
	private PMGameView gameView;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameView = new PMGameView(this);
		setContentView(gameView);
	}
	/** Detect when the player touches the screen to move the character. */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Obtain the x & y coords of the touch event.
		float x = event.getX();
		float y = event.getY();
		/* Restrict touch event to lower 3/40 (.075) of the screen.
		 * This code is my own, I used the a non-depreciated method
		 * to obtain the screen area.
		 */
		DisplayMetrics screenMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screenMetrics);
		int height = (3 * screenMetrics.heightPixels) / 40;
		int playableArea = screenMetrics.heightPixels - height;
		int width = screenMetrics.widthPixels;
		//Rest of block is from DiMarzio.
		if (y > playableArea){
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(x < width / 2){ //Left side of screen.
					PMGameEngine.playerBikeAction = PMGameEngine.PLAYER_BRAKE;
				}else{ //Right side of screen.
					PMGameEngine.playerBikeAction = PMGameEngine.PLAYER_THROTTLE;
				}
				break;
			case MotionEvent.ACTION_UP:
				PMGameEngine.playerBikeAction = PMGameEngine.PLAYER_RELEASE;
				break;
			}
		}
		
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		gameView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		gameView.onPause();
	}
}
