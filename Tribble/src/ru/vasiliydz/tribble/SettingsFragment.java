package ru.vasiliydz.tribble;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class SettingsFragment extends Fragment {
	
	private Button button_back;
	RadioGroup mDifficulty;
	CheckBox music_play;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_settings, null);
		mDifficulty = (RadioGroup) view.findViewById(R.id.difficultRadioGroup);
		music_play = (CheckBox) view.findViewById(R.id.checkMusic);
		button_back = (Button) view.findViewById(R.id.buttonBackFromSettings);
		button_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().onBackPressed();
			}
		});
		return view;
	}

}
