package com.myprog.blmberg;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents finlay orders type1 where software will process only
 * one kind of item i.e. Cod or Haddock or chips.
 * 
 * @author Rahul
 *
 */
public class OrderType1 extends FinlayOrder {

	public OrderType1(String orderNum, String time, String order, Queue<FinlayOrder> orderQueue) {
		prepareOrder(orderNum, time, order, orderQueue);
	}

	private void prepareOrder(String orderNum, String orderPlacetime, String order,
			Queue<FinlayOrder> orderQueue) {
		this.mFoodCookingList = new LinkedList<FoodItem>();
		this.isOrderAccepted = false;
		this.mLastOrderServedTime = null;
		this.mOrderPlacedTime = orderPlacetime.trim();

		orderNum = orderNum.trim();
		String[] on = orderNum.split(Constants.HASH_KEYWORD);
		this.mFoodOrderNumber = on[1];

		String[] p = null;
		if (null != order) {
			order = order.trim();
			p = order.split(Constants.EMPTY_STRING);
		}

		if (null != p[0] && null != p[1]) {
			p[0] = p[0].trim();
			p[1] = p[1].trim();
		} else {
			return;
		}

		int orderFryTime = 0;
		int totalCookServeTime = 0;
		int orderQuantity = Integer.parseInt(p[0]);

		
		if (Constants.FOOD_NAME_COD.equalsIgnoreCase(p[1])) {

			orderFryTime = Utility.getCodFryTime(orderQuantity);
			totalCookServeTime = orderFryTime - Constants.COOK_TIME_COD;

			// validate order
			if (isOrderValid(orderFryTime, totalCookServeTime, orderPlacetime,
					orderQueue)) {

				int batches = Utility.getFishBatches(orderQuantity);
				for (int i = 1; i <= batches; i++) {

					// calculation order quantity.
					int temp = 0;
					if (orderQuantity >= Constants.BATCH_QUANTITY) {
						temp = Constants.BATCH_QUANTITY;
						orderQuantity -= Constants.BATCH_QUANTITY;
					} else {
						temp = orderQuantity;
					}

					// Add fish time and quantity into list.
					mFoodCookingList.add(new CodFish(mLastOrderServedTime,
							orderPlacetime, temp, i));
				}
			}
		} else if (Constants.FOOD_NAME_HADDOCK.equalsIgnoreCase(p[1])) {

			orderFryTime = Utility.getHaddockFryTime(orderQuantity);
			totalCookServeTime = orderFryTime - Constants.COOK_TIME_HADDOCK;

			// Validate order.
			if (isOrderValid(orderFryTime, totalCookServeTime, orderPlacetime,
					orderQueue)) {

				int batches = Utility.getFishBatches(orderQuantity);
				for (int i = 1; i <= batches; i++) {

					// calculation order quantity.
					int temp = 0;
					if (orderQuantity >= Constants.BATCH_QUANTITY) {
						temp = Constants.BATCH_QUANTITY;
						orderQuantity -= Constants.BATCH_QUANTITY;
					} else {
						temp = orderQuantity;
					}

					// Add fish time and quantity into list.
					mFoodCookingList.add(new HaddockFish(mLastOrderServedTime,
							orderPlacetime, temp, i));
				}
			}
		} else if (Constants.FOOD_NAME_CHIPS.equalsIgnoreCase(p[1])) {

			orderFryTime = Utility.getChipsFryTime(orderQuantity);
			totalCookServeTime = orderFryTime - Constants.COOK_TIME_CHIPS;

			// Validate order
			if (isOrderValid(orderFryTime, totalCookServeTime, orderPlacetime,
					orderQueue)) {

				int batches = Utility.getChipsBatches(orderQuantity);

				for (int i = 1; i <= batches; i++) {

					// calculation order quantity.
					int temp = 0;
					if (orderQuantity >= Constants.BATCH_QUANTITY) {
						temp = Constants.BATCH_QUANTITY;
						orderQuantity -= Constants.BATCH_QUANTITY;
					} else {
						temp = orderQuantity;
					}

					// Add fish time and quantity into list.
					mFoodCookingList.add(new Chips(mLastOrderServedTime,
							orderPlacetime, temp, i));
				}
			}
		}
	}
}
