package space.devport.globalfund.system.currency;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.globalfund.system.currency.provider.CurrencyProvider;
import space.devport.globalfund.system.currency.provider.TokenManagerProvider;
import space.devport.globalfund.system.currency.provider.VaultProvider;

public enum CurrencyType {
    MONEY,
    TOKENS;

    @Getter
    @Setter
    private CurrencyProvider provider;

    static {
        MONEY.setProvider(new VaultProvider());
        TOKENS.setProvider(new TokenManagerProvider());
    }

    @Nullable
    public static CurrencyType fromString(@Nullable String str) {
        if (Strings.isNullOrEmpty(str)) return null;
        CurrencyType type;
        try {
            type = valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return type;
    }

    public String getName() {
        return GlobalFundPlugin.getInstance().getLanguageManager().get("Currency." + toString()).color().toString();
    }
}