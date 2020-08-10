package space.devport.globalfund;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.milestone.struct.MilestoneData;
import space.devport.globalfund.system.milestone.struct.MilestonePreset;
import space.devport.utils.text.StringUtil;
import space.devport.utils.text.language.LanguageManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GlobalFundExpansion extends PlaceholderExpansion {

    private final GlobalFundPlugin plugin;
    private final LanguageManager language;

    public GlobalFundExpansion() {
        this.plugin = GlobalFundPlugin.getInstance();
        this.language = plugin.getLanguageManager();
    }

    /*
     * %globalfund_display%
     * %globalfund_<money/tokens>_<actual/remaining/goal/donated>%
     * */

    @Override
    public String onPlaceholderRequest(Player p, String params) {

        if (params.equalsIgnoreCase("display")) {

            MilestonePreset activePreset = plugin.getMilestoneManager().getActivePreset();
            if (activePreset == null) return language.get("Placeholders.None-Active").color().toString();
            return StringUtil.color(activePreset.getDisplayName());
        } else if (params.equalsIgnoreCase("completed")) {

            MilestoneData data = plugin.getMilestoneManager().getActiveData();
            if (data == null) return language.get("Placeholders.None-Active").color().toString();
            return String.valueOf(data.isCompleted());
        } else {
            if (!params.contains("_"))
                return language.get("Placeholders.Invalid-Placeholder").color().toString();

            String[] arr = params.split("_");

            if (arr.length < 2) return language.get("Placeholders.Not-Enough-Args").color().toString();

            CurrencyType type = CurrencyType.fromString(arr[0]);
            if (type == null) return language.get("Placeholders.Invalid-Currency").color().toString();

            double value = -1;
            switch (arr[1]) {
                case "actual":
                    value = plugin.getMilestoneManager().getValue(type);
                    break;
                case "remaining":
                    value = plugin.getMilestoneManager().getRemaining(type);
                    break;
                case "goal":
                    value = plugin.getMilestoneManager().getActivePreset().getRequirements().get(type);
                    break;
                case "donated":
                    value = plugin.getRecordManager().getRecord(p).getDonated(plugin.getMilestoneManager().getActiveMilestone(), type);
                    break;
                case "required":
                    return String.valueOf(plugin.getMilestoneManager().requiresCurrency(type));
            }

            if (value == -1) return language.get("Placeholders.Invalid-Amount-Type").color().toString();
            if (value == 0) return language.get("Placeholders.Zero").color().toString();

            NumberFormat format = new DecimalFormat(language.get("Number-Format").toString());
            return format.format(value);
        }
    }

    @Override
    public String getIdentifier() {
        return "globalfund";
    }

    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }
}