package space.devport.globalfund.commands.subcommands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.SubCommand;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;
import space.devport.globalfund.system.currency.CurrencyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Remove extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Remove(GlobalFundPlugin plugin) {
        super(plugin, "remove");
        this.plugin = plugin;

        setPermissions("globalfund.admin");
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {

        if (!CommandUtils.checkActiveGoal(sender)) return CommandResult.FAILURE;

        CurrencyType currencyType = CommandUtils.checkCurrency(sender, args[0]);
        if (currencyType == null || CommandUtils.checkComplete(sender, currencyType)) return CommandResult.FAILURE;

        double amount = CommandUtils.checkAmount(sender, args[1]);
        if (amount == -1) return CommandResult.FAILURE;

        plugin.getMilestoneManager().subtract(currencyType, amount);
        language.getPrefixed("Removed")
                .replace("%amount%", String.valueOf(amount))
                .replace("%type%", currencyType.getName())
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(@NotNull CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0)
            suggestions = Arrays.stream(CurrencyType.values()).map(e -> e.toString().toLowerCase()).collect(Collectors.toList());
        else if (args.length == 1) {
            CurrencyType type = CurrencyType.fromString(args[0]);
            if (type != null)
                suggestions.add(String.valueOf(plugin.getMilestoneManager().getRemaining(type)));
        }

        return suggestions;
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% remove <currency> <amount>";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Remove an amount of currency from currently active milestone.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(2);
    }
}