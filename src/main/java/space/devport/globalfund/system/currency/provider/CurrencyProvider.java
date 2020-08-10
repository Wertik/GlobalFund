package space.devport.globalfund.system.currency.provider;

import org.bukkit.entity.Player;

public interface CurrencyProvider {

    void onLoad();

    boolean withdraw(Player player, double amount);

    // Two below are basically useless for our purposes,
    // but being the one and only method could get lonely,
    // that's why I left them here to accompany withdraw.

    boolean deposit(Player player, double amount);

    double getBalance(Player player);
}