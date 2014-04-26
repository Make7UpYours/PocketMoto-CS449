package edu.umkc.wjfkc2.pocketmoto;

import junit.framework.Assert;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class PMSuitStore extends Activity implements OnClickListener {
	PMStoreFunc storeFunc = new PMStoreFunc();
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suitstorescreen);
		registerButtons();
		storeFunc.updateCredits(this);
	}
	/** Code is run when user clicks on buttons. */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.purchaseBlueSuitButton:
			if(checkOwned(PMGameEngine.BLUE_SUIT)){
				//Player already owns the suit.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseSuit(PMGameEngine.BLUE_SUIT)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Blue Suit of Noobness!";
					purchaseVerification(text);
					storeFunc.updateCredits(this);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipBlueSuitButton:
			if(storeFunc.equipSuit(PMGameEngine.BLUE_SUIT)){
				//Player owns the suit.
				CharSequence text = "Blue Suit of Noobness is now equiped";
				equipVerification(text);
			}else{
				//Player does not own suit.
				equipFail();
			}
			break;
		case R.id.purchaseGreySuitButton:
			if(checkOwned(PMGameEngine.GREY_SUIT)){
				//Player already owns the suit.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseSuit(PMGameEngine.GREY_SUIT)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Grey Suit of Experience!";
					purchaseVerification(text);
					storeFunc.updateCredits(this);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipGreySuitButton:
			if(storeFunc.equipSuit(PMGameEngine.GREY_SUIT)){
				//Player owns the suit.
				CharSequence text = "Grey Suit of Experience is now equiped";
				equipVerification(text);
			}else{
				//Player does not own suit.
				equipFail();
			}
			break;
		case R.id.purchaseOrangeSuitButton:
			if(checkOwned(PMGameEngine.ORANGE_SUIT)){
				//Player already owns the suit.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseSuit(PMGameEngine.ORANGE_SUIT)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Extreme Orange Suit!";
					purchaseVerification(text);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipOrangeSuitButton:
			if(storeFunc.equipSuit(PMGameEngine.ORANGE_SUIT)){
				//Player owns the suit.
				CharSequence text = "Extreme Orange Suit is now equiped";
				equipVerification(text);
			}else{
				//Player does not own suit.
				equipFail();
			}
			break;
		case R.id.purchaseNeonSuitButton:
			if(checkOwned(PMGameEngine.NEON_SUIT)){
				//Player already owns the suit.
				alreadyOwned();
			}else{
				if(storeFunc.purchaseSuit(PMGameEngine.NEON_SUIT)){
					//Player has the funds.
					CharSequence text = "You are the proud new owner" +
							" of the Neon Suit of Awesomeness!";
					purchaseVerification(text);
					storeFunc.updateCredits(this);
				}else{
					//Not enough funds.
					notEnoughFunds();
				}
			}
			break;
		case R.id.equipNeonSuitButton:
			if(storeFunc.equipSuit(PMGameEngine.NEON_SUIT)){
				//Player owns the suit.
				CharSequence text = "Neon Suit of Awesomeness is now equiped";
				equipVerification(text);
			}else{
				//Player does not own suit.
				equipFail();
			}
			break;
		}
	}
	/** Registers all the buttons on screen for the onClickListener.*/
	private void registerButtons(){
		View purBlueSuit = findViewById(R.id.purchaseBlueSuitButton);
		purBlueSuit.setOnClickListener(this);
		
		View equBlueSuit = findViewById(R.id.equipBlueSuitButton);
		equBlueSuit.setOnClickListener(this);
		
		View purGreySuit = findViewById(R.id.purchaseGreySuitButton);
		purGreySuit.setOnClickListener(this);
		
		View equGreySuit = findViewById(R.id.equipGreySuitButton);
		equGreySuit.setOnClickListener(this);
		
		View purOrangeSuit = findViewById(R.id.purchaseOrangeSuitButton);
		purOrangeSuit.setOnClickListener(this);
		
		View equOrangeSuit = findViewById(R.id.equipOrangeSuitButton);
		equOrangeSuit.setOnClickListener(this);
		
		View purNeonSuit = findViewById(R.id.purchaseNeonSuitButton);
		purNeonSuit.setOnClickListener(this);
		
		View equNeonSuit = findViewById(R.id.equipNeonSuitButton);
		equNeonSuit.setOnClickListener(this);
	}
	/** Checks to see if player owns a specific suit or not.
	 *  Param suitNum is the int corresponding to the color index
	 *  for the suit.
	 *  Returns true if player owns the suit already, false otherwise.
	 *  Method makes use of assertions to verify that suitNum is a valid
	 *  parameter.
	 */
	private boolean checkOwned(int suitNum){
		Assert.assertTrue("suitNum is not a valid parameter.",
				suitNum >= 0 && suitNum < PMGameEngine.NUM_SUITS);
		for(int index = 0; index < PMGameEngine.numPurchasedSuits; index++){
			if(PMGameEngine.purchasedSuits[index] == suitNum){
				//Player is trying to purchase a suit that they already purchased.
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
	 *  they already own the suit they are trying to purchase.
	 */
	private void alreadyOwned(){
		Context context = getApplicationContext();
		CharSequence text = "You already own this suit.";
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
	 *  they do not own the suit they are trying to equip.
	 */
	private void equipFail(){
		Context context = getApplicationContext();
		CharSequence text = "You do not own this suit, purchase it first.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
