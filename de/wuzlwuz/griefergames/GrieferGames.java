package de.wuzlwuz.griefergames;

import java.util.List;

import de.wuzlwuz.griefergames.server.GrieferGamesServer;
import de.wuzlwuz.griefergames.settings.ModSettings;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.Minecraft;

public class GrieferGames extends LabyModAddon {
	private static GrieferGames griefergames;
	private static ModSettings settings;

	public static GrieferGames getGriefergames() {
		return griefergames;
	}

	public static void setGriefergames(GrieferGames griefergames) {
		GrieferGames.griefergames = griefergames;
	}

	public static ModSettings getSettings() {
		return settings;
	}

	public static void setSettings(ModSettings settings) {
		GrieferGames.settings = settings;
	}

	@Override
	public void onEnable() {
		setGriefergames(this);
		setSettings(new ModSettings());
		getApi().registerServerSupport(this, new GrieferGamesServer(Minecraft.func_71410_x()));
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
	}

	@Override
	public void loadConfig() {
		getSettings().loadConfig();
	}

	@Override
	protected void fillSettings(final List<SettingsElement> settings) {
		getSettings().fillSettings(settings);
	}
}