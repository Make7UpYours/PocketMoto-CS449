package edu.umkc.wjfkc2.pocketmoto;

import android.content.Context;
import android.opengl.GLSurfaceView;

/** Sets the current view for the game.
 *  Most of this code was came from: Practical Android 4 Games Development
 *  by J.F. DiMarzio
 *  It will be noted where I used my own code. There might be areas
 *  that are slightly modified numerically from J.F. DiMarzio's code
 *  and these will not be noted. 
 */
public class PMGameView extends GLSurfaceView {
	private PMGameRenderer renderer;
	
	public PMGameView(Context context) {
		super(context);
		
		renderer = new PMGameRenderer();
		
		this.setRenderer(renderer);
	}
}
