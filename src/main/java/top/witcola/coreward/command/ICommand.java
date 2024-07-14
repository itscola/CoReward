package top.witcola.coreward.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public interface ICommand extends CommandExecutor {
    public default List<String> getArgs(){
        return new Vector<>();
    }

    public default List<String> handleArg(CommandSender sender, String handleArg){
        return Arrays.asList("");
    }

    public default String getUsage(){
        return "";
    }


    public default String getUsageDescripition(){
        return "";
    }
}
