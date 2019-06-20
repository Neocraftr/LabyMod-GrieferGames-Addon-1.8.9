package de.wuzlwuz.griefergames.settings;

import java.util.List;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.NumberElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

public class ModSettings {
	/*
	 * Ä, ä \u00c4, \u00e4 | Ö, ö \u00d6, \u00f6 | Ü, ü \u00dc, \u00fc | ß \u00df
	 */
	private boolean modEnabled = true;

	private boolean privateChatRight = true;
	private boolean plotChatRight = true;
	private boolean filterDuplicateMessages = false;
	private Integer filterDuplicateMessagesTime = 5;

	private boolean cleanBlanks = false;
	private boolean cleanSupremeBlanks = false;

	private boolean payChatRight = true;
	private boolean payAchievement = true;
	private boolean payMarker = false;
	private boolean payHover = false;

	private boolean bankChatRight = true;
	private boolean bankAchievement = true;

	private boolean clearlagChatRight = true;

	private boolean mobRemoverChatRight = true;
	private boolean mobRemoverLastTimeHover = true;

	private boolean betterIgnoreList = true;

	private boolean serverSwitchMsg = false;

	public ModSettings() {
		// constructor
	}

	public boolean isModEnabled() {
		return this.modEnabled;
	}

	private void setModEnabled(boolean modEnabled) {
		this.modEnabled = modEnabled;
	}

	public boolean isPrivateChatRight() {
		return this.privateChatRight;
	}

	private void setPrivateChatRight(boolean privateChatRight) {
		this.privateChatRight = privateChatRight;
	}

	public boolean isPlotChatRight() {
		return this.plotChatRight;
	}

	private void setPlotChatRight(boolean plotChatRight) {
		this.plotChatRight = plotChatRight;

	}

	public boolean isFilterDuplicateMessages() {
		return this.filterDuplicateMessages;
	}

	private void setFilterDuplicateMessages(boolean filterDuplicateMessages) {
		this.filterDuplicateMessages = filterDuplicateMessages;
	}

	public Integer getFilterDuplicateMessagesTime() {
		return filterDuplicateMessagesTime;
	}

	public void setFilterDuplicateMessagesTime(Integer filterDuplicateMessagesTime) {
		this.filterDuplicateMessagesTime = filterDuplicateMessagesTime;
	}

	public boolean isCleanBlanks() {
		return this.cleanBlanks;
	}

	private void setCleanBlanks(boolean cleanBlanks) {
		this.cleanBlanks = cleanBlanks;
	}

	public boolean isCleanSupremeBlanks() {
		return this.cleanSupremeBlanks;
	}

	private void setCleanSupremeBlanks(boolean cleanSupremeBlanks) {
		this.cleanSupremeBlanks = cleanSupremeBlanks;
	}

	public boolean isPayChatRight() {
		return this.payChatRight;
	}

	private void setPayChatRight(boolean payChatRight) {
		this.payChatRight = payChatRight;
	}

	public boolean isPayAchievement() {
		return this.payAchievement;
	}

	private void setPayAchievement(boolean payAchievement) {
		this.payAchievement = payAchievement;
	}

	public boolean isPayMarker() {
		return this.payMarker;
	}

	private void setPayMarker(boolean payMarker) {
		this.payMarker = payMarker;
	}

	public boolean isPayHover() {
		return this.payHover;
	}

	private void setPayHover(boolean payHover) {
		this.payHover = payHover;
	}

	public boolean isBankChatRight() {
		return this.bankChatRight;
	}

	private void setBankChatRight(boolean bankChatRight) {
		this.bankChatRight = bankChatRight;
	}

	public boolean isBankAchievement() {
		return this.bankAchievement;
	}

	private void setBankAchievement(boolean bankAchievement) {
		this.bankAchievement = bankAchievement;
	}

	public boolean isClearlagChatRight() {
		return this.clearlagChatRight;
	}

	private void setClearlagChatRight(boolean clearlagChatRight) {
		this.clearlagChatRight = clearlagChatRight;
	}

