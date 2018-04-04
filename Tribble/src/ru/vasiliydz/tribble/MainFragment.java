package ru.vasiliydz.tribble;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements View.OnClickListener {
	
	private Button button_start;
	private Button button_settings;
	private Button button_description;
	private Button button_records;
	private Button button_exit;
	
	public interface onEventListener {
		public void butClick(View view);
	}
	
	onEventListener eventLis;
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_main, null);
			button_start = (Button)view.findViewById(R.id.buttonStart);
			button_settings = (Button)view.findViewById(R.id.buttonSettings);
			button_description = (Button)view.findViewById(R.id.buttonDescription);
			button_records = (Button)view.findViewById(R.id.buttonRecords);
			button_exit = (Button)view.findViewById(R.id.buttonExit);
			return view;
		}
		
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			eventLis = (onEventListener) activity;
		}
		
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			button_start.setOnClickListener(this);
			button_settings.setOnClickListener(this);
			button_description.setOnClickListener(this);
			button_records.setOnClickListener(this);
			button_exit.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			eventLis.butClick(v);
		}

}
