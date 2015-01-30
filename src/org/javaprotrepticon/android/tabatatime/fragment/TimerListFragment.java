package org.javaprotrepticon.android.tabatatime.fragment;

import java.sql.SQLException;

import org.javaprotrepticon.android.tabatatime.R;
import org.javaprotrepticon.android.tabatatime.fragment.base.BaseEntityListFragment;
import org.javaprotrepticon.android.tabatatime.storage.model.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class TimerListFragment extends BaseEntityListFragment<Timer> {

	@Override
	protected Adapter<?> createAdapter() {
		return new DefaultAdapter() {
			
			@Override
			public void onBindViewHolder(DefaultViewHolder holder, int position) {
				final Timer timer = mEntityList.get(position);
				
				String temp = "Pi: " + timer.getPrepareInterval() + " Wi: " + timer.getWorkInterval() + " Ri: " + timer.getRestInterval() + " Rs: " + timer.getRounds();
				
				holder.title.setText(temp);
				holder.title.setTypeface(mRobotoCondensedBold);
				
				holder.subtitle.setText(timer.getName());
				holder.subtitle.setTypeface(mRobotoCondensedRegular);
				
				holder.description.setText(timer.getDescription()); 
				holder.description.setTypeface(mRobotoCondensedBold);

				holder.itemView.setOnClickListener(new OnClickListener() { 
					
					@Override
					public void onClick(View view) {
						Bundle arguments = new Bundle();
						arguments.putParcelable("timer", timer);   

						Fragment fragment = new TimerFragment();
						fragment.setArguments(arguments); 
						
			            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
			            fragmentTransaction.replace(R.id.content_frame, fragment);
			            fragmentTransaction.addToBackStack(null);
			            fragmentTransaction.commit();	
					}
				});
				
				holder.itemView.setId(timer.getId());  
				
				registerForContextMenu(holder.itemView);  
			}
			
		};
	}

	@Override
	protected void refreshData(boolean animated) {
		new DataLoader(animated) {

			@Override
			protected Void doInBackground(Void... params) {
				super.doInBackground(params);
				
				try {
					mQueryBuilder.where()
						.like("name", mQueryText)
							.or()
						.like("description", mQueryText);
					
					mQueryBuilder.orderBy("name", true);
					
					mEntityList.addAll(mQueryBuilder.query());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				return null;
			}
			
		}.execute();
	}

	@Override
	protected Class<Timer> getType() {
		return Timer.class;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.timer_list_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.actionCreate: {
				Bundle arguments = new Bundle();
				
				Fragment fragment = new CreateTimerFragment();
				fragment.setArguments(arguments); 
				
		        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
		        fragmentTransaction.replace(R.id.content_frame, fragment);
		        fragmentTransaction.addToBackStack(null);
		        fragmentTransaction.commit();
			} break;
			default: {
				return super.onOptionsItemSelected(item);
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override  
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, view, menuInfo);  
		
        menu.setHeaderTitle("");  
        menu.add(Menu.NONE, view.getId(), Menu.NONE, "Удалить");  
    }  
  
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
    	final Integer entityId = item.getItemId();
    	
        if(item.getTitle().equals("Удалить")) {
    		new DataLoader(false) {

    			@Override
    			protected Void doInBackground(Void... params) {
    				super.doInBackground(params);
    				
    				try {
    					mDao.deleteById(entityId);
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
    				
    				return null;
    			}
    			
    			protected void onPostExecute(Void result) {
    				super.onPostExecute(result); 
    				
    				refreshData(false); 
    			};
    			
    		}.execute();
        } else 
        	return false;
        
        return true;  
    }  
    
}
