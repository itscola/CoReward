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
import java.util.stream.Collectors;

//onlinereward claim a white_cola
@ItsACommand(commandNmae = "claim",premission = "cr.admin")
public class Claim implements ICommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 3) {
            return false;
        }

        String rewardName = strings[1];
        String playerName = strings[2];

        Player p = Bukkit.getPlayer(playerName);
        if (p == null||!p.isOnline()) {
            commandSender.sendMessage("领取玩家必须在线。");
            return false;
        }
        CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().claimReward(p,rewardName);
        return true;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList("<rewardName>","<player>");
    }

    @Override
    public List<String> handleArg(CommandSender sender, String handleArg) {
        if (handleArg.equals("<rewardName>")) {
            return CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemNames();
        }

        if (handleArg.equals("<player>")) {
            return Bukkit.getOnlinePlayers().stream().map(i->i.getName()).collect(Collectors.toList());
        }
        return Arrays.asList("");
    }

    @Override
    public String getUsage() {
        return "claim <rewardName> <player>";
    }

    @Override
    public String getUsageDescripition() {
        return ICommand.super.getUsageDescripition();
    }
}
