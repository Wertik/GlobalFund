package space.devport.globalfund.system.milestone.struct;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.utils.struct.Rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MilestoneRewards extends Rewards {

    public MilestoneRewards(Rewards rewards) {
        super(rewards);
    }

    @Override
    public void give(Player player) {
        if (player == null)
            getPlaceholders().add("%player%", GlobalFundPlugin.getInstance().getLanguageManager().get("Broadcast-When-System").color().toString());
        super.give(player);

        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (loopPlayer == player) continue;

            getPlaceholders().add("%loopPlayer%", loopPlayer.getName());

            giveItems(loopPlayer);
            giveMoney(loopPlayer);
            giveTokens(loopPlayer);
        }
    }

    @Override
    public void parseCommands(Player player) {

        List<String> backup = new ArrayList<>(getCommands());

        List<String> loopCommands = getCommands().stream()
                .filter(c -> c.contains("%loopPlayer%"))
                .collect(Collectors.toList());

        getCommands().removeAll(loopCommands);

        // Reward the one with commands that don't have the loopPlayer in them
        super.parseCommands(player);

        // Set the commands to our loopCommands
        commands(loopCommands);

        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            super.parseCommands(loopPlayer);
        }

        commands(backup);
    }
}