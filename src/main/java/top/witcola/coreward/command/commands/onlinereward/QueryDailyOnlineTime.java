package top.witcola.coreward.command.commands.onlinereward;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.command.ICommand;
import top.witcola.coreward.command.ItsACommand;
import top.witcola.coreward.config.HiConfig;
import top.witcola.coreward.config.onlinereward.DailyOnlineTimeRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ItsACommand(commandNmae = "querytime", premission = "cr.admin")
public class QueryDailyOnlineTime implements ICommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 2) {
            return false;
        }

//        CoReward.getCoReward().dailyOnlineTimeRecords.reloadConfig();
        String playerName = strings[1];

        long totalMSeconds = CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerOnlineSeconds(playerName);
        long minutes = totalMSeconds  / 60 ;
        long seconds = totalMSeconds % 60;


        long dtotalMSeconds = CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerDailyOnlineSeconds(playerName);
        long dminutes = dtotalMSeconds  / 60 ;
        long dseconds = dtotalMSeconds % 60;
        commandSender.sendMessage("玩家 " + playerName + playerName + " 的本次在线时间为: " + minutes + " 分钟 " + seconds + " 秒"+"。累计在线时间为: " + dminutes + " 分钟 " + dseconds + " 秒。");


        return true;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList("<player>");
    }

    @Override
    public List<String> handleArg(CommandSender sender, String handleArg) {
        if (handleArg.equals("<player>")) {
            return Bukkit.getOnlinePlayers().stream().map(i->i.getName()).collect(Collectors.toList());
        }
        return Arrays.asList("");
    }

    @Override
    public String getUsage() {
        return "querytime <player>";
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
