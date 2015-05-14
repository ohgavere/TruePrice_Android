package org.dmb.trueprice;

import org.dmb.trueprice.connector.Connector;
import org.dmb.trueprice.connector.LoginConnector;

import android.annotation.TargetApi;
//import android.content.CursorLoader;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.app.LoaderManager.LoaderCallbacks;
//import android.content.Loader;

/**
 * A login screen that offers login via email/password.
 */
//@SuppressLint("NewApi")
public class Login extends MenuHandlerActionBarCompatAdapter  {

	
//	public final static String 	FLAG_CONNECT_SUCCEED	= "com.dmb.trueprice.android.FLAG_CONNECT_SUCCEED" ;
//	public final static int 	REQID_CONNECT_RESULT	= 1 ;
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	private AutoCompleteTextView mEmailView;
//	Have become : 
//	private EditText mEmailView ;
	
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	
	protected boolean signInStatusChanged = false ;
	protected boolean isUserSignedIn ; 
	
	protected boolean flagIsReturned = false ;
	
	private void sendFlagIntent () {
		
		if (! flagIsReturned ) {
		
			Intent i = new Intent(Login.this, Launcher.class);
				
			if (signInStatusChanged ) {
			
				i.putExtra(Launcher.FLAG_SIGNED_IN, isUserSignedIn);
			
				setResult(RESULT_OK, i);								
		
			} else {
				
				setResult(RESULT_CANCELED);
				
			}
				
			flagIsReturned = true ;
			signInStatusChanged = false ;
			
			Log.d(this.getClass().getSimpleName(),
				"Intent Sent. (Actual Local " 
				+ "FLAG == " + isUserSignedIn ) ;
			
			/* Call finsih(); here means that every time this view will be hidden,
			 * the activity will finsih with result back to Launcher activity.
			 * User will be back to main menu.	
			 */
			
			finish();						
			
		} else {
			Log.d(this.getClass().getSimpleName(),
				"FLAG already sent. (Actual Local " 
				+ "FLAG == " + isUserSignedIn	);
		}
		
		
	}
	
	public boolean isUserSignedIn() {
		return isUserSignedIn;
	}
	
	private void setIsUserSignedIn (boolean  isSignedIn) {
		isUserSignedIn = isSignedIn ;
		setStatusChanged();
	}	
	
	public void setStatusChanged () {
		signInStatusChanged = true ;
		Log.d(this.getClass().getSimpleName(),
				"SignInStatus Changed()" ) ;
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		
		Log.d(this.getClass().getSimpleName(), "onBackPressed(), FLAG == " + isUserSignedIn());		
		
		sendFlagIntent();
	}
	
	public int repeatCount = 0; 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		// Si 0, recupérer la valeur de l'event au cas ou
		// sinon laisser la valeur actuelle
		if (repeatCount == 0) { repeatCount = event.getRepeatCount() ; } 
		
		// Si boutton de retour
	    if (keyCode == KeyEvent.KEYCODE_BACK) {  
	    	
	    	// Si 1e fois
	    	if (repeatCount == 0) {
	    		Log.d(this.getClass().getSimpleName(), "onKeyDown(), key was BACK BUTTON. Hit number : " + repeatCount );
	    		// 	on demande d'appuyer encore une fois
	    		Toast.makeText(Login.this, R.string.one_more_time, 10000).show();
	    		// Incrementer le compteur
	    		repeatCount++ ;
	    		// Stopper la propagation de l'event
	        	return true;
	    	}
	    	
	    	// Si 2e fois
	    	else if (repeatCount == 1) {
	    		Log.d(this.getClass().getSimpleName(), "onKeyDown(), key was BACK BUTTON. Hit number : " + repeatCount );
	    		// 	on envoye le flag (etat actuel pris en compte automatiquement)
				sendFlagIntent();
				// Reset du compteur
				repeatCount = 0 ;
				// Stopper la propagation de l'event
	        	return true;
	    	} 
	    	
	    	// if repCount < 0 || > 1 it's not normal.
	    	// Reset it for further press
	    	else if (repeatCount > 1 || repeatCount < 0) {
	    		repeatCount = 0 ;
	    	}
	    	
	    } 
	    // ... just add else if statement with the expected keycode
	    // and do your stuff inside ...
	    
