package space.devport.globalfund.system.milestone;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.milestone.storage.MilestoneStorage;
import space.devport.globalfund.system.milestone.struct.MilestoneData;
import space.devport.globalfund.system.milestone.struct.MilestonePreset;
import space.devport.globalfund.system.milestone.struct.MilestoneRequirements;
import space.devport.globalfund.system.milestone.struct.MilestoneRewards;
import space.devport.utils.configuration.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MilestoneManager {

    private final GlobalFundPlugin plugin;

    @Getter
    @Setter
    private String activeMilestone;

    @Getter
    private final Map<String, MilestonePreset> milestonePresets = new HashMap<>();

    @Getter
    private final Map<String, MilestoneData> milestoneData = new HashMap<>();

    @Getter
    private final Configuration configuration;

    @Getter
    @Setter
    private MilestoneStorage storage;

    public MilestoneManager() {
        this.plugin = GlobalFundPlugin.getInstance();
        configuration = new Configuration(plugin, "milestones");
    }

    public void loadPresets() {
        milestonePresets.clear();

        for (String key : configuration.getFileConfiguration().getKeys(false)) {
            MilestonePreset preset = new MilestonePreset(key);

            ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(key);

            if (section == null || !section.contains("requirements")) continue;

            preset.setDisplayName(section.getString("display", key));
            preset.setRepeat(section.getBoolean("repeat", false));

            preset.setRewards(new MilestoneRewards(configuration.getRewards(key + ".rewards")));

            MilestoneRequirements requirements = new MilestoneRequirements();

            ConfigurationSection reqSection = section.getConfigurationSection("requirements");
            if (reqSection != null)
                for (String currencyKey : reqSection.getKeys(false)) {
                    CurrencyType type = CurrencyType.fromString(currencyKey);
                    if (type == null) continue;

                    requirements.set(type, reqSection.getDouble(currencyKey));
                }
            preset.setRequirements(requirements);

            milestonePresets.put(key, preset);
        }

        plugin.getConsoleOutput().info("Loaded " + milestonePresets.size() + " milestone preset(s)...");
    }

    public void loadData() {
        if (storage == null) return;
        milestoneData.clear();
        milestoneData.putAll(storage.loadAll());
        plugin.getConsoleOutput().info("Loaded " + milestoneData.size() + " milestone data set(s)...");

        Configuration data = new Configuration(plugin, "data");
        activeMilestone = data.getFileConfiguration().getString("active-milestone");

        if (!milestonePresets.containsKey(activeMilestone))
            plugin.getConsoleOutput().warn("Active milestone that was set no longer exists.");
    }

    public void saveData() {
        if (storage == null) return;
        storage.saveAll(milestoneData);

        Configuration data = new Configuration(plugin, "data");
        data.getFileConfiguration().set("active-milestone", activeMilestone);
        data.save();
    }

    public void clearMilestoneData(String name) {
        storage.delete(name);
        milestoneData.remove(name);
    }

    public MilestonePreset getPreset(String name) {
        return milestonePresets.getOrDefault(name, null);
    }

    public MilestoneData getData(String name) {
        MilestoneData data = milestoneData.getOrDefault(name, null);

        if (data == null && milestonePresets.containsKey(name)) {
            data = new MilestoneData(name);
            milestoneData.put(name, data);
        }

        return data;
    }

    public boolean complete() {
        for (CurrencyType type : getActivePreset().getRequirements().getCurrency().keySet()) {
            if (getActiveData().getCurrencyAmount(type) < getActivePreset().getRequirements().get(type))
                if (!add(type, getRemaining(type))) return false;
        }
        return true;
    }

    /**
     * Add Currency to the active milestone.
     */
    public boolean add(CurrencyType type, double amount) {
        MilestoneData data = getData(activeMilestone);
        if (data == null) return false;

        amount = Math.min(amount, getRemaining(type));

        data.add(type, amount);
        checkGoals(null);
        return true;
    }

    public boolean subtract(CurrencyType type, double amount) {
        MilestoneData data = getData(activeMilestone);
        if (data == null) return false;

        data.remove(type, amount);
        checkGoals(null);
        return true;
    }

    /**
     * Make a deposit for a player.
     */
    public boolean deposit(Player player, CurrencyType type, double amount) {

        amount = Math.min(amount, getRemaining(type));

        if (!type.getProvider().withdraw(player, amount)) return false;

        add(type, amount);
        checkGoals(player);

        // Add to players donation log
        plugin.getRecordManager().getRecord(player).addDonate(activeMilestone, type, amount);

        return true;
    }

    public void checkGoals(@Nullable Player player) {
        MilestoneData data = getData(activeMilestone);
        MilestonePreset preset = getPreset(activeMilestone);

        if (data == null || preset == null || data.isCompleted()) return;

        if (Arrays.stream(CurrencyType.values()).anyMatch(loopType -> data.getCurrencyAmount(loopType) < preset.getRequirements().get(loopType)))
            return;

        preset.getRewards().give(player);
        data.setCompleted(true);

        if (preset.isRepeat()) {
            data.clearAll();
            data.setCompleted(false);
        }
    }

    public double getRemaining(CurrencyType type) {
        MilestoneData data = getData(activeMilestone);
        MilestonePreset preset = getPreset(activeMilestone);

        if (data == null || preset == null) return 0;

        return preset.getRequirements().get(type) - data.getCurrencyAmount(type);
    }

    public double getValue(CurrencyType type) {
        return getActiveData() != null ? getActiveData().getCurrencyAmount(type) : 0;
    }

    public MilestonePreset getActivePreset() {
        return getPreset(activeMilestone);
    }

    public MilestoneData getActiveData() {
        return getData(activeMilestone);
    }

    public boolean requiresCurrency(CurrencyType type) {
        return getActiveData().getCurrencyAmount(type) < getActivePreset().getRequirements().get(type);
    }
}