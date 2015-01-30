package org.javaprotrepticon.android.tabatatime.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.javaprotrepticon.android.tabatatime.R;
import org.javaprotrepticon.android.tabatatime.storage.model.Timer;

import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimerFragment extends Fragment {
	
	private TextView mTimerCounter;
	private Timer mTimer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.timer_fragment, container, false); 
		
		Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "typeface/RobotoCondensed-Bold.ttf");
		
		mTimerCounter = (TextView) relativeLayout.findViewById(R.id.textView1);
		mTimerCounter.setTypeface(typeface1);
		mTimerCounter.setTextSize(25);
	 	
		return relativeLayout;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		beepSound = soundPool.load(getActivity(), R.raw.beep02, 1);
		finalSound = soundPool.load(getActivity(), R.raw.beep09, 1);
		
		mTimer = getArguments().getParcelable("timer");
		
		mIntervals.add(new IntervalTimerTask(mTimer.getPrepareInterval())); 
		
		for (int i = 0; i < mTimer.getRounds(); i++) {
			mIntervals.add(new IntervalTimerTask(mTimer.getWorkInterval()));
			mIntervals.add(new IntervalTimerTask(mTimer.getRestInterval()));
		}
		
		beginWorkout();
	}
	
	private int beepSound;
	private int finalSound;
	
	private SoundPool soundPool;
	
	private void playIntervals() { 
		for (IntervalTimerTask intervalTimerTask : mIntervals) {
			mIntervals.remove(intervalTimerTask);
			new java.util.Timer().schedule(intervalTimerTask, 500, 1000); break;
		}
	}
	
	class IntervalTimerTask extends TimerTask {

		private Integer mInterval;
		
		public IntervalTimerTask(Integer interval) {
			mInterval = interval;
		}
		
		@Override
		public void run() {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mInterval--;
					
					float volume;
					
					switch (mInterval) {
						case 3: {
							volume = .4f;
							
							soundPool.play(beepSound, volume, volume, 0, 0, 1f);
							
							mTimerCounter.setText("3"); 
						} break;
						case 2: {
							volume = .6f;
							
							soundPool.play(beepSound, volume, volume, 0, 0, 1f);
							
							mTimerCounter.setText("2"); 
						} break;
						case 1: {
							volume = .8f;
							
							soundPool.play(beepSound, volume, volume, 0, 0, 1f);
							
							mTimerCounter.setText("1"); 
						} break;
						case 0: {
							soundPool.play(finalSound, 1f, 1f, 0, 0, 1f);
							
							mTimerCounter.setText("Done!"); 
							
							cancel();
							playIntervals(); 
						} break;
						default: {
							mTimerCounter.setText("" + mInterval); 
						} break;
					}
				}
			});
		}
		
	}
	
	private List<IntervalTimerTask> mIntervals = new ArrayList<IntervalTimerTask>();
	
	private void beginWorkout() {
		playIntervals(); 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true); 
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.timer_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.actionRepeate: {
				beginWorkout();
			} break;
			default: {
				return super.onOptionsItemSelected(item);
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}
