package space.devport.globalfund.commands.subcommands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;

public class Reset extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Reset() {
        super("reset");
        this.plugin = GlobalFundPlugin.getInstance();

        this.preconditions = new Preconditions().permissions("globalfund.admin");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
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