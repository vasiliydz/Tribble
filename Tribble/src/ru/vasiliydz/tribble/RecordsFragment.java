package ru.vasiliydz.tribble;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecordsFragment extends Fragment {

public String records;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_descr_rec, null);
		((TextView) view.findViewById(R.id.textDescrRecTitle)).setText(getResources().getText(R.string.records));
		((TextView) view.findViewById(R.id.textDescrRec)).setText(records);
		return view;
	}

}
