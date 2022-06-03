package space.devport.globalfund.commands;

import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.MainCommand;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.subcommands.admin.*;

public class GlobalFundCommand extends MainCommand {

    public GlobalFundCommand(GlobalFundPlugin plugin) {
        super(plugin, "globalfund");
        craftPermission();

        withSubCommand(new Add(plugin));
        withSubCommand(new Complete(plugin));
        withSubCommand(new Reload(plugin));
        withSubCommand(new Remove(plugin));
        withSubCommand(new Reset(plugin));
        withSubCommand(new SetActive(plugin));
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Displays this.";
    }
}