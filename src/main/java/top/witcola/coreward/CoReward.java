package top.witcola.coreward;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import top.witcola.coreward.command.CommandHandler;
import top.witcola.coreward.command.commands.onlinereward.*;
import top.witcola.coreward.config.HiConfig;
import top.witcola.coreward.config.onlinereward.DailyOnlineTimeRecord;
import top.witcola.coreward.config.onlinereward.OnlineRewardItemRecord;
import top.witcola.coreward.listeners.PlayerTimeTrackListener;
import top.witcola.coreward.placeholderhook.CoRewardPlaceholderExpansion;

import java.nio.charset.Charset;
import java.util.List;

public class CoReward extends JavaPlugin {
    protected CommandHandler commands = new CommandHandler(this,"onlinereward");
    public static CoReward coReward = null;
    {
        coReward = this;
    }
    public HiConfig<OnlineRewardItemRecord> onlineRewardItemRecords = new HiConfig<>(this.getDataFolder()+"/OnlineRewardRecords.json", OnlineRewardItemRecord.class, Charset.forName("utf8"),getCoReward());
    public HiConfig<DailyOnlineTimeRecord> dailyOnlineTimeRecords = new HiConfig<>(this.getDataFolder()+"/DailyOnlineTimeRecords.json", DailyOnlineTimeRecord.class, Charset.forName("utf8"),getCoReward());



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return commands.onCommand(sender,command,label,args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return commands.onTabComplete(sender,command,alias,args);
    }

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        dailyOnlineTimeRecords.saveConfig();

    }

    public void init() {

        registerListeners();
        registerCommands();
        registerHooks();
    }

    public void registerHooks() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CoRewardPlaceholderExpansion().register();
        }
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerTimeTrackListener(),this);
    }





    public void registerCommands() {
        commands.addCommand(new Add());
        commands.addCommand(new Delete());
        commands.addCommand(new QueryDailyOnlineTime());
        commands.addCommand(new Claim());
        commands.addCommand(new ResetClaimRecord());
        commands.addCommand(new AddReward());
        commands.addCommand(new RemoveReward());
        commands.addCommand(new ListReward());
    }

    public static CoReward getCoReward() {
        return coReward;
    }
}
