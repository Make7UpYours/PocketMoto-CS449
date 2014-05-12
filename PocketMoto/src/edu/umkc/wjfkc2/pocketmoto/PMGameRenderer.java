package edu.umkc.wjfkc2.pocketmoto;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import junit.framework.Assert;
import android.opengl.GLSurfaceView.Renderer;

/** Renders game images.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMGameRenderer implements Renderer {
	private PMBackground grassBackground = new PMBackground();
	private PMBackground roadBackground = new PMBackground();
	private PMBike playerBike = new PMBike();
	private PMSuit playerSuit = new PMSuit();
	private PMMovementControl movementButtons = new PMMovementControl();
	private PMEnvironmentObject[] environmentObjects =
			new PMEnvironmentObject[PMGameEngine.MAX_ENVIRO_OBJECTS];
	private PMGameInfo[] gameInfos =
			new PMGameInfo[PMGameEngine.NUM_GAME_INFO_TEXTURES];
	private PMNumbers[] numbers = new PMNumbers[PMGameEngine.NUM_NUMBERS];
	private PMTextures textureLoader;
	private int[] spriteSheets = new int[PMGameEngine.NUM_SPRITESHEETS];
	
	private float bgScroll;
	
	public final class OutOfBounds extends Exception{
		public OutOfBounds() {}
		public OutOfBounds(String msg){
			super(msg);
		}
	}
	
	public final class ObjectScaleError extends Exception{
		public ObjectScaleError() {}
		public ObjectScaleError(String msg){
			super(msg);
		}
	}
	
	public final class ColorError extends Exception{
		public ColorError() {}
		public ColorError(String msg){
			super(msg);
		}
	}
	
	/** Primary Game Loop. */
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); //Clear buffers.
		
		scrollBackground(gl);
		drawPlayer1(gl);
		try{
			moveEnvironmentObjects(gl);	
		}
		catch(ObjectScaleError ex){
			System.out.println("PMGameEngine.OBJECT_SCALE HAS BEEN MODIFIED, COLLISION DETECTION WILL NOT WORK PROPERLY!!!");
		}
		//My own function calls.
		drawGameInfo(gl);
		showButtons(gl);		
		detectInitialEnviroCollisions();
		detectPlayerCollisions();
		drawHPNums(gl);
		drawScoreNums(gl);
		checkHP();
		if(PMGameEngine.gameOver){
			drawCreditsNums(gl);
			awardCredits();
			//Determine high score.
			if(PMGameEngine.score > PMGameEngine.highScore){
				PMGameEngine.highScore = PMGameEngine.score;
			}
		}
		
		//Enable transparency for textures.
		gl.glEnable(GL10.GL_BLEND); 
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}
	/** Draws the current credits the player has earned throughout gameplay
	 *  to the screen.
	 *  The method will calculate the numerical value for thousands, hundreds, tens,
	 *  and units and will display their values to the screen.
	 *  The max possible score displayed is 9999.
	 */
	private void drawCreditsNums(GL10 gl){
		if(PMGameEngine.score * 2 == 0){
			drawZero(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
			drawZero(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
			drawZero(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
			drawZero(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if(PMGameEngine.score * 2 >= 9999){
			drawNine(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
			drawNine(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
			drawNine(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
			drawNine(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else{
			calculateCredits(gl);
		}
	}
	/** Determines the current credits that the player is to be awarded and displays
	 *  the correct numerical result to the screen.
	 */
	private void calculateCredits(GL10 gl){
		int thous = (PMGameEngine.score * 2) / 1000;
		int hunds = ((PMGameEngine.score * 2) % 1000) / 100;
		int tens = (((PMGameEngine.score * 2) % 1000) % 100) / 10;
		int units = (((PMGameEngine.score * 2) % 1000) % 100) % 10;
		//Determine thous placement.
		if(thous == 9){
			drawNine(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 8){
			drawEight(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 7){
			drawSeven(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 6){
			drawSix(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 5){
			drawFive(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 4){
			drawFour(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 3){
			drawThree(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 2){
			drawTwo(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (thous == 1){
			drawOne(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.CREDIT_THOUSANDS_NUMBERS_INDEX], PMGameEngine.CREDITS_THOUS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		//Determine hunds placement.
		if(hunds == 9){
			drawNine(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 8){
			drawEight(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 7){
			drawSeven(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 6){
			drawSix(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 5){
			drawFive(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 4){
			drawFour(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 3){
			drawThree(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 2){
			drawTwo(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (hunds == 1){
			drawOne(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.CREDIT_HUNDS_NUMBERS_INDEX], PMGameEngine.CREDITS_HUNDS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		//Determine tens placement.
		if(tens == 9){
			drawNine(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 8){
			drawEight(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 7){
			drawSeven(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 6){
			drawSix(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 5){
			drawFive(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 4){
			drawFour(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 3){
			drawThree(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 2){
			drawTwo(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (tens == 1){
			drawOne(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.CREDIT_TENS_NUMBERS_INDEX], PMGameEngine.CREDITS_TENS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		//Determine units placement.
		if(units == 9){
			drawNine(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 8){
			drawEight(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 7){
			drawSeven(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 6){
			drawSix(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 5){
			drawFive(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 4){
			drawFour(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 3){
			drawThree(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 2){
			drawTwo(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else if (units == 1){
			drawOne(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.CREDIT_UNITS_NUMBERS_INDEX], PMGameEngine.CREDITS_UNITS_NUM_X, PMGameEngine.CREDITS_NUM_Y, gl);
		}
	}
	/** Determines the current score the player has earned and draws the correct
	 *  numerical values to the screen.
	 */
	private void calculateScore(GL10 gl){
		int hunds = PMGameEngine.score / 100;
		int tens = (PMGameEngine.score % 100) / 10;
		int units = (PMGameEngine.score % 100) % 10;
		//Determine hunds placement.
		if(hunds == 9){
			drawNine(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 8){
			drawEight(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 7){
			drawSeven(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 6){
			drawSix(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 5){
			drawFive(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 4){
			drawFour(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 3){
			drawThree(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 2){
			drawTwo(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (hunds == 1){
			drawOne(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		//Determine tens placement.
		if(tens == 9){
			drawNine(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 8){
			drawEight(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 7){
			drawSeven(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 6){
			drawSix(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 5){
			drawFive(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 4){
			drawFour(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 3){
			drawThree(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 2){
			drawTwo(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (tens == 1){
			drawOne(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		//Determine units placement.
		if(units == 9){
			drawNine(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 8){
			drawEight(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 7){
			drawSeven(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 6){
			drawSix(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 5){
			drawFive(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 4){
			drawFour(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 3){
			drawThree(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 2){
			drawTwo(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if (units == 1){
			drawOne(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
	}
	/** Draws the current score the player has earned throughout gameplay
	 *  to the screen.
	 *  The method will calculate the numerical value for hundreds, tens,
	 *  and units and will display their values to the screen.
	 *  The max possible score displayed is 999.
	 */
	private void drawScoreNums(GL10 gl){
		if(PMGameEngine.score == 0){
			drawZero(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
			drawZero(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
			drawZero(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else if(PMGameEngine.score >= 999){
			drawNine(numbers[PMGameEngine.SCORE_UNITS_NUMBERS_INDEX], PMGameEngine.SCORE_UNITS_X, PMGameEngine.SCORE_NUM_Y, gl);
			drawNine(numbers[PMGameEngine.SCORE_TENS_NUMBERS_INDEX], PMGameEngine.SCORE_TENS_X, PMGameEngine.SCORE_NUM_Y, gl);
			drawNine(numbers[PMGameEngine.SCORE_HUNDS_NUMBERS_INDEX], PMGameEngine.SCORE_HUNDS_X, PMGameEngine.SCORE_NUM_Y, gl);
		}
		else{
			calculateScore(gl);
		}
	}
	/** Draws the current HP of the player to the screen.
	 *  Determines the current value of playerHP and calls the proper method
	 *  for draw(Number).
	 */
	private void drawHPNums(GL10 gl){
		if(PMGameEngine.curPlayerHP == 9){
			drawNine(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 8){
			drawEight(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 7){
			drawSeven(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 6){
			drawSix(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 5){
			drawFive(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 4){
			drawFour(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 3){
			drawThree(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 2){
			drawTwo(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else if (PMGameEngine.curPlayerHP == 1){
			drawOne(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
		else{
			drawZero(numbers[PMGameEngine.HP_NUMBERS_INDEX], PMGameEngine.HP_NUM_X,
					PMGameEngine.HP_NUM_Y, gl);
		}
	}
	/** Draws a nine to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawNine(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.25f, 0.5f, 0.0f);
		//Draw nine to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws an eight to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawEight(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.5f, 0.0f);
		//Draw eight to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a seven to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawSeven(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.75f, 0.25f, 0.0f);
		//Draw seven to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a six to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawSix(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.5f, 0.25f, 0.0f);
		//Draw six to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a five to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawFive(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.25f, 0.25f, 0.0f);
		//Draw five to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a four to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawFour(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.25f, 0.0f);
		//Draw four to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a three to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawThree(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.75f, 0.0f, 0.0f);
		//Draw three to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a two to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawTwo(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.5f, 0.0f, 0.0f);
		//Draw two to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a one to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawOne(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.25f, 0.0f, 0.0f);
		//Draw one to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Draws a zero to the screen, used to display score and HP.
	 *  Must pass the instance of PMNumbers that is to the drawn, the
	 *  x position on the screen, and the y position.
	 */
	private void drawZero(PMNumbers num, float x, float y, GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.2575f, .25f, 1f);
		gl.glTranslatef(x, y, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		//Draw zero to screen & pop matrix off stack.
		num.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Determines if the player has run out of hp, if they have
	 *  then method sets PMGameEngine.gameOver to true.
	 */
	private void checkHP(){
		if(PMGameEngine.curPlayerHP <= 0){
			PMGameEngine.gameOver = true;
		}
	}
	/** Displays information about the game state.
	 *  Always displays the score and HP status, will display
	 *  a start message at the very beginning of the game, if
	 *  the player runs out of HP then the method will display
	 *  a game over message.
	 */
	private void drawGameInfo(GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(1f, .125f, 1f);
		gl.glTranslatef(0.0f, 7.0f, 0.0f);
		//Load texture matrix mode and select the correct sprite.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		//Draw score texture to screen & pop matrix off stack.
		gameInfos[PMGameEngine.SCORE_GAME_INFO_INDEX].draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		//Draw game status update.
		if(bgScroll < 3){
			//Display start message.
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f, .5f, 1f);
			gl.glTranslatef(0.0f, -(bgScroll), 0.0f);
			//Load texture matrix mode and select the correct sprite.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, .25f, 0.0f);
			//Draw button texture to screen & pop matrix off stack.
			gameInfos[PMGameEngine.SCORE_GAME_INFO_INDEX].draw(gl, spriteSheets);
			gl.glPopMatrix();
			gl.glLoadIdentity();
		}
		if(PMGameEngine.gameOver == true){
			//Display Game Over message.
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f, .5f, 1f);
			gl.glTranslatef(0.0f, 0.5f, 0.0f);
			//Load texture matrix mode and select the correct sprite.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, .5f, 0.0f);
			//Draw button texture to screen & pop matrix off stack.
			gameInfos[PMGameEngine.SCORE_GAME_INFO_INDEX].draw(gl, spriteSheets);
			gl.glPopMatrix();
			gl.glLoadIdentity();
		}
	}
	/** Awards credits that the player has earned based upon their score
	 *  during the game.
	 */
	private void awardCredits(){
		if (!PMGameEngine.creditsAwarded){
			PMGameEngine.playerEarnings += PMGameEngine.score * 2;
			PMGameEngine.creditsAwarded = true;
		}
	}
	/** Draws the bike and suit image to the screen	 */
	private void drawPlayer1(GL10 gl){
		try{
			drawBike(gl);
			drawSuit(gl);
		}
		catch(ObjectScaleError ex){
			System.out.println("PMGameEngine.OBJECT_SCALE HAS BEEN MODIFIED, COLLISION DETECTION WILL NOT WORK PROPERLY!!!");
		}
		try{
			movePlayer1Lat();	
		}
		catch (OutOfBounds ex){
			System.out.println("Player has exceeded boundry limits.");
		}
	}
	/** Determines if the screen is being rotated by the player.
	 *  If the screen is being rotated, the method will update the
	 *  biker's x position on screen.
	 *  The rate at which the biker moves is modified by the bike's
	 *  handling.
	 *  If the biker somehow ends up going off screen, method will throw
	 *  an OutOfBounds exception.
	 *  Uses an assert to verify that PMGameEngine.playerBikeHandling is
	 *  not a negative value.
	 */
	private void movePlayer1Lat() throws OutOfBounds{
		Assert.assertTrue("PMGameEngine.playerBikeHandling should be a positive value.",
				PMGameEngine.playerBikeHandling > 0);
		switch(PMGameEngine.playerBikeLatAction){
		case PMGameEngine.SCREEN_ROT_LEFT:
			if(PMGameEngine.curPlayerPosX > 0){
				PMGameEngine.curPlayerPosX += PMGameEngine.playerBikeTurnRate
						* PMGameEngine.playerBikeHandling;
			}
			break;
		case PMGameEngine.SCREEN_ROT_RIGHT:
			if (PMGameEngine.curPlayerPosX < 3){
				PMGameEngine.curPlayerPosX += PMGameEngine.playerBikeTurnRate
						* PMGameEngine.playerBikeHandling;
			}
			break;
		}
		if (PMGameEngine.curPlayerPosX < -.875
				|| PMGameEngine.curPlayerPosX > 3.125){
			OutOfBounds boundryError =
					new OutOfBounds("Somehow player fell off the screen!");
			throw boundryError;
		}
	}
	/** Detects collisions between the player and environment objects. Takes
	 *  into account four points on the player texture image to test for collisions.
	 *  The numbers in the if blocks were calculated from the scale and position of
	 *  the texture images.
	 *  CHANGING THE SCALE OR TEXTURE WILL AFFECT THE NUMERICAL VALUES IN THE IF
	 *  STATEMENTS!!!
	 *  This method was designed by myself.
	 */
	private void detectPlayerCollisions(){
		for (int index = 0; index < PMGameEngine.MAX_ENVIRO_OBJECTS; index++){
			//Take into account the difference between enviroType image sizes.
			switch(environmentObjects[index].enviroType){
			case PMGameEngine.OBJ_TYPE_ROCK:
				if (((PMGameEngine.PLAYER_Y_POS - 0.0625f >= environmentObjects[index].posY - 0.875f
						&& PMGameEngine.PLAYER_Y_POS - 0.0625f <= environmentObjects[index].posY - 0.475f)
						&& (PMGameEngine.curPlayerPosX + 0.25f <= environmentObjects[index].posX + 0.90625f
						&& PMGameEngine.curPlayerPosX + 0.25f >= environmentObjects[index].posX + 0.1171875f))
						//Take into account the upper right origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.0625f >= environmentObjects[index].posY - 0.875f
						&& PMGameEngine.PLAYER_Y_POS - 0.0625f <= environmentObjects[index].posY - 0.475f)
						&& (PMGameEngine.curPlayerPosX + 0.75f <= environmentObjects[index].posX + 0.90625f
						&& PMGameEngine.curPlayerPosX + 0.75f >= environmentObjects[index].posX+ 0.1171875f)
						//Take into account the lower left origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.921875f >= environmentObjects[index].posY - 0.875f
						&& PMGameEngine.PLAYER_Y_POS - 0.921875f <= environmentObjects[index].posY - 0.475f)
						&& (PMGameEngine.curPlayerPosX + 0.390625f <= environmentObjects[index].posX + 0.90625f
						&& PMGameEngine.curPlayerPosX + 0.390625f >= environmentObjects[index].posX+ 0.1171875f)
						//Take into account the lower right origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.921875f >= environmentObjects[index].posY - 0.875f
						&& PMGameEngine.PLAYER_Y_POS - 0.921875f <= environmentObjects[index].posY - 0.475f)
						&& (PMGameEngine.curPlayerPosX + 0.609375f <= environmentObjects[index].posX + 0.90625f
						&& PMGameEngine.curPlayerPosX + 0.609375f >= environmentObjects[index].posX+ 0.1171875f)){
					//Player has collided with environment
					//TODO: SLOW OR STOP PLAYER!!!
					if (!environmentObjects[index].hitPlayer){
						PMGameEngine.curPlayerHP--;
						environmentObjects[index].hitPlayer = true;
					}
				}
				break;
			case PMGameEngine.OBJ_TYPE_UPWRD_CAR:
				if (((PMGameEngine.PLAYER_Y_POS - 0.0625f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.0625f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.25f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.25f >= environmentObjects[index].posX + 0.171875f))
						//Take into account the upper right origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.0625f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.0625f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.75f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.75f >= environmentObjects[index].posX + 0.171875f)
						//Take into account the lower left origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.921875f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.921875f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.390625f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.390625f >= environmentObjects[index].posX + 0.171875f)
						//Take into account the lower right origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.921875f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.921875f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.609375f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.609375f >= environmentObjects[index].posX + 0.171875f)){
					//Player has collided with environment
					//TODO: SLOW OR STOP PLAYER!!!
					if (!environmentObjects[index].hitPlayer){
						PMGameEngine.curPlayerHP--;
						environmentObjects[index].hitPlayer = true;
					}
				}
				break;
			case PMGameEngine.OBJ_TYPE_DWNWRD_CAR:
				if (((PMGameEngine.PLAYER_Y_POS - 0.0625f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.0625f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.25f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.25f >= environmentObjects[index].posX + 0.171875f))
						//Take into account the upper right origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.0625f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.0625f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.75f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.75f >= environmentObjects[index].posX + 0.171875f)
						//Take into account the lower left origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.921875f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.921875f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.390625f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.390625f >= environmentObjects[index].posX + 0.171875f)
						//Take into account the lower right origin point of the player image.
						|| (PMGameEngine.PLAYER_Y_POS - 0.921875f >= environmentObjects[index].posY - 1
						&& PMGameEngine.PLAYER_Y_POS - 0.921875f <= environmentObjects[index].posY - 0.03125f)
						&& (PMGameEngine.curPlayerPosX + 0.609375f <= environmentObjects[index].posX + 0.828125f
						&& PMGameEngine.curPlayerPosX + 0.609375f >= environmentObjects[index].posX + 0.171875f)){
					//Player has collided with environment
					//TODO: SLOW OR STOP PLAYER!!!
					if (!environmentObjects[index].hitPlayer){
						PMGameEngine.curPlayerHP--;
						environmentObjects[index].hitPlayer = true;
					}
				}
				break;
			}
		}
	}
	/** Detects collision events between environmental objects before they
	 *  appear on screen. If a collision does occur then the object will be
	 *  redrawn.
	 *  This function is a heavily modified version of DiMarzio's
	 *  detectCollisions() function.
	 */
	private void detectInitialEnviroCollisions(){
		for (int i = 0; i < PMGameEngine.MAX_ENVIRO_OBJECTS; i++){
			//Determine the initial object's type.
			switch(environmentObjects[i].enviroType){
			case PMGameEngine.OBJ_TYPE_ROCK:
				for (int j = 0; j < PMGameEngine.MAX_ENVIRO_OBJECTS; j++){
					if (i != j){
						//Objects will only collide if they are the same type.
						if (environmentObjects[j].enviroType 
								== PMGameEngine.OBJ_TYPE_ROCK){
							if((environmentObjects[i].posY >= environmentObjects[j].posY - 1
									&& environmentObjects[i].posY <= environmentObjects[j].posY)
									&& (environmentObjects[i].posX <= environmentObjects[j].posX + 1
									&& environmentObjects[i].posX >= environmentObjects[j].posX)){
								//Collision occurred reinitialize object.
								environmentObjects[i].initializeEnvironmentVariables();
							}
							
						}
					}
				}
				break;
			case PMGameEngine.OBJ_TYPE_UPWRD_CAR:
				for (int j = 0; j < PMGameEngine.MAX_ENVIRO_OBJECTS; j++){
					if (i != j){
						//Objects will only collide if they are the same type.
						if (environmentObjects[j].enviroType
								== PMGameEngine.OBJ_TYPE_UPWRD_CAR){
							if((environmentObjects[i].posY >= environmentObjects[j].posY - 1
									&& environmentObjects[i].posY <= environmentObjects[j].posY)
									&& (environmentObjects[i].posX <= environmentObjects[j].posX + 1
									&& environmentObjects[i].posX >= environmentObjects[j].posX)){
								//Collision occurred reinitialize object.
								environmentObjects[i].initializeEnvironmentVariables();
							}
						}	
					}
				}
				break;
			case PMGameEngine.OBJ_TYPE_DWNWRD_CAR:
				for (int j = 0; j < PMGameEngine.MAX_ENVIRO_OBJECTS; j++){
					if (i != j){
						//Objects will only collide if they are the same type.
						if (environmentObjects[j].enviroType
								== PMGameEngine.OBJ_TYPE_DWNWRD_CAR){
							if((environmentObjects[i].posY >= environmentObjects[j].posY - 1.75
									&& environmentObjects[i].posY <= environmentObjects[j].posY)
									&& (environmentObjects[i].posX <= environmentObjects[j].posX + 1
									&& environmentObjects[i].posX >= environmentObjects[j].posX)){
								//Collision occurred reinitialize object.
								environmentObjects[i].initializeEnvironmentVariables();
							}
						}	
					}
				}
				break;
			}
		}
	}
	/** Initializes instances of Numbers objects. */
	private void initializeNumbers(){
		for (int index = 0; index < PMGameEngine.NUM_NUMBERS; index++){
			numbers[index] = new PMNumbers();
		}
	}
	/** Initializes Game Info objects. */
	private void initializeGameInfo(){
		for (int index = 0; index < PMGameEngine.NUM_GAME_INFO_TEXTURES; index++){
			gameInfos[index] = new PMGameInfo();
		}
	}
	/** Initializes environment objects. */
	private void initializeEnvironment(){
		for (int index = 0; index < PMGameEngine.MAX_ENVIRO_OBJECTS; index++){
			environmentObjects[index] = new PMEnvironmentObject();
			environmentObjects[index].initializeEnvironmentVariables();
		}
	}
	/** Iterates through each environmentObject & draws it on screen.
	 *  Method is similar to moveEnemy() from DiMarzio but is heavily
	 *  modified to fit my game.
	 *  If for some reason PMGameEngine.OBJECT_SCALE gets changed from 0.25,
	 *  this method will throw an ObjectScaleError exception.
	 *  This exception adds additional error detection for collisions
	 */
	@SuppressWarnings("unused")
	private void moveEnvironmentObjects(GL10 gl) throws ObjectScaleError{
		for (int index = 0; index < PMGameEngine.MAX_ENVIRO_OBJECTS; index++){
			switch(environmentObjects[index].enviroType){
			case PMGameEngine.OBJ_TYPE_ROCK:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed;
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.0f, 0.0f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();	
				break;
			case PMGameEngine.OBJ_TYPE_UPWRD_CAR:
				drawCarColor(gl, index, PMGameEngine.OBJ_TYPE_UPWRD_CAR);
				break;
			case PMGameEngine.OBJ_TYPE_DWNWRD_CAR:
				drawCarColor(gl, index, PMGameEngine.OBJ_TYPE_DWNWRD_CAR);
				break;
			}
			//Object fell off screen.
			if (environmentObjects[index].posY <= -2 
					|| environmentObjects[index].posY >= 11){
				if (!environmentObjects[index].hitPlayer 
						&& !PMGameEngine.gameOver){
					//Award points to player.
					PMGameEngine.score++;
				}
				environmentObjects[index].initializeEnvironmentVariables();
			}
		}
		if (PMGameEngine.OBJECT_SCALE != 0.25f){
			ObjectScaleError objectScaleError =
					new ObjectScaleError("PMGameEngine.OBJECT_SCALE has been modified.");
			throw objectScaleError;
		}
	}
	/** Draws the correct color of the car and draws it on screen.
	 *  I designed this function myself. There is absolutely no
	 *  code taken from DiMarzio.
	 */
	private void drawCarColor(GL10 gl, int index, int carDirection){
		if(carDirection == PMGameEngine.OBJ_TYPE_UPWRD_CAR){
			switch(environmentObjects[index].carColor){
			case PMGameEngine.YELLOW_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in upward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						- PMGameEngine.CAR_SPEED; 
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.25f, 0.0f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();
				break;
			case PMGameEngine.GREY_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in upward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						- PMGameEngine.CAR_SPEED; 
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.25f, 0.25f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();
				break;
			case PMGameEngine.RED_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in upward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						- PMGameEngine.CAR_SPEED; 
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.25f, 0.5f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();
				break;
			case PMGameEngine.GREEN_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in upward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						- PMGameEngine.CAR_SPEED; 
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.25f, 0.75f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();
				break;
			}	
		}else{
			switch(environmentObjects[index].carColor){
			case PMGameEngine.YELLOW_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in downward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						+ PMGameEngine.CAR_SPEED;
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.5f, 0.0f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();	
				break;
			case PMGameEngine.GREY_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in downward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						+ PMGameEngine.CAR_SPEED;
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.5f, 0.25f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();	
				break;
			case PMGameEngine.RED_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in downward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						+ PMGameEngine.CAR_SPEED;
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.5f, 0.5f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();	
				break;
			case PMGameEngine.GREEN_CAR:
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
				//Car always moves in downward direction.
				environmentObjects[index].posY -= PMGameEngine.backgroundScrollSpeed
						+ PMGameEngine.CAR_SPEED;
				gl.glTranslatef(environmentObjects[index].posX,
						environmentObjects[index].posY, 0f);
				//Load up texture mode and select the current texture.
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(0.5f, 0.75f, 0.0f);
				environmentObjects[0].draw(gl, spriteSheets);
				gl.glPopMatrix();
				gl.glLoadIdentity();	
				break;
			}	
		}
	}
	/** Determines if the player has run off the road.
	 *  If the player is off-road then reduce their acceleration and speed.
	 *  Method will slow down the player gradually when the bike is off-road.
	 *  This method was designed by myself.
	 */
	//TODO: MAY NEED TO TWEAK NUMBER VALUES AFTER GAMEPLAY TESTING!!!
	private void detectOffRoad(){
		if ((PMGameEngine.curPlayerPosX + 0.4f < 1)
				|| (PMGameEngine.curPlayerPosX + 0.6f > 3)){
			//Player has gone off-road, reduce speed and acceleration.
			PMGameEngine.curPlayerBikeSpeed = PMGameEngine.playerBikeSpeed / 2;
			PMGameEngine.curPlayerBikeAcceleration = PMGameEngine.playerBikeAcceleration / 2;
			if (PMGameEngine.backgroundScrollSpeed 
					< (PMGameEngine.curPlayerBikeSpeed - PMGameEngine.curPlayerBikeAcceleration)){
				PMGameEngine.backgroundScrollSpeed += PMGameEngine.curPlayerBikeAcceleration;
			}else if (PMGameEngine.backgroundScrollSpeed > PMGameEngine.curPlayerBikeSpeed){
				//Gradually slow down the player when they go off-road.
				PMGameEngine.backgroundScrollSpeed -= PMGameEngine.playerBikeHandling / 2;
			}
		}else{
			//Player on-road, bring curSpeed & curAcceleration back to normal values.
			PMGameEngine.curPlayerBikeSpeed = PMGameEngine.playerBikeSpeed;
			PMGameEngine.curPlayerBikeAcceleration = PMGameEngine.playerBikeAcceleration;
			if (PMGameEngine.backgroundScrollSpeed
					< (PMGameEngine.curPlayerBikeSpeed - PMGameEngine.curPlayerBikeAcceleration)){
				PMGameEngine.backgroundScrollSpeed += PMGameEngine.curPlayerBikeAcceleration;
			}
		}
	}
	/** Displays the proper animation for the movement control buttons.
	 *  This function uses some code from DiMarzio, but only the OpenGL calls
	 *  and the switch case design.
	 *  I designed this function myself.
	 */
	private void showButtons(GL10 gl){
		//Load model matrix mode & scale height.
		switch (PMGameEngine.playerBikeAction){
		case PMGameEngine.PLAYER_BRAKE:
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f, .075f, 1f);
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			//Load texture matrix mode and select the correct sprite.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f,0.5f, 0.0f);
			//Draw button texture to screen & pop matrix off stack.
			movementButtons.draw(gl, spriteSheets);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			//Slow scroll speed but prevent negative scroll values.
			if (PMGameEngine.backgroundScrollSpeed > 0){
				PMGameEngine.backgroundScrollSpeed -= PMGameEngine.playerBikeHandling
						* PMGameEngine.BRAKE_SPEED_MODIFIER;
			} else{
				PMGameEngine.backgroundScrollSpeed = 0;
			}
			break;
		case PMGameEngine.PLAYER_THROTTLE:
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f, .075f, 1f);
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			//Load texture matrix mode and select the correct sprite.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f,0.25f, 0.0f); 
			//Draw button texture to screen & pop matrix off stack.
			movementButtons.draw(gl, spriteSheets);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			//Determine the player's speed & acceleration.
			detectOffRoad();
			break;
		case PMGameEngine.PLAYER_RELEASE:
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f, .075f, 1f);
			//Draw button texture to screen & pop matrix off stack.
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			movementButtons.draw(gl, spriteSheets);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			//Slow scroll speed but prevent negative scroll values.
			if (PMGameEngine.backgroundScrollSpeed > 0){
				//Engine bogs down at a lesser rate than braking
				PMGameEngine.backgroundScrollSpeed -= PMGameEngine.playerBikeHandling
						* (PMGameEngine.BRAKE_SPEED_MODIFIER / 2);
			} else{
				PMGameEngine.backgroundScrollSpeed = 0;
			}
			break;
		default:
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f, .075f, 1f);
			//Draw button texture to screen & pop matrix off stack.
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			movementButtons.draw(gl, spriteSheets);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			break;
		}
	}
	/** Draws the bike in the correct location of the screen.
	 *  If for some reason PMGameEngine.OBJECT_SCALE gets changed from 0.25,
	 *  this method will throw an ObjectScaleError exception.
	 *  This exception adds additional error detection for collisions
	 */
	@SuppressWarnings("unused")
	private void drawBike(GL10 gl) throws ObjectScaleError{
		//Load model matrix mode & scale by .25.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
		//Update player's position.
		gl.glTranslatef(PMGameEngine.curPlayerPosX, PMGameEngine.PLAYER_Y_POS, 0f);
		//Select the proper texture in the sprite sheet.
		loadBikeTexture(gl);
		//Draw player texture to screen & pop matrix off the stack.
		playerBike.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		if (PMGameEngine.OBJECT_SCALE != 0.25f){
			ObjectScaleError objectScaleError =
					new ObjectScaleError("PMGameEngine.OBJECT_SCALE has been modified.");
			throw objectScaleError;
		}
	}
	/** Draws the suit in the correct location of the screen.
	 *  If for some reason PMGameEngine.OBJECT_SCALE gets changed from 0.25,
	 *  this method will throw an ObjectScaleError exception.
	 *  This exception adds additional error detection for collisions
	 */
	@SuppressWarnings("unused")
	private void drawSuit(GL10 gl) throws ObjectScaleError{
		//Load model matrix mode & scale by .25.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(PMGameEngine.OBJECT_SCALE, PMGameEngine.OBJECT_SCALE, 1f);
		//Update player's position.
		gl.glTranslatef(PMGameEngine.curPlayerPosX, PMGameEngine.PLAYER_Y_POS, 0f);
		//Select the proper texture in the sprite sheet.
		loadSuitTexture(gl);
		//Draw player texture to screen & pop matrix off the stack.
		playerSuit.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		if (PMGameEngine.OBJECT_SCALE != 0.25f){
			ObjectScaleError objectScaleError =
					new ObjectScaleError("PMGameEngine.OBJECT_SCALE has been modified.");
			throw objectScaleError;
		}
	}
	/** Selects the proper bike image from the bike sprite sheet based upon
	 *  the current bike color selection.
	 *  Method makes use of assertions to verify that playerBike.bikeColor has
	 *  not been set to an improper value.
	 */
	private void loadBikeTexture(GL10 gl){
		Assert.assertTrue("playerBike.bikeColor exceeds PMGameEngine.NUM_BIKES.",
				playerBike.bikeColor < PMGameEngine.NUM_BIKES);
		Assert.assertTrue("playerBike.bikeColor is less than 0.",
				playerBike.bikeColor >= 0);
		switch(playerBike.bikeColor){
		case PMGameEngine.RED_BIKE:
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			break;
		case PMGameEngine.PURPLE_BIKE:
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.25f, 0.0f);
			break;
		case PMGameEngine.YELLOW_BIKE:
			//Load up texture mode and select the current texture.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.5f, 0.0f);
			break;
		case PMGameEngine.GREEN_BIKE:
			//Load up texture mode and select the current texture.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.75f, 0.0f);
			break;
		}
	}
	/** Selects the proper suit image from the bike sprite sheet based upon
	 *  the current suit color selection.
	 *  Method makes use of assertions to verify that playerBike.bikeColor has
	 *  not been set to an improper value.
	 */
	private void loadSuitTexture(GL10 gl){
		Assert.assertTrue("playerSuit.suitColor exceeds PMGameEngine.NUM_SUITS.",
				playerSuit.suitColor < PMGameEngine.NUM_SUITS);
		Assert.assertTrue("playerSuit.suitColor is less than 0.",
				playerSuit.suitColor >= 0);
		switch(playerSuit.suitColor){
		case PMGameEngine.BLUE_SUIT:
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			break;
		case PMGameEngine.GREY_SUIT:
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.25f, 0.0f);
			break;
		case PMGameEngine.ORANGE_SUIT:
			//Load up texture mode and select the current texture.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.5f, 0.0f);
			break;
		case PMGameEngine.NEON_SUIT:
			//Load up texture mode and select the current texture.
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.75f, 0.0f);
			break;
		}
	}
	/** Scrolls the background image. */
	private void scrollBackground(GL10 gl) {
		//Prevent bgScroll1 from exceeding a max float value.
		assert bgScroll < Float.MAX_VALUE:
			"bgScroll has exceeded the max value of a float.";
		if (bgScroll == Float.MAX_VALUE){
			bgScroll = 0f;
		}
		/* This code just resets the scale and translate of the
		 * Model matrix mode, we are not moving it.*/
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(1f, 1f, 1f);
		gl.glTranslatef(0f, 0f, 0f);
		//Load up texture matrix mode and begin scrolling.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, bgScroll, 0.0f); //scrolling the texture.
		grassBackground.draw(gl);
		/* Scale down and draw the road image.
		 * This code segment is my own code and not J.F. DiMarzio's.
		 */
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glScalef(.5f, 1f, 1f); //Scale down the road image by half on the width.
		gl.glTranslatef(.5f, 0f, 0f); //Translate road image to middle
		//Load up texture matrix mode and begin scrolling.
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, bgScroll, 0.0f);
		roadBackground.draw(gl);
		//pop matrix back on the stack.
		gl.glPopMatrix();
		bgScroll += PMGameEngine.backgroundScrollSpeed;
		gl.glLoadIdentity();
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//Load up texture sheet.
		textureLoader = new PMTextures(gl);
		//Load sprite sheets.
		try{
			spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.PLAYER_BIKE_SHEET,
					PMGameEngine.context, PMGameEngine.BIKE_SPRITE_INDEX + 1);
			spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.MOVEMENT_BUTTONS,
					PMGameEngine.context, PMGameEngine.MOVEMENT_BUTTONS_INDEX + 1);
			spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.ENVIRONMENT_OBJECTS,
					PMGameEngine.context, PMGameEngine.ENVIRONMENT_SPRITE_INDEX + 1);
			spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.PLAYER_SUIT_SHEET,
					PMGameEngine.context, PMGameEngine.SUIT_SPRITE_INDEX + 1);
			spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.GAME_INFO,
					PMGameEngine.context, PMGameEngine.GAME_INFO_INDEX + 1);
			spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.NUMBERS,
					PMGameEngine.context, PMGameEngine.NUMBERS_INDEX + 1);
		}
		catch(NullPointerException e){
			System.out.println("Failed to load sprite textures.");
		}
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		//Load textures for background image
		try{
			grassBackground.loadTexture(gl, PMGameEngine.BACKGROUND_LAYER_ONE, PMGameEngine.context);
			roadBackground.loadTexture(gl, PMGameEngine.BACKGROUND_LAYER_TWO, PMGameEngine.context);	
		}
		catch(NullPointerException e){
			System.out.println("Failed to load background textures.");
		}
		//Initialize multiple object instances.
		initializeEnvironment();
		initializeGameInfo();
		initializeNumbers();
	}
}
