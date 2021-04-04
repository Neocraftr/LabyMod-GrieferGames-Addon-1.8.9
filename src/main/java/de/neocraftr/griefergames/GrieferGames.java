package de.neocraftr.griefergames;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import de.neocraftr.griefergames.booster.Booster;
import de.neocraftr.griefergames.listener.SubServerListener;
import de.neocraftr.griefergames.server.GrieferGamesServer;
import de.neocraftr.griefergames.settings.ModSettings;
import de.neocraftr.griefergames.chat.Chat;
import de.neocraftr.griefergames.enums.EnumLanguages;
import de.neocraftr.griefergames.utils.FileManager;
import de.neocraftr.griefergames.utils.Helper;
import de.neocraftr.griefergames.utils.Updater;
import net.labymod.addon.AddonLoader;
import net.labymod.addon.online.AddonInfoManager;
import net.labymod.addon.online.info.AddonInfo;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public class GrieferGames extends LabyModAddon {
	public static final String PREFIX = "§8[§6GrieferGames-Addon§8] §r";
	public static final String VERSION = "1.9.0";
	public static final String SERVER_IP = "griefergames.net", SECOND_SERVER_IP = "griefergames.de";

	private static GrieferGames griefergames;
	private ModSettings settings;
	private SubServerListener subServerListener;
	private GrieferGamesServer ggserver;
	private Updater updater;
	private Helper helper;
	private FileManager fileManager;

	private boolean onGrieferGames = false;
	private boolean showBoosterDummy = false;
	private boolean vanishActive = false;
	private boolean auraActive = false;
	private boolean godActive = false;
	private boolean flyActive = false;
	private boolean newsStart = false;
	private boolean afk = false;
	private boolean firstJoin = false;
	private boolean hideBoosterMenu = false;
	private ModuleCategory moduleCategory;
	private List<Booster> boosters = new ArrayList<Booster>();
	private List<Chat> chatModules = new ArrayList<Chat>();
	private String nickname = "";
	private String playerRank = "";
	private String subServer = "";
	private String filterDuplicateLastMessage = "";
	private String lastLabyChatSubServer = "", lastDiscordSubServer = "";
	private int timeToWait = 0;
	private double income = 0;
	private long lastActiveTime = System.currentTimeMillis();
	private BlockPos lastPlayerPosition = new BlockPos(0, 0, 0);

	public static GrieferGames getGriefergames() {
		return griefergames;
	}

	@Override
	public void onEnable() {
		griefergames = this;

		updater = new Updater();
		helper = new Helper();
		settings = new ModSettings();
		fileManager = new FileManager();

		System.out.println("[GrieferGames-Addon] enabled.");
	}

	public void loadTranslations() {
		String lang = settings.getLanguage().name();
		if(settings.getLanguage() == EnumLanguages.GAMELANGUAGE) {
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
				System.out.println("[GrieferGames-Addon] Couldn't load language file " + lang);
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
				System.out.println("[GrieferGames-Addon] Couldn't load language file EN");
			}

		} catch (Exception error) {
			error.printStackTrace();
			System.out.println("[GrieferGames-Addon] Couldn't load language file " + lang + " " + error.getMessage());
		}
	}

	public AddonInfo getAddonnfo() {
		AddonInfoManager manager = AddonInfoManager.getInstance();
		manager.init();
		AddonInfo addonInfo = manager.getAddonInfoMap().get(about.uuid);
		if (addonInfo == null) {
			addonInfo = AddonLoader.getOfflineAddons().stream()
					.filter(addon -> addon.getUuid().equals(about.uuid))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Unable to find addon info of \"" + about.name + "\" (" + about.uuid + ")!"));
		}
		return addonInfo;
	}

	@Override
	public void onDisable() {
		System.out.println("[GrieferGames-Addon] disabled.");
	}

	@Override
	public void loadConfig() {
		updater.setAddonJar(AddonLoader.getFiles().get(about.uuid));

		settings.loadConfig();

		loadTranslations();

		moduleCategory = new ModuleCategory(LanguageManager.translateOrReturnKey("modules_category_gg"),
				true, new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/icon.png")));

		subServerListener = new SubServerListener();
		ggserver = new GrieferGamesServer();
		getApi().registerServerSupport(griefergames, ggserver);
	}

	@Override
	protected void fillSettings(final List<SettingsElement> settingsList) {
		settings.fillSettings(settingsList);
	}


	public ModSettings getSettings() {
		return settings;
	}

	public GrieferGamesServer getGGServer() {
		return ggserver;
	}

	public Updater getUpdater() {
		return updater;
	}

	public Helper getHelper() {
		return helper;
	}

	public FileManager getFileManager() {
		return fileManager;
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

	public List<Booster> getBoosters() {
		return boosters;
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

	public boolean isNicknameActive() {
		return !LabyModCore.getMinecraft().getPlayer().getName().trim().equalsIgnoreCase(nickname);
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

	public boolean isAfk() {
		return afk;
	}

	public void setAfk(boolean afk) {
		this.afk = afk;
	}

	public BlockPos getLastPlayerPosition() {
		return lastPlayerPosition;
	}

	public void setLastPlayerPosition(BlockPos lastPlayerPosition) {
		this.lastPlayerPosition = lastPlayerPosition;
	}

	public void callSubServerEvent(String subServerNameOld, String subServerName) {
		subServerListener.onSubServerChanged(subServerNameOld, subServerName);
	}

	public boolean isFirstJoin() {
		return firstJoin;
	}

	public void setFirstJoin(boolean firstJoin) {
		this.firstJoin = firstJoin;
	}

	public long getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(long lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	public String getSubServer() {
		return subServer;
	}

	public void setSubServer(String subServer) {
		this.subServer = subServer;
	}

	public String getFilterDuplicateLastMessage() {
		return filterDuplicateLastMessage;
	}

	public void setFilterDuplicateLastMessage(String filterDuplicateLastMessage) {
		this.filterDuplicateLastMessage = filterDuplicateLastMessage;
	}

	public String getLastDiscordSubServer() {
		return lastDiscordSubServer;
	}

	public void setLastDiscordSubServer(String lastDiscordSubServer) {
		this.lastDiscordSubServer = lastDiscordSubServer;
	}

	public String getLastLabyChatSubServer() {
		return lastLabyChatSubServer;
	}

	public void setLastLabyChatSubServer(String lastLabyChatSubServer) {
		this.lastLabyChatSubServer = lastLabyChatSubServer;
	}

	public boolean isOnGrieferGames() {
		return onGrieferGames;
	}

	public void setOnGrieferGames(boolean onGrieferGames) {
		this.onGrieferGames = onGrieferGames;
	}

	public boolean isHideBoosterMenu() {
		return hideBoosterMenu;
	}

	public void setHideBoosterMenu(boolean hideBoosterMenu) {
		this.hideBoosterMenu = hideBoosterMenu;
	}
}
