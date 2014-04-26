package edu.umkc.wjfkc2.pocketmoto;

import junit.framework.Assert;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class PMBikeStore extends Activity implements OnClickListener {
	PMStoreFunc storeFunc = new PMStoreFunc();
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bikestorescreen);	
		registerButtons();
		storeFunc.updateCredits(this);
	}
	/** Code is run when user clicks on buttons. */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.purchaseRedBike:
			if(checkOwned(PMGameEngine.RED_BIKE)){
				//Player already owns the bike.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseBike(PMGameEngine.RED_BIKE)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Grandma's Moped!";
					purchaseVerification(text);
					storeFunc.updateCredits(this);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipRedBike:
			if(storeFunc.equipBike(PMGameEngine.RED_BIKE)){
				//Player owns the bike.
				CharSequence text = "Grandma's Moped is now equiped";
				equipVerification(text);
			}else{
				//Player does not own bike.
				equipFail();
			}
			break;
		case R.id.purchasePurpleBike:
			if(checkOwned(PMGameEngine.PURPLE_BIKE)){
				//Player already owns the bike.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseBike(PMGameEngine.PURPLE_BIKE)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Donatello!";
					purchaseVerification(text);
					storeFunc.updateCredits(this);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipPurpleBike:
			if(storeFunc.equipBike(PMGameEngine.PURPLE_BIKE)){
				//Player owns the bike.
				CharSequence text = "Donatello is now equiped";
				equipVerification(text);
			}else{
				//Player does not own bike.
				equipFail();
			}
			break;
		case R.id.purchaseYellowBike:
			if(checkOwned(PMGameEngine.YELLOW_BIKE)){
				//Player already owns the bike.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseBike(PMGameEngine.YELLOW_BIKE)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Super Sonic Bike!";
					purchaseVerification(text);
					storeFunc.updateCredits(this);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipYellowBike:
			if(storeFunc.equipBike(PMGameEngine.YELLOW_BIKE)){
				//Player owns the bike.
				CharSequence text = "Super Sonic Bike is now equiped";
				equipVerification(text);
			}else{
				//Player does not own bike.
				equipFail();
			}
			break;
		case R.id.purchaseGeenBike:
			if(checkOwned(PMGameEngine.GREEN_BIKE)){
				//Player already owns the bike.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseBike(PMGameEngine.GREEN_BIKE)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Mega Awesome Bike!";
					purchaseVerification(text);
					storeFunc.updateCredits(this);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipGreenBike:
			if(storeFunc.equipBike(PMGameEngine.GREEN_BIKE)){
				//Player owns the bike.
				CharSequence text = "Mega Awesome Bike is now equiped";
				equipVerification(text);
			}else{
				//Player does not own bike.
				equipFail();
			}
			break;
		}
	}
	/** Registers all the buttons on screen for the onClickListener.*/
	private void registerButtons(){
		View purRedBike = findViewById(R.id.purchaseRedBike);
		purRedBike.setOnClickListener(this);
		
		View equRedBike = findViewById(R.id.equipRedBike);
		equRedBike.setOnClickListener(this);
		
		View purPurpleBike = findViewById(R.id.purchasePurpleBike);
		purPurpleBike.setOnClickListener(this);
		
		View equPurpleBike = findViewById(R.id.equipPurpleBike);
		equPurpleBike.setOnClickListener(this);
		
		View purYellowBike = findViewById(R.id.purchaseYellowBike);
		purYellowBike.setOnClickListener(this);
		
		View equYellowBike = findViewById(R.id.equipYellowBike);
		equYellowBike.setOnClickListener(this);
		
		View purGreenBike = findViewById(R.id.purchaseGeenBike);
		purGreenBike.setOnClickListener(this);
		
		View equGreenBike = findViewById(R.id.equipGreenBike);
		equGreenBike.setOnClickListener(this);
	}
	/** Checks to see if player owns a specific bike or not.
	 *  Param bikeNum is the int corresponding to the color index
	 *  for the bike.
	 *  Returns true if player owns the bike already, false otherwise.
	 *  Method makes use of assertions to verify that bikeNum is a valid
	 *  parameter.
	 */
	private boolean checkOwned(int bikeNum){
		Assert.assertTrue("bikeNum is not a valid parameter.",
				bikeNum >= 0 && bikeNum < PMGameEngine.NUM_BIKES);
		for(int index = 0; index < PMGameEngine.numPurchasedBikes; index++){
			if(PMGameEngine.purchasedBikes[index] == bikeNum){
				//Player is trying to purchase a bike that they already purchased.
				return true;
			}
		}
		return false;
	}
	/** Displays a toast message letting the player know that
	 *  they do not have the required funds for the purchase.
	 */
	private void notEnoughFunds(){
		Context context = getApplicationContext();
		CharSequence text = "You do not have enough credits for this transaction.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	/** Displays a toast message letting the player know that
	 *  they already own the bike they are trying to purchase.
	 */
	private void alreadyOwned(){
		Context context = getApplicationContext();
		CharSequence text = "You already own this bike.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	/** Displays a toast message letting the player know that
	 *  their purchase was successful.
	 *  The message to be displayed will be passed as param msg.
	 */
	private void purchaseVerification(CharSequence msg){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();
	}/** Displays a toast message letting the player know that
	 *  their equip was successful.
	 *  The message to be displayed will be passed as param msg.
	 */
	private void equipVerification(CharSequence msg){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();
	}
	/** Displays a toast message letting the player know that
	 *  they do not own the bike they are trying to equip.
	 */
	private void equipFail(){
		Context context = getApplicationContext();
		CharSequence text = "You do not own this bike, purchase it first.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
