package space.devport.globalfund.commands.subcommands.player;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.text.message.Message;

public class Status extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Status() {
        super("status");
        this.plugin = GlobalFundPlugin.getInstance();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

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