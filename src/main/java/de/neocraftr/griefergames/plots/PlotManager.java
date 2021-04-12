package de.neocraftr.griefergames.plots;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.CityBuild;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlotManager {

    private final Gson gson = new Gson();
    private final Type plotsType = new TypeToken<List<Plot>>(){}.getType();

    private List<Plot> plots = new ArrayList<>();

    public void loadConfig() {
        if(getConfig().has("plots")) {
            plots = gson.fromJson(getConfig().get("plots"), plotsType);
        } else {
            // Create config entry
            savePlots();
        }
    }

    public void savePlots() {
        getConfig().add("plots", gson.toJsonTree(plots));
        saveConfig();
    }

    public List<Plot> getPlots(CityBuild cityBuild) {
        return plots.stream().filter(plot -> cityBuild == CityBuild.ALL || plot.getCityBuild() == cityBuild).collect(Collectors.toList());
    }

    public List<Plot> getPlots() {
        return plots;
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }

    private JsonObject getConfig() {
        return GrieferGames.getGriefergames().getConfig();
    }

    private void saveConfig() {
        GrieferGames.getGriefergames().saveConfig();
    }
}
