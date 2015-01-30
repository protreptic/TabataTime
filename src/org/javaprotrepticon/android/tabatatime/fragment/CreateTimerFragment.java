package org.javaprotrepticon.android.tabatatime.fragment;

import java.sql.SQLException;

import org.javaprotrepticon.android.tabatatime.R;
import org.javaprotrepticon.android.tabatatime.storage.Storage;
import org.javaprotrepticon.android.tabatatime.storage.model.Timer;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

public class CreateTimerFragment extends Fragment {
	
	private EditText field1;
	private EditText field2;
	private EditText field3;
	private EditText field4;
	private EditText field5;
	private EditText field6;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.create_timer_fragment, container, false); 
		
		Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "typeface/RobotoCondensed-Bold.ttf");
		
		field1 = (EditText) relativeLayout.findViewById(R.id.field1);
		field1.setTypeface(typeface1);
		field1.setText("10"); 
		
		TextView labelField1 = (TextView) relativeLayout.findViewById(R.id.labelField1);
		labelField1.setTypeface(typeface1);
		
		field2 = (EditText) relativeLayout.findViewById(R.id.field2);
		field2.setTypeface(typeface1);
		field2.setText("20"); 
		
		TextView labelField2 = (TextView) relativeLayout.findViewById(R.id.labelField2);
		labelField2.setTypeface(typeface1);
		
		field3 = (EditText) relativeLayout.findViewById(R.id.field3);
		field3.setTypeface(typeface1);
		field3.setText("10"); 
		
		TextView labelField3 = (TextView) relativeLayout.findViewById(R.id.labelField3);
		labelField3.setTypeface(typeface1);
		
		field4 = (EditText) relativeLayout.findViewById(R.id.field4);
		field4.setTypeface(typeface1);
		field4.setText("8"); 
		
		TextView labelField4 = (TextView) relativeLayout.findViewById(R.id.labelField4);
		labelField4.setTypeface(typeface1);
		
		field5 = (EditText) relativeLayout.findViewById(R.id.field5);
		field5.setTypeface(typeface1);
		
		TextView labelField5 = (TextView) relativeLayout.findViewById(R.id.labelField5);
		labelField5.setTypeface(typeface1);
		
		field6 = (EditText) relativeLayout.findViewById(R.id.field6);
		field6.setTypeface(typeface1);
		
		TextView labelField6 = (TextView) relativeLayout.findViewById(R.id.labelField6);
		labelField6.setTypeface(typeface1);
		
		return relativeLayout;
	}
	
	private class CreateTimer extends AsyncTask<Timer, Void, Void> {

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Timer... params) {
			Storage storage = new Storage(getActivity());
			Dao<Timer, Integer> dao = (Dao<Timer, Integer>) storage.createDao(params[0].getClass());
			try {
				dao.create(params[0]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
		}
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		hideKeyboard();
	}
	
	private void hideKeyboard() {
	    View view = getActivity().getCurrentFocus();
	    
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true); 
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.create_timer_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.actionSave: {
				Timer timer = new Timer();
				timer.setName(field6.getText().toString());
				timer.setDescription(field5.getText().toString());
				timer.setPrepareInterval(Integer.valueOf(field1.getText().toString())); 
				timer.setWorkInterval(Integer.valueOf(field2.getText().toString()));
				timer.setRestInterval(Integer.valueOf(field3.getText().toString()));
				timer.setRounds(Integer.valueOf(field4.getText().toString())); 
				
				new CreateTimer().execute(timer);
			} break;
			default: {
				return super.onOptionsItemSelected(item);
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}
