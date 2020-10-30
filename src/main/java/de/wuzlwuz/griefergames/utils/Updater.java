package de.wuzlwuz.griefergames.utils;

import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Updater {

    private static final String UPDATE_URL = "https://api.github.com/repos/Neocraftr/LabyMod-GrieferGames-Addon-1.8.9/releases/latest";

    private boolean updateAvailable = false;
    private String downloadUrl = null;
    private String latestVersion = null;
    private File addonJar = null;

    public Updater() {
        checkForUpdates();

        GrieferGames.getGriefergames().getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
            @Override
            public void accept(ServerData serverData) {
                if(updateAvailable) {
                    String msg = GrieferGames.PREFIX+"§3";
                    if(GrieferGames.getSettings().isAutoUpdate()) {
                        if(!canDoUpdate()) {
                            msg += LanguageManager.translateOrReturnKey("message_gg_updateFailed");
                        } else {
                            msg += LanguageManager.translateOrReturnKey("message_gg_updateReady");
                        }
                    } else {
                        msg += LanguageManager.translateOrReturnKey("message_gg_updateAvailable");
                    }
                    msg = msg.replace("${version}", "§ev"+latestVersion+"§3").replace("${dl_link}", "§e"+downloadUrl+"§3");
                    GrieferGames.getGriefergames().getApi().displayMessageInChat(msg);
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(updateAvailable && GrieferGames.getSettings().isAutoUpdate()) {
                update();
            }
        }));
    }

    private void checkForUpdates() {
        try {
            BufferedReader reader = Resources.asCharSource(new URL(UPDATE_URL), StandardCharsets.UTF_8).openBufferedStream();
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
            if(json.has("tag_name") && json.has("assets")) {
                latestVersion = json.get("tag_name").getAsString().replace("v", "");
                if(!GrieferGames.VERSION.equals(latestVersion)) {
                    JsonArray assets = json.get("assets").getAsJsonArray();
                    if(assets.size() > 0)  {
                        JsonObject grieferGamesAsset = assets.get(0).getAsJsonObject();
                        if(grieferGamesAsset.has("browser_download_url")) {
                            downloadUrl = grieferGamesAsset.get("browser_download_url").getAsString();
                            updateAvailable = true;
                        }
                    }
                }
            } else {
                System.out.println("[GrieferGames] Could not check for updates: Invalid response.");
            }
        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
            System.out.println("[GrieferGames] Could not check for updates: "+e.getMessage());
        }
    }

    private void update() {
        if(!canDoUpdate()) return;
        addonJar.delete();
        try {
            FileUtils.copyURLToFile(new URL(downloadUrl), addonJar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean canDoUpdate() {
        return addonJar != null && addonJar.isFile();
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setAddonJar(File addonJar) {
        this.addonJar = addonJar;
    }
}
