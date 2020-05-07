package space.devport.globalfund.commands.subcommands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;

public class Complete extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Complete() {
        super("complete");
        this.plugin = GlobalFundPlugin.getInstance();

        this.preconditions = new Preconditions().permissions("globalfund.admin");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        if (!CommandUtils.checkActiveGoal(sender)) return CommandResult.FAILURE;

        if (CommandUtils.checkComplete(sender, null)) return CommandResult.FAILURE;

        if (!plugin.getMilestoneManager().complete()) {
            language.sendPrefixed(sender, "Could-Not-Complete");
            return CommandResult.FAILURE;
        }
        language.sendPrefixed(sender, "Completed");
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% complete";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Complete the currently active milestone.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}