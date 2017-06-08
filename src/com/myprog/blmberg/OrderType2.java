package com.myprog.blmberg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represents finlay orders type2 where software can process any
 * combination of two food items i. e. (Cod & Haddock) or (Cod & Chips) or
 * (Haddock & chips).
 * 
 * @author Rahul
 *
 */
public class OrderType2 extends FinlayOrder {

	public OrderType2(String orderNum, String time, String order1, String order2,
			Queue<FinlayOrder> orderQueue) {
		prepareOrder(orderNum, time, order1, order2, orderQueue);
	}

	private void prepareOrder(String orderNum, String orderPlaceTime, String order1,
			String order2, Queue<FinlayOrder> orderQueue) {
		this.mFoodCookingList = new LinkedList<FoodItem>();
		this.isOrderAccepted = false;
		this.mLastOrderServedTime = null;
		this.mOrderPlacedTime = orderPlaceTime.trim();

		orderNum = orderNum.trim();
		String[] on = orderNum.split(Constants.HASH_KEYWORD);
		this.mFoodOrderNumber = on[1];

		String[] O1 = null;
		if (null != order1) {
			order1 = order1.trim();
			O1 = order1.split(Constants.EMPTY_STRING);
		}

		String[] O2 = null;
		if (null != order2) {
			order2 = order2.trim();
			O2 = order2.split(Constants.EMPTY_STRING);
		}

		int codQuantity = 0;
		int haddockQuantity = 0;
		int codFryTime = 0;
		int haddockFryTime = 0;
		int chipsQuantity = 0;
		int chipsFryTime = 0;
		int orderFryTime = 0;
		int totalCookServeTime = 0;

		if (O1[1].equalsIgnoreCase(Constants.FOOD_NAME_COD)
				&& O2[1].equalsIgnoreCase(Constants.FOOD_NAME_HADDOCK)) {

			codQuantity = Integer.parseInt(O1[0]);
			haddockQuantity = Integer.parseInt(O2[0]);

			codFryTime = Utility.getCodFryTime(codQuantity);
			haddockFryTime = Utility.getHaddockFryTime(haddockQuantity);

			orderFryTime = Utility.getFishFryTime(codQuantity, haddockQuantity);

			// Change sec
			if (codFryTime > haddockFryTime) {
				totalCookServeTime = orderFryTime - Constants.COOK_TIME_COD;
			} else {
				totalCookServeTime = orderFryTime - Constants.COOK_TIME_HADDOCK;
			}

			// Validate order
			if (isOrderValid(orderFryTime, totalCookServeTime, orderPlaceTime,
					orderQueue)) {

				List<FoodItem> list = new ArrayList<FoodItem>();

				int rows = Utility.getFishBatches(codQuantity)
						+ Utility.getFishBatches(haddockQuantity);

				int codQ = 0;
				int hadQ = 0;
				int codTimeDiff = 0;
				int hadTimeDiff = 0;

				for (int i = 1; i <= rows; i++) {

					// calculation haddock fish quantity.
					if (haddockQuantity >= Constants.BATCH_QUANTITY) {
						hadQ = Constants.BATCH_QUANTITY;
						haddockQuantity -= Constants.BATCH_QUANTITY;
					} else {
						hadQ = haddockQuantity;
						haddockQuantity = 0;
					}

					// calculation cod fish quantity.
					if (codQuantity >= Constants.BATCH_QUANTITY) {
						codQ = Constants.BATCH_QUANTITY;
						codQuantity -= Constants.BATCH_QUANTITY;
					} else {
						codQ = codQuantity;
						codQuantity = 0;
					}

					// Adding food item in list.
					if (hadQ == Constants.BATCH_QUANTITY) {
						codQuantity += codQ;

						list.add(new HaddockFish(Utility.getOrderTimeDiff(
								mLastOrderServedTime, codTimeDiff), Utility
								.getOrderTimeDiff(orderPlaceTime, codTimeDiff),
								hadQ, i));
						hadTimeDiff = Constants.FISH_TIME_DIFF;
						codTimeDiff = 0;
					} else if (hadQ != 0 && codQ != 0
							&& hadQ + codQ > Constants.BATCH_QUANTITY) {

						codQuantity += ((hadQ + codQ) - Constants.BATCH_QUANTITY);
						codQ = Constants.BATCH_QUANTITY - hadQ;

						list.add(new HaddockFish(Utility.getOrderTimeDiff(
								mLastOrderServedTime, codTimeDiff), Utility
								.getOrderTimeDiff(orderPlaceTime, codTimeDiff),
								hadQ, i));

						list.add(new CodFish(Utility.getOrderTime(
								mLastOrderServedTime, hadTimeDiff), Utility
								.getOrderTime(orderPlaceTime, hadTimeDiff),
								codQ, i));

						hadTimeDiff = Constants.FISH_TIME_DIFF;
						codTimeDiff = 0;
					} else if (hadQ != 0 && codQ != 0
							&& hadQ + codQ <= Constants.BATCH_QUANTITY) {

						list.add(new HaddockFish(Utility.getOrderTimeDiff(
								mLastOrderServedTime, codTimeDiff), Utility
								.getOrderTimeDiff(orderPlaceTime, codTimeDiff),
								hadQ, i));

						list.add(new CodFish(Utility.getOrderTime(
								mLastOrderServedTime, hadTimeDiff), Utility
								.getOrderTime(orderPlaceTime, hadTimeDiff),
								codQ, i));

						hadTimeDiff = Constants.FISH_TIME_DIFF;
						codTimeDiff = 0;
					} else if (codQ == Constants.BATCH_QUANTITY) {

						haddockQuantity += hadQ;

						list.add(new CodFish(Utility.getOrderTime(
								mLastOrderServedTime, hadTimeDiff), Utility
								.getOrderTime(orderPlaceTime, hadTimeDiff),
								codQ, i));

						hadTimeDiff = 0;
						codTimeDiff = Constants.FISH_TIME_DIFF;
					} else if (hadQ != 0 && codQ == 0) {
						list.add(new HaddockFish(Utility.getOrderTimeDiff(
								mLastOrderServedTime, codTimeDiff), Utility
								.getOrderTimeDiff(orderPlaceTime, codTimeDiff),
								hadQ, i));
						hadTimeDiff = Constants.FISH_TIME_DIFF;
						codTimeDiff = 0;
					} else if (hadQ == 0 && codQ != 0) {
						list.add(new CodFish(Utility.getOrderTime(
								mLastOrderServedTime, hadTimeDiff), Utility
								.getOrderTime(orderPlaceTime, hadTimeDiff),
								codQ, i));
						hadTimeDiff = 0;
						codTimeDiff = Constants.FISH_TIME_DIFF;
					}

				}
				// sort list according to time
				Collections.sort(list);
				mFoodCookingList = list;

			}
		} else if (O1[1].equalsIgnoreCase(Constants.FOOD_NAME_HADDOCK)
				&& O2[1].equalsIgnoreCase(Constants.FOOD_NAME_CHIPS)) {

			haddockQuantity = Integer.parseInt(O1[0]);
			chipsQuantity = Integer.parseInt(O2[0]);
			haddockFryTime = Utility.getHaddockFryTime(haddockQuantity);
			chipsFryTime = Utility.getChipsFryTime(chipsQuantity);

			// Change sec
			if (chipsFryTime > haddockFryTime) {
				orderFryTime = chipsFryTime;
				totalCookServeTime = orderFryTime - Constants.COOK_TIME_CHIPS;
			} else {
				orderFryTime = haddockFryTime;
				totalCookServeTime = orderFryTime - Constants.COOK_TIME_HADDOCK;
			}

			if (isOrderValid(orderFryTime, totalCookServeTime, orderPlaceTime,
					orderQueue)) {

				int fishWaitingTime = 0;
				int chipsWaitingTime = 0;
				if (haddockFryTime > chipsFryTime) {
					chipsWaitingTime = haddockFryTime - chipsFryTime;
				} else {
					fishWaitingTime = chipsFryTime - haddockFryTime;
				}

				List<FoodItem> list = new ArrayList<FoodItem>();

				// Add fish
				String fishOrderPlaceTime = Utility.getOrderTime(
						orderPlaceTime, fishWaitingTime);
				String fishLastOrderTime = Utility.getOrderTime(
						mLastOrderServedTime, fishWaitingTime);

				int batches = Utility.getFishBatches(haddockQuantity);
				for (int i = 1; i <= batches; i++) {

					// calculation order quantity.
					int temp = 0;
					if (haddockQuantity >= Constants.BATCH_QUANTITY) {
						temp = Constants.BATCH_QUANTITY;
						haddockQuantity -= Constants.BATCH_QUANTITY;
					} else {
						temp = haddockQuantity;
					}

					list.add(new HaddockFish(fishLastOrderTime,
							fishOrderPlaceTime, temp, i));
				}

				// Add chips
				String chipsOrderTime = Utility.getOrderTime(orderPlaceTime,
						chipsWaitingTime);
				String chipsLastOrderTime = Utility.getOrderTime(
						mLastOrderServedTime, chipsWaitingTime);

				batches = Utility.getChipsBatches(chipsQuantity);
				for (int i = 1; i <= batches; i++) {

					// calculation chips quantity.
					int temp = 0;
					if (chipsQuantity >= Constants.BATCH_QUANTITY) {
						temp = Constants.BATCH_QUANTITY;
						chipsQuantity -= Constants.BATCH_QUANTITY;
					} else {
						temp = chipsQuantity;
					}

					list.add(new Chips(chipsLastOrderTime, chipsOrderTime,
							temp, i));
				}
				// sort list according to time
				Collections.sort(list);
				mFoodCookingList = list;

			}
		} else if (O1[1].equalsIgnoreCase(Constants.FOOD_NAME_COD)
				&& O2[1].equalsIgnoreCase(Constants.FOOD_NAME_CHIPS)) {

			codQuantity = Integer.parseInt(O1[0]);
			chipsQuantity = Integer.parseInt(O2[0]);
			codFryTime = Utility.getCodFryTime(codQuantity);
			chipsFryTime = Utility.getChipsFryTime(chipsQuantity);

		
			// Change sec
			if (chipsFryTime > codFryTime) {
				orderFryTime = chipsFryTime;
				totalCookServeTime = orderFryTime - Constants.COOK_TIME_CHIPS;
			} else {
				orderFryTime = codFryTime;
				totalCookServeTime = orderFryTime - Constants.COOK_TIME_COD;
			}

			if (isOrderValid(orderFryTime, totalCookServeTime, orderPlaceTime,
					orderQueue)) {

				int fishWaitingTime = 0;
				int chipsWaitingTime = 0;
				if (codFryTime > chipsFryTime) {
					chipsWaitingTime = codFryTime - chipsFryTime;
				} else {
					fishWaitingTime = chipsFryTime - codFryTime;
				}

				List<FoodItem> list = new ArrayList<FoodItem>();

				// Add fish
				String fishOrderPlaceTime = Utility.getOrderTime(
						orderPlaceTime, fishWaitingTime);
				String fishLastOrderTime = Utility.getOrderTime(
						mLastOrderServedTime, fishWaitingTime);

				int batches = Utility.getFishBatches(codQuantity);
				for (int i = 1; i <= batches; i++) {

					// calculation order quantity.
					int temp = 0;
					if (codQuantity >= Constants.BATCH_QUANTITY) {
						temp = Constants.BATCH_QUANTITY;
						codQuantity -= Constants.BATCH_QUANTITY;
					} else {
						temp = codQuantity;
					}

					list.add(new CodFish(fishLastOrderTime, fishOrderPlaceTime,
							temp, i));
				}

				// Add chips
				String chipsOrderTime = Utility.getOrderTime(orderPlaceTime,
						chipsWaitingTime);
				String chipsLastOrderTime = Utility.getOrderTime(
						mLastOrderServedTime, chipsWaitingTime);

				batches = Utility.getChipsBatches(chipsQuantity);
				for (int i = 1; i <= batches; i++) {

					// calculation chips quantity.
					int temp = 0;
					if (chipsQuantity >= Constants.BATCH_QUANTITY) {
						temp = Constants.BATCH_QUANTITY;
						chipsQuantity -= Constants.BATCH_QUANTITY;
					} else {
						temp = chipsQuantity;
					}

					list.add(new Chips(chipsLastOrderTime, chipsOrderTime,
							temp, i));
				}
				// sort list according to time
				Collections.sort(list);
				mFoodCookingList = list;
			}
		}
	}
}
