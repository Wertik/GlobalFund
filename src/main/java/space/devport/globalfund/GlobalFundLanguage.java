package space.devport.globalfund;

import space.devport.utils.text.language.LanguageDefaults;

public class GlobalFundLanguage extends LanguageDefaults {

    @Override
    public void setDefaults() {
        addDefault("No-Provider", "&cThis currency has no provider initialized. Inform the admin.");

        addDefault("Status.Header", "&8&m    &f Milestone: &r%milestoneDisplay%", "&7Completed: &r%completed%");
        addDefault("Status.Currency-Line", "&7%currencyName% &8- &e%currencyActual% &7/ &f%currencyGoal%");
        addDefault("Status.Footer", "&8&m        ");

        addDefault("No-Active-Goal", "&cThere's no active goal.");
        addDefault("Currency-Invalid", "&f%param% 7cis not a valid currency.");
        addDefault("Not-A-Number", "&f%param% &cis not a number.");
        addDefault("Cannot-Be-Negative", "&cAmount cannot be negative.");

        addDefault("Goal-Already-Reached", "&cGoal has been already reached.");

        addDefault("Deposit.Not-Enough", "&cYou're too poor to do that.");
        addDefault("Deposit.Could-Not", "&cCould not deposit to the fund. Something unexpected went wrong.");
        addDefault("Deposit.Done", "&7Deposited &r%amount% %type% &7to the global fund.");

        addDefault("Donated.Header", "&8&m    &f Donate History &8&m    ");
        addDefault("Donated.Record-Header", "&8- &f%milestoneName% %activeOrNot%");
        addDefault("Donated.Record-Currency-Line", "&8 - &f%currencyName% &e%donatedAmount%");
        addDefault("Donated.Footer", "&8&m        ");
        addDefault("Donated.No-History", "&cNo history yet.");
        addDefault("Donated.Placeholders.Active", "&7( &6Active &7)");
        addDefault("Donated.Placeholders.Not-Active", "");

        addDefault("Added", "&7Added &r%amount% %type% &7to the global fund.");
        addDefault("Removed", "&7Removed &r%amount% %type% &7from the global fund.");
        addDefault("Reset", "&7Reset was successful.");
        addDefault("Could-Not-Complete", "&cCould not complete the goal.");
        addDefault("Completed", "&7Current global fund goal should be fulfilled.");

        addDefault("Invalid-Milestone", "&f%param% &cis not a valid milestone.");
        addDefault("Set-Active", "&7Active milestone was set to &f%activeGoal%");

        addDefault("Currency.TOKENS", "&btokens");
        addDefault("Currency.MONEY", "&6money");

        addDefault("Broadcast-When-System", "&eSystem");

        addDefault("Placeholders.None-Active", "&cNo goal active");
        addDefault("Placeholders.Invalid-Placeholder", "&cInvalid placeholder");
        addDefault("Placeholders.Not-Enough-Args", "&cNot enough args");
        addDefault("Placeholders.Invalid-Currency", "&cInvalid currency");
        addDefault("Placeholders.Invalid-Amount-Type", "&cInvalid amount type");
        addDefault("Placeholders.Zero", "&c0");
        addDefault("Number-Format", "#.##");

        addDefault("Broadcasts.Deposit", "&5>> &f%player% &7deposited &r%amount% &r%type% &7to the global fund! &f%actual%&7/&e%goal% &7( &f%remaining% &7)");
    }
}