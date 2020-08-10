package space.devport.globalfund.system.milestone.struct;

import lombok.Getter;
import lombok.Setter;

public class MilestonePreset {

    @Getter
    private final String name;

    @Getter
    @Setter
    private String displayName;

    @Getter
    @Setter
    private boolean repeat;

    @Getter
    @Setter
    private MilestoneRequirements requirements;

    @Getter
    @Setter
    private MilestoneRewards rewards;

    public MilestonePreset(String name) {
        this.name = name;
    }
}