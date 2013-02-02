package com.shopper.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	public static final String REGISTER = "register";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	private User user;
	
	// UI references.
	private EditText emailView;
	private EditText passwordView;
	private View loginFormView;
	private View loginStatusView;
	private TextView loginStatusMessageView;
	private boolean register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		register = getIntent().getBooleanExtra(REGISTER, false);
		System.out.println("register: "+register);
		setContentView(R.layout.activity_login);
		changeLayout();
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

		loginFormView = findViewById(R.id.login_form);
		loginStatusView = findViewById(R.id.login_status);
		loginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	private void changeLayout() {
		if (!register) {
			return;
		}
		Button registerButton = (Button)findViewById(R.id.sign_in_button);
		registerButton.setText(R.string.action_register);
	}

	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		emailView.setError(null);
		passwordView.setError(null);

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
		@Override
		protected Boolean doInBackground(Void... params) {
			int successCode = -1;
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
			} else {
				passwordView
						.setError(getString(R.string.error_incorrect_password));
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
