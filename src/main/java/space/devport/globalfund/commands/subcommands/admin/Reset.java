package space.devport.globalfund.commands.subcommands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.SubCommand;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;

public class Reset extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Reset(GlobalFundPlugin plugin) {
        super(plugin, "reset");
        this.plugin = plugin;

        setPermissions("globalfund.admin");
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (!CommandUtils.checkActiveGoal(sender)) return CommandResult.FAILURE;

        plugin.getMilestoneManager().getActiveData().clearAll();
        plugin.getMilestoneManager().getActiveData().setCompleted(false);

        language.sendPrefixed(sender, "Reset");
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% reset";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Reset the currently active milestone.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}