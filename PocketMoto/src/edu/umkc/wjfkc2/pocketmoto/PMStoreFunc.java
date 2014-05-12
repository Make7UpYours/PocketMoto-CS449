package edu.umkc.wjfkc2.pocketmoto;

import junit.framework.Assert;
import android.app.Activity;
import android.widget.TextView;
/** Implements business logic for the stores.
 *  Class methods are used for purchasing and selecting
 *  suits and bikes.
 */
public class PMStoreFunc {
	/** Store the stats for the various suits for purchase.*/
	private class SuitStats{
		private int suitColor;
		private float suitHandling;
		private int suitHP;
		private int suitCost;
		SuitStats() {
			suitColor = -1;
			suitHandling = -1;
			suitHP = -1;
			suitCost = -1;
		}
		SuitStats(int color, float handling, int hp, int cost){
			suitColor = color;
			suitHandling = handling;
			suitHP = hp;
			suitCost = cost;
		}
		public int getSuitColor(){
			return suitColor;
		}
		public float getSuitHandling(){
			return suitHandling;
		}
		public int getSuitHP(){
			return suitHP;
		}
		public int getSuitCost(){
			return suitCost;
		}
	}
	/** Store the stats for the various bikes for purchase.*/
	private class BikeStats{
		private int bikeColor;
		private float bikeAcceleration;
		private float bikeSpeed;
		private int bikeCost;
		BikeStats() {
			bikeColor = -1;
			bikeAcceleration = -1;
			bikeSpeed = -1;
			bikeCost = -1;
		}
		BikeStats(int color, float acceleration, float speed, int cost){
			bikeColor = color;
			bikeAcceleration = acceleration;
			bikeSpeed = speed;
			bikeCost = cost;
		}
		public int getBikeColor(){
			return bikeColor;
		}
		public float getBikeAcceleration(){
			return bikeAcceleration;
		}
		public float getBikeSpeed(){
			return bikeSpeed;
		}
		public float getBikeCost(){
			return bikeCost;
		}
	}
	
	private BikeStats[] bikeSelections = new BikeStats[PMGameEngine.NUM_BIKES];
	private SuitStats[] suitSelections = new SuitStats[PMGameEngine.NUM_SUITS];
	
