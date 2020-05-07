package space.devport.globalfund.system.currency.provider;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import space.devport.globalfund.GlobalFundPlugin;

public class VaultProvider implements CurrencyProvider {

    private final GlobalFundPlugin plugin;

    public VaultProvider() {
        this.plugin = GlobalFundPlugin.getInstance();

        setupEconomy();
    }

    private static Economy economy = null;

    private void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return;
        }

        economy = rsp.getProvider();
    }

    @Override
    public boolean withdraw(Player player, double amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean deposit(Player player, double amount) {
        return economy.depositPlayer(player, amount).transactionSuccess();
    }

    @Override
    public double getBalance(Player player) {
        return economy.getBalance(player);
    }
}
