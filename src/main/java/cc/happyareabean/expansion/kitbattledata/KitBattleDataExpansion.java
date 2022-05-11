package cc.happyareabean.expansion.kitbattledata;

import lombok.Getter;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Taskable;
import me.wazup.kitbattle.KitbattleAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
public class KitBattleDataExpansion extends PlaceholderExpansion implements Configurable, Cacheable, Taskable {

    @Getter private static KitBattleDataExpansion instance;
    private BukkitTask fetchTask;
    private KitBattleData kitBattleData;

    private int amount;

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor() {
        return "HappyAreaBean";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier() {
        return "kitbattledata";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "KitBattle";
    }

    @Override
    public Map<String, Object> getDefaults() {

        LinkedHashMap<String, Object> defaults = new LinkedHashMap<>();

        defaults.put("emptyPlayers", "---");
        defaults.put("format", "&e<position>. &b<player> &e- &e<value>");
        defaults.put("refreshInterval", 60);
        defaults.put("amount", 10);

        return defaults;
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player     A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param identifier A String containing the identifier/value.
     * @return Possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        String[] split = identifier.split("_");
        String type = split[0];
        int number = (split.length > 1) ? Integer.parseInt(split[1]) : 0;
        if (number == 0 || number > 10) return null;

        String[] data;
        String format = this.getString("format", "&e<position>. &b<player> &e- &e<value>");
        HashMap<KitbattleAPI.Stat, LinkedHashMap<String, String>> stats = kitBattleData.getStats();

        // %kitbattledata_kills_<number>%
        switch (type) {
            case "coins":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.COINS), number - 1);
                break;
            case "killstreaks":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.KILLSTREAKS_EARNED), number - 1);
                break;
            case "kills":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.KILLS), number - 1);
                break;
            case "tournament-wins":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.TOURNAMENT_WINS), number - 1);
                break;
            case "challenge-wins":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.CHALLENGE_WINS), number - 1);
                break;
            case "abilities-used":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.ABILITIES_USED), number - 1);
                break;
            case "elo":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.ELO), number - 1);
                break;
            case "deaths":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.DEATHS), number - 1);
                break;
            case "exp":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.EXP), number - 1);
                break;
            case "projectiles-hit":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.PROJECTILES_HIT), number - 1);
                break;
            case "soups-eaten":
                data = Utils.getCombinedValueAt(stats.get(KitbattleAPI.Stat.SOUPS_EATEN), number - 1);
                break;
            default:
                data = null;
                break;
        }

        // We return null if an invalid placeholder (f.e. %kitbattledata_placeholder3%)
        // was provided
        return data == null ? null : Utils.toFormatted(format, number, data[0], data[1]);
    }

    @Override
    public void clear() {
        kitBattleData.clear();
        fetchTask.cancel();
    }

    @Override
    public void start() {
        instance = this;
        this.amount = this.getInt("amount", 10);

        kitBattleData = new KitBattleData();
        fetchTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlaceholderAPI(), new KitBattleDataTask(), 1, TimeUnit.MINUTES.toSeconds(this.getInt("refreshInterval", 60)) * 20);
    }

    @Override
    public void stop() {

    }
}
