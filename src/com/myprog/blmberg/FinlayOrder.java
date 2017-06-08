package com.myprog.blmberg;

import java.util.List;
import java.util.Queue;

/**
 * This class represents finlay orders.
 * 
 * @author Rahul
 *
 */
public abstract class FinlayOrder {

	protected boolean isOrderAccepted;
	protected String mOrderPlacedTime;
	protected String mOrderServedTime;
	protected String mLastOrderServedTime;
	protected String mFoodOrderNumber;
	protected List<FoodItem> mFoodCookingList;

	public boolean isOrderAccepted() {
		return isOrderAccepted;
	}

	public String getOrderPlacedTime() {
		return mOrderPlacedTime;
	}

	public String getOrderServedTime() {
		return mOrderServedTime;
	}

	public List<FoodItem> getFoodCookingList() {
		return mFoodCookingList;
	}

	public String getFoodOrderNumber() {
		return mFoodOrderNumber;
	}

	/**
	 * This method is used to test, whether order is valid or not.
	 * 
	 * @param orderFryTime
	 * @param totalCookServeTime
	 * @param lastOrderTime
	 * @param orderPlacetime
	 * @param orderQueue
	 * @return
	 */
	public boolean isOrderValid(int orderFryTime, int totalCookServeTime,
			String orderPlacetime, Queue<FinlayOrder> orderQueue) {
		if (orderFryTime <= Constants.MAXIMUM_ORDER_TIME
				&& totalCookServeTime <= Constants.MAXIMUM_SINGLE_ORDER_SERVED_TIME) {

			mOrderServedTime = Utility.getOrderServedTime(orderPlacetime,
					orderFryTime, orderQueue);

			if (null != Utility.getLastAcceptedOrder(orderQueue)) {
				String lastOrderServedTime = Utility
						.getLastAcceptedOrder(orderQueue).mOrderServedTime;
				if (Utility.getTimeInMS(lastOrderServedTime) >= Utility
						.getTimeInMS(orderPlacetime)) {
					mLastOrderServedTime = lastOrderServedTime;
				} else {
					mLastOrderServedTime = orderPlacetime;
				}
			}

			if (Utility.validateOrderQueueAvailability(mLastOrderServedTime,
					orderPlacetime, orderFryTime, orderQueue)) {
				isOrderAccepted = true;
			}
		}
		return isOrderAccepted;
	}
}
