package edu.umkc.wjfkc2.test;

import android.content.Context;
import android.test.AndroidTestCase;
import edu.umkc.wjfkc2.pocketmoto.PMStoreFunc;

public class PMStoreFuncTest extends AndroidTestCase {
	private PMStoreFunc store;
	//private BikeStats bikeStats;
	//private SuitStats suitStats;
	
	protected void setUp() throws Exception {
		super.setUp();
		Context c = getContext();
		
		//store = new PMStoreFunc();
	}
	
	public void testInit() {
		//Check first bike.
		/*bikeStats = store.getBikeStats(PMGameEngine.RED_BIKE);
		Assert.assertTrue("Bike is not colored red.", bikeStats.getBikeColor() == PMGameEngine.RED_BIKE);
		Assert.assertTrue("Bike has incorrect acceleration.", bikeStats.getBikeAcceleration() == PMGameEngine.TIER_1_ACCELERATION);
		Assert.assertTrue("Bike does not have correct speed.", bikeStats.getBikeSpeed() == PMGameEngine.TIER_1_SPEED);
		//Check second bike.
		bikeStats = store.getBikeStats(PMGameEngine.PURPLE_BIKE);
		Assert.assertTrue("Bike is not colored purple.", bikeStats.getBikeColor() == PMGameEngine.PURPLE_BIKE);
		Assert.assertTrue("Bike has incorrect acceleration.", bikeStats.getBikeAcceleration() == PMGameEngine.TIER_2_ACCELERATION);
		Assert.assertTrue("Bike does not have correct speed.", bikeStats.getBikeSpeed() == PMGameEngine.TIER_2_SPEED);
		//Check third bike.
		bikeStats = store.getBikeStats(PMGameEngine.YELLOW_BIKE);
		Assert.assertTrue("Bike is not colored yellow.", bikeStats.getBikeColor() == PMGameEngine.YELLOW_BIKE);
		Assert.assertTrue("Bike has incorrect acceleration.", bikeStats.getBikeAcceleration() == PMGameEngine.TIER_3_ACCELERATION);
		Assert.assertTrue("Bike does not have correct speed.", bikeStats.getBikeSpeed() == PMGameEngine.TIER_3_SPEED);
		//Check fourth bike.
		bikeStats = store.getBikeStats(PMGameEngine.GREEN_BIKE);
		Assert.assertTrue("Bike is not colored green.", bikeStats.getBikeColor() == PMGameEngine.GREEN_BIKE);
		Assert.assertTrue("Bike has incorrect acceleration.", bikeStats.getBikeAcceleration() == PMGameEngine.TIER_4_ACCELERATION);
		Assert.assertTrue("Bike does not have correct speed.", bikeStats.getBikeSpeed() == PMGameEngine.TIER_4_SPEED);
		//Check suits.
		//Check first suit.
		suitStats = store.getSuitStats(PMGameEngine.BLUE_SUIT);
		Assert.assertTrue("Suit is not colored blue.", suitStats.getSuitColor() == PMGameEngine.BLUE_SUIT);
		Assert.assertTrue("Suit has incorrect handling.", suitStats.getSuitHandling() == PMGameEngine.TIER_1_HANDLING);
		Assert.assertTrue("Suit does not have correct HP.", suitStats.getSuitHP() == PMGameEngine.TIER_1_HP);
		//Check second suit.
		suitStats = store.getSuitStats(PMGameEngine.GREY_SUIT);
		Assert.assertTrue("Suit is not colored grey.", suitStats.getSuitColor() == PMGameEngine.GREY_SUIT);
		Assert.assertTrue("Suit has incorrect handling.", suitStats.getSuitHandling() == PMGameEngine.TIER_2_HANDLING);
		Assert.assertTrue("Suit does not have correct HP.", suitStats.getSuitHP() == PMGameEngine.TIER_2_HP);
		//Check third suit.
		suitStats = store.getSuitStats(PMGameEngine.ORANGE_SUIT);
		Assert.assertTrue("Suit is not colored orange.", suitStats.getSuitColor() == PMGameEngine.ORANGE_SUIT);
		Assert.assertTrue("Suit has incorrect handling.", suitStats.getSuitHandling() == PMGameEngine.TIER_3_HANDLING);
		Assert.assertTrue("Suit does not have correct HP.", suitStats.getSuitHP() == PMGameEngine.TIER_3_HP);
		//Check fourth suit.
		suitStats = store.getSuitStats(PMGameEngine.NEON_SUIT);
		Assert.assertTrue("Suit is not colored neon.", suitStats.getSuitColor() == PMGameEngine.NEON_SUIT);
		Assert.assertTrue("Suit has incorrect handling.", suitStats.getSuitHandling() == PMGameEngine.TIER_4_HANDLING);
		Assert.assertTrue("Suit does not have correct HP.", suitStats.getSuitHP() == PMGameEngine.TIER_4_HP);*/
	}

}
