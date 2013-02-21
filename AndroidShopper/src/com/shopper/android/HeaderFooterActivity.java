package com.shopper.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HeaderFooterActivity extends Activity {	
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.header_footer);         
    }
    
    protected void setProgressMessage(int id) {
    	((TextView)findViewById(R.id.progress_message)).setText(id);
	}
    
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	protected void showProgress(final boolean show) {
		final View progress = findViewById(R.id.progress);
		final View content =  findViewById(R.id.main_content);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			progress.setVisibility(View.VISIBLE);
			progress.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							progress.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			content.setVisibility(View.VISIBLE);
			content.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							content.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			progress.setVisibility(show ? View.VISIBLE : View.GONE);
			content.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
 }
