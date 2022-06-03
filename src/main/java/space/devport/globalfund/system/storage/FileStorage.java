package space.devport.globalfund.system.storage;

import com.google.common.base.Strings;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.record.storage.RecordStorage;
import space.devport.globalfund.record.struct.PlayerRecord;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.milestone.storage.MilestoneStorage;
import space.devport.globalfund.system.milestone.struct.MilestoneData;
import space.devport.utils.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileStorage implements MilestoneStorage, RecordStorage {

    private final Configuration data;

    public FileStorage() {
        this.data = GlobalFundPlugin.getInstance().getData();
    }

    @Override
    public void delete(@Nullable String name) {
        if (Strings.isNullOrEmpty(name)) return;
        ConfigurationSection section = checkSection();
        section.set(name, null);
        this.data.save();
    }

    @Override
    public void save(@Nullable MilestoneData data) {

        ConfigurationSection section = checkSection();

        if (data == null)
            return;

        ConfigurationSection dataSection = section.getConfigurationSection(data.getName());

        if (dataSection == null)
            dataSection = section.createSection(data.getName());

        for (Map.Entry<CurrencyType, Double> entry : data.getCurrencyStorage().getAmounts().entrySet()) {
            dataSection.set(entry.getKey().toString(), entry.getValue());
        }

        dataSection.set("completed", data.isCompleted());

        this.data.save();
    }

    @Override
    public void clear() {
        data.getFileConfiguration().set("milestone-data", null);
    }

    @Override
    public @NotNull Map<String, MilestoneData> loadAll() {

        Map<String, MilestoneData> dataMap = new HashMap<>();

        ConfigurationSection section = checkSection();

        for (String key : section.getKeys(false)) {
            ConfigurationSection keySection = section.getConfigurationSection(key);

            if (keySection == null) continue;

            MilestoneData data = new MilestoneData();

            for (String currencyKey : keySection.getKeys(false)) {
                CurrencyType type = CurrencyType.fromString(currencyKey);
                if (type == null) continue;
                data.set(type, keySection.getDouble(currencyKey));
            }

            data.setCompleted(section.getBoolean("completed", false));
            dataMap.put(key, data);
        }

        return dataMap;
    }

    @Override
    public void saveAll(@Nullable Map<String, MilestoneData> dataMap) {

        data.clear();
        if (dataMap == null) return;

        ConfigurationSection section = checkSection();

        for (Map.Entry<String, MilestoneData> entry : dataMap.entrySet()) {
            ConfigurationSection dataSection = section.createSection(entry.getKey());

            for (Map.Entry<CurrencyType, Double> currencyEntry : entry.getValue().getCurrencyStorage().getAmounts().entrySet()) {
                dataSection.set(currencyEntry.getKey().toString(), currencyEntry.getValue());
            }

            dataSection.set("completed", entry.getValue().isCompleted());
        }

        data.save();
    }

    private ConfigurationSection checkSection() {
        ConfigurationSection section = data.getFileConfiguration().getConfigurationSection("milestone-data");
        return section != null ? section : data.getFileConfiguration().createSection("milestone-data");
    }

    @Override
    public PlayerRecord loadRecord(UUID uuid) {
        throw new NotImplementedException("Not implemented, too lazy");
    }

    @Override
    public void deleteRecord(UUID uuid) {
        throw new NotImplementedException("Not implemented, too lazy");
    }

    @Override
    public void saveRecord(PlayerRecord record) {
        throw new NotImplementedException("Not implemented, too lazy");
    }

    @Override
    public void saveAllRecords(Map<UUID, PlayerRecord> dataMap) {
        throw new NotImplementedException("Not implemented, too lazy");
    }

    @Override
    public Map<UUID, PlayerRecord> loadAllRecords() {
        throw new NotImplementedException("Not implemented, too lazy");
    }
}