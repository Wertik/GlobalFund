package space.devport.globalfund.system.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.record.storage.RecordStorage;
import space.devport.globalfund.record.storage.dao.PlayerRecordDAO;
import space.devport.globalfund.record.struct.PlayerRecord;
import space.devport.globalfund.system.milestone.storage.MilestoneStorage;
import space.devport.globalfund.system.milestone.storage.dao.MilestoneDataDAO;
import space.devport.globalfund.system.milestone.struct.MilestoneData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MongoStorage implements MilestoneStorage, RecordStorage {

    private final GlobalFundPlugin plugin;

    private MilestoneDataDAO milestoneDataDAO;

    private PlayerRecordDAO playerRecordDAO;

    public MongoStorage() {
        this.plugin = GlobalFundPlugin.getInstance();
    }

    public void init() {
        String host = plugin.getConfig().getString("storage.mongo.host", "localhost");
        int port = plugin.getConfig().getInt("storage.mongo.port", 27017);

        ServerAddress address = new ServerAddress(host, port);

        MongoClient mongoClient;
        if (plugin.getConfig().getBoolean("storage.mongo.use-credentials")) {
            String user = plugin.getConfig().getString("storage.mongo.user");
            String authDB = plugin.getConfig().getString("storage.mongo.auth-db", "admin");
            String pass = plugin.getConfig().getString("storage.mongo.pass");

            Objects.requireNonNull(user, "You have to provide a MongoDB user.");
            Objects.requireNonNull(authDB, "You have to provide a MongoDB auth database.");
            Objects.requireNonNull(pass, "You have to provide a MongoDB password.");

            mongoClient = new MongoClient(address,
                    MongoCredential.createScramSha1Credential(user, authDB, pass.toCharArray()),
                    MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(address);
        }

        Morphia morphia = new Morphia();

        Datastore dataStore = morphia.createDatastore(mongoClient, plugin.getConfig().getString("storage.mongo.database", "globalfund"));
        dataStore.ensureIndexes();

        milestoneDataDAO = new MilestoneDataDAO(MilestoneData.class, dataStore);
        playerRecordDAO = new PlayerRecordDAO(PlayerRecord.class, dataStore);
    }

    @Override
    public void delete(@Nullable String name) {
        Query<MilestoneData> query = milestoneDataDAO.createQuery().field("name").equal(name);
        milestoneDataDAO.deleteByQuery(query);
    }

    @Override
    public void clear() {
        milestoneDataDAO.getCollection().drop();
    }

    @Override
    public void save(@Nullable MilestoneData data) {
        milestoneDataDAO.save(data);
    }

    @Override
    public @NotNull Map<String, MilestoneData> loadAll() {
        Map<String, MilestoneData> dataMap = new HashMap<>();
        for (MilestoneData data : milestoneDataDAO.find())
            dataMap.put(data.getName(), data);
        return dataMap;
    }

    @Override
    public void saveAll(@Nullable Map<String, MilestoneData> dataMap) {
        if (dataMap == null) return;
        for (MilestoneData data : dataMap.values())
            save(data);
    }

    @Override
    public PlayerRecord loadRecord(UUID uuid) {
        return playerRecordDAO.findOne("uuid", uuid);
    }

    @Override
    public void deleteRecord(UUID uuid) {
        Query<PlayerRecord> query = playerRecordDAO.createQuery().field("uuid").equal(uuid);
        playerRecordDAO.deleteByQuery(query);
    }

    @Override
    public void saveRecord(PlayerRecord record) {
        playerRecordDAO.save(record);
    }

    @Override
    public void saveAllRecords(Map<UUID, PlayerRecord> dataMap) {
        if (dataMap == null) return;
        for (PlayerRecord data : dataMap.values())
            saveRecord(data);
    }

    @Override
    public Map<UUID, PlayerRecord> loadAllRecords() {
        Map<UUID, PlayerRecord> dataMap = new HashMap<>();
        for (PlayerRecord data : playerRecordDAO.find())
            dataMap.put(data.getUuid(), data);
        return dataMap;
    }
}
