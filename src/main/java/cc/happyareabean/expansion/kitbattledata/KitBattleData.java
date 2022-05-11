package cc.happyareabean.expansion.kitbattledata;

import lombok.Getter;
import me.wazup.kitbattle.KitbattleAPI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("FieldMayBeFinal")
public class KitBattleData {

	@Getter
	private HashMap<KitbattleAPI.Stat, LinkedHashMap<String, String>> stats;

	public KitBattleData() {
		stats = new HashMap<>();
	}

	public void clear() {
		stats.clear();
	}

	public void fetchData() {
		boolean isDebug = KitBattleDataExpansion.getInstance().getPlaceholderAPI().getPlaceholderAPIConfig().isDebugMode();
		CompletableFuture.supplyAsync(KitbattleAPI::getAllPlayersData)
					.thenAcceptAsync((playerMap) -> {
						for (KitbattleAPI.Stat statValue : KitbattleAPI.Stat.values()) {
							if (isDebug) KitBattleDataExpansion.getInstance().info("Fetching data: §e" + statValue.name() + "...");

							List<Map.Entry<String, Integer>> top = KitbattleAPI.getTopPlayers(playerMap, statValue, KitBattleDataExpansion.getInstance().getAmount());
							LinkedHashMap<String, String> data = new LinkedHashMap<>();

							for(int i = 0; i < 10; i++) {
								data.put(top.get(i).getKey(), String.valueOf(top.get(i).getValue()));
								stats.put(statValue, data);
							}

							if (isDebug) KitBattleDataExpansion.getInstance().info("- Fetched data " + statValue.name() + "!");
						}

						if (isDebug) KitBattleDataExpansion.getInstance().info(Arrays.toString(stats.keySet().toArray()));
					});
	}
}
