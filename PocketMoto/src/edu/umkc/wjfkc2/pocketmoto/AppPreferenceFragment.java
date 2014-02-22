package edu.umkc.wjfkc2.pocketmoto;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AppPreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  addPreferencesFromResource(R.xml.settings);
	}
}
