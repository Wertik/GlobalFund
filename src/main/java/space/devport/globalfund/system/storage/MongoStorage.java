package space.devport.globalfund.system.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.system.storage.dao.MilestoneDataDAO;
import space.devport.globalfund.system.struct.MilestoneData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MongoStorage implements MilestoneStorage {

    private final GlobalFundPlugin plugin;

    private MilestoneDataDAO milestoneDataDAO;

    public MongoStorage() {
        this.plugin = GlobalFundPlugin.getInstance();
    }

    public void init() {
        String host = plugin.getConfig().getString("storage.mongo.host", "localhost");
        int port = plugin.getConfig().getInt("storage.mongo.port", 27017);

        ServerAddress address = new ServerAddress(host, port);

        MongoClient mongoClient;
        if (plugin.getConfig().getBoolean("storage.mongo.use-credentials")) {
            mongoClient = new MongoClient(address, Collections.singletonList(
                    MongoCredential.createScramSha1Credential(plugin.getConfig().getString("storage.mongo.user"),
                            Objects.requireNonNull(plugin.getConfig().getString("storage.mongo.auth-db", "admin")),
                            plugin.getConfig().getString("storage.mongo.pass").toCharArray())));
        } else
            mongoClient = new MongoClient(address);

        Morphia morphia = new Morphia();

        Datastore dataStore = morphia.createDatastore(mongoClient, plugin.getConfig().getString("storage.mongo.database", "globalfund"));
        dataStore.ensureIndexes();

        milestoneDataDAO = new MilestoneDataDAO(MilestoneData.class, dataStore);
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
    public @Nullable MilestoneData load(@Nullable String name) {
        return milestoneDataDAO.findOne("name", name);
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
}
