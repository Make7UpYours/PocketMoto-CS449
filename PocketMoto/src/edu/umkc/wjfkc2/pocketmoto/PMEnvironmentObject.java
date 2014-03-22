package edu.umkc.wjfkc2.pocketmoto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
/** Creates instances of environmental objects that the player must dodge.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMEnvironmentObject {
	public float posY = 0f;
	public float posX = 0f;
	public boolean drawEnviroObject = false;
	public int enviroType = 0;
	
	public Random random = new Random();
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;
	//Image corner vertices.
	private float vertices[] = {
			0.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
	};
	//Texture mapping array, displays 1/4 of the sprite sheet.
	private float texture[] = {
			0.0f, 0.0f,
			0.25f, 0.0f,
			0.25f, 0.25f,
			0.0f, 0.25f,
	};
	//Byte positions forming a square using triangles.
	private byte indices[] = {
			0,1,2,
			0,2,3,
	};
	/** Determines what type of environment object will be drawn,
	 *  if it will actually be drawn to the screen, and the 
	 *  starting position of the object.
	 *  This method was my own creation.
	 */
	public void initializeEnvironmentVariables(){
		enviroType = random.nextInt(PMGameEngine.NUM_ENVIRO_TYPES);
		switch(enviroType){
		case PMGameEngine.OBJ_TYPE_ROCK:
			posX = random.nextFloat() * 3;
			posY = 1f; //TEMP PLACEHOLDER
			break;
		case PMGameEngine.OBJ_TYPE_UPWRD_CAR:
			posX = 2.0f;
			posY = 2f; //TEMP PLACEHOLDER
			break;
		case PMGameEngine.OBJ_TYPE_DWNWRD_CAR:
			posX = 1.0f;
			posY = 3f; //TEMP PLACEHOLDER
			break;
		}
	}
	/** Load up necessary attributes for PMEnvironmentObject. */
	public PMEnvironmentObject() {
		//Populate buffers.
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	/** Draw environment object image. */
	public void draw(GL10 gl, int[] spriteSheet) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, spriteSheet[PMGameEngine.ENVIRONMENT_SPRITE_INDEX]);
		//Enable culling & ignore any vertices not on the front face.
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		//Enable vertex and texture states, load vertices & texture buffers.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2,  GL10.GL_FLOAT, 0, textureBuffer);
		//Draw texture & disable states.
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
}
