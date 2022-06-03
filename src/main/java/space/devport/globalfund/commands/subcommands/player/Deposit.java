package space.devport.globalfund.commands.subcommands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.SubCommand;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.dock.commands.struct.Preconditions;
import space.devport.dock.text.message.Message;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.commands.CommandUtils;
import space.devport.globalfund.system.currency.CurrencyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Deposit extends SubCommand {

    private final GlobalFundPlugin plugin;

    public Deposit(GlobalFundPlugin plugin) {
        super(plugin, "deposit");
        this.plugin = plugin;

        setAliases("dep", "donate", "give");
        getPreconditions().permissions("globalfund.deposit").playerOnly();
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {

        if (!CommandUtils.checkActiveGoal(sender)) return CommandResult.FAILURE;

        CurrencyType currencyType = CommandUtils.checkCurrency(sender, args[0]);
        if (currencyType == null || !CommandUtils.checkProvider(sender, currencyType) || CommandUtils.checkComplete(sender, currencyType))
            return CommandResult.FAILURE;

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

        Message broadcast = language.getPrefixed("Broadcasts.Deposit")
                .replace("%player%", sender.getName())
                .replace("%type%", currencyType.getName())
                .replace("%amount%", amount)
                .replace("%actual%", plugin.getMilestoneManager().getActiveData().getCurrencyAmount(currencyType))
                .replace("%goal%", plugin.getMilestoneManager().getActivePreset().getRequirements().get(currencyType))
                .replace("%remaining%", plugin.getMilestoneManager().getRemaining(currencyType));

        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            broadcast.send(loopPlayer);
        }

        language.getPrefixed("Deposit.Done")
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