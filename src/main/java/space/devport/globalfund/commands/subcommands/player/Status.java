package space.devport.globalfund.commands.subcommands.player;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.SubCommand;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.dock.text.message.Message;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;
import space.devport.globalfund.system.currency.CurrencyType;

public class Status extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Status(GlobalFundPlugin plugin) {
        super(plugin, "status");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {

        if (!CommandUtils.checkActiveGoal(sender)) return CommandResult.FAILURE;

        Message status = language.get("Status.Header")
                .replace("%milestoneDisplay%", plugin.getMilestoneManager().getActivePreset().getDisplayName())
                .replace("%completed%", plugin.getMilestoneManager().getActiveData().isCompleted());

        for (CurrencyType type : CurrencyType.values()) {
            status.append(language.get("Status.Currency-Line")
                    .replace("%currencyName%", type.getName())
                    .replace("%currencyActual%", plugin.getMilestoneManager().getActiveData().getCurrencyAmount(type))
                    .replace("%currencyGoal%", plugin.getMilestoneManager().getActivePreset().getRequirements().get(type)));
        }

        status.append(language.get("Status.Footer"))
                .replace("%milestoneDisplay%", plugin.getMilestoneManager().getActivePreset().getDisplayName())
                .replace("%completed%", plugin.getMilestoneManager().getActiveData().isCompleted())
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% status";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Display progression of the currently active milestone.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}