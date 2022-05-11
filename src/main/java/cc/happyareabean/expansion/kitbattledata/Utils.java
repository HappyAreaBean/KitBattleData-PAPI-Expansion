package cc.happyareabean.expansion.kitbattledata;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

	public static String toFormatted(String toBeFormatted, int position, String playerName, String value) {
		toBeFormatted = toBeFormatted.replace("<position>", String.valueOf(position));
		toBeFormatted = toBeFormatted.replace("<player>", (playerName.startsWith("NO") ? KitBattleDataExpansion.getInstance().getString("emptyPlayers", "---") : playerName));
		toBeFormatted = toBeFormatted.replace("<value>", value);

		return toBeFormatted;
	}

	// use LinkedHashMap if you want to read values from the hashmap in the same order as you put them into it
	public static String[] getCombinedValueAt(LinkedHashMap<String, String> hashMap, int index)
	{
		Map.Entry<String, String> entry = (Map.Entry<String, String>) hashMap.entrySet().toArray()[index];
		String result = entry.getKey() + ";" + entry.getValue();
		return result.split(";");
	}
}
