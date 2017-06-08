package com.myprog.blmberg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

/**
 * This class is used to provide utility method, help to calculate common stuff.
 * 
 * @author Rahul
 *
 */
public class Utility {

	/**
	 * This function is used to get fish batches to fry.
	 * 
	 * @param fish
	 * @return
	 */
	public static int getFishBatches(int fish) {
		int batches = (fish / Constants.BATCH_QUANTITY);
		if (fish % Constants.BATCH_QUANTITY != 0) {
			batches += 1;
		}
		return batches;
	}

	/**
	 * This method is used to calculate chips batches to fry.
	 * 
	 * @param chips
	 * @return
	 */
	public static int getChipsBatches(int chips) {
		int batches = chips / Constants.BATCH_QUANTITY;
		if (chips % Constants.BATCH_QUANTITY != 0) {
			batches += 1;
		}
		return batches;
	}

	/**
	 * This method is used to calculate code fish fly time.
	 * 
	 * @param cod
	 * @return
	 */
	public static int getCodFryTime(int cod) {
		int c = (cod / Constants.BATCH_QUANTITY);
		if (cod % Constants.BATCH_QUANTITY != 0) {
			c += 1;
		}
		return (c * Constants.COOK_TIME_COD);
	}

	/**
	 * This method is used to calculate haddock fish fry time.
	 * 
	 * @param haddock
	 * @return
	 */
	public static int getHaddockFryTime(int haddock) {
		int h = (haddock / Constants.BATCH_QUANTITY);
		if (haddock % Constants.BATCH_QUANTITY != 0) {
			h += 1;
		}
		return (h * Constants.COOK_TIME_HADDOCK);
	}

	/**
	 * This method is used to calculate chips fry time.
	 * 
	 * @param chips
	 * @return
	 */
	public static int getChipsFryTime(int chips) {
		int batch = chips / Constants.BATCH_QUANTITY;
		if (chips % Constants.BATCH_QUANTITY != 0) {
			batch += 1;
		}
		return (batch * Constants.COOK_TIME_CHIPS);
	}

	/**
	 * This method is used to calculate total fry time, if both cod and haddock
	 * fish are in order.
	 * 
	 * @param cod
	 * @param haddock
	 * @return
	 */
	public static int getFishFryTime(int cod, int haddock) {
		int c = (cod / Constants.BATCH_QUANTITY);
		if (cod % Constants.BATCH_QUANTITY != 0) {
			c += 1;
		}

		int h = (haddock / Constants.BATCH_QUANTITY);
		if (haddock % Constants.BATCH_QUANTITY != 0) {
			h += 1;
		}

		int t = (cod + haddock) / Constants.BATCH_QUANTITY;
		if ((cod + haddock) % Constants.BATCH_QUANTITY != 0) {
			t += 1;
		}

		int time = 0;
		/*
		 * if total portion equal to sum of cod portion and haddock portion then
		 * calculated time will be each portion multiply frying time. else if
		 * else if else if
		 */
		if (t == (c + h)) {
			time = c * Constants.COOK_TIME_COD + h
					* Constants.COOK_TIME_HADDOCK;
		} else if (t < (c + h) && c == h) {
			time = Constants.COOK_TIME_HADDOCK;
		} else if (t < (c + h) && c > h) {
			time = (c - h) * Constants.COOK_TIME_COD + h
					* Constants.COOK_TIME_HADDOCK;
		} else if (t < (c + h) && h > c) {
			time = h * Constants.COOK_TIME_HADDOCK;
		}
		return time;
	}

