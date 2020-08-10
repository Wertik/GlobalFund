package space.devport.globalfund.system.milestone.struct;

import lombok.*;
import org.mongodb.morphia.annotations.*;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.struct.CurrencyStorage;

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
    private CurrencyStorage currencyStorage = new CurrencyStorage();

    public MilestoneData(String name) {
        this.name = name;
    }

    public void add(CurrencyType type, double amount) {
        currencyStorage.add(type, amount);
    }

    public void set(CurrencyType type, double amount) {
        currencyStorage.set(type, amount);
    }

    public void remove(CurrencyType type, double amount) {
        currencyStorage.remove(type, amount);
    }

    public void clear(CurrencyType type) {
        currencyStorage.clear(type);
    }

    public void clearAll() {
        currencyStorage.clear();
    }

    public double getCurrencyAmount(CurrencyType type) {
        return currencyStorage.get(type);
    }
}