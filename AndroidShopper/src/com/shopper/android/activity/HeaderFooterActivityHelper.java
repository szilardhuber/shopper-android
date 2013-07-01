package com.shopper.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopper.android.R;

public class HeaderFooterActivityHelper {
	Activity owner;
	
	public HeaderFooterActivityHelper(Activity owner) {
		this.owner = owner;
	}
    protected void onCreate() {
    	System.out.println("setContentView");
         owner.setContentView(R.layout.header_footer);         
    }
    
    public void inflateLayout(int layout){
    	ViewGroup vg = (ViewGroup) owner.findViewById(R.id.main_content);
    	System.out.println("inflate: "+layout);
        ViewGroup.inflate(owner, layout, vg);
    }
    
    public void setProgressMessage(int id) {
    	((TextView)owner.findViewById(R.id.progress_message)).setText(id);
	}
    
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	protected void showProgress(final boolean show) {
		final View progress = owner.findViewById(R.id.progress);
		final View content =  owner.findViewById(R.id.main_content);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = owner.getResources().getInteger(
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
