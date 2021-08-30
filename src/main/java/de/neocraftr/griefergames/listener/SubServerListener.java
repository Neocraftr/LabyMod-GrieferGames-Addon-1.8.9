package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;

import java.util.concurrent.TimeUnit;

public class SubServerListener {

    public void onSubServerChanged(String subServerName) {
        getGG().setNickname("");
        getGG().setNewsStart(false);
        getGG().setClearLagTime(0);

        getGG().getHelper().updateBalance("cash");
        getGG().getHelper().updateBalance("bank");

        if(getGG().getSettings().isShowPrefixInDisplayName()) {
            getGG().getHelper().colorizePlayerNames();
        }

        if(getGG().getSettings().isLabyChatShowSubServerEnabled()) {
            getGG().getHelper().updateLabyChatSubServer(subServerName);
        }
        if(getGG().getSettings().isDiscordShowSubServerEnabled()) {
            getGG().getHelper().updateDiscordSubServer(subServerName);
        }

        if (getGG().getSubServerGroups().doResetBooster()) {
            getGG().getBoosterModule().resetBoosters();
        }

        if(getGG().getSubServerGroups().isCityBuild()) {
            getGG().setCityBuildDelay(false);
            getGG().setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
        } else if(subServerName.equalsIgnoreCase("skyblock")) {
            if(!getGG().isCityBuildDelay()) getGG().setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
        } else if(subServerName.equalsIgnoreCase("portal")) {
            if(!getGG().isCityBuildDelay()) getGG().setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(12));
        } else {
            if(!getGG().isCityBuildDelay()) getGG().setWaitTime(0);
        }

        getGG().setGodActive(false);
        getGG().setVanishActive(getGG().getPlayerRankGroups().getDefaultVanishState(getGG().getPlayerRank()));
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