	public boolean isMobRemoverChatRight() {
		return this.mobRemoverChatRight;
	}

	private void setMobRemoverChatRight(boolean mobRemoverChatRight) {
		this.mobRemoverChatRight = mobRemoverChatRight;
	}

	public boolean isMobRemoverLastTimeHover() {
		return this.mobRemoverLastTimeHover;
	}

	private void setMobRemoverLastTimeHover(boolean mobRemoverLastTimeHover) {
		this.mobRemoverLastTimeHover = mobRemoverLastTimeHover;
	}

	public boolean isBetterIgnoreList() {
		return this.betterIgnoreList;
	}

	private void setBetterIgnoreList(boolean betterIgnoreList) {
		this.betterIgnoreList = betterIgnoreList;
	}

	public boolean isServerSwitchMsg() {
		return this.serverSwitchMsg;
	}

	private void setServerSwitchMsg(boolean serverSwitchMsg) {
		this.serverSwitchMsg = serverSwitchMsg;
	}

	public void loadConfig() {
		if (GrieferGames.getGriefergames().getConfig().has("modEnabled"))
			setModEnabled(GrieferGames.getGriefergames().getConfig().get("modEnabled").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("privateChatRight"))
			setPrivateChatRight(GrieferGames.getGriefergames().getConfig().get("privateChatRight").getAsBoolean());
		if (GrieferGames.getGriefergames().getConfig().has("filterDuplicateMessages"))
			setFilterDuplicateMessages(
					GrieferGames.getGriefergames().getConfig().get("filterDuplicateMessages").getAsBoolean());
		if (GrieferGames.getGriefergames().getConfig().has("filterDuplicateMessagesTime"))
			setFilterDuplicateMessagesTime(
					GrieferGames.getGriefergames().getConfig().get("filterDuplicateMessagesTime").getAsInt());

		if (GrieferGames.getGriefergames().getConfig().has("cleanBlanks"))
			setCleanBlanks(GrieferGames.getGriefergames().getConfig().get("cleanBlanks").getAsBoolean());
		if (GrieferGames.getGriefergames().getConfig().has("cleanSupremeBlanks"))
			setCleanSupremeBlanks(GrieferGames.getGriefergames().getConfig().get("cleanSupremeBlanks").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("payChatRight"))
			setPayChatRight(GrieferGames.getGriefergames().getConfig().get("payChatRight").getAsBoolean());
		if (GrieferGames.getGriefergames().getConfig().has("payAchievement"))
			setPayAchievement(GrieferGames.getGriefergames().getConfig().get("payAchievement").getAsBoolean());
		if (GrieferGames.getGriefergames().getConfig().has("payMarker"))
			setPayMarker(GrieferGames.getGriefergames().getConfig().get("payMarker").getAsBoolean());
		if (GrieferGames.getGriefergames().getConfig().has("payHover"))
			setPayHover(GrieferGames.getGriefergames().getConfig().get("payHover").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("bankChatRight"))
			setBankChatRight(GrieferGames.getGriefergames().getConfig().get("bankChatRight").getAsBoolean());
		if (GrieferGames.getGriefergames().getConfig().has("bankAchievement"))
			setBankAchievement(GrieferGames.getGriefergames().getConfig().get("bankAchievement").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("plotChatRight"))
			setPlotChatRight(GrieferGames.getGriefergames().getConfig().get("plotChatRight").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("clearlagChatRight"))
			setClearlagChatRight(GrieferGames.getGriefergames().getConfig().get("clearlagChatRight").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("mobRemoverChatRight"))
			setMobRemoverChatRight(
					GrieferGames.getGriefergames().getConfig().get("mobRemoverChatRight").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("mobRemoverLastTimeHover"))
			setMobRemoverLastTimeHover(
					GrieferGames.getGriefergames().getConfig().get("mobRemoverLastTimeHover").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("betterIgnoreList"))
			setBetterIgnoreList(GrieferGames.getGriefergames().getConfig().get("betterIgnoreList").getAsBoolean());

		if (GrieferGames.getGriefergames().getConfig().has("betterIgnoreList"))
			setServerSwitchMsg(GrieferGames.getGriefergames().getConfig().get("betterIgnoreList").getAsBoolean());
	}

