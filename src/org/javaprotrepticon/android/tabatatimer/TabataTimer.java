package org.javaprotrepticon.android.tabatatimer;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConfigurationException;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "") 
public class TabataTimer extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		initAcra();
	}
	
	private void initAcra() {
        ACRA.init(this);
        
		ACRAConfiguration configuration = ACRA.getConfig();
		
		try {
			configuration.setFormUri("https://sfs.magnat.ru:8443/ws_acra_submit_crash_report"); 
			configuration.setDisableSSLCertValidation(true); 
			configuration.setSocketTimeout(5000); 
			configuration.setConnectionTimeout(5000); 
			
			configuration.setMode(ReportingInteractionMode.TOAST);
		} catch (ACRAConfigurationException e) {
			e.printStackTrace();
		}
        
        ACRA.setConfig(configuration); 
	}
	
}
