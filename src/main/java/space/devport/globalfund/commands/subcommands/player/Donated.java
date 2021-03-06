package space.devport.globalfund.commands.subcommands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.SubCommand;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.dock.text.message.Message;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.record.struct.PlayerRecord;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.struct.CurrencyStorage;

import java.util.Map;

public class Donated extends SubCommand {

    public Donated(GlobalFundPlugin plugin) {
        super(plugin, "donated");

        getPreconditions()
                .permissions("globalfund.donated")
                .playerOnly();
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Player player = (Player) sender;

        PlayerRecord record = GlobalFundPlugin.getInstance().getRecordManager().getRecord(player);

        Message header = language.get("Donated.Header");

        if (record.getDonated().isEmpty()) {
            header.append(language.get("Donated.No-History"));
        } else {
            String active = language.get("Donated.Placeholders.Active").color().toString();
            String notActive = language.get("Donated.Placeholders.Not-Active").color().toString();

            for (Map.Entry<String, CurrencyStorage> currencyStorage : record.getDonated().entrySet()) {
                Message recordHeader = language.get("Donated.Record-Header")
                        .replace("%milestoneName%", currencyStorage.getKey())
                        .replace("%activeOrNot%", GlobalFundPlugin.getInstance().getMilestoneManager().getActiveMilestone().equals(currencyStorage.getKey()) ?
                                active : notActive);

                for (Map.Entry<CurrencyType, Double> currency : currencyStorage.getValue().getAmounts().entrySet()) {
                    Message currencyLine = language.get("Donated.Record-Currency-Line")
                            .replace("%currencyName%", currency.getKey().getName())
                            .replace("%donatedAmount%", currency.getValue());
                    recordHeader.append(currencyLine);
                }

                header.append(recordHeader);
            }
        }

        Message footer = language.get("Donated.Footer");
        header.append(footer);

        header.send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% donated";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Display how much you've donated so far.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}