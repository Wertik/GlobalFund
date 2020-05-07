package space.devport.globalfund.system.currency.provider;

import lombok.NoArgsConstructor;
import me.realized.tokenmanager.TokenManagerPlugin;
import org.bukkit.entity.Player;

@NoArgsConstructor
public class TokenManagerProvider implements CurrencyProvider {

    @Override
    public boolean withdraw(Player player, double amount) {
        return TokenManagerPlugin.getInstance().removeTokens(player, (long) amount);
    }

    @Override
    public boolean deposit(Player player, double amount) {
        return TokenManagerPlugin.getInstance().addTokens(player, (long) amount);
    }

    @Override
    public double getBalance(Player player) {
        return TokenManagerPlugin.getInstance().getTokens(player).orElse(0);
    }
}