	public void fillSettings(final List<SettingsElement> settings) {
		settings.add(new HeaderElement("Allgemein"));

		final BooleanElement modEnabledBtn = new BooleanElement("Addon Aktiv",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean modEnabled) {
						setModEnabled(modEnabled);
						GrieferGames.getGriefergames().getConfig().addProperty("modEnabled", modEnabled);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isModEnabled());
		settings.add(modEnabledBtn);

		settings.add(new HeaderElement("Nachrichten"));
		final BooleanElement privateChatRightBtn = new BooleanElement("Private Nachrichten 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean privateChatRight) {
						setPrivateChatRight(privateChatRight);
						GrieferGames.getGriefergames().getConfig().addProperty("privateChatRight", privateChatRight);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isPrivateChatRight());
		settings.add(privateChatRightBtn);

		final BooleanElement plotChatRightBtn = new BooleanElement("Plot Chat 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean plotChatRight) {
						setPlotChatRight(plotChatRight);
						GrieferGames.getGriefergames().getConfig().addProperty("plotChatRight", plotChatRight);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isPlotChatRight());
		settings.add(plotChatRightBtn);

		final BooleanElement filterDuplicateMessagesBtn = new BooleanElement("Doppelte Nachrichten l\u00f6schen",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean filterDuplicateMessages) {
						setFilterDuplicateMessages(filterDuplicateMessages);
						GrieferGames.getGriefergames().getConfig().addProperty("filterDuplicateMessages",
								filterDuplicateMessages);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isFilterDuplicateMessages());
		settings.add(filterDuplicateMessagesBtn);

		final NumberElement filterDuplicateMessagesTimeNumber = new NumberElement("Zeitabstand doppelte Nachrichten",
				new ControlElement.IconData("labymod/textures/settings/settings/autotext.png"),
				getFilterDuplicateMessagesTime());
		filterDuplicateMessagesTimeNumber.setMinValue(3);
		filterDuplicateMessagesTimeNumber.setMaxValue(120);
		filterDuplicateMessagesTimeNumber.addCallback(new Consumer<Integer>() {
			@Override
			public void accept(Integer filterDuplicateMessagesTime) {
				setFilterDuplicateMessagesTime(filterDuplicateMessagesTime);
				GrieferGames.getGriefergames().getConfig().addProperty("filterDuplicateMessagesTime",
						filterDuplicateMessagesTime);
				GrieferGames.getGriefergames().saveConfig();
			}
		});
		settings.add(filterDuplicateMessagesTimeNumber);

		settings.add(new HeaderElement("Leerzeilen"));
		final BooleanElement cleanBlanksBtn = new BooleanElement("Leerzeilen l\u00f6schen",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean cleanBlanks) {
						setCleanBlanks(cleanBlanks);
						GrieferGames.getGriefergames().getConfig().addProperty("cleanBlanks", cleanBlanks);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isCleanBlanks());
		settings.add(cleanBlanksBtn);

		final BooleanElement cleanSupremeBlanksBtn = new BooleanElement("Supreme Leerzeilen l\u00f6schen",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean cleanSupremeBlanks) {
						setCleanSupremeBlanks(cleanSupremeBlanks);
						GrieferGames.getGriefergames().getConfig().addProperty("cleanSupremeBlanks",
								cleanSupremeBlanks);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isCleanSupremeBlanks());
		settings.add(cleanSupremeBlanksBtn);

		settings.add(new HeaderElement("Bezahlung / Zahlung"));
		final BooleanElement payChatRightBtn = new BooleanElement("Bezahlung 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean payChatRight) {
						setPayChatRight(payChatRight);
						GrieferGames.getGriefergames().getConfig().addProperty("payChatRight", payChatRight);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isPayChatRight());
		settings.add(payChatRightBtn);

		final BooleanElement payAchievementBtn = new BooleanElement("Bezahlung Fortschrittsmeldung",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean payAchievement) {
						setPayAchievement(payAchievement);
						GrieferGames.getGriefergames().getConfig().addProperty("payAchievement", payAchievement);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isPayAchievement());
		settings.add(payAchievementBtn);

		final BooleanElement payMarkerBtn = new BooleanElement("Bezahlung markieren",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean payMarker) {
						setPayMarker(payMarker);
						GrieferGames.getGriefergames().getConfig().addProperty("payMarker", payMarker);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isPayMarker());
		settings.add(payMarkerBtn);

		final BooleanElement payHoverBtn = new BooleanElement("Bezahlung hover",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean payHover) {
						setPayHover(payHover);
						GrieferGames.getGriefergames().getConfig().addProperty("payHover", payHover);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isPayHover());
		settings.add(payHoverBtn);

		settings.add(new HeaderElement("Bank"));
		final BooleanElement bankChatRightBtn = new BooleanElement("Bank 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean bankChatRight) {
						setBankChatRight(bankChatRight);
						GrieferGames.getGriefergames().getConfig().addProperty("bankChatRight", bankChatRight);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isBankChatRight());
		settings.add(bankChatRightBtn);

		final BooleanElement bankAchievementBtn = new BooleanElement("Bank Fortschrittsmeldung",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean bankAchievement) {
						setBankAchievement(bankAchievement);
						GrieferGames.getGriefergames().getConfig().addProperty("bankAchievement", bankAchievement);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isBankAchievement());
		settings.add(bankAchievementBtn);

		settings.add(new HeaderElement("ClearLag"));
		final BooleanElement clearlagChatRightBtn = new BooleanElement("ClearLag 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean clearlagChatRight) {
						setClearlagChatRight(clearlagChatRight);
						GrieferGames.getGriefergames().getConfig().addProperty("clearlagChatRight", clearlagChatRight);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isClearlagChatRight());
		settings.add(clearlagChatRightBtn);

		settings.add(new HeaderElement("MobRemover"));
		final BooleanElement mobRemoverChatRightBtn = new BooleanElement("Mobremover 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean mobRemoverChatRight) {
						setMobRemoverChatRight(mobRemoverChatRight);
						GrieferGames.getGriefergames().getConfig().addProperty("mobRemoverChatRight",
								mobRemoverChatRight);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isMobRemoverChatRight());
		settings.add(mobRemoverChatRightBtn);

		final BooleanElement mobRemoverLastTimeHoverBtn = new BooleanElement("Mobremover Zeitstempel hover",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean mobRemoverLastTimeHover) {
						setMobRemoverLastTimeHover(mobRemoverLastTimeHover);
						GrieferGames.getGriefergames().getConfig().addProperty("mobRemoverLastTimeHover",
								mobRemoverLastTimeHover);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isMobRemoverLastTimeHover());
		settings.add(mobRemoverLastTimeHoverBtn);

		settings.add(new HeaderElement("Ignorierte Spieler"));
		final BooleanElement betterIgnoreListBtn = new BooleanElement("verbesserte Ignorierliste",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean betterIgnoreList) {
						setBetterIgnoreList(betterIgnoreList);
						GrieferGames.getGriefergames().getConfig().addProperty("betterIgnoreList", betterIgnoreList);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isBetterIgnoreList());
		settings.add(betterIgnoreListBtn);

		settings.add(new HeaderElement("Server-Change"));
		final BooleanElement serverSwitchMsgBtn = new BooleanElement("Nachricht bei Serverwechsel",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean serverSwitchMsg) {
						setServerSwitchMsg(serverSwitchMsg);
						GrieferGames.getGriefergames().getConfig().addProperty("serverSwitchMsg", serverSwitchMsg);
						GrieferGames.getGriefergames().saveConfig();
					}
				}, isServerSwitchMsg());
		settings.add(serverSwitchMsgBtn);
	}
}