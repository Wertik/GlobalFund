package space.devport.globalfund.system.milestone.storage.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import space.devport.globalfund.system.milestone.struct.MilestoneData;

public class MilestoneDataDAO extends BasicDAO<MilestoneData, String> {
    public MilestoneDataDAO(Class<MilestoneData> entityClass, Datastore ds) {
        super(entityClass, ds);
    }
}