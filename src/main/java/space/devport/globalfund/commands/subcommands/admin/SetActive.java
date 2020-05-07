package space.devport.globalfund.commands.subcommands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class SetActive extends SubCommand {

    private final GlobalFundPlugin plugin;

    public SetActive() {
        super("setactive");
        this.plugin = GlobalFundPlugin.getInstance();

        this.preconditions = new Preconditions().permissions("globalfund.admin");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (!plugin.getMilestoneManager().getMilestonePresets().containsKey(args[0])) {
            language.getPrefixed("Invalid-Milestone")
                    .replace("%param%", args[0])
                    .send(sender);
            return CommandResult.FAILURE;
        }

        plugin.getMilestoneManager().setActiveMilestone(args[0]);
        language.getPrefixed("Set-Active")
                .replace("%activeGoal%", plugin.getMilestoneManager().getActiveMilestone())
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0)
            return new ArrayList<>(plugin.getMilestoneManager().getMilestonePresets().keySet());
        return new ArrayList<>();
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% setactive <milestone>";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Change the currently active milestone.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}