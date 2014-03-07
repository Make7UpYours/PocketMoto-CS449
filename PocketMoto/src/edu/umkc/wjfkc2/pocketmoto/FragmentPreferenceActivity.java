/*THIS CLASS HAS BEEN OMITTED FROM THE PROGRAM
  IT HAS BEEN KEPT FOR REFERENCE AND LATER USE!!!
package edu.umkc.wjfkc2.pocketmoto;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class FragmentPreferenceActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction mFragmentTransaction = mFragmentManager
				.beginTransaction();
		AppPreferenceFragment mPrefsFragment = new AppPreferenceFragment();
		mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
		mFragmentTransaction.commit();

	}
	  
	 //I added this method so clients don't
	 //have to be concerned with PreferenceManager.
	public static boolean getMusicSetting(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(context.getString(R.string.backgroundMusicKey), false);
	}
}
*/