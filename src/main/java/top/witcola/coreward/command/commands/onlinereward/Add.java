package top.witcola.coreward.command.commands.onlinereward;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.command.ICommand;
import top.witcola.coreward.command.ItsACommand;
import top.witcola.coreward.config.onlinereward.RewardItem;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

//onlinereward add a 1h
@ItsACommand(commandNmae = "add",premission = "cr.admin")
public class Add implements ICommand {
    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {
        if (strings.length < 3) {
            return false;
        }

        String name = strings[1];
        String minutes = strings[2];

        if (!Pattern.matches("^(?:[1-9]\\d{0,2}|1[0-3]\\d{2}|14[0-3]\\d|1440)m$", minutes)) {
            return false;
        }

        CoReward.getCoReward().onlineRewardItemRecords.getConfig().items.add(new RewardItem(name,Integer.parseInt(minutes.replace("m",""))));
        CoReward.getCoReward().onlineRewardItemRecords.saveConfig();
        commandSender.sendMessage("成功添加一条在线奖励项。名为: "+name+" 时间: "+minutes);
        return true;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList("<name>","<min>");
    }

    @Override
    public List<String> handleArg(CommandSender sender, String handleArg) {
        if (handleArg.equals("<name>")) {
            return Arrays.asList("a","b","c");
        }

        if (handleArg.equals("<min>")) {
            return Arrays.asList("10m","30m","60m","120m");
        }
        return ICommand.super.handleArg(sender, handleArg);

    }

    @Override
    public String getUsage() {
        return "add <name> <minutes>";
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
