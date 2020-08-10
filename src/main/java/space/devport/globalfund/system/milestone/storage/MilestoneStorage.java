package space.devport.globalfund.system.milestone.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.globalfund.system.milestone.struct.MilestoneData;

import java.util.Map;

public interface MilestoneStorage {

    void delete(@Nullable String name);

    void clear();

    void save(@Nullable MilestoneData data);

    @NotNull
    Map<String, MilestoneData> loadAll();

    void saveAll(@Nullable Map<String, MilestoneData> dataMap);
}