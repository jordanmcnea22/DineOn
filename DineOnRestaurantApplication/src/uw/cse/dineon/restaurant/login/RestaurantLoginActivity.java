package uw.cse.dineon.restaurant.login;

import uw.cse.dineon.library.Restaurant;
import uw.cse.dineon.library.util.CredentialValidator;
import uw.cse.dineon.library.util.CredentialValidator.Resolution;
import uw.cse.dineon.library.util.Utility;
import uw.cse.dineon.restaurant.DineOnRestaurantApplication;
import uw.cse.dineon.restaurant.R;
import uw.cse.dineon.restaurant.RestaurantDownloader;
import uw.cse.dineon.restaurant.RestaurantDownloader.RestaurantDownLoaderCallback;
import uw.cse.dineon.restaurant.active.RestauarantMainActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery.CachePolicy;
import com.parse.ParseUser;

/**
 * Login Activity for Restaurant users.
 * 
 * @author mhotan
 */
public class RestaurantLoginActivity extends FragmentActivity implements
LoginFragment.OnLoginListener, RestaurantDownLoaderCallback {

	/**
	 * Progress bar dialog for showing user progress.
	 */
	private ProgressDialog mProgressDialog;

	/**
	 * This will hold the object ID to a Restaurant object associated to the
	 * User that logs in to this application.
	 */
	private Restaurant mRestaurant;
	
	/**
	 * Reference to this Activity instance for anonymous inner classes.
	 */
	private RestaurantLoginActivity This;


	// //////////////////////////////////////////////////////////////////////
	// /// Activity specific
	// //////////////////////////////////////////////////////////////////////

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            Bundle to store created activity.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		This = this;
		
		ParseUser user = ParseUser.getCurrentUser();
		if (user != null && user.isAuthenticated() 
				&& user.getUsername() != null) {
			RestaurantDownloader downloader = new LoginRestaurantDownloader(user, This);
			downloader.execute(CachePolicy.NETWORK_ELSE_CACHE);
		}
	}

	@Override
	public void onLogin(String username, String password) {
		createProgressDialog();

		Resolution unResolution = CredentialValidator.isValidUserName(username);
		Resolution pwResolution = CredentialValidator.isValidPassword(password);

		StringBuffer buf = new StringBuffer();
		if (!unResolution.isValid()) {
			buf.append(unResolution.getMessage() + getString(R.string.new_line));
		}
		if (!pwResolution.isValid()) {
			buf.append(pwResolution.getMessage() + getString(R.string.new_line));
		}

		if (buf.length() > 0) {
			Utility.getGeneralAlertDialog(getString(R.string.login_failure), 
					buf.toString(), this).show();
			return;
		}
	
		// Log in using asyncronous callback
		ParseUser.logInInBackground(username, password, new RestaurantLoginCallback());
	}

	/**
	 * Starts the Main activity for this restaurant.
	 */
	private void startMainActivity() {
		Intent i = new Intent(this, RestauarantMainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(i);
		finish();
	}
	
	@Override
	protected void onPause() {
		destroyProgressDialog();
		super.onPause();
	}

	// //////////////////////////////////////////////////////////////////////
	// //// Callback for logging in
	// //////////////////////////////////////////////////////////////////////

	/**
	 * Login Callback for restaurant downloads.
	 * @author mhotan
	 */
	private class RestaurantLoginCallback extends LogInCallback {

		@Override
		public void done(ParseUser user, ParseException e) {
			if (e == null && user != null) {
				// Login successful 
				// because create Restaurant activity always handles first time
				// users we know that this user is returning
				// Therefore there has to be a Restaurant instance in the cloud
				RestaurantDownloader downloader = new LoginRestaurantDownloader(user, This);
				downloader.execute(CachePolicy.NETWORK_ONLY);
				
			} else if (user == null) {
				destroyProgressDialog();
				Utility.getFailedToCreateAccountDialog(
						getString(R.string.invalid_credentials), This).show();
			} else {
				destroyProgressDialog();
				Utility.getGeneralAlertDialog(
						getString(R.string.server_error), e.getMessage(), This).show();
			}
		}
	}
	
	// //////////////////////////////////////////////////////////////////////
	// /// UI Specific methods
	// //////////////////////////////////////////////////////////////////////

	/**
	 * Instantiates a new progress dialog and shows it on the screen.
	 */
	protected void createProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			return;
		}
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(getString(R.string.logging_in));
		mProgressDialog.setMessage(getString(R.string.getting_ready));
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
	}

	/**
	 * Hides the progress dialog if there is one.
	 */
	protected void destroyProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onFailToDownLoadRestaurant(String message) {
		ParseUser.logOut();
		Utility.getGeneralAlertDialog(getString(R.string.no_restaurant), 
				getString(R.string.no_restaurant_reason) + message, This).show();
	}

	@Override
	public void onDownloadedRestaurant(Restaurant rest) {
		if (rest != null) {
			DineOnRestaurantApplication.logIn(rest);
			startMainActivity();
		}
	}
	
	/**
	 * A restaurant downloader that controls progress dialog.
	 * @author mhotan
	 */
	private class LoginRestaurantDownloader extends RestaurantDownloader {

		/**
		 * Creates a Login Restaurant Downloader that can control progress dialogs.
		 * @param user ParseUser
		 * @param callback RestaurantDownLoaderCallback
		 */
		public LoginRestaurantDownloader(ParseUser user,
				RestaurantDownLoaderCallback callback) {
			super(user, callback);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			createProgressDialog();
		}
	
		@Override
		protected void onPostExecute(Restaurant result) {
			destroyProgressDialog();
			super.onPostExecute(result);
		}
		
	}
	
}
