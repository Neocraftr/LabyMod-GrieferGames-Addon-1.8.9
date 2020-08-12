package de.wuzlwuz.griefergames;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import de.wuzlwuz.griefergames.booster.Booster;
import de.wuzlwuz.griefergames.chat.Chat;
import de.wuzlwuz.griefergames.chatMenu.TeamMenu;
import de.wuzlwuz.griefergames.enums.EnumLanguages;
import de.wuzlwuz.griefergames.helper.Helper;
import de.wuzlwuz.griefergames.server.GrieferGamesServer;
import de.wuzlwuz.griefergames.settings.ModSettings;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
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
	private Runnable continueEnable;
	private static ModSettings settings;
	private String serverIp = "";
	private String secondServerIp = "";
	private boolean showModules = false;
	private boolean showBoosterDummy = false;
	private boolean vanishActive = false;
	private boolean auraActive = false;
	private boolean godActive = false;
	private boolean flyActive = false;
	private ModuleCategory moduleCategory;
	private List<Booster> boosters = new ArrayList<Booster>();
	private Helper helper;
	private List<Chat> chatModules = new ArrayList<Chat>();
	private boolean isInTeam = false;
	private String nickname = "";
	private String playerRank = "Spieler";
	private boolean newsStart = false;
	private int timeToWait = 0;
	private double income;

	public static GrieferGames getGriefergames() {
		return griefergames;
	}

	private static void setGriefergames(GrieferGames griefergames) {
		GrieferGames.griefergames = griefergames;
	}

	private Runnable getContinueEnable() {
		return continueEnable;
	}

	private void setContinueEnable(Runnable continueEnable) {
		this.continueEnable = continueEnable;
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

	public String getSecondServerIp() {
		return secondServerIp;
	}

	private void setSecondServerIp(String secondServerIp) {
		this.secondServerIp = secondServerIp;
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

	public boolean isAuraActive() {
		return auraActive;
	}

	public void setAuraActive(boolean auraActive) {
		this.auraActive = auraActive;
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
		if (getBoosters().size() > 0) {
			for (Booster curBooster : getBoosters()) {
				if (curBooster.getType().equalsIgnoreCase(booster.getType())) {
					found = true;
					if (booster.getCount() == -1) {
						curBooster.incrementCount();
						curBooster.addEndDates(booster.getEndDate());
					} else {
						curBooster.setCount(booster.getCount());
						if (booster.getEndDate() != null) {
							if (booster.getResetEndDates()) {
								curBooster.setEndDates(booster.getEndDates());
							} else {
								curBooster.addEndDates(booster.getEndDate());
							}
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
					if (booster.getCount() > 0) {
						booster.decreaseEndDates();
						booster.decreaseCount();
					}
				}
			}
		}
	}

	public Helper getHelper() {
		return helper;
	}

	private void setHelper(Helper helper) {
		this.helper = helper;
	}

	public List<Chat> getChatModules() {
		return chatModules;
	}

	public void addChatModule(Chat chatModule) {
		boolean found = false;
		for (Chat chatList : chatModules) {
			if (chatList.getName().equalsIgnoreCase(chatModule.getName())) {
				found = true;
			}
		}
		if (!found) {
			this.chatModules.add(chatModule);
		}
	}

	public boolean getIsInTeam() {
		return isInTeam;
	}

	public void setIsInTeam(boolean isInTeam) {
		TeamMenu.printMenu();
		this.isInTeam = isInTeam;
	}

	public boolean isNicknameActive() {
		return (!LabyModCore.getMinecraft().getPlayer().getName().trim().equalsIgnoreCase(getNickname()));
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		if (nickname.trim().length() <= 0) {
			this.nickname = LabyModCore.getMinecraft().getPlayer().getName().trim();
		} else {
			this.nickname = nickname;
		}
	}

	public String getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(String playerRank) {
		this.playerRank = playerRank;
	}

	public boolean getNewsStart() {
		return newsStart;
	}

	public void setNewsStart(boolean newsStart) {
		this.newsStart = newsStart;
	}

	public int getTimeToWait() {
		return timeToWait;
	}

	public void setTimeToWait(int timeToWait) {
		this.timeToWait = timeToWait;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	private GrieferGamesServer ggserver;

	public GrieferGamesServer getGGServer() {
		return ggserver;
	}

	public void setGGServer(GrieferGamesServer ggserver) {
		this.ggserver = ggserver;
	}

	@Override
	public void onEnable() {
		// save instance
		setGriefergames(this);

		// set ip
		setServerIp("griefergames.net");
		setSecondServerIp("griefergames.de");

		// setup helper
		setHelper(new Helper());

		// load settings
		setSettings(new ModSettings());

		// wait for settings to be loaded
		setContinueEnable(new Runnable() {
			@Override
			public void run() {
				// extend translations
				if(getSettings().getLanguage() == EnumLanguages.GAMELANGUAGE) {
					loadTranslations(null);
				} else {
					loadTranslations(getSettings().getLanguage().name());
				}

				// set module category
				setModuleCategory(new ModuleCategory(LanguageManager.translateOrReturnKey("modules_category_gg"),
						true, new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/icon.png"))));

				setGGServer(new GrieferGamesServer(Minecraft.getMinecraft()));

				// set server support
				getApi().registerServerSupport(getGriefergames(), getGGServer());

				// show / hide gg modules
				getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
					@Override
					public void accept(ServerData serverData) {
						boolean showModules = (serverData.getIp().toLowerCase().contains(getServerIp())
								|| serverData.getIp().toLowerCase().contains(getSecondServerIp()));
						setShowModules(showModules);
					}
				});

				System.out.println("[GrieferGames] enabled.");
			}
		});
	}

	public void loadTranslations(String lang) {
		if(lang == null) {
			List<String> items = Arrays.asList(LanguageManager.getLanguage().getName().split("_"));
			lang = items.get(0).toUpperCase();
		}
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
				InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
				prop.load(reader);
				reader.close();
				found = true;

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
		getContinueEnable().run();
	}

	@Override
	protected void fillSettings(final List<SettingsElement> settings) {
		getSettings().fillSettings(settings);
	}
}