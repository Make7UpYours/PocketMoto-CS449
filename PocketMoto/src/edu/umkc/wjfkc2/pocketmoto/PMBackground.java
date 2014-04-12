package edu.umkc.wjfkc2.pocketmoto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/** Controls the background images for the game.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMBackground {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;
	
	private int[] textures = new int[1];
	//Image corner vertices.
	private float vertices[] = {
			0.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
	};
	//Texture edge positions.
	private float texture[] = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			1f, 1.0f,
			0f, 1f,
	};
	//Byte positions forming a square using triangles.
	private byte indices[] = {
			0,1,2,
			0,2,3,
	};
	/** Draw background image. */
	public void draw(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		//Enable culling & ignore any vertices not on the front face.
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		//Enable vertex and texture states & load vertices & texture buffers.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		//Draw texture & disable states.
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	/** Constructor for the PMBackground class.*/
	public PMBackground() {
		//Populate buffers with appropriate arrays.
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
	/** Loads in a texture, to be called at initialization.
	 *  Method throws a NullPointerException when it is passed an invalid
	 *  texture parameter.
	 */
	public void loadTexture(GL10 gl, int texture, Context context) throws NullPointerException {
		//Load texture into stream.
		InputStream imagestream = context.getResources().openRawResource(texture);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(imagestream);
		} catch(Exception e){
			
		} finally {
			//Always clear and close.
			try {
				imagestream.close();
				imagestream = null;
			} catch(IOException e){
				
			}
		}
		gl.glGenTextures(1, textures, 0); //Generate a texture pointer.
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]); //Bind texture to OpenGL.
		//Map texture onto vertices.
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		//Repeats texture to allow for scrolling.
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		//Associate bitmap input stream.
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}
}
