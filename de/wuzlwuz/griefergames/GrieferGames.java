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
	private boolean vanishActive = false;
	private boolean godActive = false;
	private boolean flyActive = false;

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

	public boolean isVanishActive() {
		return vanishActive;
	}

	public void setVanishActive(boolean vanishActive) {
		this.vanishActive = vanishActive;
	}

	public boolean isGodActive() {
		return godActive;
	}

	public void setGodActive(boolean godActive) {
		this.godActive = godActive;
	}

	public boolean isFlyActive() {
		return flyActive;
	}

	public void setFlyActive(boolean flyActive) {
		this.flyActive = flyActive;
	}

	@Override
	public void onEnable() {
		setGriefergames(this);
		setSettings(new ModSettings());
		getApi().registerServerSupport(this, new GrieferGamesServer(Minecraft.getMinecraft()));
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