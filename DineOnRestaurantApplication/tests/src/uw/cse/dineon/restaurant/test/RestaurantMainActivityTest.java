package uw.cse.dineon.restaurant.test;

import java.util.ArrayList;
import java.util.List;

import uw.cse.dineon.library.CustomerRequest;
import uw.cse.dineon.library.DineOnUser;
import uw.cse.dineon.library.DiningSession;
import uw.cse.dineon.library.MenuItem;
import uw.cse.dineon.library.Order;
import uw.cse.dineon.library.Restaurant;
import uw.cse.dineon.library.util.DineOnConstants;
import uw.cse.dineon.restaurant.active.RestauarantMainActivity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class RestaurantMainActivityTest extends
ActivityInstrumentationTestCase2<RestauarantMainActivity> {

	private RestauarantMainActivity mActivity;
	private ParseUser mUser;
	
	private DineOnUser mUI;
	private Restaurant mRestaurant;

	private CustomerRequest mRequest;
	private Order mOrder;
	private DiningSession testSession;
	
	int orderNum = 100;
	
	private static final String fakeUserName = "fakeLoginName";
	private static final String fakePassword = "fakeLoginPassword";

	private static String menuItemText = "Fake Menu Item 1"; 
	private static String menuItemDescription = "Fake Menu Item 1 Description";

	public RestaurantMainActivityTest() throws ParseException {
		super(RestauarantMainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Parse.initialize(getInstrumentation().getTargetContext(), 
				"RUWTM02tSuenJPcHGyZ0foyemuL6fjyiIwlMO0Ul", 
				"wvhUoFw5IudTuKIjpfqQoj8dADTT1vJcJHVFKWtK");
		
		setActivityInitialTouchMode(false);
		
		mUser = new ParseUser();
		mUser.setUsername(fakeUserName);
		mUser.setPassword(fakePassword);
		mUser.signUp();

		mUI = new DineOnUser(mUser);
		mRestaurant = new Restaurant(mUser);
		mRequest = new CustomerRequest("Me Hungy", mUI.getUserInfo());
		
		List<MenuItem> items = new ArrayList<MenuItem>();
		items.add(new MenuItem(123, 1.99, "Yum yums", "description"));
		mOrder = new Order(1, mUI.getUserInfo(), items);
		testSession = new DiningSession(1, mUI.getUserInfo(), mRestaurant.getInfo());
		mRestaurant.addCustomerRequest(mRequest);
		mRestaurant.addOrder(mOrder);
		mRestaurant.addDiningSession(testSession);

		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				RestauarantMainActivity.class);
		intent.putExtra(DineOnConstants.KEY_RESTAURANT, mRestaurant);
		setActivityIntent(intent);

		setActivityInitialTouchMode(false);

		mActivity = getActivity();
	}

	@Override
	protected void tearDown() throws Exception {
		mUser.delete();
		mRequest.deleteFromCloud();
		mOrder.deleteFromCloud();
		testSession.deleteFromCloud();
		mUI.deleteFromCloud();
		mRestaurant.deleteFromCloud();
		mActivity.finish();
		super.tearDown();
	}

	public void testNavigateTabs() { 
		android.support.v4.view.ViewPager pager = (android.support.v4.view.ViewPager) 
				mActivity.findViewById(uw.cse.dineon.restaurant.R.id.pager_restaurant_main);
		PagerAdapter adapter = pager.getAdapter();
		assertNotNull(adapter);
		
		pager.setCurrentItem(0);
		
	}
	
	public void testOrderLayoutItemsPopulate() {
		TextView orderTitle = (TextView) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.button_order_title);
		TextView orderTime = (TextView) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.label_order_time);
		ImageButton arrowButton = (ImageButton) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.button_expand_order);
		assertNotNull(orderTitle);
		assertNotNull(orderTime);
		assertNotNull(arrowButton);
	}

	public void testRequestLayoutItemsPopulate() {
		TextView requestTitle = (TextView) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.label_request_title);
		TextView requestTime = (TextView) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.label_request_time);
		ImageButton arrowButton = (ImageButton) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.button_expand_request);
		assertNotNull(requestTitle);
		assertNotNull(requestTime);
		assertNotNull(arrowButton);
	}

}
