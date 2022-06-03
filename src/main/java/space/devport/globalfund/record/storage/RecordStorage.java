package space.devport.globalfund.record.storage;

import space.devport.globalfund.record.struct.PlayerRecord;

import java.util.Map;
import java.util.UUID;

public interface RecordStorage {

    PlayerRecord loadRecord(UUID uuid);

    void deleteRecord(UUID uuid);

    void saveRecord(PlayerRecord record);

    void saveAllRecords(Map<UUID, PlayerRecord> dataMap);

    Map<UUID, PlayerRecord> loadAllRecords();
}