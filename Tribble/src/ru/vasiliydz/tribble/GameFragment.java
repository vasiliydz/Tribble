package ru.vasiliydz.tribble;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameFragment extends Fragment {
	
	GameSurface surface;
	int difficulty;
	float centerAngleX;
	float centerAngleY;
	Timer timer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		surface = new GameSurface(getActivity());
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics metricsB = new DisplayMetrics();
		display.getMetrics(metricsB);
		surface.Start(difficulty, metricsB.widthPixels, metricsB.heightPixels);
		timer = new Timer();
		timer.scheduleAtFixedRate(new GraphUpdater(surface), 0, 30);
		return surface;
	}
	
	public class GraphUpdater extends TimerTask {
		
		GameSurface surface;
		public GraphUpdater(GameSurface surface) {
			this.surface = surface;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			surface.GameDynamic();
			surface.DrawScreen();
		}
		
	}
}

