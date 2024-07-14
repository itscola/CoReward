package top.witcola.coreward.config.onlinereward;



import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.placeholderhook.CoRewardPlaceholderExpansion;
import top.witcola.coreward.utils.LocalDateUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DailyOnlineTimeRecord {
    private String date;
    private ConcurrentHashMap<String, String> playerLastLogin = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> playerOnlineSeconds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> playerDailyOnlineSeconds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, List<String>> playerDailyRewardClaim = new ConcurrentHashMap<>();


    public DailyOnlineTimeRecord() {

    }

    public boolean hasClaim(String playerName, String rewardName) {
        RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(rewardName);
        if (rewardItem == null) {
            return false;
        }

        List<String> claimedRewards = playerDailyRewardClaim.get(playerName);
        if (claimedRewards != null) {
            return claimedRewards.contains(rewardName);
        }

        return false;
    }

    public boolean claimReward(Player p, String rewardName){
        long dOnlineMins = getPlayerDailyOnlineSeconds(p.getName())/60;
        RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(rewardName);
        if (rewardItem == null) {
            p.sendMessage("奖励项 "+rewardName+" 不存在。");
            return false;
        }


        if (hasClaim(p.getName(), rewardName)) {
            p.sendMessage("奖励项 "+rewardName+" 今天已经被领取过了。");
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 2);
            return false;
        }

        if (dOnlineMins>=rewardItem.minutes){
            // 可领取
            addClaimValue(p.getName(),rewardName);
            List<String> commands = rewardItem.ExcutingCommands;
            Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
            if (commands!=null&&!commands.isEmpty()) {
                Bukkit.getScheduler().runTask(CoReward.getCoReward(), () -> {
                    for (String command : commands) {
                        if (placeholderAPI != null && placeholderAPI.isEnabled()) {
                            String parsedCommand = PlaceholderAPI.setPlaceholders(p, command);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
                            continue;
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player_name%", p.getName()));

                        }

                    }
                });
            }

            p.sendMessage("已领取奖励。");
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 2, 2);
            CoReward.getCoReward().dailyOnlineTimeRecords.saveConfig();

            return true;
        } else {
            p.sendMessage("无法领取此奖励，时间不足以达到条件，");
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 2);
            return false;
        }
    }

    public void addClaimValue( String key, String value) {
        playerDailyRewardClaim.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public ConcurrentHashMap<String, Long> getPlayerDailyOnlineSeconds() {
        return playerDailyOnlineSeconds;
    }

    public ConcurrentHashMap<String, Long> getPlayerOnlineSeconds() {
        return playerOnlineSeconds;
    }

    public ConcurrentHashMap<String, List<String>> getPlayerDailyRewardClaim() {
        return playerDailyRewardClaim;
    }

    public ConcurrentHashMap<String, String> getPlayerLastLogin() {
        return playerLastLogin;
    }

    public long getPlayerDailyOnlineSeconds(String playerName) {
        checkIfSameDayOrReset();
        caculateOnlineTime(playerName);
        return playerDailyOnlineSeconds.getOrDefault(playerName,0l)+playerOnlineSeconds.getOrDefault(playerName,0l);
    }

    //onexit
    public void summarySeconds(String playerName){
        if (playerDailyOnlineSeconds.containsKey(playerName)){
            playerDailyOnlineSeconds.put(playerName, playerDailyOnlineSeconds.get(playerName)+playerOnlineSeconds.get(playerName));
        } else {
            playerDailyOnlineSeconds.put(playerName, playerOnlineSeconds.get(playerName));
        }

        playerOnlineSeconds.clear();
    }

    public Long getPlayerOnlineSeconds(String playerName) {
        checkIfSameDayOrReset();
        caculateOnlineTime(playerName);
        return playerOnlineSeconds.get(playerName);
    }

    public void checkIfSameDayOrReset() {
        if (date==null) {
            date = now();
        }

        if (!LocalDateUtils.isSameDay(LocalDateUtils.parseToLocalDateTime(date),LocalDateTime.now())){
            System.out.println("clean");
            date = now();
            CoReward.getCoReward().dailyOnlineTimeRecords.config.playerLastLogin.clear();
            CoReward.getCoReward().dailyOnlineTimeRecords.config.playerOnlineSeconds.clear();
            CoReward.getCoReward().dailyOnlineTimeRecords.config.playerDailyOnlineSeconds.clear();
            CoReward.getCoReward().dailyOnlineTimeRecords.config.playerDailyRewardClaim.clear();
            CoReward.getCoReward().dailyOnlineTimeRecords.saveConfig();

        }

    }

    public LocalDateTime getPlayerLastLogin(String playerName) {
        return LocalDateUtils.parseToLocalDateTime(playerLastLogin.get(playerName));
    }

    public void setPlayerLastLogin(String playerName) {
        playerLastLogin.put(playerName, now());
    }

    public void removePlayerLastLogin(String playerName) {
        playerLastLogin.remove(playerName);
    }

    public void caculateOnlineTime(String playerName) {
        if (!playerLastLogin.containsKey(playerName)) {
            playerLastLogin.put(playerName, now());
        }
        long tims = LocalDateUtils.secondsBetween(getPlayerLastLogin(playerName),LocalDateTime.now());
        playerOnlineSeconds.put(playerName, tims);

    }

    public static String now() {
        return LocalDateUtils.formatLocalDateTime(LocalDateTime.now());
    }


}

