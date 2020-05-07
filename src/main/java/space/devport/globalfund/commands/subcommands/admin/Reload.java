package space.devport.globalfund.commands.subcommands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.globalfund.GlobalFundPlugin;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;

public class Reload extends SubCommand {

    public Reload() {
        super("reload");
        this.preconditions = new Preconditions().permissions("globalfund.admin");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        GlobalFundPlugin.getInstance().reload(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% reload";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Reloads the plugin configuration.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}