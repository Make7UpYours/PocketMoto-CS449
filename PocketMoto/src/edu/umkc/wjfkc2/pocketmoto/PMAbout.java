package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.os.Bundle;

/** Displays information about the app. */
public class PMAbout extends Activity {
	/** Called upon activity creation. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appinfoscreen);
    }
}
