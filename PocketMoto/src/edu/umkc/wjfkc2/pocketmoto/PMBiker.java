package edu.umkc.wjfkc2.pocketmoto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/** Loads & displays the Biker texture.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMBiker {
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
	/** Load up necessary attributes for SFGoodGuy. */
	public PMBiker() {
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
	/** Draw Biker image. */
	public void draw(GL10 gl, int[] spriteSheet) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, spriteSheet[PMGameEngine.BIKE_SPRITE_INDEX]);
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
