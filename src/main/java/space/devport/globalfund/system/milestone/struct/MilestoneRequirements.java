package space.devport.globalfund.system.milestone.struct;

import lombok.Getter;
import lombok.NoArgsConstructor;
import space.devport.globalfund.system.currency.CurrencyType;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MilestoneRequirements {

    @Getter
    private final Map<CurrencyType, Double> currency = new HashMap<>();

    public void set(CurrencyType type, double amount) {
        currency.put(type, amount);
    }

    public double get(CurrencyType type) {
        return currency.containsKey(type) ? currency.get(type) : 0;
    }
}