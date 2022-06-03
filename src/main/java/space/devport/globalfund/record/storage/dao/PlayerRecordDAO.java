package space.devport.globalfund.record.storage.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import space.devport.globalfund.record.struct.PlayerRecord;

public class PlayerRecordDAO extends BasicDAO<PlayerRecord, String> {
    public PlayerRecordDAO(Class<PlayerRecord> entityClass, Datastore ds) {
        super(entityClass, ds);
    }
}