package com.shopper.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shopper.android.R;
import com.shopper.android.model.User;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	private User user;
	
	// UI references.
	private EditText emailView;
	private EditText passwordView;
	private EditText passwordView2;
	private View loginFormView;
	private View loginStatusView;
	private TextView loginStatusMessageView;
	private Button submitButton;
	private boolean register = false;

	private Button toggleButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Set up the login form.
		emailView = (EditText) findViewById(R.id.email);
		if (user != null) {			
			emailView.setText(user.getEmail());
		}

		passwordView = (EditText) findViewById(R.id.password);
		passwordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		passwordView2 = (EditText) findViewById(R.id.password2);
		loginFormView = findViewById(R.id.login_form);
		loginStatusView = findViewById(R.id.login_status);
		loginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		
		submitButton = (Button)findViewById(R.id.submit_button);
		submitButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		toggleButton = (Button)findViewById(R.id.swich_mode);
		toggleButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						toggleLayout();
					}
				});
	}

	private void toggleLayout() {		
		register = !register;
		if (register) {
			passwordView2.setVisibility(View.VISIBLE);
			submitButton.setText(R.string.action_register);
			toggleButton.setText(R.string.action_swich_login);
		}else {
			passwordView2.setVisibility(View.GONE);
			submitButton.setText(R.string.action_sign_in);
			toggleButton.setText(R.string.action_swich_register);
		}
		System.out.println("new mode register: "+register);
	}

	public void attemptLogin() {
		System.out.println("Register: "+register);
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		emailView.setError(null);
		passwordView.setError(null);
		passwordView2.setError(null);

		// Store values at the time of the login attempt.
		user = new User(emailView.getText().toString(), passwordView.getText().toString());
				

		boolean cancel = false;
		View focusView = null;
		System.out.println("User: "+user);
		int userValidationCode = SecurityHandler.validateUser(user);
		System.out.println("userValidationCode: "+userValidationCode);
		switch (userValidationCode) {
			case SecurityHandler.EMPTY_PASSWORD:
				passwordView.setError(getString(R.string.error_field_required));
				focusView = passwordView;
                cancel = true;
				break;
			case SecurityHandler.EMPTY_EMAIL:
				emailView.setError(getString(R.string.error_field_required));
				focusView = emailView;
                cancel = true;
				break;
			case SecurityHandler.INVALID_EMAIL:
				emailView.setError(getString(R.string.error_invalid_email));
				focusView = emailView;
                cancel = true;
				break;
			case SecurityHandler.INVALID_PASSWORD:
				passwordView.setError(getString(R.string.error_invalid_password));
				focusView = passwordView;
                cancel = true;
				break;
			default:
				break;
		}
		
		if (register) {
			if (!user.getPassword().equals(passwordView2.getText().toString())) {
				passwordView2.setError(getString(R.string.error_password_mismatch));
				focusView = passwordView2;
                cancel = true;
			}
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			loginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			loginStatusView.setVisibility(View.VISIBLE);
			loginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			loginFormView.setVisibility(View.VISIBLE);
			loginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {		
		
		private int successCode = -1;
		
		@Override
		protected Boolean doInBackground(Void... params) {			
			if (register) {
				successCode = SecurityHandler.register(user, LoginActivity.this);
			} else {
				successCode = SecurityHandler.login(user, LoginActivity.this);
			}
			return successCode == SecurityHandler.OK;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
				startActivity(new Intent(LoginActivity.this, Shopper.class));
			} else {
				switch (successCode) {
				case SecurityHandler.FAIL:
					passwordView
					.setError(getString(R.string.error_incorrect_data));
					break;
				case SecurityHandler.UNVERIFIED:
					passwordView
					.setError(getString(R.string.error_unverified_registration));
					break;
				case SecurityHandler.IO_ERROR:
					passwordView
					.setError(getString(R.string.error_io));
					break;

				default:
					break;
				}
				passwordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
