package com.myprog.blmberg;

/**
 * This class represents food items.
 * 
 * @author Rahul
 *
 */
public abstract class FoodItem implements Comparable<FoodItem> {

	private int mFoodQuantity;
	private String mFoodName;
	private String mFoodCookingTime;

	public void setFoodName(String name) {
		this.mFoodName = name;
	}

	public void setFoodQuantity(int quantity) {
		this.mFoodQuantity = quantity;
	}

	public void setFoodCookingTime(String time) {
		this.mFoodCookingTime = time;
	}

	/**
	 * This method is used to show time and quantity of order begin cooking.
	 * 
	 * @return
	 */
	public String getOrderCookingDetails() {
		return Constants.AT + Constants.EMPTY_STRING + mFoodCookingTime.trim()
				+ Constants.COMMA + Constants.EMPTY_STRING
				+ Constants.BEGIN_COOKING + Constants.EMPTY_STRING
				+ mFoodQuantity + Constants.EMPTY_STRING + mFoodName.trim();
	}

	public int compareTo(FoodItem item) {
		// TODO Auto-generated method stub
		int time = (int)Utility.getTimeInMS(((FoodItem)item).mFoodCookingTime) / Constants.SECOND_DIVIDEND;
		return ((int)(Utility.getTimeInMS(this.mFoodCookingTime) / Constants.SECOND_DIVIDEND)) - time;
	}
}
