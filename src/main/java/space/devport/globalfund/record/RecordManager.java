package space.devport.globalfund.record;

import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.globalfund.record.storage.RecordStorage;
import space.devport.globalfund.record.struct.PlayerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log
public class RecordManager {

    @Setter
    private RecordStorage storage;

    private final Map<UUID, PlayerRecord> records = new HashMap<>();

    public RecordManager() {
    }

    public void saveData() {
        if (storage == null) return;
        try {
            storage.saveAllRecords(records);
        } catch (NotImplementedException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        if (storage == null) return;
        records.clear();

        loadOnline();

        log.info("Loaded " + records.size() + " milestone data set(s)...");
    }

    @NotNull
    public PlayerRecord getRecord(OfflinePlayer offlinePlayer) {
        return getRecord(offlinePlayer.getUniqueId());
    }

    @NotNull
    public PlayerRecord getRecord(UUID uuid) {
        PlayerRecord record = records.getOrDefault(uuid, loadRecord(uuid));
        return record != null ? record : createRecord(uuid);
    }

    @NotNull
    public PlayerRecord createRecord(UUID uuid) {
        PlayerRecord record = new PlayerRecord(uuid);
        records.put(uuid, record);
        return record;
    }

    @Nullable
    public PlayerRecord loadRecord(UUID uuid) {
        return storage.loadRecord(uuid);
    }

    public void loadOnline() {
        for (Player player : Bukkit.getOnlinePlayers())
            loadRecord(player.getUniqueId());
    }

    public void deleteRecord(UUID uuid) {
        records.remove(uuid);
        storage.deleteRecord(uuid);
    }

    public void unloadRecord(UUID uuid) {
        PlayerRecord record = getRecord(uuid);

        // Save only when the record is not empty, otherwise we can just create a new one when needed.
        if (!record.isEmpty())
            storage.saveRecord(record);

        records.remove(uuid);
    }
}