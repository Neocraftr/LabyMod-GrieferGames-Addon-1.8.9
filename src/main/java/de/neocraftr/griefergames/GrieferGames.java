package de.neocraftr.griefergames;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import de.neocraftr.griefergames.listener.SubServerListener;
import de.neocraftr.griefergames.modules.BoosterModule;
import de.neocraftr.griefergames.plots.PlotManager;
import de.neocraftr.griefergames.plots.gui.PlotSwitchGui;
import de.neocraftr.griefergames.server.GrieferGamesServer;
import de.neocraftr.griefergames.settings.ModSettings;
import de.neocraftr.griefergames.chat.Chat;
import de.neocraftr.griefergames.enums.EnumLanguages;
import de.neocraftr.griefergames.utils.*;
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
	public static final String VERSION = "1.16.2";
	public static final String SERVER_IP = "griefergames.net", SECOND_SERVER_IP = "griefergames.de";

	private static GrieferGames griefergames;
	private ModSettings settings;
	private SubServerListener subServerListener;
	private GrieferGamesServer ggserver;
	private Updater updater;
	private Helper helper;
	private FileManager fileManager;
	private PlotManager plotManager;
	private PlotSwitchGui plotSwitchGui;
	private BoosterModule boosterModule;
	private SubServerGroups subServerGroups;
	private PlayerRankGroups playerRankGroups;

	private boolean onGrieferGames = false;
	private boolean vanishActive = false;
	private boolean auraActive = false;
	private boolean godActive = false;
	private boolean redstoneActive = false;
	private boolean newsStart = false;
	private boolean afk = false;
	private boolean hideBoosterMenu = false;
	private boolean cityBuildDelay = false;
	private ModuleCategory moduleCategory;
	private List<Chat> chatModules = new ArrayList<Chat>();
	private String nickname = "";
	private String playerRank = "";
	private String subServer = "";
	private String filterDuplicateLastMessage = "";
	private String lastLabyChatSubServer = "", lastDiscordSubServer = "";
	private long waitTime = 0;
	private long clearLagTime = 0;
	private double income = 0;
	private int balance = 0;
	private int bankBalance = 0;
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
		plotManager = new PlotManager();
		plotSwitchGui = new PlotSwitchGui();
		subServerGroups = new SubServerGroups();
		playerRankGroups = new PlayerRankGroups();

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
	public void loadConfig() {
		updater.setAddonJar(AddonLoader.getFiles().get(about.uuid));

		settings.loadConfig();
		plotManager.loadConfig();

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

	public PlotManager getPlotManager() {
		return plotManager;
	}

	public PlotSwitchGui getPlotSwitchGui() {
		return plotSwitchGui;
	}

	public BoosterModule getBoosterModule() {
		return boosterModule;
	}

	public SubServerGroups getSubServerGroups() {
		return subServerGroups;
	}

	public PlayerRankGroups getPlayerRankGroups() {
		return playerRankGroups;
	}

	public void setBoosterModule(BoosterModule boosterModule) {
		this.boosterModule = boosterModule;
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

	public boolean isRedstoneActive() {
		return redstoneActive;
	}
	public void setRedstoneActive(boolean redstoneActive) {
		this.redstoneActive = redstoneActive;
	}

	public ModuleCategory getModuleCategory() {
		return moduleCategory;
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

	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}

	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getBankBalance() {
		return bankBalance;
	}
	public void setBankBalance(int bankBalance) {
		this.bankBalance = bankBalance;
	}

	public long getClearLagTime() {
		return clearLagTime;
	}
	public void setClearLagTime(long clearLagTime) {
		this.clearLagTime = clearLagTime;
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

	public void callSubServerEvent(String subServerName) {
		subServerListener.onSubServerChanged(subServerName);
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

	public boolean isCityBuildDelay() {
		return cityBuildDelay;
	}

	public void setCityBuildDelay(boolean cityBuildDelay) {
		this.cityBuildDelay = cityBuildDelay;
	}
}
