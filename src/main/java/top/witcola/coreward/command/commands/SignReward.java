package top.witcola.coreward.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import top.witcola.coreward.command.ICommand;

import java.util.List;

public class SignReward implements ICommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> getArgs() {
        return ICommand.super.getArgs();
    }

    @Override
    public List<String> handleArg(CommandSender sender, String handleArg) {
        return ICommand.super.handleArg(sender, handleArg);
    }

    @Override
    public String getUsage() {
        return ICommand.super.getUsage();
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
