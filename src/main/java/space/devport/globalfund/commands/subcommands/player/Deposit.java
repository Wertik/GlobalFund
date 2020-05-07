package space.devport.globalfund.commands.subcommands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Deposit extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Deposit() {
        super("deposit");
        this.plugin = GlobalFundPlugin.getInstance();

        setAliases("dep", "donate", "give");
        this.preconditions = new Preconditions()
                .permissions("globalfund.deposit")
                .playerOnly();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (!CommandUtils.checkActiveGoal(sender)) return CommandResult.FAILURE;

        CurrencyType currencyType = CommandUtils.checkCurrency(sender, args[0]);
        if (currencyType == null || CommandUtils.checkComplete(sender, currencyType)) return CommandResult.FAILURE;

        double amount = CommandUtils.checkAmount(sender, args[1]);
        if (amount == -1) return CommandResult.FAILURE;

        if (currencyType.getProvider().getBalance((Player) sender) < amount) {
            language.sendPrefixed(sender, "Deposit.Not-Enough");
            return CommandResult.FAILURE;
        }

        if (!plugin.getMilestoneManager().deposit((Player) sender, currencyType, amount)) {
            language.sendPrefixed(sender, "Deposit.Could-Not");
            return CommandResult.FAILURE;
        }

        language.getPrefixed("Deposit.Done")
                .replace("%amount%", String.valueOf(amount))
                .replace("%type%", currencyType.getName())
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
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
        return "/%label% deposit <currency> <amount>";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Deposit an amount of currency to currently active milestone.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(2);
    }
}