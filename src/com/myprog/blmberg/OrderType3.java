package com.myprog.blmberg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represents finlay orders type2 where software can process any
 * combination of all three food items i. e. (Cod & Haddock & Chips).
 * 
 * @author Rahul
 *
 */
public class OrderType3 extends FinlayOrder {

	public OrderType3(String orderNum, String time, String cod, String haddock,
			String chips, Queue<FinlayOrder> orderQueue) {
		prepareOrder(orderNum, time, cod, haddock, chips, orderQueue);
	}

	private void prepareOrder(String orderNum, String orderPlaceTime,
			String codOrder, String haddockOrder, String chipsOrder,
			Queue<FinlayOrder> orderQueue) {
		this.mFoodCookingList = new LinkedList<FoodItem>();
		this.isOrderAccepted = false;
		this.mLastOrderServedTime = null;
		this.mOrderPlacedTime = orderPlaceTime.trim();

		orderNum = orderNum.trim();
		String[] on = orderNum.split(Constants.HASH_KEYWORD);
		this.mFoodOrderNumber = on[1];

		String[] cod = null;
		if (null != codOrder) {
			codOrder = codOrder.trim();
			cod = codOrder.split(Constants.EMPTY_STRING);
		}

		String[] haddock = null;
		if (null != haddockOrder) {
			haddockOrder = haddockOrder.trim();
			haddock = haddockOrder.split(Constants.EMPTY_STRING);
		}

		String[] chips = null;
		if (null != chipsOrder) {
			chipsOrder = chipsOrder.trim();
			chips = chipsOrder.split(Constants.EMPTY_STRING);
		}

		int codQuantity = Integer.parseInt(cod[0]);
		int haddockQuantity = Integer.parseInt(haddock[0]);
		int totalfishFryTimeishFryTime = Utility.getFishFryTime(codQuantity,
				haddockQuantity);
		int chipsQuantity = Integer.parseInt(chips[0]);
		int chipsFryTime = Utility.getChipsFryTime(chipsQuantity);

		int orderFryTime = chipsFryTime >= totalfishFryTimeishFryTime ? chipsFryTime
				: totalfishFryTimeishFryTime;

		int firstPortionFryTime = chipsFryTime >= totalfishFryTimeishFryTime ? Constants.COOK_TIME_CHIPS
				: Constants.COOK_TIME_HADDOCK;

		int totalCookServeTime = orderFryTime - firstPortionFryTime;

		if (isOrderValid(orderFryTime, totalCookServeTime, orderPlaceTime,
				orderQueue)) {

			int fishWaitingTime = 0;
			int chipsWaitingTime = 0;
			if (totalfishFryTimeishFryTime > chipsFryTime) {
				chipsWaitingTime = totalfishFryTimeishFryTime - chipsFryTime;
			} else {
				fishWaitingTime = chipsFryTime - totalfishFryTimeishFryTime;
			}

			List<FoodItem> list = new ArrayList<FoodItem>();

			// Add fish
			String fishOrderTime = Utility.getOrderTime(orderPlaceTime,
					fishWaitingTime);
			String fishLastOrderTime = Utility.getOrderTime(
					mLastOrderServedTime, fishWaitingTime);

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
							fishLastOrderTime, codTimeDiff), Utility
							.getOrderTimeDiff(fishOrderTime, codTimeDiff),
							hadQ, i));
					hadTimeDiff = Constants.FISH_TIME_DIFF;
					codTimeDiff = 0;
				} else if (hadQ != 0 && codQ != 0
						&& hadQ + codQ > Constants.BATCH_QUANTITY) {

					codQuantity += ((hadQ + codQ) - Constants.BATCH_QUANTITY);
					codQ = Constants.BATCH_QUANTITY - hadQ;

					list.add(new HaddockFish(Utility.getOrderTimeDiff(
							fishLastOrderTime, codTimeDiff), Utility
							.getOrderTimeDiff(fishOrderTime, codTimeDiff),
							hadQ, i));

					list.add(new CodFish(Utility.getOrderTime(
							fishLastOrderTime, hadTimeDiff), Utility
							.getOrderTime(fishOrderTime, hadTimeDiff), codQ, i));

					hadTimeDiff = Constants.FISH_TIME_DIFF;
					codTimeDiff = 0;
				} else if (hadQ != 0 && codQ != 0
						&& hadQ + codQ <= Constants.BATCH_QUANTITY) {

					list.add(new HaddockFish(Utility.getOrderTimeDiff(
							fishLastOrderTime, codTimeDiff), Utility
							.getOrderTimeDiff(fishOrderTime, codTimeDiff),
							hadQ, i));

					list.add(new CodFish(Utility.getOrderTime(
							fishLastOrderTime, hadTimeDiff), Utility
							.getOrderTime(fishOrderTime, hadTimeDiff), codQ, i));

					hadTimeDiff = Constants.FISH_TIME_DIFF;
					codTimeDiff = 0;
				} else if (codQ == Constants.BATCH_QUANTITY) {

					haddockQuantity += hadQ;

					list.add(new CodFish(Utility.getOrderTime(
							fishLastOrderTime, hadTimeDiff), Utility
							.getOrderTime(fishOrderTime, hadTimeDiff), codQ, i));

					hadTimeDiff = 0;
					codTimeDiff = Constants.FISH_TIME_DIFF;
				} else if (hadQ != 0 && codQ == 0) {
					list.add(new HaddockFish(Utility.getOrderTimeDiff(
							fishLastOrderTime, codTimeDiff), Utility
							.getOrderTimeDiff(fishOrderTime, codTimeDiff),
							hadQ, i));
					hadTimeDiff = Constants.FISH_TIME_DIFF;
					codTimeDiff = 0;
				} else if (hadQ == 0 && codQ != 0) {
					list.add(new CodFish(Utility.getOrderTime(
							fishLastOrderTime, hadTimeDiff), Utility
							.getOrderTime(fishOrderTime, hadTimeDiff), codQ, i));
					hadTimeDiff = 0;
					codTimeDiff = Constants.FISH_TIME_DIFF;
				}

			}
			// Add chips
			String chipsOrderTime = Utility.getOrderTime(orderPlaceTime,
					chipsWaitingTime);
			String chipsLastOrderTime = Utility.getOrderTime(
					mLastOrderServedTime, chipsWaitingTime);

			int batches = Utility.getChipsBatches(chipsQuantity);
			for (int i = 1; i <= batches; i++) {

				// calculation chips quantity.
				int temp = 0;
				if (chipsQuantity >= Constants.BATCH_QUANTITY) {
					temp = Constants.BATCH_QUANTITY;
					chipsQuantity -= Constants.BATCH_QUANTITY;
				} else {
					temp = chipsQuantity;
				}

				list.add(new Chips(chipsLastOrderTime, chipsOrderTime, temp, i));
			}
			// sort list according to time
			Collections.sort(list);
			mFoodCookingList = list;
		}
	}
}