	    // This is default case ! NEVER REMOVE IT !
	    return super.onKeyDown(keyCode, event);		
	    
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		/*	GPC ADDED	*/
		
		Log.d(this.getClass().getSimpleName(), "Login Activity onStop(), FLAG == " + isUserSignedIn());
		
//		sendFlagIntent(false);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		Log.d(this.getClass().getSimpleName(),
//				"Login Activity onDestroy(), FLAG == " + isUserSignedIn);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_login);
		
//		setContentView(R.layout.activity_login_compat);
		
		
		setupActionBar();

		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		
//		Have become :
//		mEmailView = (EditText) findViewById(R.id.email);
		
//		populateAutoComplete();
		
		mEmailView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showSoftKeyboard(mEmailView);
			}
		});
		
		

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
//				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//					@Override
//					public boolean onEditorAction(TextView textView, int id,
//							KeyEvent keyEvent) {
//						if (id == R.id.password || id == EditorInfo.IME_NULL) {
//							attemptLogin();
//							return true;
//						}
//						return false;
//					}
//				});
		.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showSoftKeyboard(mPasswordView);
			}
		});
		
		

		Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
				hideSoftKeyboard();
			}
		});

//		mLoginFormView = findViewById(R.id.login_form);
		mLoginFormView = findViewById(R.id.email_login_form);
		mProgressView = findViewById(R.id.login_progress);
		
		
		/*		GPC ADDED	*/
		Intent i = getIntent();
		
		/*	It's the only place we set SiginInStatus directly, 
		 * each other time, we change status is for (dis)connection completed*/
		isUserSignedIn = i.getExtras().getBoolean(Launcher.FLAG_SIGNED_IN, false);
		
		Log.d(this.getClass().getSimpleName(), 
		"Bundle Extra FLAG == " + isUserSignedIn());
		
		if (isUserSignedIn() ) {
			
			mLoginFormView.setEnabled(false);
			mProgressView.setEnabled(false);
			mEmailSignInButton.setEnabled(false);
			mEmailView.setEnabled(false);
			mPasswordView.setEnabled(false);
			
			mLoginFormView.setFocusable(false);
			mProgressView.setFocusable(false);
			mEmailSignInButton.setFocusable(false);
			mEmailView.setFocusable(false);
			mPasswordView.setFocusable(false);			
			
			Button bttSignOff = (Button) findViewById(R.id.email_sign_off_button);
			
				bttSignOff.setText(getResources().getString(R.string.action_sign_off));
				
				bttSignOff.setVisibility(View.VISIBLE);
				
			bttSignOff.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					setIsUserSignedIn(false) ;
					sendFlagIntent();
				}
			});
			
			Toast.makeText(this, "Vous etes deja connecte", Toast.LENGTH_SHORT).show();
			
			findViewById(R.id.login_progress).invalidate();
			
		}
		
		hideSoftKeyboard();
		
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
//			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(email, password);
			mAuthTask.execute((Void) null);
		}
	}

	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		
		Log.i("Login showProgress()", "start showP() ? [" + show + "]");
		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//			
