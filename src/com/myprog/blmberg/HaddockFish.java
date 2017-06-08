package com.myprog.blmberg;

/**
 * This class represents haddock fish.
 * 
 * @author Rahul
 *
 */
public class HaddockFish extends FoodItem {

	public HaddockFish(String lastOrderTime, String orderPaceTime,
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
		this.setFoodName(Constants.FOOD_NAME_HADDOCK);
		if (null == lastOrderTime && index == 1) {
			this.setFoodCookingTime(orderPaceTime);
		} else if (null == lastOrderTime && index > 1) {
			this.setFoodCookingTime(Utility.getOrderTime(orderPaceTime,
					((index - 1) * Constants.COOK_TIME_HADDOCK)));
		} else {
			this.setFoodCookingTime(Utility.getOrderTime(lastOrderTime,
					((index - 1) * Constants.COOK_TIME_HADDOCK)));
		}

		this.setFoodQuantity(orderQuantity);
	}
}
