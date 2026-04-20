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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DailyOnlineTimeRecord {
    private String date;
    // 存储结构不变，key 改为 UUID 字符串
    private ConcurrentHashMap<String, String> playerLastLogin = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> playerOnlineSeconds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> playerDailyOnlineSeconds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, List<String>> playerDailyRewardClaim = new ConcurrentHashMap<>();

    public DailyOnlineTimeRecord() {

    }

    public boolean hasClaim(UUID playerUUID, String rewardName) {
        RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(rewardName);
        if (rewardItem == null) {
            return false;
        }

        List<String> claimedRewards = playerDailyRewardClaim.get(playerUUID.toString());
        if (claimedRewards != null) {
            return claimedRewards.contains(rewardName);
        }

        return false;
    }

    public boolean claimReward(Player p, String rewardName){
        String playerUUID = p.getUniqueId().toString();
        long dOnlineMins = getPlayerDailyOnlineSeconds(p.getUniqueId())/60;
        RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(rewardName);
        if (rewardItem == null) {
            p.sendMessage("奖励项 "+rewardName+" 不存在。");
            return false;
        }


        if (hasClaim(p.getUniqueId(), rewardName)) {
            p.sendMessage("奖励项 "+rewardName+" 今天已经被领取过了。");
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 2);
            return false;
        }

        if (dOnlineMins>=rewardItem.minutes){
            // 可领取
            addClaimValue(playerUUID, rewardName);
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

    public void addClaimValue(String playerUUID, String value) {
        playerDailyRewardClaim.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(value);
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

    public long getPlayerDailyOnlineSeconds(UUID playerUUID) {
        checkIfSameDayOrReset();
        caculateOnlineTime(playerUUID);
        String uuidStr = playerUUID.toString();
        return playerDailyOnlineSeconds.getOrDefault(uuidStr, 0L) + playerOnlineSeconds.getOrDefault(uuidStr, 0L);
    }

    //onexit
    public void summarySeconds(UUID playerUUID){
        String uuidStr = playerUUID.toString();
        if (playerDailyOnlineSeconds.containsKey(uuidStr)){
            playerDailyOnlineSeconds.put(uuidStr, playerDailyOnlineSeconds.get(uuidStr) + playerOnlineSeconds.get(uuidStr));
        } else {
            playerDailyOnlineSeconds.put(uuidStr, playerOnlineSeconds.get(uuidStr));
        }

        playerOnlineSeconds.clear();
    }

    public Long getPlayerOnlineSeconds(UUID playerUUID) {
        checkIfSameDayOrReset();
        caculateOnlineTime(playerUUID);
        return playerOnlineSeconds.get(playerUUID.toString());
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

    public LocalDateTime getPlayerLastLogin(UUID playerUUID) {
        return LocalDateUtils.parseToLocalDateTime(playerLastLogin.get(playerUUID.toString()));
    }

    public void setPlayerLastLogin(UUID playerUUID) {
        playerLastLogin.put(playerUUID.toString(), now());
    }

    public void removePlayerLastLogin(UUID playerUUID) {
        playerLastLogin.remove(playerUUID.toString());
    }

    public void caculateOnlineTime(UUID playerUUID) {
        String uuidStr = playerUUID.toString();
        if (!playerLastLogin.containsKey(uuidStr)) {
            playerLastLogin.put(uuidStr, now());
        }
        long tims = LocalDateUtils.secondsBetween(getPlayerLastLogin(playerUUID),LocalDateTime.now());
        playerOnlineSeconds.put(uuidStr, tims);

    }

    public static String now() {
        return LocalDateUtils.formatLocalDateTime(LocalDateTime.now());
    }

    // 根据名称查找离线玩家的UUID
    public static UUID getPlayerUUID(String playerName) {
        org.bukkit.OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);
        return offline.getUniqueId();
    }

}
