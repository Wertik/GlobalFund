package space.devport.globalfund.record.struct;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.*;
import space.devport.globalfund.system.currency.CurrencyType;
import space.devport.globalfund.system.struct.CurrencyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity(noClassnameStored = true, value = "PlayerRecord")
public class PlayerRecord {

    @Id
    @Indexed(options = @IndexOptions(unique = true))
    @Getter
    private final UUID uuid;

    @Embedded
    private Map<String, CurrencyStorage> donated = new HashMap<>();

    public double getDonated(String milestoneName, CurrencyType currencyType) {
        CurrencyStorage record = donated.getOrDefault(milestoneName, null);

        if (record == null)
            return 0;

        return record.get(currencyType);
    }

    public void addDonate(String milestoneName, CurrencyType type, double amount) {
        CurrencyStorage currencyStorage = donated.getOrDefault(milestoneName, null);

        if (currencyStorage == null) {
            currencyStorage = new CurrencyStorage();
            donated.put(milestoneName, currencyStorage);
        }

        currencyStorage.add(type, amount);
    }

    public boolean isEmpty() {
        for (CurrencyStorage storage : donated.values()) {
            if (!storage.getAmounts().isEmpty()) return false;

            if (storage.getAmounts().values().stream().mapToDouble(i -> i).sum() != 0)
                return false;
        }
        return true;
    }
}