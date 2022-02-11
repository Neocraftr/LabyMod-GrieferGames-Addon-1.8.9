package de.neocraftr.griefergames.grieferwert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrieferWertManager {

    private final String API_URL = "https://neoservices.ml/api/grieferwert.json";
    private final Gson gson = new Gson();
    private final Type gwItemType = new TypeToken<List<GrieferWertItem>>(){}.getType();

    private List<GrieferWertItem> items = new ArrayList<>();

    public void downloadList() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            items = gson.fromJson(reader, gwItemType);
        } catch (IOException e) {
            System.out.println("[GrieferGames-Addon] Error while downloading GrieferWert list: "+e);
        }
    }

    public void onItemTooltip(ItemStack stack, List<String> toolTip) {
        // TODO: Display price tooltip
    }

    public List<GrieferWertItem> searchByName(String name) {
        return items.stream().filter(item -> item.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    public List<GrieferWertItem> getItems() {
        return items;
    }
}
