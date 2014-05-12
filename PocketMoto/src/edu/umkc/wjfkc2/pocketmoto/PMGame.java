package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
public class PMGame extends Activity implements SensorEventListener {
	private PMGameView gameView;
	private SensorManager mSensorManager;
	private Sensor mGyroSensor;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Initialize sensor detection.
		 * Code was obtained from:
		 * http://developer.android.com/guide/topics/sensors/sensors_position.html#sensors-pos-gamerot
		 */
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		if(PMGameEngine.gameOver){
			reset();
		}
		gameView = new PMGameView(this);
		setContentView(gameView);
	}
	/** Resets values to their default so that the player may
	 *  play another game after dying.
	 */
	private void reset(){
		PMGameEngine.gameOver = false;
		PMGameEngine.creditsAwarded = false;
		PMGameEngine.score = 0;
		PMGameEngine.curPlayerPosX = 2f;
		PMGameEngine.curPlayerHP = PMGameEngine.playerHP;
	}
	/** Detect when the player rotates the device left or right.
	 *  This method will be used to move the biker left & right.
	 *  The faster the player rotates the screen, the faster the
	 *  biker will move.
	 *  Will only run code if the game is not over.
	 */
	@Override
	public void onSensorChanged(SensorEvent event){
		if (!PMGameEngine.gameOver){
			if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
				float yAxisRot = event.values[1];
				if (yAxisRot < -(PMGameEngine.START_ROT_MOVEMENT)){
					PMGameEngine.playerBikeLatAction = PMGameEngine.SCREEN_ROT_LEFT;
					PMGameEngine.playerBikeTurnRate = yAxisRot;
				}else if(yAxisRot > PMGameEngine.START_ROT_MOVEMENT){
					PMGameEngine.playerBikeLatAction = PMGameEngine.SCREEN_ROT_RIGHT;
					PMGameEngine.playerBikeTurnRate = yAxisRot;
				}else{
					//Screen is not rotating, return to default state.
					PMGameEngine.playerBikeLatAction = PMGameEngine.SCREEN_NO_ROT;
				}
			}	
		}
	}
	/** Detect when the player touches the screen to move the character.
	 *  Will only run code if the game is not over.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!PMGameEngine.gameOver){
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
		}
		else{
			PMGameEngine.playerBikeAction = PMGameEngine.PLAYER_RELEASE;
		}
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_GAME);
		gameView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		gameView.onPause();
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//Do Nothing
	}
}
