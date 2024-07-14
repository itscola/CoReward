package top.witcola.coreward.command.commands.onlinereward;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.command.ICommand;
import top.witcola.coreward.command.ItsACommand;
import top.witcola.coreward.config.onlinereward.RewardItem;

import java.util.Arrays;
import java.util.List;

//onlinereward removereward a 1
@ItsACommand(commandNmae = "removereward",premission = "or.admin")
public class RemoveReward implements ICommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        String rewardName = strings[1];
        RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(rewardName);
        if (rewardItem == null) {
            commandSender.sendMessage("奖励项 "+rewardName+" 不存在。");
            return true;
        }

        int index;

        try {
            index = Integer.valueOf(strings[2])-1;
        }catch (Exception e) {
            return false;
        }

//        StringBuilder sbu = new StringBuilder();
//        for (int i = 2; i < strings.length; i++) {
//            sbu.append(strings[i]);
//            if (i != strings.length - 1) {
//                sbu.append(" ");
//            }
//        }
//        String theCommand = sbu.toString();
//
//        if (!rewardItem.ExcutingCommands.contains(theCommand)) {
//            commandSender.sendMessage("奖励项 "+rewardName+" 不存在命令 '"+theCommand+"'");
//            return true;
//        }

        String theCommand = rewardItem.ExcutingCommands.get(index);

        rewardItem.ExcutingCommands.remove(index);
        CoReward.getCoReward().onlineRewardItemRecords.saveConfig();
        commandSender.sendMessage("已成功删除奖励项命令: "+theCommand+" 从 "+rewardName+" 。");
        return true;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList("<record>","<index>");
    }

    @Override
    public List<String> handleArg(CommandSender sender, String handleArg) {
        if (handleArg.equals("<record>")) {
            return CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemNames();
        }

        if (handleArg.equals("<index>")) {
            return Arrays.asList("1","2","3","4","5");
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
