package edu.umkc.wjfkc2.pocketmoto;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PMHighScore extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscorescreen);
		updateScore();
	}
	private void updateScore(){
		TextView highScore = (TextView)this.findViewById(R.id.highScoreText);
		highScore.setText(Integer.toString(PMGameEngine.highScore));
	}
}
