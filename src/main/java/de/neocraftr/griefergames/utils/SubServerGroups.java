package de.neocraftr.griefergames.utils;

import de.neocraftr.griefergames.GrieferGames;

import java.util.Arrays;
import java.util.List;

public class SubServerGroups {

    private final List<String> resetBoosterServers = Arrays.asList("lobby", "portal", "skyblock", "cb0", "kreativ", "hardcore", "gestrandet");
    private final List<String> showBalanceServers = Arrays.asList("extreme", "evil", "nature", "lava", "wasser", "skyblock", "kreativ", "hardcore", "gestrandet");
    private final List<String> showBankBalanceServers = Arrays.asList("extreme", "evil", "nature");
    private final List<String> cityBuilds = Arrays.asList("extreme", "evil", "nature", "lava", "wasser");

    public boolean doResetBooster() {
        return resetBoosterServers.contains(getSubServer().toLowerCase());
    }

    public boolean doShowBalance() {
        if(getSubServer().startsWith("cb")) return true;
        return showBalanceServers.contains(getSubServer());
    }

    public boolean doShowBankBalance() {
        if(getSubServer().equalsIgnoreCase("cb0")) return false;
        if(getSubServer().startsWith("cb")) return true;
        return showBankBalanceServers.contains(getSubServer());
    }

    public boolean isCityBuild() {
        if(getSubServer().equalsIgnoreCase("cb0")) return false;
        if(getSubServer().startsWith("cb")) return true;
        return cityBuilds.contains(getSubServer());
    }

    public String getSubServer() {
        return GrieferGames.getGriefergames().getSubServer();
    }
}
