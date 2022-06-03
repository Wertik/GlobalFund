package space.devport.globalfund.system.struct;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import space.devport.globalfund.system.currency.CurrencyType;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Entity(noClassnameStored = true, value = "CurrencyStorage")
public class CurrencyStorage {

    @Embedded
    private Map<CurrencyType, Double> amounts = new HashMap<>();

    public double get(CurrencyType type) {
        return amounts.getOrDefault(type, 0D);
    }

    public void add(CurrencyType type, double amount) {
        amount = amounts.containsKey(type) ? amount + amounts.get(type) : amount;
        amounts.put(type, Math.max(0, amount));
    }

    public void remove(CurrencyType type, double amount) {
        amount = get(type) - amount;
        if (amount > 0)
            amounts.put(type, amount);
        else amounts.remove(type);
    }

    public void set(CurrencyType type, double amount) {
        amounts.put(type, amount);
    }

    public void clear(CurrencyType type) {
        set(type, 0);
    }

    public void clear() {
        for (CurrencyType type : amounts.keySet()) {
            clear(type);
        }
    }
}