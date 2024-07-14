package top.witcola.coreward.placeholderhook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import top.witcola.coreward.CoReward;
import top.witcola.coreward.config.onlinereward.RewardItem;

//%coreward_daily_online_time_m%
public class CoRewardPlaceholderExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "CoReward";
    }

    @Override
    public String getAuthor() {
        return "White_cola";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }


        if (identifier.equals("daily_online_time_m")) {
            return String.valueOf(CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerDailyOnlineSeconds(player.getName())/60);
        }

        if (identifier.equals("daily_online_time_s")) {
            return String.valueOf(CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerDailyOnlineSeconds(player.getName()));
        }

        if (identifier.equals("daily_online_time_mos")) {
            return String.valueOf(CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerDailyOnlineSeconds(player.getName())%60);
        }

        if (identifier.startsWith("is_record_claimed_")) {
            String[] args = identifier.split("_");
            String record = args[3];
            if(record==null){
                return "";
            }

            if(CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().hasClaim(player.getName(),record)){
                return "已领取";
            } else {
                return "未领取";
            }
        }

        if (identifier.startsWith("is_record_claimable_")) {
            String[] args = identifier.split("_");
            String record = args[3];
            if(record==null){
                return "";
            }
            RewardItem rewardItem = CoReward.getCoReward().onlineRewardItemRecords.getConfig().getItemByName(record);
            if (rewardItem == null) {
                return "奖励项 "+record+" 不存在";
            }

            long mins = CoReward.getCoReward().dailyOnlineTimeRecords.getConfig().getPlayerDailyOnlineSeconds(player.getName())/60;
            if (mins>=rewardItem.minutes) {
                return "已达到时间";
            } else {
                return "未达到时间";
            }
        }

        return "";
    }
}
