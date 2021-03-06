package space.devport.globalfund.commands;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import space.devport.dock.text.language.LanguageManager;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.milestone.struct.MilestoneData;
import space.devport.globalfund.system.milestone.struct.MilestonePreset;

@UtilityClass
public class CommandUtils {

    public boolean checkProvider(CommandSender sender, CurrencyType type) {
        if (!type.hasProvider()) {
            GlobalFundPlugin.getInstance().getManager(LanguageManager.class).sendPrefixed(sender, "No-Provider");
            return false;
        }
        return true;
    }

    public CurrencyType checkCurrency(CommandSender sender, String arg) {
        CurrencyType currencyType = CurrencyType.fromString(arg);
        if (currencyType == null) {
            GlobalFundPlugin.getInstance().getManager(LanguageManager.class)
                    .getPrefixed("Currency-Invalid")
                    .replace("%param%", arg)
                    .send(sender);
            return null;
        }
        return currencyType;
    }

    public boolean checkComplete(CommandSender sender, CurrencyType type) {
        MilestoneData data = GlobalFundPlugin.getInstance().getMilestoneManager().getActiveData();

        if (data == null) return false;

        if (data.isCompleted()) {
            GlobalFundPlugin.getInstance().getManager(LanguageManager.class).sendPrefixed(sender, "Goal-Already-Reached");
            return true;
        }

        if (type == null) {
            if (data.isCompleted()) {
                GlobalFundPlugin.getInstance().getManager(LanguageManager.class).sendPrefixed(sender, "Goal-Already-Reached");
                return true;
            }
        } else {
            MilestonePreset preset = GlobalFundPlugin.getInstance().getMilestoneManager().getActivePreset();
            if (preset != null && data.getCurrencyAmount(type) >= preset.getRequirements().get(type)) {
                GlobalFundPlugin.getInstance().getManager(LanguageManager.class).sendPrefixed(sender, "Goal-Already-Reached");
                return true;
            }
        }
        return false;
    }

    public double checkAmount(CommandSender sender, String arg) {
        double amount;
        try {
            amount = Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            GlobalFundPlugin.getInstance().getManager(LanguageManager.class)
                    .getPrefixed("Not-A-Number")
                    .replace("%param%", arg)
                    .send(sender);
            return -1;
        }

        if (amount <= 0) {
            GlobalFundPlugin.getInstance().getManager(LanguageManager.class)
                    .sendPrefixed(sender, "Cannot-Be-Negative");
            return -1;
        }

        return amount;
    }

    public boolean checkActiveGoal(CommandSender sender) {
        if (GlobalFundPlugin.getInstance().getMilestoneManager().getActivePreset() == null) {
            GlobalFundPlugin.getInstance().getManager(LanguageManager.class).sendPrefixed(sender, "No-Active-Goal");
            return false;
        } else return true;
    }
}