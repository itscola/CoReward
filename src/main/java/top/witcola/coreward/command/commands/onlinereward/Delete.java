package top.witcola.coreward.command.commands.onlinereward;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.command.ICommand;
import top.witcola.coreward.command.ItsACommand;

import java.util.Arrays;
import java.util.List;
//onlinereward delete a
@ItsACommand(commandNmae = "delete",premission = "cr.admin")
public class Delete implements ICommand{
    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {
        if (strings.length < 2) {
            return false;
        }

        String name = strings[1];
        if (!CoReward.getCoReward().onlineRewardItemRecords.getConfig().hasItem(name)) {
            commandSender.sendMessage("不存在名为 "+name+" 的在线奖励项。");
            return true;
        }


        CoReward.getCoReward().onlineRewardItemRecords.getConfig().deleteItem(name);
        CoReward.getCoReward().onlineRewardItemRecords.saveConfig();
        commandSender.sendMessage("成功删除奖励项 "+name+" 。");

        return true;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList("<name>");
    }

    @Override
    public List<String> handleArg(CommandSender sender, String handleArg) {
        if (handleArg.equals("<name>")) {
            return CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemNames();
        }
        return ICommand.super.handleArg(sender, handleArg);
    }

    @Override
    public String getUsage() {
        return "coreward delete <name>";
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