	/** Initializes the instances of bikeSelections and suitSelections with
	 *  appropriate values for each available selection from the store.
	 */
	PMStoreFunc(){
		//load bike values.
		bikeSelections[PMGameEngine.RED_BIKE] = new BikeStats(PMGameEngine.RED_BIKE,
				PMGameEngine.TIER_1_ACCELERATION, PMGameEngine.TIER_1_SPEED,
				PMGameEngine.TIER_1_BIKE_COST);
		bikeSelections[PMGameEngine.PURPLE_BIKE] = new BikeStats(PMGameEngine.PURPLE_BIKE,
				PMGameEngine.TIER_2_ACCELERATION, PMGameEngine.TIER_2_SPEED,
				PMGameEngine.TIER_2_BIKE_COST);
		bikeSelections[PMGameEngine.YELLOW_BIKE] = new BikeStats(PMGameEngine.YELLOW_BIKE,
				PMGameEngine.TIER_3_ACCELERATION, PMGameEngine.TIER_3_SPEED,
				PMGameEngine.TIER_3_BIKE_COST);
		bikeSelections[PMGameEngine.GREEN_BIKE] = new BikeStats(PMGameEngine.GREEN_BIKE,
				PMGameEngine.TIER_4_ACCELERATION, PMGameEngine.TIER_4_SPEED,
				PMGameEngine.TIER_4_BIKE_COST);
		//load suit values.
		suitSelections[PMGameEngine.BLUE_SUIT] = new SuitStats(PMGameEngine.BLUE_SUIT,
				PMGameEngine.TIER_1_HANDLING, PMGameEngine.TIER_1_HP,
				PMGameEngine.TIER_1_SUIT_COST);
		suitSelections[PMGameEngine.GREY_SUIT] = new SuitStats(PMGameEngine.GREY_SUIT,
				PMGameEngine.TIER_2_HANDLING, PMGameEngine.TIER_2_HP,
				PMGameEngine.TIER_2_SUIT_COST);
		suitSelections[PMGameEngine.ORANGE_SUIT] = new SuitStats(PMGameEngine.ORANGE_SUIT,
				PMGameEngine.TIER_3_HANDLING, PMGameEngine.TIER_3_HP,
				PMGameEngine.TIER_3_SUIT_COST);
		suitSelections[PMGameEngine.NEON_SUIT] = new SuitStats(PMGameEngine.NEON_SUIT,
				PMGameEngine.TIER_4_HANDLING, PMGameEngine.TIER_4_HP,
				PMGameEngine.TIER_4_SUIT_COST);
	}
	/** Called when the user purchases a suit from the suit store.
	 *  Method verifies that the player has enough funds for the transaction.
	 *  Method uses assertions to verify suitNum as a parameter and assumes that
	 *  the caller has already checked to see if the user is trying to purchase
	 *  a suit that has already been bought.
	 */
	public boolean purchaseSuit(int suitNum){
		Assert.assertTrue("suitNum is not a valid parameter.",
				suitNum >= 0 && suitNum < PMGameEngine.NUM_SUITS);
		boolean notDuplicateSuit = true;
		for(int index = 0; index < PMGameEngine.numPurchasedSuits; index++){
			if(PMGameEngine.purchasedSuits[index] == suitNum){
				//Player is trying to purchase a suit that they already purchased.
				notDuplicateSuit = false;
			}
		}
		Assert.assertTrue("Player has already purchased this suit.",
				notDuplicateSuit);
		if (PMGameEngine.playerEarnings >= suitSelections[suitNum].getSuitCost()){
			//Increase numPurchased and add to purchased list.
			PMGameEngine.numPurchasedSuits += 1;
			PMGameEngine.purchasedSuits[PMGameEngine.numPurchasedSuits - 1] = suitNum;
			PMGameEngine.playerEarnings -= suitSelections[suitNum].getSuitCost();
			return true;
		}
		//Player does not have the funds.
		return false;
	}
	/** Called when the user purchases a bike from the bike store.
	 *  Method verifies that the player has enough funds for the transaction.
	 *  Method uses assertions to verify bikeNum as a parameter and assumes that
	 *  the caller has already checked to see if the user is trying to purchase
	 *  a bike that has already been bought.
	 */
	public boolean purchaseBike(int bikeNum){
		Assert.assertTrue("bikeNum is not a valid parameter.",
				bikeNum >= 0 && bikeNum < PMGameEngine.NUM_BIKES);
		boolean notDuplicateSuit = true;
		for(int index = 0; index < PMGameEngine.numPurchasedBikes; index++){
			if(PMGameEngine.purchasedBikes[index] == bikeNum){
				//Player is trying to purchase a bike that they already purchased.
				notDuplicateSuit = false;
			}
		}
		Assert.assertTrue("Player has already purchased this bike.",
				notDuplicateSuit);
		if (PMGameEngine.playerEarnings >= bikeSelections[bikeNum].getBikeCost()){
			//Increase numPurchased and add to purchased list.
			PMGameEngine.numPurchasedBikes += 1;
			PMGameEngine.purchasedBikes[PMGameEngine.numPurchasedBikes - 1] = bikeNum;
			PMGameEngine.playerEarnings -= bikeSelections[bikeNum].getBikeCost();
			return true;
		}
		//Player does not have the funds.
		return false;
	}
	/** Attempts to equip a bike and update the current player stats.
	 *  If the bike has not been purchased, method will return false,
	 *  true otherwise.
	 *  Method uses assertions to verify that bikeNum is indeed a valid
	 *  parameter for indexing bikes.
	 */
	public boolean equipBike(int bikeNum){
		Assert.assertTrue("bikeNum is not a valid parameter.",
				bikeNum >= 0 && bikeNum < PMGameEngine.NUM_BIKES);
		for(int index = 0; index < PMGameEngine.numPurchasedBikes; index++){
			if (PMGameEngine.purchasedBikes[index] == bikeSelections[bikeNum].getBikeColor()){
				//Bike owned, update stats.
				PMGameEngine.curPlayerBike = bikeSelections[bikeNum].getBikeColor();
				PMGameEngine.playerBikeAcceleration = 
						bikeSelections[bikeNum].getBikeAcceleration();
				PMGameEngine.playerBikeSpeed = 
						bikeSelections[bikeNum].getBikeSpeed();
				return true;
			}
		}
		//Bike not owned.
		return false;
	}
	/** Attempts to equip a suit and update the current player stats.
	 *  If the suit has not been purchased, method will return false,
	 *  true otherwise.
	 *  Method uses assertions to verify that suitNum is indeed a valid
	 *  parameter for indexing suits.
	 */
	public boolean equipSuit(int suitNum){
		Assert.assertTrue("suitNum is not a valid parameter.",
				suitNum >= 0 && suitNum < PMGameEngine.NUM_SUITS);
		for(int index = 0; index < PMGameEngine.numPurchasedSuits; index++){
			if (PMGameEngine.purchasedSuits[index] == suitSelections[suitNum].getSuitColor()){
				//Suit owned, update stats.
				PMGameEngine.curPlayerSuit = suitSelections[suitNum].getSuitColor();
				PMGameEngine.playerBikeHandling = 
						suitSelections[suitNum].getSuitHandling();
				PMGameEngine.playerHP = suitSelections[suitNum].getSuitHP();
				if (PMGameEngine.gameOver){
					PMGameEngine.curPlayerHP = PMGameEngine.playerHP;	
				}
				return true;
			}
		}
		//Suit not owned.
		return false;
	}
	/** Updates the UI displaying the new value for player earned credits.
	 *  Method should be called upon Activity creation and after each 
	 *  successful transaction.
	 *  Param act is the Activity which is currently displaying the UI.
	 */
	public void updateCredits(Activity act){
		TextView credits = (TextView)act.findViewById(R.id.numCreditsText);
		credits.setText(Integer.toString(PMGameEngine.playerEarnings));
	}
}
