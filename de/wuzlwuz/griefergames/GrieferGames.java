package de.wuzlwuz.griefergames;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import de.wuzlwuz.griefergames.booster.Booster;
import de.wuzlwuz.griefergames.server.GrieferGamesServer;
import de.wuzlwuz.griefergames.settings.ModSettings;
import net.labymod.api.LabyModAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GrieferGames extends LabyModAddon {
	private static GrieferGames griefergames;
	private static ModSettings settings;
	private String serverIp = "";
	private boolean showModules = false;
	private boolean showBoosterDummy = false;
	private boolean vanishActive = false;
	private boolean godActive = false;
	private boolean flyActive = false;
	private ModuleCategory moduleCategory;
	private List<Booster> boosters = new ArrayList<Booster>();

	public static GrieferGames getGriefergames() {
		return griefergames;
	}

	private static void setGriefergames(GrieferGames griefergames) {
		GrieferGames.griefergames = griefergames;
	}

	public static ModSettings getSettings() {
		return settings;
	}

	private static void setSettings(ModSettings settings) {
		GrieferGames.settings = settings;
	}

	public String getServerIp() {
		return serverIp;
	}

	private void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public boolean isShowModules() {
		return showModules;
	}

	public void setShowModules(boolean showModules) {
		this.showModules = showModules;
	}

	public boolean isShowBoosterDummy() {
		return showBoosterDummy;
	}

	public void setShowBoosterDummy(boolean showBoosterDummy) {
		this.showBoosterDummy = showBoosterDummy;
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

	public ModuleCategory getModuleCategory() {
		return moduleCategory;
	}

	public void setModuleCategory(ModuleCategory moduleCategory) {
		this.moduleCategory = moduleCategory;
	}

	public List<Booster> getBoosters() {
		return boosters;
	}

	public void setBoosters(List<Booster> boosters) {
		this.boosters = boosters;
	}

	public void addBooster(Booster booster) {
		boolean found = false;
		if (boosters.size() > 0) {
			for (Booster boosterList : boosters) {
				if (boosterList.getType().equalsIgnoreCase(booster.getType())) {
					found = true;
					if (booster.getCount() == -1) {
						boosterList.incrementCount();
						boosterList.setEndDate(booster.getEndDate());
					} else {
						boosterList.setCount(booster.getCount());
						if (booster.getEndDate() != null) {
							boosterList.setEndDate(booster.getEndDate());
						}
					}
				}
			}
		}
		if (!found) {
			if (booster.getCount() == -1) {
				booster.setCount(1);
			}
			this.boosters.add(booster);
		}
	}

	public void delBooster(Booster booster) {
		this.boosters.remove(booster);
	}

	public void boosterDone(String type) {
		if (boosters.size() > 0) {
			for (Booster booster : boosters) {
				if (booster.getType().equalsIgnoreCase(type)) {
					booster.setCount(0);
				}
			}
		}
	}

	@Override
	public void onEnable() {
		System.out.println("[GrieferGames] enabled.");
		// set ip
		setServerIp("griefergames.net");

		// save instance
		setGriefergames(this);

		// load settings
		setSettings(new ModSettings());

		// extend translations
		loadTranslations();

		// set module category
		setModuleCategory(new ModuleCategory(LanguageManager.translateOrReturnKey("modules_category_gg", new Object[0]),
				true, new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/icon.png"))));

		// set server support
		getApi().registerServerSupport(this, new GrieferGamesServer(Minecraft.getMinecraft()));

		// show / hide gg modules
		getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
			@Override
			public void accept(ServerData serverData) {
				setShowModules(serverData.getIp().toLowerCase().indexOf(getServerIp()) >= 0);
			}
		});
	}

	private void loadTranslations() {
		List<String> items = Arrays.asList(LanguageManager.getLanguage().getName().split("_"));
		String lang = items.get(items.size() - 1).toUpperCase();
		try {
			Properties prop = new Properties();
			boolean found = false;
			InputStream defaultStream = GrieferGames.class
					.getResourceAsStream("/assets/minecraft/griefergames/lang/EN.properties");

			InputStream stream = GrieferGames.class
					.getResourceAsStream("/assets/minecraft/griefergames/lang/" + lang + ".properties");

			if (stream == null) {
				stream = defaultStream;
				System.out.println("[GrieferGames] Couldn't load language file " + lang);
			}

			if (stream != null) {
				InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
				prop.load(reader);
				reader.close();
				found = true;

				@SuppressWarnings("unchecked")
				Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
				while (enums.hasMoreElements()) {
					String key = enums.nextElement();
					String value = prop.getProperty(key);
					LanguageManager.getLanguage().add(key, value);
				}
			}

			if (!found) {
				System.out.println("[GrieferGames] Couldn't load language file EN");
			}

		} catch (Exception error) {
			error.printStackTrace();
			System.out.println("[GrieferGames] Couldn't load language file " + lang + " " + error.getMessage());
		}
	}

	@Override
	public void onDisable() {
		System.out.println("[GrieferGames] disabled.");
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