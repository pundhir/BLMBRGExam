package com.myprog.blmberg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution {

	public static void main(String[] args) {

		List<String> orderList = new ArrayList<String>();
		orderList.add("Order #1, 12:00:00, 8 Haddock, 6 Chips");
		orderList.add("Order #2, 12:01:00, 2 Haddock, 1 Chips");
		orderList.add("Order #3, 12:05:00, 1 Cod, 1 Chips");
		orderList.add("Order #4, 12:03:00, 2 Cod, 1 Chips");
		orderList.add("Order #4, 12:08:00, 3 Cod, 5 Chips");
		orderList.add("Order #4, 12:02:30, 1 Cod, 1 Chips");
		Queue<FinlayOrder> orderQueue = parseOrderInput(orderList);
		showOrderStatus(orderQueue);
	}

	/**
	 * This method is used to parse response and create order queue.
	 * 
	 * @param input
	 *            It defines order list.
	 * @return Queue<String>
	 */
	private static Queue<FinlayOrder> parseOrderInput(List<String> input) {
		Queue<FinlayOrder> orderQueue = new LinkedList<FinlayOrder>();

		for (String orderLine : input) {
			String[] p = orderLine.split(Constants.COMMA);
			int len = p.length;

			if (len == 3) {
				if (null != p[0] && null != p[1] && null != p[2]) {
					FinlayOrder order = new OrderType1(p[0], p[1], p[2],
							orderQueue);
					orderQueue.add(order);
				}
			} else if (len == 4) {
				if (null != p[0] && null != p[1] && null != p[2]
						&& null != p[3]) {
					FinlayOrder order = new OrderType2(p[0], p[1], p[2], p[3],
							orderQueue);
					orderQueue.add(order);
				}
			} else if (len == 5) {
				if (null != p[0] && null != p[1] && null != p[2]
						&& null != p[3] && null != p[4]) {
					FinlayOrder order = new OrderType3(p[0], p[1], p[2], p[3],
							p[4], orderQueue);
					orderQueue.add(order);
				}
			}

		}
		return orderQueue;
	}

	/**
	 * This method is used to show finlay shop order status.
	 * 
	 * @param orderQueue
	 */
	private static void showOrderStatus(Queue<FinlayOrder> orderQueue) {
		Iterator<FinlayOrder> iterator = orderQueue.iterator();
		while (iterator.hasNext()) {
			FinlayOrder order = (FinlayOrder) iterator.next();
			if (order.isOrderAccepted()) {
				System.out.println(Constants.AT + Constants.EMPTY_STRING
						+ order.getOrderPlacedTime().trim() + Constants.COMMA
						+ Constants.EMPTY_STRING + Constants.ORDER
						+ order.getFoodOrderNumber().trim()
						+ Constants.EMPTY_STRING + Constants.ACCEPTED);
				for (FoodItem cookingDetails : order.getFoodCookingList()) {
					System.out.println(cookingDetails.getOrderCookingDetails());
				}
				System.out.println(Constants.AT + Constants.EMPTY_STRING
						+ order.getOrderServedTime().trim() + Constants.COMMA
						+ Constants.EMPTY_STRING + Constants.SERVE
						+ Constants.EMPTY_STRING + Constants.ORDER
						+ order.getFoodOrderNumber().trim());
			} else {
				System.out.println(Constants.AT + Constants.EMPTY_STRING
						+ order.getOrderPlacedTime().trim() + Constants.COMMA
						+ Constants.EMPTY_STRING + Constants.ORDER
						+ order.getFoodOrderNumber().trim()
						+ Constants.EMPTY_STRING + Constants.REJECTED);
			}
		}
	}
}
