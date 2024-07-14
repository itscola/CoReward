package top.witcola.coreward.config.onlinereward;

import java.util.List;
import java.util.Vector;

public class OnlineRewardItemRecord {
    public Vector<RewardItem> items = new Vector<>();

    public OnlineRewardItemRecord() {}

    public RewardItem getItemByName(String name) {
        for (RewardItem record : items) {
            if (record.name.equals(name)) {
                return record;
            }
        }
        return null;
    }

    public boolean hasItem(String name) {
        for (RewardItem item : items) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void deleteItem(String name) {
        for (RewardItem record : items) {
            if (record.name.equals(name)) {
                items.remove(record);
            }
        }
    }

    public List<String> getItemNames() {
        List<String> names = new Vector<>();
        for (RewardItem record : items) {
            names.add(record.name);
        }
        return names;
    }



    public void removeItemByName(String name) {
        for (RewardItem record : items) {
            if (record.name.equals(name)) {
                removeItem(record);
            }
        }
    }

    public void removeItem(RewardItem record) {
        items.remove(record);
    }

    public void addItem(RewardItem record) {
        items.add(record);
    }


}

