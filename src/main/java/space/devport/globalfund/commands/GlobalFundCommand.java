package space.devport.globalfund.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;

public class GlobalFundCommand extends MainCommand {

    public GlobalFundCommand() {
        super("globalfund");
        this.preconditions = new Preconditions().permissions("globalfund.commands");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        return super.perform(sender, label, args);
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Displays this.";
    }
}