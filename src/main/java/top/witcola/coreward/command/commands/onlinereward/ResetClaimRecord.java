package top.witcola.coreward.command.commands.onlinereward;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.command.ICommand;
import top.witcola.coreward.command.ItsACommand;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//onlinereward resetclaimrecord white_cola
@ItsACommand(commandNmae = "resetclaimrecord" , premission = "cr.admin")
public class ResetClaimRecord implements ICommand{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 2) {
            return false;
        }
        String playerName = strings[1];

        ConcurrentHashMap<String, List<String>> playerDailyRewardClaim = CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerDailyRewardClaim();

        if (playerDailyRewardClaim.containsKey(playerName)) {
            playerDailyRewardClaim.remove(playerName);
            commandSender.sendMessage("Successfully reset the claim record for player: " + playerName);
        } else {
            commandSender.sendMessage("No claim record found for player: " + playerName);
        }

        CoReward.getCoReward().dailyOnlineTimeRecords.saveConfig();

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
        return "resetclaimrecord <player>";
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