	/**
	 * This method is used to check order queue availability to add or reject
	 * new order.
	 * 
	 * @param lastOrderServedTime
	 * @param orderPlaceTime
	 * @param fryTime
	 * @param orderQueue
	 * @return
	 */
	public static boolean validateOrderQueueAvailability(
			String lastOrderServedTime, String orderPlaceTime, int fryTime,
			Queue<FinlayOrder> orderQueue) {
		if (null != orderQueue && orderQueue.isEmpty()
				&& fryTime <= Constants.MAXIMUM_ORDER_TIME) {
			return true;
		} else if (null != orderQueue && orderQueue.isEmpty()
				&& fryTime > Constants.MAXIMUM_ORDER_TIME) {
			return false;
		} else if (null != lastOrderServedTime) {
			String orderServedTime = getOrderTime(lastOrderServedTime, fryTime);
			if (subtractTwoTime(orderServedTime, orderPlaceTime) <= Constants.MAXIMUM_ORDER_TIME) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * This method is used to get order served time.
	 * 
	 * @param orderPlaceTime
	 * @param seconds
	 * @param orderQueue
	 * @return
	 */
	public static String getOrderServedTime(String orderPlaceTime, int seconds,
			Queue<FinlayOrder> orderQueue) {
		if (null != orderQueue && orderQueue.isEmpty()) {
			return getOrderTime(orderPlaceTime, seconds);
		} else if (null != getLastAcceptedOrder(orderQueue)){
			String lastOrderServedTime = getLastAcceptedOrder(orderQueue).mOrderServedTime;
			if (getTimeInMS(lastOrderServedTime) >= getTimeInMS(orderPlaceTime)) {
				return getOrderTime(lastOrderServedTime, seconds);	
			} else {
				return getOrderTime(orderPlaceTime, seconds);
			}
		} else {
			return getOrderTime(orderPlaceTime, seconds);
		}
	}

	/**
	 * This method is used to return last accepted order.
	 * 
	 * @param orderQueue
	 * @return
	 */
	public static FinlayOrder getLastAcceptedOrder(Queue<FinlayOrder> orderQueue) {
		FinlayOrder lastOrder = null;
		if (null != orderQueue && !orderQueue.isEmpty()) {
			for (FinlayOrder order : orderQueue) {
				if (order.isOrderAccepted) {
					lastOrder = order;
				}
			}
		}
		return lastOrder;
	}

	/**
	 * This method is used to calculate basic order time, help to calculate
	 * order serve time and order cooking time.
	 * 
	 * @param orderPlaceTime
	 * @param seconds
	 * @return
	 */
	public static String getOrderTime(String orderPlaceTime, int seconds) {
		SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_FORMAT);
		Date d = null;
		long time = 0;
		String orderTime = null;
		try {
			if (orderPlaceTime != null) {
				d = df.parse(orderPlaceTime);
				time = d.getTime() + (seconds * Constants.SECOND_DIVIDEND);
				orderTime = df.format(new Date(time));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return orderTime;
	}

	/**
	 * This method is used to calculate fish cooking time difference.
	 * 
	 * @param orderPlaceTime
	 * @param seconds
	 * @return
	 */
	public static String getOrderTimeDiff(String orderPlaceTime, int seconds) {
		SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_FORMAT);
		Date d = null;
		long time = 0;
		String orderTime = null;
		try {
			if (orderPlaceTime != null) {
				d = df.parse(orderPlaceTime);
				time = d.getTime() - (seconds * Constants.SECOND_DIVIDEND);
				orderTime = df.format(new Date(time));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return orderTime;
	}

	/**
	 * This method is use to subtract two times and return seconds.
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	private static int subtractTwoTime(String time1, String time2) {
		SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_FORMAT);
		Date d1 = null;
		Date d2 = null;
		int seconds = 0;
		try {
			d1 = df.parse(time1);
			d2 = df.parse(time2);
			seconds = (int) ((d1.getTime() - d2.getTime()) / Constants.SECOND_DIVIDEND);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return seconds;
	}

	/**
	 * This method is used to convert string time into milliseconds.
	 * 
	 * @param timeS
	 * @return
	 */
	public static long getTimeInMS(String timeS) {
		SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_FORMAT);
		long timeL = 0;
		Date d = null;
		try {
			if (timeS != null) {
				d = df.parse(timeS);
				timeL = d.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeL;
	}

	/**
	 * This method is used to convert milliseconds time into string time.
	 * 
	 * @param timeL
	 * @return
	 */
	public static String getTimeInString(long timeL) {
		SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_FORMAT);
		String timeS = null;
		timeS = df.format(new Date(timeL));
		return timeS;
	}
}
