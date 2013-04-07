package com.shopper.android;

import java.util.Locale;

import android.app.Application;
import android.content.res.Configuration;

import com.shopper.android.model.ApplicationModel;
import com.shopper.android.util.LocalStorage;

public class ShopperApplication extends Application{
	
	    @Override
	    public void onConfigurationChanged(Configuration newConfig)
	    {
	        super.onConfigurationChanged(newConfig);
	        setLang(newConfig);
	    }

	    @Override
	    public void onCreate()
	    {
	        super.onCreate();
	        LocalStorage.setProperty(Constants.PREFERENCE_LANG, "HU", this);
	        setLang(null);
	        ApplicationModel.getInstance(this).load();
	    }

		private void setLang(Configuration c){
			if (c==null) {				
				c = getBaseContext().getResources().getConfiguration();
			}
			String lang = LocalStorage.getProperty(Constants.PREFERENCE_LANG, this, Constants.DEFAULT_LANG);
			System.out.println("Lang: "+lang);
			c.locale = new Locale(lang);
			getBaseContext().getResources().updateConfiguration(c, getBaseContext().getResources().getDisplayMetrics());
		}

	}