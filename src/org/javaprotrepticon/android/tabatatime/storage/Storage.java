package org.javaprotrepticon.android.tabatatime.storage;

import java.sql.SQLException;

import org.javaprotrepticon.android.androidutils.Apps;
import org.javaprotrepticon.android.tabatatime.storage.model.Exercise;
import org.javaprotrepticon.android.tabatatime.storage.model.Timer;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Storage {
	
	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private JdbcPooledConnectionSource mConnection;
	
	public Storage(Context context) {
		String storageFolder = context.getDir("data", Context.MODE_PRIVATE).getPath() + "/" + context.getPackageName() + "-" + Apps.getVersionName(context);
		
		try {
			mConnection = new JdbcPooledConnectionSource("jdbc:h2:" + storageFolder + ";AUTO_SERVER=TRUE;"); 
			
			TableUtils.createTableIfNotExists(mConnection, Timer.class);
			TableUtils.createTableIfNotExists(mConnection, Exercise.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			mConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Dao<?, Integer> createDao(Class<?> type) {
		Dao<?, Integer> result = null;
		
		try {
			result = (Dao<?, Integer>) DaoManager.createDao(mConnection, type);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
