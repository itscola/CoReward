package top.witcola.coreward.config.onlinereward;

import java.util.List;

public class RewardItem {
    public String name;
    public int minutes;
    public List<String> Description;
    public List<String> ExcutingCommands;

    public RewardItem() {}

    public RewardItem(String name, int minutes) {
        this.name = name;
        this.minutes = minutes;
    }

    public RewardItem(String name, int minutes, List<String> description, List<String> excutingCommands) {
        this.name = name;
        this.minutes = minutes;
        Description = description;
        ExcutingCommands = excutingCommands;
    }
}
