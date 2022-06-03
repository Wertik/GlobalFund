package space.devport.globalfund;

import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import space.devport.dock.DockedPlugin;
import space.devport.dock.UsageFlag;
import space.devport.dock.configuration.Configuration;
import space.devport.globalfund.commands.GlobalFundCommand;
import space.devport.globalfund.listeners.PlayerListener;
import space.devport.globalfund.record.RecordManager;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.milestone.MilestoneManager;
import space.devport.globalfund.system.storage.FileStorage;
import space.devport.globalfund.system.storage.MongoStorage;

@Log
public class GlobalFundPlugin extends DockedPlugin {

    @Getter
    private static GlobalFundPlugin instance;

    @Getter
    private MilestoneManager milestoneManager;

    @Getter
    private RecordManager recordManager;

    @Getter
    private Configuration data;

    @Override
    public void onPluginEnable() {
        GlobalFundPlugin.instance = this;

        this.data = new Configuration(this, "data");

        new GlobalFundLanguage(this).register();

        this.milestoneManager = new MilestoneManager();
        milestoneManager.loadPresets();

        this.recordManager = new RecordManager();

        initStorage();

        milestoneManager.loadData();
        recordManager.loadData();

        setupPlaceholderAPI();

        registerMainCommand(new GlobalFundCommand(this));
        registerListener(new PlayerListener(this));

        // Check Currency providers after everything is loaded.
        Bukkit.getScheduler().runTaskLater(this, () -> {
            log.fine("Looking for deps..");
            for (CurrencyType type : CurrencyType.values())
                type.getProvider().onLoad();
        }, 1L);
    }

    private void initStorage() {
        String type = configuration.getFileConfiguration().getString("storage.type");
        if (type == null) type = "file";
        switch (type.toLowerCase()) {
            case "mongo":
                MongoStorage mongoStorage = new MongoStorage();
                mongoStorage.init();

                milestoneManager.setStorage(mongoStorage);
                recordManager.setStorage(mongoStorage);
                break;
            case "file":
                FileStorage fileStorage = new FileStorage();

                milestoneManager.setStorage(fileStorage);
                recordManager.setStorage(fileStorage);
                break;
        }
        log.info("Using " + type + " storage.");
    }

    @Override
    public void onPluginDisable() {
        milestoneManager.saveData();
    }

    @Override
    public void onReload() {
        milestoneManager.getConfiguration().load();
        milestoneManager.loadPresets();

        milestoneManager.saveData();
        recordManager.saveData();

        initStorage();

        recordManager.saveData();
        milestoneManager.saveData();

        log.fine("Looking for deps..");
        for (CurrencyType type : CurrencyType.values())
            type.getProvider().onLoad();

        setupPlaceholderAPI();
    }

    @Override
    public UsageFlag[] usageFlags() {
        return new UsageFlag[0];
    }

    private void setupPlaceholderAPI() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            log.info("Found PAPI! &aRegistering our expansion..");
            new GlobalFundExpansion(this).register();
        }
    }
}