//			Log.i("Login showProgress()", "showP() with upper API");
//			
//			int shortAnimTime = getResources().getInteger(
//					android.R.integer.config_shortAnimTime);
//
//			Log.i("Login showProgress()", "try hide login form");
//			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//			Log.i("Login showProgress()", "form GONE. try animate()");
//			mLoginFormView.animate().setDuration(shortAnimTime)
//					.alpha(show ? 0 : 1)
//					.setListener(new AnimatorListenerAdapter() {
//						@Override
//						public void onAnimationEnd(Animator animation) {
//							mLoginFormView.setVisibility(show ? View.GONE
//									: View.VISIBLE);
//						}
//					});
//
//			Log.i("Login showProgress()", "try show progress bar");
//			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//			Log.i("Login showProgress()", "progBar VISIBLE. try animate()");
//			mProgressView.animate().setDuration(shortAnimTime)
//					.alpha(show ? 1 : 0)
//					.setListener(new AnimatorListenerAdapter() {
//						@Override
//						public void onAnimationEnd(Animator animation) {
//							mProgressView.setVisibility(show ? View.VISIBLE
//									: View.GONE);
//						}
//					});
//		
//		}
//		// lower api
//		else {
//			Log.i("Login showProgress()", "showP() with lower API");
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			Log.i("Login showProgress()", "try hide login form");
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			Log.i("Login showProgress()", "try show progress bar");
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//		}
	}

	/**
	 * Hides the soft keyboard
	 */
	public void hideSoftKeyboard() {
	    if(getCurrentFocus()!=null) {
	        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(Login.this.getCurrentFocus().getWindowToken(), 0);
	    }
	}

	/**
	 * Shows the soft keyboard
	 */
	public void showSoftKeyboard(View view) {
	    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	    view.requestFocus();
	    inputMethodManager.showSoftInput(view, 0);
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mEmail;
		private final String mPassword;

		private String errorMessage;
		public String getErrorMessage() { return errorMessage 
			== null ?	 ""  : errorMessage ; 
		}
		
		UserLoginTask(String email, String password) {
			mEmail = email;
			mPassword = password;
			
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			
			showProgress(true);

			try {
				Thread.sleep(1400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
					
//			disablePolicy();

			LoginConnector loginForm = new LoginConnector(Login.this);
			
			boolean resultat = loginForm.attemptLogin(mEmail, mPassword);
			errorMessage = loginForm.getErrorMessage() ;
			
//			Log.w(this.getClass().getSimpleName(), "Connection attempt returned : is user logged in ? => [" + resultat + "]");
						
			return resultat ;
			
			
			//	DUMMY AUTHENTICATION
//			for (String credential : DUMMY_CREDENTIALS) {
//				String[] pieces = credential.split(":");
//				if (pieces[0].equals(mEmail)) {
//					if (pieces[1].equals(mPassword) ) return true ; }
//				else { return false ;}					
//			}
			
//			return true;
			
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
//			showProgress(false);
			
			sendLoginError(success, getErrorMessage());
			
			// Then handle success or not
			// Si reussi login
			if (success) {
				// enregistrer l'etat connecté
				setIsUserSignedIn(true);
				// Enregsitrer l'email de l('utilisateur pour retrouver son dossier par la suite
				Launcher.setCurrentUserMail(mEmail);
				// L'etat viens de changer, on a termine notre boulot. 
				// On peut terminer cette activité et retourner au menu principal
				sendFlagIntent();
				
			} else {
				// Supposed not need because false by default, 
				//	and if true at Login onCreate then login supposed disabled 
//				setIsUserSignedIn (false) ;
				
				mPasswordView.setError(getString(R.string.error_incorrect_password));
//				mPasswordView.requestFocus();
				
				showProgress(false);
			}
			
		}

		@Override
		protected void onCancelled() {
			
			Log.d(this.getClass().getSimpleName(), "Login UserLoginTask onCancelled(), FLAG == " + isUserSignedIn);
			
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	
	public void sendLoginError(boolean success, String errorMessage) {
		// Manage different errors to give right indication to the user
		if (success == false && errorMessage == "Authentication failed. Bad Value.") {
			
			Toast.makeText(this,
					R.string.error_login_failure
					+ R.string.error_incorrect_login
					+ R.string.operator_or
					+ R.string.error_incorrect_password,
					100000).show();
			
		} else if (errorMessage == Connector.CON_ERROR_UNREACHABLE) {
			Toast.makeText(this, R.string.error_connector_network_unreachable, 100000).show();
		} else if (errorMessage == Connector.CON_ERROR_TIMEOUT) {
			Toast.makeText(this, R.string.error_connector_timeout, 100000).show();
		}		
	}
	
	

	
    public void disablePolicy() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
        	Log.d(this.getClass().getSimpleName(), "Disabling Policy For SDK API [" + Build.VERSION.SDK_INT + "]");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
	
    
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);	
		
	}	
	
}
