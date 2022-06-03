package space.devport.globalfund.commands.subcommands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.SubCommand;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.globalfund.GlobalFundPlugin;

import java.util.ArrayList;
import java.util.List;

public class SetActive extends SubCommand {

    private final GlobalFundPlugin plugin;

    public SetActive(GlobalFundPlugin plugin) {
        super(plugin, "setactive");
        this.plugin = plugin;

        setPermissions("globalfund.admin");
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {

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
    public List<String> requestTabComplete(@NotNull CommandSender sender, String[] args) {
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