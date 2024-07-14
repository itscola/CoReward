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


//onlinereward addreward a say hi
@ItsACommand(commandNmae = "addreward",premission = "or.admin")
public class AddReward implements ICommand {
    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {
        if(strings.length < 2) {
            return false;
        }
        String rewardName = strings[1];
        RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(rewardName);
        if (rewardItem == null) {
            commandSender.sendMessage("奖励项 "+rewardName+" 不存在。");
            return true;
        }

        StringBuilder sbu = new StringBuilder();
        for (int i = 2; i < strings.length; i++) {
            sbu.append(strings[i]);
            if (i != strings.length - 1) {
                sbu.append(" ");
            }
        }
        String theCommand = sbu.toString();
        if (rewardItem.ExcutingCommands==null) {
            rewardItem.ExcutingCommands = new ArrayList<>();
        }
        rewardItem.ExcutingCommands.add(theCommand);
        CoReward.getCoReward().onlineRewardItemRecords.saveConfig();
        commandSender.sendMessage("已成功添加奖励项命令: "+theCommand+" 到 "+rewardName+" 。");
        return true;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList("<record>","<command>");
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
        return "addreward <record> <command>";
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
