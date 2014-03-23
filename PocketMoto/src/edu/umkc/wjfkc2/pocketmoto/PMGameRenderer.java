package edu.umkc.wjfkc2.pocketmoto;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
	private PMBiker player1 = new PMBiker();
	private PMMovementControl movementButtons = new PMMovementControl();
	private PMEnvironmentObject[] environmentObjects =
			new PMEnvironmentObject[PMGameEngine.MAX_ENVIRO_OBJECTS];
	private PMTextures textureLoader;
	private int[] spriteSheets = new int[PMGameEngine.NUM_SPRITESHEETS];
	
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	
	private float bgScroll;
	
	/** Primary Game Loop. */
	@Override
	public void onDrawFrame(GL10 gl) {
		//TODO Auto-generated method stub
		//Ensure that the game runs at 60 fps.
		loopStart = System.currentTimeMillis();
		try {
			if (loopRunTime < PMGameEngine.GAME_THREAD_FPS_SLEEP){
				Thread.sleep(PMGameEngine.GAME_THREAD_FPS_SLEEP);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); //Clear buffers.
		
		scrollBackground(gl);
		movePlayer1(gl);
		moveEnvironmentObjects(gl);
		//My own function calls.
		showButtons(gl);
		
		//Enable transparency for textures.
		gl.glEnable(GL10.GL_BLEND); 
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    loopEnd = System.currentTimeMillis();
	    loopRunTime = loopEnd - loopStart;
	}
	/** Initializes environment objects. */
	private void initializeEnvironment(){
		for (int index = 0; index < PMGameEngine.MAX_ENVIRO_OBJECTS - 1; index++){
			environmentObjects[index] = new PMEnvironmentObject();
			environmentObjects[index].initializeEnvironmentVariables();
		}
	}
	/** Iterates through each environmentObject & draws it on screen.
	 *  Method is similar to moveEnemy() from DiMarzio but is heavily
	 *  modified to fit my game.
	 */
	private void moveEnvironmentObjects(GL10 gl){
		for (int index = 0; index < PMGameEngine.MAX_ENVIRO_OBJECTS -1; index++){
			if (environmentObjects[index].drawEnviroObject){
				switch(environmentObjects[index].enviroType){
				case PMGameEngine.OBJ_TYPE_ROCK:
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glLoadIdentity();
					gl.glPushMatrix();
					gl.glScalef(.25f, .25f, 1f);
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
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glLoadIdentity();
					gl.glPushMatrix();
					gl.glScalef(.25f, .25f, 1f);
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
				case PMGameEngine.OBJ_TYPE_DWNWRD_CAR:
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glLoadIdentity();
					gl.glPushMatrix();
					gl.glScalef(.25f, .25f, 1f);
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
				}
			}
			//Object fell off screen or had not been drawn.
			if (environmentObjects[index].posY <= 0 
					|| !environmentObjects[index].drawEnviroObject){
				environmentObjects[index].initializeEnvironmentVariables();
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
				PMGameEngine.backgroundScrollSpeed -= PMGameEngine.playerBikeHandling;
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
			//TODO REPLACE MAX_BIKE_SPEED WITH BIKE SPEED!!!
			if (PMGameEngine.backgroundScrollSpeed < PMGameEngine.MAX_BIKE_SPEED){
				PMGameEngine.backgroundScrollSpeed += PMGameEngine.playerBikeAcceleration;
			}
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
				PMGameEngine.backgroundScrollSpeed -= PMGameEngine.playerBikeHandling / 2;
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
	/** Controls movement animation of the player character. */
	private void movePlayer1(GL10 gl){
		//Load model matrix mode & scale by .25.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.25f,  .25f,  1f);
		//Place player the default x and y positions.
		gl.glTranslatef(PMGameEngine.curPlayerPosX, .25f, 0f);
		//Draw player texture to screen & pop matrix off the stack.
		player1.draw(gl, spriteSheets);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** Scrolls the stars background image. */
	private void scrollBackground(GL10 gl) {
		//Prevent bgScroll1 from exceeding a max float value.
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
		spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.PLAYER_BIKE_SHEET,
				PMGameEngine.context, PMGameEngine.BIKE_SPRITE_INDEX + 1);
		spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.MOVEMENT_BUTTONS,
				PMGameEngine.context, PMGameEngine.MOVEMENT_BUTTONS_INDEX + 1);
		spriteSheets = textureLoader.loadTexture(gl, PMGameEngine.ENVIRONMENT_OBJECTS,
				PMGameEngine.context, PMGameEngine.ENVIRONMENT_SPRITE_INDEX + 1);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		//Load textures for background image
		grassBackground.loadTexture(gl, PMGameEngine.BACKGROUND_LAYER_ONE, PMGameEngine.context);
		roadBackground.loadTexture(gl, PMGameEngine.BACKGROUND_LAYER_TWO, PMGameEngine.context);
		
		initializeEnvironment();
	}
}
