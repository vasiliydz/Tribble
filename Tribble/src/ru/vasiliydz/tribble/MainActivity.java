package ru.vasiliydz.tribble;

import ru.vasiliydz.tribble.MainFragment.onEventListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements onEventListener {
public static final int VERY_SIMPLE = 1;
public static final int SIMPLE = 2;
public static final int HARDCORE = 3;
public int difficulty = VERY_SIMPLE;
private FragmentManager fragmentManager;
private FragmentTransaction transaction;
MainFragment mainFragment;
DescriptionFragment descrFragment;
RecordsFragment recFragment;
GameFragment gameFragment;
SettingsFragment settingsFragment;
public boolean isPlaying = false;
MediaPlayer music;
LinearLayout view;
EditText input_name;
public int[] min_record = new int[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		music = MediaPlayer.create(this, R.raw.music);
		music.setLooping(true);
		mainFragment = new MainFragment();
		descrFragment = new DescriptionFragment();
		recFragment = new RecordsFragment();
		settingsFragment = new SettingsFragment();
		fragmentManager = getFragmentManager();
		SharedPreferences records = getSharedPreferences("records", Context.MODE_PRIVATE);
		if (!records.contains("name11")) {
			SharedPreferences.Editor editor = records.edit();
			String names[] = {"Mr. Spock","Scotty","Captain Kirk","Васян","Chekov","Dr. McCoy","Captain Kirk","Chekov","Dr. McCoy","Mr. Spock","Scotty","Васян","Captain Kirk","Mr. Spock","Dr. McCoy","Васян","Chekov","Scotty"};
			int scores[] = {361,243,225,169,121,100,97,86,62,54,42,36,80,63,46,38,30,17};
			for (int i = 1; i <= 3; i++) {
				for (int j = 1; j <= 6; j++) {
					editor.putString("name"+i+j, names[6*i+j-7]);
					editor.putInt("score"+i+j, scores[6*i+j-7]);
				}
			}
			editor.apply();
		}
		for (int i = 0; i < 3; i++) {
			min_record[i] = records.getInt("score"+(i+1)+"6", 0);
		}
		transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.fragmentContainer, settingsFragment);
		transaction.commit();
		transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragmentContainer, mainFragment);
		transaction.commit();

	}
	
	@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			if(music.isPlaying()) music.pause();
		}
	@Override
		protected void onRestart() {
			// TODO Auto-generated method stub
			super.onRestart();
			if(settingsFragment.music_play.isChecked()) music.start();
		}
	
	@Override
	public void butClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.buttonExit) {
			music.stop();
			finish();
		}
		transaction = getFragmentManager().beginTransaction();
		switch (v.getId()) {
		case R.id.buttonStart:
			gameFragment = new GameFragment();
			gameFragment.difficulty = difficulty;
			transaction.replace(R.id.fragmentContainer, gameFragment);
			isPlaying = true;
			break;
			
		case R.id.buttonSettings:
			transaction.replace(R.id.fragmentContainer, settingsFragment);
			break;
			
		case R.id.buttonDescription:
			transaction.replace(R.id.fragmentContainer, descrFragment);
			break;
			
		case R.id.buttonRecords:
			recFragment.records = makeRecordList();
			transaction.replace(R.id.fragmentContainer, recFragment);
			break;
		}
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isPlaying) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				gameFragment.surface.press = true;
				if (event.getX() <= gameFragment.surface.screenX/2) {gameFragment.surface.counterclockwise = true;}
				else {gameFragment.surface.counterclockwise = false;}
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				gameFragment.surface.press = false;
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
		if(isPlaying) {
			if (gameFragment.surface.eated > min_record[difficulty-1]) {
				showDialog(1);	
				}
			else {
				super.onBackPressed();
				isPlaying = false;
				if(gameFragment != null) {
					gameFragment = null;
				}
				}
			}
		else {
			super.onBackPressed();
			if(settingsFragment.music_play.isChecked() & (!music.isPlaying())) music.start();
			if((!settingsFragment.music_play.isChecked()) & music.isPlaying()) music.pause();
			switch(settingsFragment.mDifficulty.getCheckedRadioButtonId()) {
				
				case R.id.radioDiffVerySimple:
					difficulty = VERY_SIMPLE;
					break;
					
				case R.id.radioDiffSimple:
					difficulty = SIMPLE;
					break;
					
				case R.id.radioDiffHardcore:
					difficulty = HARDCORE;
					break;
				}
			}
		}
	
	public String makeRecordList() {
		String string = "";
		SharedPreferences records = getSharedPreferences("records", Context.MODE_PRIVATE);
		for (int i = 1; i <= 6; i++) {
			string = string + records.getString("name"+difficulty+i, "")+" "+records.getInt("score"+difficulty+i, 0)+"\n";
		}
		return string;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle(getResources().getString(R.string.your_name));
	view = (LinearLayout) getLayoutInflater().inflate(R.layout.record_dialog, null);
	builder.setView(view);
	builder.setPositiveButton(R.string.ok, dialogClickListener);
	builder.setNeutralButton(R.string.cancel, dialogClickListener);
	input_name = (EditText)view.findViewById(R.id.textName);
		return builder.create();
	}

OnClickListener dialogClickListener = new OnClickListener() {
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case Dialog.BUTTON_POSITIVE:
			putNameToRecordList(input_name.getText().toString(), gameFragment.surface.eated);
			input_name.setText("");
			isPlaying = false;
			onBackPressed();
			transaction = getFragmentManager().beginTransaction();
			recFragment.records = makeRecordList();
			transaction.replace(R.id.fragmentContainer, recFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
			
		case Dialog.BUTTON_NEUTRAL:
			break;

		default:
			break;
		}
	}
};
	
	public void putNameToRecordList(String name, int score) {
		SharedPreferences records = getSharedPreferences("records", Context.MODE_PRIVATE);
		int i = 6;
		while ((score > records.getInt("score"+difficulty+i, 0)) & i!=0) {
			i--;
		}
		if (i < 6) {
			SharedPreferences.Editor editor = records.edit();
			for (int j = 6; j > i+1; j--) {
				editor.putString("name"+difficulty+j, records.getString("name"+difficulty+(j-1), ""));
				editor.putInt("score"+difficulty+j, records.getInt("score"+difficulty+(j-1), 0));
			}
			if(name.isEmpty()) {name = getResources().getString(R.string.unknown);}
			editor.putString("name"+difficulty+(i+1), name);
			editor.putInt("score"+difficulty+(i+1), score);
			editor.apply();
		}
		for (int j = 0; j < 3; j++) {
			min_record[j] = records.getInt("score"+(j+1)+"6", 0);
		}
	}
	
}