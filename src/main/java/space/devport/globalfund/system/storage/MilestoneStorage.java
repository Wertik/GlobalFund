package space.devport.globalfund.system.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.globalfund.system.struct.MilestoneData;

import java.util.Map;

public interface MilestoneStorage {

    void delete(@Nullable String name);

    void clear();

    @Nullable
    MilestoneData load(@Nullable String name);

    void save(@Nullable MilestoneData data);

    @NotNull
    Map<String, MilestoneData> loadAll();

    void saveAll(@Nullable Map<String, MilestoneData> dataMap);
}