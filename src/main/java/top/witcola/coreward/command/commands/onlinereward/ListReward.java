package top.witcola.coreward.command.commands.onlinereward;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.command.ICommand;
import top.witcola.coreward.command.ItsACommand;
import top.witcola.coreward.config.onlinereward.RewardItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//onlinereward listreward a
@ItsACommand(commandNmae = "listreward",premission = "or.admin")
public class ListReward implements ICommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length <2) {
            return false;
        }

        String rewardName = strings[1];
        RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(rewardName);
        if (rewardItem == null) {
            commandSender.sendMessage("奖励项 "+rewardName+" 不存在。");
            return true;
        }

        List<String> commands = rewardItem.ExcutingCommands;
        if (rewardItem.ExcutingCommands==null) {
            rewardItem.ExcutingCommands = new ArrayList<>();
        }

        commandSender.sendMessage("奖励项 "+rewardName+" 命令奖励如下：");
        for (int i=0;i<commands.size();i++) {
            commandSender.sendMessage(i+1+": "+commands.get(i));
        }


        return true;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList("<record>");
    }

    @Override
    public List<String> handleArg(CommandSender sender, String handleArg) {
        if (handleArg.equals("<record>")) {
            return CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemNames();
        }


        return ICommand.super.handleArg(sender, handleArg);
    }

    @Override
    public String getUsage() {
        return "addreward <record> <index>";
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
