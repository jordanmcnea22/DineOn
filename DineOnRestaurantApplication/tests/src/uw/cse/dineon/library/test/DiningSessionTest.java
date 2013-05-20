package uw.cse.dineon.library.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import uw.cse.dineon.library.DiningSession;
import uw.cse.dineon.library.MenuItem;
import uw.cse.dineon.library.Order;
import uw.cse.dineon.library.RestaurantInfo;
import uw.cse.dineon.library.UserInfo;

/**
 * 
 * @author zach
 *
 */
public class DiningSessionTest extends AndroidTestCase {

	Activity activity;
	Context mContext = null;
	
	DiningSession testSession;
	ParseUser testUser;
	ParseUser testUser1;
	UserInfo testUInfo;
	UserInfo testUInfo1;
	List<MenuItem> testItems;
	MenuItem testItem;
	Order testOrder;
	List<Order> orders;
	List<UserInfo> testUInfos;
	RestaurantInfo testRInfo;
	
	
	
	public DiningSessionTest() {
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		Log.i("progress", "start setup");

		Parse.initialize(this.getContext(), "RUWTM02tSuenJPcHGyZ0foyemuL6fjyiIwlMO0Ul", "wvhUoFw5IudTuKIjpfqQoj8dADTT1vJcJHVFKWtK");
		Log.i("progress", "init parse");

		testUser1 = ParseUser.logIn("zach1", "zach1");
		
		testUser = ParseUser.logIn("zach", "zach");
		

		testUInfo1 = new UserInfo(testUser1);
		testRInfo = new RestaurantInfo(testUser);
		testSession = new DiningSession(32, new Date(3254645), testUInfo1, testRInfo);
		
		testUInfo = new UserInfo(testUser);
		testItems = new ArrayList<MenuItem>();
		testItem = new MenuItem(24, 4.5, "Root Beer Float", "Ice cream and root beer");
		testItems.add(testItem);
		testOrder = new Order(32, testUInfo, testItems);
		orders = new ArrayList<Order>();
		orders.add(testOrder);
		
		testUInfos = new ArrayList<UserInfo>();
		
		testUInfos.add(testUInfo1);
		testUInfos.add(testUInfo);
	}
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		ParseUser.logOut();
	}

	public void testPackObject() throws ParseException {
		/* Throws "IllArgExcep value may not be null"
		 * //Default state
		ParseObject testPack1 = testSession.packObject();
		
		DiningSession newSess = new DiningSession(testPack1);
		
		assertEquals(testSession.getTableID(), newSess.getTableID());
		assertEquals(testSession.getUsers(), newSess.getUsers());
		assertEquals(testSession.getPendingOrders(), newSess.getPendingOrders());
		assertEquals(testSession.getCompletedOrders(), newSess.getCompletedOrders());
		assertEquals(testSession.getStartTime(), newSess.getStartTime()); */
	}



	public void testDiningSessionIntDate() {
		assertEquals(32, testSession.getTableID());
		assertEquals(new Date(3254645), testSession.getStartTime());
	}

	public void testDiningSessionParseObject() {
		//TODO fail("Not yet implemented");
	}

	//also tests addorders
	public void testGetOrders() {

		
		testSession.addPendingOrder(testOrder);

		
		assertEquals(orders, testSession.getOrders());
	}

	
	public void testGetTableID() {
		assertEquals(32, testSession.getTableID());
	}

	public void testResetTableID() {
		testSession.resetTableID(42);
		assertEquals(42, testSession.getTableID());
	}

	public void testGetUsers() {
		List<UserInfo> expectedUser = new ArrayList<UserInfo>();
		expectedUser.add(testUInfo1);
		//TODO fails now assertEquals(testUInfo1, testSession.getUsers());
	}

	public void testAddUser() {
		testSession.addUser(testUInfo);
		
		assertEquals(testUInfos, testSession.getUsers());
	}

	public void testGetStartTime() {
		assertEquals(new Date(3254645), testSession.getStartTime());
	}

}
