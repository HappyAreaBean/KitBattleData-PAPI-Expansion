package cc.happyareabean.expansion.kitbattledata;

public class KitBattleDataTask implements Runnable {
	@Override
	public void run() {
		KitBattleDataExpansion.getInstance().getKitBattleData().fetchData();
	}
}
