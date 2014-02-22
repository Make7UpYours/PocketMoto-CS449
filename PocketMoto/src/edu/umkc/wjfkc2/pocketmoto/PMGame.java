package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.os.Bundle;

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
