package admanager.budget;

import java.util.List;

import admanager.entity.Advertisement;
import database.AdDBHelper;

public class BudgetManager {

	//Verify if a client has money to show ads
	public static void queryBudgetStatus(List<Advertisement> candiAds){
		for (Advertisement ad: candiAds){
			AdDBHelper.queryClientInfo(ad);
			if (ad.getPrice() > ad.getBalance()) {
				candiAds.remove(ad);
			}
		}
		return;
	}
	
	
	public static void charge(List<Advertisement> rankedAds){
		for (Advertisement ad: rankedAds){
			boolean finish = AdDBHelper.charge(ad);
			if (!finish){
				System.err.println("Error when charging for " + ad.getClientid());
			}	
		}
		return;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
