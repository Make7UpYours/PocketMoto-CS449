package edu.umkc.wjfkc2.pocketmoto;

import java.util.Random;

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
//TODO: IMPLEMENT FUNCTIONALITY TO DRAW PLAYER SUIT ON TOP OF BIKE
public class PMGameRenderer implements Renderer {
	private PMBackground grassBackground = new PMBackground();
	private PMBackground roadBackground = new PMBackground();
	private PMBike playerBike = new PMBike();
	private PMSuit playerSuit = new PMSuit();
	private PMMovementControl movementButtons = new PMMovementControl();
	private PMEnvironmentObject[] environmentObjects =
			new PMEnvironmentObject[PMGameEngine.MAX_ENVIRO_OBJECTS];
	private PMTextures textureLoader;
	private int[] spriteSheets = new int[PMGameEngine.NUM_SPRITESHEETS];
		
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	
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
		//Ensure that the game runs at 60 fps.
		loopStart = System.currentTimeMillis();
		/* Comment out for now, seems to be causing game to run choppy.
		 * Need more understanding of how this block works.
		 * try {
			if (loopRunTime < PMGameEngine.GAME_THREAD_FPS_SLEEP){
				Thread.sleep(PMGameEngine.GAME_THREAD_FPS_SLEEP);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
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
		showButtons(gl);		
		detectInitialEnviroCollisions();
		detectPlayerCollisions();
		
		//Enable transparency for textures.
		gl.glEnable(GL10.GL_BLEND); 
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    loopEnd = System.currentTimeMillis();
	    loopRunTime = loopEnd - loopStart;
	}
	
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
					//TODO: SLOW OR STOP PLAYER AND REDUCE HP ONCE IMPLEMENTED!!!
					//Temp collision event, transport player to new random position.
					Random rand = new Random();
					PMGameEngine.curPlayerPosX = rand.nextFloat() * 3;
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
					//TODO: SLOW OR STOP PLAYER AND REDUCE HP ONCE IMPLEMENTED!!!
					//Temp collision event, transport player to new random position.
					Random rand = new Random();
					PMGameEngine.curPlayerPosX = rand.nextFloat() * 3;
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
					//TODO: SLOW OR STOP PLAYER AND REDUCE HP ONCE IMPLEMENTED!!!
					//Temp collision event, transport player to new random position.
					Random rand = new Random();
					PMGameEngine.curPlayerPosX = rand.nextFloat() * 3;
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
		
		initializeEnvironment();
	}
}
