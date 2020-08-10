package space.devport.globalfund;

import lombok.Getter;
import org.bukkit.Bukkit;
import space.devport.globalfund.commands.GlobalFundCommand;
import space.devport.globalfund.commands.subcommands.admin.*;
import space.devport.globalfund.commands.subcommands.player.Deposit;
import space.devport.globalfund.commands.subcommands.player.Donated;
import space.devport.globalfund.commands.subcommands.player.Status;
import space.devport.globalfund.record.RecordManager;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.milestone.MilestoneManager;
import space.devport.globalfund.system.storage.FileStorage;
import space.devport.globalfund.system.storage.MongoStorage;
import space.devport.utils.DevportPlugin;
import space.devport.utils.configuration.Configuration;

public class GlobalFundPlugin extends DevportPlugin {

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
        instance = this;
        setInstance(this);

        data = new Configuration(this, "data");

        new GlobalFundLanguage();

        milestoneManager = new MilestoneManager();
        milestoneManager.loadPresets();

        recordManager = new RecordManager();

        initStorage();
        milestoneManager.loadData();
        recordManager.loadData();

        setupPlaceholderAPI();

        addMainCommand(new GlobalFundCommand())
                .addSubCommand(new Deposit())
                .addSubCommand(new Reload())
                .addSubCommand(new Complete())
                .addSubCommand(new Add())
                .addSubCommand(new Reset())
                .addSubCommand(new SetActive())
                .addSubCommand(new Remove())
                .addSubCommand(new Status())
                .addSubCommand(new Donated());

        // Check Currency providers after everything is loaded.
        Bukkit.getScheduler().runTaskLater(this, () -> {
            consoleOutput.debug("Looking for deps..");
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
        consoleOutput.info("Using " + type + " storage.");
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

        consoleOutput.debug("Looking for deps..");
        for (CurrencyType type : CurrencyType.values())
            type.getProvider().onLoad();

        setupPlaceholderAPI();
    }

    @Override
    public boolean useLanguage() {
        return true;
    }

    @Override
    public boolean useHolograms() {
        return false;
    }

    @Override
    public boolean useMenus() {
        return false;
    }

    private void setupPlaceholderAPI() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            consoleOutput.info("Found PAPI! &aRegistering our expansion..");
            new GlobalFundExpansion().register();
        }
    }
}
