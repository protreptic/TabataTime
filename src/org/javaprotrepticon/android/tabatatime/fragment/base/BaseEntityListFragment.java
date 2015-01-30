package org.javaprotrepticon.android.tabatatime.fragment.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.javaprotrepticon.android.tabatatime.R;
import org.javaprotrepticon.android.tabatatime.storage.Storage;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public abstract class BaseEntityListFragment<T> extends Fragment implements OnRefreshListener {
	
	protected Typeface mRobotoCondensedBold;
	protected Typeface mRobotoCondensedRegular;
	protected Typeface mRobotoCondensedLight;
	
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter<?> mRecyclerViewAdapter;
	
	private SwipeRefreshLayout mSwipeRefreshWidget;
	
	protected List<T> mEntityList = new ArrayList<T>();
	
	private Handler mHandler = new Handler();
	
    private final Runnable mRefreshDone = new Runnable() {

        @Override
        public void run() {
            mSwipeRefreshWidget.setRefreshing(false);
        }

    };
    
    private final Runnable mRefreshBegin = new Runnable() {

        @Override
        public void run() {
            mSwipeRefreshWidget.setRefreshing(true);
        }

    };
	
    protected Calendar mCalendar = Calendar.getInstance(new Locale("ru", "RU"));
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.recyclerview, container, false);
	}
	
	@Override
	public void onRefresh() {
		refreshData(true);
	}
	
	protected abstract RecyclerView.Adapter<?> createAdapter();
	protected abstract void refreshData(boolean animated);
	protected abstract Class<T> getType();
	
	public abstract class DefaultAdapter extends RecyclerView.Adapter<DefaultViewHolder> {
		
		@Override
		public int getItemCount() {
			return mEntityList.size();
		}
		
		@Override
		public DefaultViewHolder onCreateViewHolder(ViewGroup parent, int position) {
			return new DefaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.default_list_item_cardview, parent, false));
		}
		
	}
	
    public abstract class DataLoader extends AsyncTask<Void, Void, Void> {
    	
    	protected Storage mStorage;
    	protected Dao<T, Integer> mDao;
    	protected QueryBuilder<T, Integer> mQueryBuilder;
    	
    	private boolean mAnimated;
    	
    	public DataLoader(boolean animated) {
    		mAnimated = animated;
    	}
    	
		@Override
    	protected void onPreExecute() {
    		mIsLoading = true;
    		
    		mEntityList.clear();
    		
    		if (mAnimated) {
        		mHandler.removeCallbacks(mRefreshDone);
                mHandler.removeCallbacks(mRefreshBegin);
                mHandler.postDelayed(mRefreshBegin, 250);
                
                mRecyclerViewAdapter.notifyDataSetChanged();
    		}
    	}
    	
		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Void... params) {
    		Class<T> type = getType();
    		
    		mStorage = new Storage(getActivity());
    		mDao = (Dao<T, Integer>) mStorage.createDao(type);
    		mQueryBuilder = mDao.queryBuilder();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mStorage.closeConnection();
			
			mRecyclerViewAdapter.notifyDataSetChanged();
			
			if (mAnimated) {
				mHandler.removeCallbacks(mRefreshBegin);
	            mHandler.removeCallbacks(mRefreshDone);
	            mHandler.postDelayed(mRefreshDone, 10);
			}
			
			mIsLoading = false;
		}
    	
    }
    
    protected String mQueryText = "%%";
    
    private boolean mIsLoading;
    
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
				
		inflater.inflate(R.menu.base_entity_list_fragment, menu);
		
		MenuItem menuItem = menu.findItem(R.id.actionSearch);
		menuItem.setIcon(getResources().getDrawable(R.drawable.search));
		 
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.actionSearch));
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
	    	private boolean makeQuery(String query) {
	    		boolean result = false;
	    		
				if (query.length() >= 3) {
					mQueryText = "%" + query + "%";
					
					refreshData(true);
					
					result = true;
				}
				if (query.isEmpty()) {
					mQueryText = "%%";
					
					refreshData(true);
					
					result = true;
				}
				
				return result;
	    	}
	    	
			@Override
			public boolean onQueryTextSubmit(String query) {
				return mIsLoading ? false : makeQuery(query);
			}
			
			@Override
			public boolean onQueryTextChange(String query) {
				return mIsLoading ? false : makeQuery(query);
			}
		});
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true); 
	}
	
	private int columns() {
		int columns = 2;
		
		Configuration config = getResources().getConfiguration();
		
		int screenSize = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 2;
		    } else {
		    	columns = 1;
		    }
		} 
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 2;
		    } else {
		    	columns = 1;
		    }
		} 
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 3;
		    } else {
		    	columns = 2;
		    }
		} 
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 4;
		    } else {
		    	columns = 2;
		    }
		} 
		
		return columns;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mRobotoCondensedBold = Typeface.createFromAsset(getActivity().getAssets(), "typeface/RobotoCondensed-Bold.ttf");
		mRobotoCondensedRegular = Typeface.createFromAsset(getActivity().getAssets(), "typeface/RobotoCondensed-Regular.ttf");
		mRobotoCondensedLight = Typeface.createFromAsset(getActivity().getAssets(), "typeface/RobotoCondensed-Light.ttf");
		
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns());
        layoutManager.setReverseLayout(false);
		
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setVerticalScrollBarEnabled(true); 
        
		mRecyclerViewAdapter = createAdapter();
		mRecyclerView.setAdapter(mRecyclerViewAdapter); 
        
        mSwipeRefreshWidget = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setOnRefreshListener(this); 
        
        refreshData(true);
	}
	
	public static class DefaultViewHolder extends RecyclerView.ViewHolder {
		
		public TextView title;
		public TextView subtitle;
		public TextView description;
		
		public DefaultViewHolder(View itemView) {
			super(itemView); 
			
			title = (TextView) itemView.findViewById(R.id.title);
			subtitle = (TextView) itemView.findViewById(R.id.subtitle);
			description = (TextView) itemView.findViewById(R.id.description);
		}
		
	}

}
