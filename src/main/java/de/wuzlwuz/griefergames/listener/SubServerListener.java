package de.wuzlwuz.griefergames.listener;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.modules.AuraModule;
import de.wuzlwuz.griefergames.modules.GodmodeModule;
import de.wuzlwuz.griefergames.modules.VanishModule;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.Minecraft;

public class SubServerListener {

    private boolean modulesLoaded = false;

    public void onSubServerChanged(String subServerNameOld, String subServerName) {
        getGG().setNickname("");
        getGG().setNewsStart(false);

        if(getGG().getSettings().isLabyChatShowSubServerEnabled()) {
            getGG().getHelper().updateLabyChatSubServer(subServerName);
        }
        if(getGG().getSettings().isDiscordShowSubServerEnabled()) {
            getGG().getHelper().updateDiscordSubServer(subServerName);
        }

        if (getGG().getHelper().doResetBoosterBySubserver(subServerName)) {
            getGG().getBoosters().clear();
        }

        if(getGG().getHelper().doHaveToWaitAfterJoin(subServerName)) {
            getGG().setTimeToWait(15);
        } else if(subServerName.equalsIgnoreCase("portal")) {
            getGG().setTimeToWait(12);
        } else {
            getGG().setTimeToWait(0);
        }

        if (subServerName.equalsIgnoreCase("lobby")) {
            getGG().setTimeToWait(0);
            //getGG().setShowBoosterDummy(true);

            Thread thread = new Thread() {
                public void run() {
                    try {
                        int errorCount = 0;
                        while(errorCount < 3) {
                            if(getGG().getHelper().loadPlayerRank()) {
                                if (!modulesLoaded) {
                                    modulesLoaded = true;

                                    if (getGG().getHelper().showGodModule(getGG().getPlayerRank())) {
                                        new GodmodeModule();
                                    }
                                    if (getGG().getHelper().showAuraModule(getGG().getPlayerRank())) {
                                        new AuraModule();
                                    }
                                    if (getGG().getHelper().showVanishModule(getGG().getPlayerRank())) {
                                        new VanishModule();
                                    }
                                }
                                if(getGG().isFirstJoin() && getGG().getSettings().isAutoPortl()) {
                                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/portal");
                                }
                                break;
                            } else {
                                errorCount++;
                                Thread.sleep(500);
                            }
                        }
                        if(errorCount >= 3) {
                            getGG().getApi().displayMessageInChat(GrieferGames.PREFIX+"§4"+ LanguageManager.translateOrReturnKey("message_gg_error")+
                                    ": §c"+LanguageManager.translateOrReturnKey("message_gg_rankError"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getGG().setFirstJoin(false);
                }
            };
            thread.start();
        }
        getGG().setGodActive(false);
        getGG().setVanishActive(getGG().getHelper().vanishDefaultState(getGG().getPlayerRank()));
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
