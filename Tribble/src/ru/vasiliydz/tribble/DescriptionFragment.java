package ru.vasiliydz.tribble;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DescriptionFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_descr_rec, null);
		((TextView) view.findViewById(R.id.textDescrRecTitle)).setText(getResources().getText(R.string.title_description));
		((TextView) view.findViewById(R.id.textDescrRec)).setText(getResources().getText(R.string.description));
		return view;
	}

}
