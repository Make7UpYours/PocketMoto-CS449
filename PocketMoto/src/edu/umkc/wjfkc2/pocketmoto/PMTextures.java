package edu.umkc.wjfkc2.pocketmoto;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/** A generic texture class used to load textures. 
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMTextures {
	private int[] textures = new int[PMGameEngine.NUM_SPRITESHEETS];
	/** Generate textures in constructor to avoid multiple generations. */
	public PMTextures(GL10 gl){
		gl.glGenTextures(PMGameEngine.NUM_SPRITESHEETS, textures, 0);
	}
	/** Loads textures and returns their texture array.
	 *  Method throws a NullPointerException if it is passed an invalid
	 *  textureNumber or texture parameter.
	 */
	public int[] loadTexture(GL10 gl, int texture, Context context, int textureNumber) throws NullPointerException{
		//Obtain texture input stream.
		InputStream imagestream = context.getResources().openRawResource(texture);
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeStream(imagestream);
		}catch(Exception e){
			
		}finally{
			try{
				//Always attempt to close.
				imagestream.close();
				imagestream = null;
			}catch(IOException e){
				
			}
		}
		//Set texture options.
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[textureNumber - 1]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		return textures;
	}
}
