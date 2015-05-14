package org.javaprotrepticon.android.tabatatime.storage;

import java.sql.SQLException;

import org.javaprotrepticon.android.androidutils.Apps;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public JdbcPooledConnectionSource getConnection() {
		return mConnection;
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
