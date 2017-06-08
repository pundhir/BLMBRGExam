package com.myprog.blmberg;

/**
 * This class represents chips.
 * 
 * @author Rahul
 *
 */
public class Chips extends FoodItem {

	public Chips(String lastOrderTime, String orderPaceTime, int orderQuantity,
			int index) {
		fillItemDetails(lastOrderTime, orderPaceTime, orderQuantity, index);
	}

	/**
	 * This method is used to fill order details.
	 * 
	 * @param order
	 */
	private void fillItemDetails(String lastOrderTime, String orderPaceTime,
			int orderQuantity, int index) {
		this.setFoodName(Constants.FOOD_NAME_CHIPS);
		if (null == lastOrderTime && index == 1) {
			this.setFoodCookingTime(orderPaceTime);
		} else if (null == lastOrderTime && index > 1) {
			this.setFoodCookingTime(Utility.getOrderTime(orderPaceTime,
					((index - 1) * Constants.COOK_TIME_CHIPS)));
		} else {
			this.setFoodCookingTime(Utility.getOrderTime(lastOrderTime,
					((index - 1) * Constants.COOK_TIME_CHIPS)));
		}

		this.setFoodQuantity(orderQuantity);
	}
}
