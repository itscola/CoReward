package top.witcola.coreward.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.config.HiConfig;
import top.witcola.coreward.config.onlinereward.DailyOnlineTimeRecord;


public class PlayerTimeTrackListener implements Listener {
    private HiConfig<DailyOnlineTimeRecord> dailyOnlineTimeConfig;

    public PlayerTimeTrackListener() {
        this.dailyOnlineTimeConfig = CoReward.getCoReward().dailyOnlineTimeRecords;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        dailyOnlineTimeConfig.getConfig().setPlayerLastLogin(event.getPlayer().getName());
        dailyOnlineTimeConfig.saveConfig();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        long totalMSeconds = CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerDailyOnlineSeconds(playerName);
        long minutes = totalMSeconds  / 60 ;
        long seconds = totalMSeconds % 60;
        Bukkit.broadcastMessage("[CoReward] 玩家"+playerName+" 本次在线 "+minutes+" 分, "+seconds+" 秒。");
        dailyOnlineTimeConfig.getConfig().caculateOnlineTime(event.getPlayer().getName());
        dailyOnlineTimeConfig.getConfig().summarySeconds(event.getPlayer().getName());
        dailyOnlineTimeConfig.getConfig().removePlayerLastLogin(event.getPlayer().getName());
        dailyOnlineTimeConfig.saveConfig();
    }


}