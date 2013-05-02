package uw.cse.dineon.library;

import java.util.*;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

/**
 * 
 * @author zachr81
 *
 */
public class Restaurant extends Storable implements Parcelable {

	private Map<String,Menu> menus;
	private List<Reservation> reservationList;
	private RestaurantInfo info;
	private List<Order> orders;
	private List<DiningSession> sessions;
	
	/**
	 * @param Menu
	 * @param reservationList
	 * @param info
	 * @param orders
	 * @Param sessions
	 */
	public Restaurant(Map<String,Menu> menus, List<Reservation> reservationList, RestaurantInfo info,
			List<Order> orders, List<DiningSession> sessions) {
		super();
		this.menus = menus;
		this.reservationList = reservationList;
		this.info = info;
		this.orders = orders;
		this.sessions = sessions;
	}
	
	public Restaurant(Parcel source) {
		readFromParcel(source);
	}

	/**
	 * @return a shallow copy of the Menu map
	 */
	public Map<String,Menu> getMenus() {
		return new HashMap<String,Menu>(menus);
	}
	
	/**
	 * @param menu: A Menu object to set as the new menu
	 * 
	 */
	public void setMenus(Map<String,Menu> menus) {
		this.menus = menus;
	}
	
	/**
	 * @return List<Reservation>
	 */
	public List<Reservation> getReservationList() {
		List<Reservation> copy = new ArrayList<Reservation>(reservationList.size());
		Collections.copy(copy, reservationList);
		return copy;
	}
	
	/**
	 * @param reservationList: A List of Reservations to set as the new reservation list
	 * 
	 */
	public void setReservationList(List<Reservation> newReservationList) {
		this.reservationList = newReservationList;
	}
	
	/**
	 * @return RestaurantInfo
	 */
	public RestaurantInfo getInfo() {
		return info;
	}
	
	/**
	 * Sets info to the param value
	 * @param RestaurantInfo
	 */
	public void setInfo(RestaurantInfo newInfo) {
		this.info = newInfo;
	}
	
	/**
	 * @return List<Order>
	 */
	public List<Order> getOrders() {
		List<Order> copy = new ArrayList<Order>(orders.size());
		Collections.copy(copy, orders);
		return copy;
	}
	
	/**
	 * Sets orders to the parameter value
	 * @param List<Order>
	 */
	public void setOrders(List<Order> newOrders) {
		orders = newOrders;
	}
	
	/**
	 * @return List<DiningSession>
	 */
	public List<DiningSession> getSessions() {
		List<DiningSession> copy = new ArrayList<DiningSession>(sessions.size());
		Collections.copy(copy, sessions);
		return copy;
	}
	
	/**
	 * Sets sessions to the parameter value
	 * @param List<DiningSession>
	 */
	public void setSessions(List<DiningSession> newSessions) {
		sessions = newSessions;
	}
	
	/**
	 * Adds the given menu to the menu map
	 * @param String for the name
	 * @param Menu to add
	 */
	public void addMenu(String name, Menu newMenu) {
		menus.put(name, newMenu);
	}
	
	/**
	 * Remove the specified menu
	 * @param name
	 */
	public void removeMenu(String name) {
		menus.remove(name);
	}
	
	/**
	 * Return the menu for the specified name
	 * @param name
	 */
	public Menu getMenu(String name) {
		return menus.get(name);
	}
	
	/**
	 * Adds the given reservation to the reservation list
	 * @param Reservation
	 */
	public void addReservation(Reservation newReservation) {
		reservationList.add(newReservation);
	}
	
	/**
	 * Remove the specified reservation
	 * @param Reservation
	 */
	public void removeReservation(Reservation removeReservation) {
		reservationList.remove(removeReservation);
	}
	
	/**
	 * Adds given order to orders
	 * @param Order
	 */
	public void addOrder(Order order) {
		orders.add(order);
	}
	
	/**
	 * Remove given order from orders
	 * @param Order
	 */
	public void removeOrder(Order order) {
		orders.remove(order);
	}
	
	/**
	 * Adds given DiningSession to sessions
	 * @param DiningSession
	 */
	public void addDiningSession(DiningSession session) {
		sessions.add(session);
	}
	
	/**
	 * Removes given DiningSession from sessions
	 * @param DiningSession
	 */
	public void removeDiningSession(DiningSession session) {
		sessions.remove(session);
	}

	@Override
	public ParseObject packObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unpackObject(ParseObject pobj) {
		// TODO Auto-generated method stub
	}

	@Override
	public int describeContents() {
		// TODO ?? - examples online use 0.
		return 0;
	}

	/**
	 * Writes this Restaurant to Parcel dest in the order:
	 * Map<String, Menu>, List<Reservation>, RestaurantInfo, List<Order>, List<DiningSession>
	 * to be retrieved at a later time.
	 * 
	 * @param dest Parcel to write Restaurant data to.
	 * @param flags
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeMap(menus); // TODO use writeBundle instead (see writeMap documentation)
		dest.writeTypedList(reservationList);
		dest.writeParcelable(info, flags);
		dest.writeTypedList(orders);
		dest.writeTypedList(sessions);
	}
	
	/**
	 * Helper method for updating Restaurant with the data from a Parcel.
	 * @param source Parcel containing data in the order:
	 * 		Map<String, Menu>, List<Reservation>, RestaurantInfo, List<Order>, List<DiningSession>
	 */
	private void readFromParcel(Parcel source) {
		source.readMap(menus, null); // TODO use readBundle instead (see writeToParcel above)
		source.readTypedList(reservationList, null);
		info = source.readParcelable(null);
		source.readTypedList(orders, null);
		source.readTypedList(sessions, null);
	}
	
	/**
	 * Parcelable creator object of a Restaurant.
	 * Can create a Restaurant from a Parcel.
	 */
	public static final Parcelable.Creator<Restaurant> CREATOR = 
			new Parcelable.Creator<Restaurant>() {

				@Override
				public Restaurant createFromParcel(Parcel source) {
					return new Restaurant(source);
				}

				@Override
				public Restaurant[] newArray(int size) {
					return new Restaurant[size];
				}
			};
}
