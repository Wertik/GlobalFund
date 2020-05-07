package space.devport.globalfund.system.struct;

import lombok.*;
import org.mongodb.morphia.annotations.*;
import space.devport.globalfund.system.currency.CurrencyType;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(noClassnameStored = true, value = "MilestoneData")
public class MilestoneData {

    @Id
    @Indexed(options = @IndexOptions(unique = true))
    @Getter
    private String name;

    @Getter
    @Setter
    private boolean completed = false;

    @Getter
    @Setter
    @Embedded
    private Map<CurrencyType, Double> currency = new HashMap<>();

    public MilestoneData(String name) {
        this.name = name;
    }

    public void add(CurrencyType type, double amount) {
        amount = currency.containsKey(type) ? amount + currency.get(type) : amount;
        currency.put(type, Math.max(0, amount));
    }

    public void set(CurrencyType type, double amount) {
        currency.put(type, amount);
    }

    public void remove(CurrencyType type, double amount) {
        amount = getCurrencyAmount(type) - amount;
        if (amount > 0)
            currency.put(type, amount);
        else currency.remove(type);
    }

    public void clear(CurrencyType type) {
        currency.remove(type);
    }

    public void clearAll() {
        currency.clear();
    }

    public double getCurrencyAmount(CurrencyType type) {
        return currency.containsKey(type) ? currency.get(type) : 0;
    }
}