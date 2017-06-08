package com.myprog.blmberg;

/**
 * This class represents cod fish.
 * 
 * @author Rahul
 *
 */
public class CodFish extends FoodItem {

	public CodFish(String lastOrderTime, String orderPaceTime,
			int orderQuantity, int index) {
		fillItemDetails(lastOrderTime, orderPaceTime, orderQuantity, index);
	}

	/**
	 * This method is used to fill order details.
	 * 
	 * @param order
	 */
	private void fillItemDetails(String lastOrderTime, String orderPaceTime,
			int orderQuantity, int index) {
		this.setFoodName(Constants.FOOD_NAME_COD);
		if (null == lastOrderTime && index == 1) {
			this.setFoodCookingTime(orderPaceTime);
		} else if (null == lastOrderTime && index > 1) {
			this.setFoodCookingTime(Utility.getOrderTime(orderPaceTime,
					((index - 1) * Constants.COOK_TIME_COD)));
		} else {
			this.setFoodCookingTime(Utility.getOrderTime(lastOrderTime,
					((index - 1) * Constants.COOK_TIME_COD)));
		}

		this.setFoodQuantity(orderQuantity);
	}
}
