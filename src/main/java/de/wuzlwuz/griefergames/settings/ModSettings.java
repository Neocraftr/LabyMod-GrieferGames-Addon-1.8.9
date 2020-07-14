package de.wuzlwuz.griefergames.settings;

import java.util.List;

import com.google.gson.JsonObject;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.enums.EnumRealnameShown;
import de.wuzlwuz.griefergames.enums.EnumSounds;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.NumberElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

public class ModSettings {
	private boolean modEnabled = true;

	private boolean showChatTime = false;
	private boolean privateChatRight = true;
	private boolean plotChatRight = true;
	private EnumSounds privateChatSound = EnumSounds.NONE;
	private EnumRealnameShown realname = EnumRealnameShown.DEFAULT;
	// private boolean realnameClick = false;
	private boolean msgDisplayNameClick = false;
	private boolean clanTagClick = false;
	private boolean filterDuplicateMessages = false;
	private Integer filterDuplicateMessagesTime = 5;

	private boolean cleanBlanks = false;
	private boolean cleanSupremeBlanks = false;

	private boolean payChatRight = true;
	private boolean payAchievement = false;
	private boolean payMarker = false;
	private boolean payHover = false;

	private boolean bankChatRight = true;
	private boolean bankAchievement = false;

	private boolean itemRemoverChatRight = true;
	private boolean itemRemoverLastTimeHover = false;

	private boolean mobRemoverChatRight = true;
	private boolean mobRemoverLastTimeHover = false;

	private boolean betterIgnoreList = true;

	private boolean ampEnabled = false;
	private boolean ampClanEnabled = false;
	private String ampChatReplacement = "";
	private String defaultAMPChatReplacement = "[AMP] %CLEAN%";
	private String ampTablistReplacement = "";
	private String defaultAMPTablistReplacement = "[AMP] %CLEAN%";

	private boolean preventCommandFailure = false;

	private boolean markTPAMsg = true;

	private boolean cleanVoteMsg = false;

	private boolean cleanNewsMsg = false;

	private boolean updateBoosterState = false;

	private boolean clearMapCache = false;

	private boolean labyChatShowSubServerEnabled = false;

	private boolean autoPortal = false;

	private boolean vanishHelper = false;

	private boolean checkPlotHelper = false;

	protected GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	protected JsonObject getConfig() {
		return getGG().getConfig();
	}

	protected void saveConfig() {
		getGG().saveConfig();
	}

	public ModSettings() {
		// constructor
	}

	public boolean isModEnabled() {
		return this.modEnabled;
	}

	private void setModEnabled(boolean modEnabled) {
		this.modEnabled = modEnabled;
	}

	public boolean isShowChatTime() {
		return this.showChatTime;
	}

	private void setShowChatTime(boolean showChatTime) {
		this.showChatTime = showChatTime;
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

	public EnumSounds getPrivateChatSound() {
		return this.privateChatSound;
	}

	private void setPrivateChatSound(EnumSounds privateChatSound) {
		this.privateChatSound = privateChatSound;
	}

	public boolean isPrivateChatSound() {
		return (getPrivateChatSoundPath().length() > 0);
	}

	public String getPrivateChatSoundPath() {
		switch (this.privateChatSound) {
		case BASS:
			return "note.bass";
		case BASSDRUM:
			return "note.bd";
		case HARP:
			return "note.harp";
		case HAT:
			return "note.hat";
		case PLING:
			return "note.pling";
		case POP:
			return "random.pop";
		case SNARE:
			return "note.snare";
		default:
			return "";
		}
	}

	public EnumRealnameShown getRealname() {
		return this.realname;
	}

	public boolean isRealnameRight() {
		return (this.realname.equals(EnumRealnameShown.SECONDCHAT) || this.realname.equals(EnumRealnameShown.BOTH));
	}

	public boolean isRealnameBoth() {
		return this.realname.equals(EnumRealnameShown.BOTH);
	}

	/*
	 * private void setRealnameClick(boolean realnameClick) { this.realnameClick =
	 * realnameClick; }
	 * 
	 * public boolean isRealnameClick() { return this.realnameClick; }
	 */

	private void setRealname(EnumRealnameShown realname) {
		this.realname = realname;
	}

	public boolean isMsgDisplayNameClick() {
		return this.msgDisplayNameClick;
	}

	private void setMsgDisplayNameClick(boolean msgDisplayNameClick) {
		this.msgDisplayNameClick = msgDisplayNameClick;
	}

	public boolean isClanTagClick() {
		return this.clanTagClick;
	}

	private void setClanTagClick(boolean clanTagClick) {
		this.clanTagClick = clanTagClick;
	}

	public boolean isPreventCommandFailure() {
		return this.preventCommandFailure;
	}

	private void setPreventCommandFailure(boolean preventCommandFailure) {
		this.preventCommandFailure = preventCommandFailure;
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

	public boolean isItemRemoverChatRight() {
		return this.itemRemoverChatRight;
	}

	private void setItemRemoverChatRight(boolean itemRemoverChatRight) {
		this.itemRemoverChatRight = itemRemoverChatRight;
	}

	public boolean isItemRemoverLastTimeHover() {
		return this.itemRemoverLastTimeHover;
	}

	private void setItemRemoverLastTimeHover(boolean itemRemoverLastTimeHover) {
		this.itemRemoverLastTimeHover = itemRemoverLastTimeHover;
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

	public boolean isAMPEnabled() {
		return this.ampEnabled;
	}

	private void setAMPEnabled(boolean ampEnabled) {
		this.ampEnabled = ampEnabled;
	}

	public boolean isAMPClanEnabled() {
		return this.ampClanEnabled;
	}

	private void setAMPClanEnabled(boolean ampClanEnabled) {
		this.ampClanEnabled = ampClanEnabled;
	}

	public String getAMPChatReplacement() {
		return ampChatReplacement;
	}

	private void setAMPChatReplacement(String ampChatReplacement) {
		this.ampChatReplacement = ampChatReplacement;
	}

	public String getDefaultAMPChatReplacement() {
		return defaultAMPChatReplacement;
	}

	public void setDefaultAMPChatReplacement(String defaultAMPChatReplacement) {
		this.defaultAMPChatReplacement = defaultAMPChatReplacement;
	}

	public String getAMPTablistReplacement() {
		return ampTablistReplacement;
	}

	private void setAMPTablistReplacement(String ampTablistReplacement) {
		this.ampTablistReplacement = ampTablistReplacement;
	}

	public String getDefaultAMPTablistReplacement() {
		return defaultAMPTablistReplacement;
	}

	public void setDefaultAMPTablistReplacement(String defaultAMPTablistReplacement) {
		this.defaultAMPTablistReplacement = defaultAMPTablistReplacement;
	}

	public boolean isMarkTPAMsg() {
		return markTPAMsg;
	}

	public void setMarkTPAMsg(boolean markTPAMsg) {
		this.markTPAMsg = markTPAMsg;
	}

	public boolean isCleanVoteMsg() {
		return cleanVoteMsg;
	}

	public void setCleanVoteMsg(boolean cleanVoteMsg) {
		this.cleanVoteMsg = cleanVoteMsg;
	}

	public boolean isCleanNewsMsg() {
		return cleanNewsMsg;
	}

	public void setCleanNewsMsg(boolean cleanNewsMsg) {
		this.cleanNewsMsg = cleanNewsMsg;
	}

	public boolean isUpdateBoosterState() {
		return updateBoosterState;
	}

	public void setUpdateBoosterState(boolean updateBoosterState) {
		this.updateBoosterState = updateBoosterState;
	}

	public boolean isClearMapCache() {
		return clearMapCache;
	}

	public void setClearMapCache(boolean clearMapCache) {
		this.clearMapCache = clearMapCache;
	}

	public boolean isLabyChatShowSubServerEnabled() {
		return labyChatShowSubServerEnabled;
	}

	public void setLabyChatShowSubServerEnabled(boolean labyChatShowSubServerEnabled) {
		JsonObject serverMessage = new JsonObject();
		serverMessage.addProperty("show_gamemode", false);
		serverMessage.addProperty("gamemode_name", "");

		if (labyChatShowSubServerEnabled) {
			String LabyConnectString = getGG().getHelper().getServerMessageName("lobby");

			if (LabyConnectString != null && LabyConnectString.trim().length() > 0) {
				serverMessage.addProperty("show_gamemode", true);
				serverMessage.addProperty("gamemode_name", LabyConnectString);
			}
		}
		LabyMod.getInstance().getLabyConnect().onServerMessage("server_gamemode", serverMessage);

		this.labyChatShowSubServerEnabled = labyChatShowSubServerEnabled;
	}

	public void setAutoPortal(boolean autoPortal) {
		this.autoPortal = autoPortal;
	}

	public boolean isAutoPortl() {
		return autoPortal;
	}

	public boolean isVanishHelper() {
		return vanishHelper;
	}

	public void setVanishHelper(boolean vanishHelper) {
		this.vanishHelper = vanishHelper;
	}

	public void deactivateVanishHelper() {
		setVanishHelper(false);
		getConfig().addProperty("vanishHelper", false);
		saveConfig();
	}

	public void activateVanishHelper() {
		setVanishHelper(true);
		getConfig().addProperty("vanishHelper", true);
		saveConfig();
	}

	public boolean isCheckPlotHelper() {
		return checkPlotHelper;
	}

	public void setCheckPlotHelper(boolean checkPlotHelper) {
		this.checkPlotHelper = checkPlotHelper;
	}

	public void deactivateCheckPlotHelper() {
		setCheckPlotHelper(false);
		getConfig().addProperty("checkPlotHelper", false);
		saveConfig();
	}

	public void activateCheckPlotHelper() {
		setCheckPlotHelper(true);
		getConfig().addProperty("checkPlotHelper", true);
		saveConfig();
	}

	public void loadConfig() {
		if (getConfig().has("modEnabled"))
			setModEnabled(getConfig().get("modEnabled").getAsBoolean());

		if (getConfig().has("showChatTime"))
			setShowChatTime(getConfig().get("showChatTime").getAsBoolean());

		if (getConfig().has("privateChatRight"))
			setPrivateChatRight(getConfig().get("privateChatRight").getAsBoolean());

		if (getConfig().has("privateChatSound")) {
			setPrivateChatSound(EnumSounds.NONE);
			for (EnumSounds sound : EnumSounds.values()) {
				if (sound.name().equalsIgnoreCase(getConfig().get("privateChatSound").getAsString())) {
					setPrivateChatSound(sound);
				}
			}
		}

		if (getConfig().has("msgDisplayNameClick"))
			setMsgDisplayNameClick(getConfig().get("msgDisplayNameClick").getAsBoolean());

		if (getConfig().has("clanTagClick"))
			setClanTagClick(getConfig().get("clanTagClick").getAsBoolean());

		if (getConfig().has("filterDuplicateMessages"))
			setFilterDuplicateMessages(getConfig().get("filterDuplicateMessages").getAsBoolean());

		if (getConfig().has("filterDuplicateMessagesTime"))
			setFilterDuplicateMessagesTime(getConfig().get("filterDuplicateMessagesTime").getAsInt());

		if (getConfig().has("cleanBlanks"))
			setCleanBlanks(getConfig().get("cleanBlanks").getAsBoolean());

		if (getConfig().has("cleanSupremeBlanks"))
			setCleanSupremeBlanks(getConfig().get("cleanSupremeBlanks").getAsBoolean());

		if (getConfig().has("payChatRight"))
			setPayChatRight(getConfig().get("payChatRight").getAsBoolean());

		if (getConfig().has("payAchievement"))
			setPayAchievement(getConfig().get("payAchievement").getAsBoolean());

		if (getConfig().has("payMarker"))
			setPayMarker(getConfig().get("payMarker").getAsBoolean());

		if (getConfig().has("payHover"))
			setPayHover(getConfig().get("payHover").getAsBoolean());

		if (getConfig().has("bankChatRight"))
			setBankChatRight(getConfig().get("bankChatRight").getAsBoolean());

		if (getConfig().has("bankAchievement"))
			setBankAchievement(getConfig().get("bankAchievement").getAsBoolean());

		if (getConfig().has("plotChatRight"))
			setPlotChatRight(getConfig().get("plotChatRight").getAsBoolean());

		if (getConfig().has("realname")) {
			setRealname(EnumRealnameShown.DEFAULT);
			for (EnumRealnameShown enumRealname : EnumRealnameShown.values()) {
				if (enumRealname.name().equalsIgnoreCase(getConfig().get("realname").getAsString())) {
					setRealname(enumRealname);
				}
			}
		}

		/*
		 * if (getConfig().has("realnameClick"))
		 * setRealnameClick(getConfig().get("realnameClick").getAsBoolean());
		 */

		if (getConfig().has("itemRemoverChatRight"))
			setItemRemoverChatRight(getConfig().get("itemRemoverChatRight").getAsBoolean());

		if (getConfig().has("itemRemoverLastTimeHover"))
			setItemRemoverLastTimeHover(getConfig().get("itemRemoverLastTimeHover").getAsBoolean());

		if (getConfig().has("mobRemoverChatRight"))
			setMobRemoverChatRight(getConfig().get("mobRemoverChatRight").getAsBoolean());

		if (getConfig().has("mobRemoverLastTimeHover"))
			setMobRemoverLastTimeHover(getConfig().get("mobRemoverLastTimeHover").getAsBoolean());

		if (getConfig().has("betterIgnoreList"))
			setBetterIgnoreList(getConfig().get("betterIgnoreList").getAsBoolean());

		if (getConfig().has("ampEnabled"))
			setAMPEnabled(getConfig().get("ampEnabled").getAsBoolean());

		if (getConfig().has("ampClanEnabled"))
			setAMPClanEnabled(getConfig().get("ampClanEnabled").getAsBoolean());

		if (getConfig().has("chatReplacement"))
			setAMPChatReplacement(getConfig().get("chatReplacement").getAsString());

		if (getConfig().has("tablistReplacement"))
			setAMPTablistReplacement(getConfig().get("tablistReplacement").getAsString());

		if (getConfig().has("preventCommandFailure"))
			setPreventCommandFailure(getConfig().get("preventCommandFailure").getAsBoolean());

		if (getConfig().has("markTPAMsg"))
			setMarkTPAMsg(getConfig().get("markTPAMsg").getAsBoolean());

		if (getConfig().has("cleanVoteMsg"))
			setCleanVoteMsg(getConfig().get("cleanVoteMsg").getAsBoolean());

		if (getConfig().has("cleanNewsMsg"))
			setCleanNewsMsg(getConfig().get("cleanNewsMsg").getAsBoolean());

		if (getConfig().has("updateBoosterState"))
			setUpdateBoosterState(getConfig().get("updateBoosterState").getAsBoolean());

		if (getConfig().has("clearMapCache"))
			setClearMapCache(getConfig().get("clearMapCache").getAsBoolean());

		if (getConfig().has("labyChatShowSubServerEnabled"))
			setLabyChatShowSubServerEnabled(getConfig().get("labyChatShowSubServerEnabled").getAsBoolean());

		if (getConfig().has("autoPortal"))
			setAutoPortal(getConfig().get("autoPortal").getAsBoolean());

		if (getConfig().has("vanishHelper"))
			setVanishHelper(getConfig().get("vanishHelper").getAsBoolean());

		if (getConfig().has("checkPlotHelper"))
			setCheckPlotHelper(getConfig().get("checkPlotHelper").getAsBoolean());
	}

	public void fillSettings(final List<SettingsElement> settings) {
		settings.add(new HeaderElement("Allgemein"));

		final BooleanElement modEnabledBtn = new BooleanElement("Addon Enabled",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean modEnabled) {
						setModEnabled(modEnabled);
						getConfig().addProperty("modEnabled", modEnabled);
						saveConfig();
					}
				}, isModEnabled());
		settings.add(modEnabledBtn);

		settings.add(new HeaderElement("Nachrichten"));
		final BooleanElement showChatTimeBtn = new BooleanElement("Chatzeit anzeigen",
				new ControlElement.IconData("labymod/textures/settings/modules/date.png"), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean showChatTime) {
						setShowChatTime(showChatTime);
						getConfig().addProperty("showChatTime", showChatTime);
						saveConfig();
					}
				}, isShowChatTime());
		settings.add(showChatTimeBtn);

		final BooleanElement privateChatRightBtn = new BooleanElement("Private Nachrichten 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean privateChatRight) {
						setPrivateChatRight(privateChatRight);
						getConfig().addProperty("privateChatRight", privateChatRight);
						saveConfig();
					}
				}, isPrivateChatRight());
		settings.add(privateChatRightBtn);

		final DropDownMenu<EnumSounds> privateChatSoundDropDownMenu = new DropDownMenu<EnumSounds>(
				"Private Nachricht Sound" /* Display name */, 0, 0, 0, 0).fill(EnumSounds.values());

		final DropDownElement<EnumSounds> privateChatSoundDropDown = new DropDownElement<EnumSounds>(
				"Private Nachricht Sound", privateChatSoundDropDownMenu);

		// Set selected entry
		privateChatSoundDropDownMenu.setSelected(getPrivateChatSound());

		// Listen on changes
		privateChatSoundDropDown.setChangeListener(new Consumer<EnumSounds>() {
			@Override
			public void accept(EnumSounds privateChatSound) {
				setPrivateChatSound(privateChatSound);
				getConfig().addProperty("privateChatSound", privateChatSound.name());
				saveConfig();
			}
		});
		settings.add(privateChatSoundDropDown);

		final BooleanElement plotChatRightBtn = new BooleanElement("Plot Chat 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean plotChatRight) {
						setPlotChatRight(plotChatRight);
						getConfig().addProperty("plotChatRight", plotChatRight);
						saveConfig();
					}
				}, isPlotChatRight());
		settings.add(plotChatRightBtn);

		final BooleanElement msgDisplayNameClickBtn = new BooleanElement("Klicken zum antworten",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean msgDisplayNameClick) {
						setMsgDisplayNameClick(msgDisplayNameClick);
						getConfig().addProperty("msgDisplayNameClick", msgDisplayNameClick);
						saveConfig();
					}
				}, isMsgDisplayNameClick());
		settings.add(msgDisplayNameClickBtn);

		final BooleanElement clanTagClickBtn = new BooleanElement("Clan-Tag klicken f\u00fcr Info",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean clanTagClick) {
						setClanTagClick(clanTagClick);
						getConfig().addProperty("clanTagClick", clanTagClick);
						saveConfig();
					}
				}, isClanTagClick());
		settings.add(clanTagClickBtn);

		final BooleanElement filterDuplicateMessagesBtn = new BooleanElement("Doppelte Nachrichten l\u00f6schen",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean filterDuplicateMessages) {
						setFilterDuplicateMessages(filterDuplicateMessages);
						getConfig().addProperty("filterDuplicateMessages", filterDuplicateMessages);
						saveConfig();
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
				getConfig().addProperty("filterDuplicateMessagesTime", filterDuplicateMessagesTime);
				saveConfig();
			}
		});
		settings.add(filterDuplicateMessagesTimeNumber);

		final BooleanElement preventCommandFailureBtn = new BooleanElement("Fehlerhaften Befehl blockieren",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean preventCommandFailure) {
						setPreventCommandFailure(preventCommandFailure);
						getConfig().addProperty("preventCommandFailure", preventCommandFailure);
						saveConfig();
					}
				}, isPreventCommandFailure());
		settings.add(preventCommandFailureBtn);

		final BooleanElement markTPAMsgBtn = new BooleanElement("TPA Nachrichten markieren",
				new ControlElement.IconData("labymod/textures/settings/settings/autotext.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean markTPAMsg) {
						setMarkTPAMsg(markTPAMsg);
						getConfig().addProperty("markTPAMsg", markTPAMsg);
						saveConfig();
					}
				}, isMarkTPAMsg());
		settings.add(markTPAMsgBtn);

		final BooleanElement cleanVoteMsgBtn = new BooleanElement("Vote Nachrichten l\u00f6schen",
				new ControlElement.IconData("labymod/textures/settings/settings/autotext.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean cleanVoteMsg) {
						setCleanVoteMsg(cleanVoteMsg);
						getConfig().addProperty("cleanVoteMsg", cleanVoteMsg);
						saveConfig();
					}
				}, isCleanVoteMsg());
		settings.add(cleanVoteMsgBtn);

		final BooleanElement cleanNewsMsgBtn = new BooleanElement("News Nachrichten l\u00f6schen",
				new ControlElement.IconData("labymod/textures/settings/settings/autotext.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean cleanNewsMsg) {
						setCleanNewsMsg(cleanNewsMsg);
						getConfig().addProperty("cleanNewsMsg", cleanNewsMsg);
						saveConfig();
					}
				}, isCleanNewsMsg());
		settings.add(cleanNewsMsgBtn);

		settings.add(new HeaderElement("Realname"));
		final DropDownMenu<EnumRealnameShown> realnameDropDownMenu = new DropDownMenu<EnumRealnameShown>(
				"Realname anzeigen" /* Display name */, 0, 0, 0, 0).fill(EnumRealnameShown.values());

		final DropDownElement<EnumRealnameShown> realnameDropDown = new DropDownElement<EnumRealnameShown>(
				"Realname anzeigen", realnameDropDownMenu);

		// Set selected entry
		realnameDropDownMenu.setSelected(getRealname());

		// Listen on changes
		realnameDropDown.setChangeListener(new Consumer<EnumRealnameShown>() {
			@Override
			public void accept(EnumRealnameShown realname) {
				setRealname(realname);
				getConfig().addProperty("realname", realname.name());
				saveConfig();
			}
		});
		settings.add(realnameDropDown);

		settings.add(new HeaderElement("Leerzeilen"));
		final BooleanElement cleanBlanksBtn = new BooleanElement("Leerzeilen l\u00f6schen",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean cleanBlanks) {
						setCleanBlanks(cleanBlanks);
						getConfig().addProperty("cleanBlanks", cleanBlanks);
						saveConfig();
					}
				}, isCleanBlanks());
		settings.add(cleanBlanksBtn);

		final BooleanElement cleanSupremeBlanksBtn = new BooleanElement("Supreme Leerzeilen l\u00f6schen",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean cleanSupremeBlanks) {
						setCleanSupremeBlanks(cleanSupremeBlanks);
						getConfig().addProperty("cleanSupremeBlanks", cleanSupremeBlanks);
						saveConfig();
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
						getConfig().addProperty("payChatRight", payChatRight);
						saveConfig();
					}
				}, isPayChatRight());
		settings.add(payChatRightBtn);

		final BooleanElement payAchievementBtn = new BooleanElement("Bezahlung Fortschrittsmeldung",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean payAchievement) {
						setPayAchievement(payAchievement);
						getConfig().addProperty("payAchievement", payAchievement);
						saveConfig();
					}
				}, isPayAchievement());
		settings.add(payAchievementBtn);

		final BooleanElement payMarkerBtn = new BooleanElement("Bezahlung markieren",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean payMarker) {
						setPayMarker(payMarker);
						getConfig().addProperty("payMarker", payMarker);
						saveConfig();
					}
				}, isPayMarker());
		settings.add(payMarkerBtn);

		final BooleanElement payHoverBtn = new BooleanElement("Bezahlung hover",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean payHover) {
						setPayHover(payHover);
						getConfig().addProperty("payHover", payHover);
						saveConfig();
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
						getConfig().addProperty("bankChatRight", bankChatRight);
						saveConfig();
					}
				}, isBankChatRight());
		settings.add(bankChatRightBtn);

		final BooleanElement bankAchievementBtn = new BooleanElement("Bank Fortschrittsmeldung",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean bankAchievement) {
						setBankAchievement(bankAchievement);
						getConfig().addProperty("bankAchievement", bankAchievement);
						saveConfig();
					}
				}, isBankAchievement());
		settings.add(bankAchievementBtn);

		settings.add(new HeaderElement("ItemRemover"));
		final BooleanElement itemRemoverChatRightBtn = new BooleanElement("ItemRemover 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean itemRemoverChatRight) {
						setItemRemoverChatRight(itemRemoverChatRight);
						getConfig().addProperty("itemRemoverChatRight", itemRemoverChatRight);
						saveConfig();
					}
				}, isItemRemoverChatRight());
		settings.add(itemRemoverChatRightBtn);

		final BooleanElement itemRemoverLastTimeHoverBtn = new BooleanElement("Itemremover Zeitstempel hover",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean itemRemoverLastTimeHover) {
						setItemRemoverLastTimeHover(itemRemoverLastTimeHover);
						getConfig().addProperty("itemRemoverLastTimeHover", itemRemoverLastTimeHover);
						saveConfig();
					}
				}, isItemRemoverLastTimeHover());
		settings.add(itemRemoverLastTimeHoverBtn);

		settings.add(new HeaderElement("MobRemover"));
		final BooleanElement mobRemoverChatRightBtn = new BooleanElement("Mobremover 2. Chat",
				new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean mobRemoverChatRight) {
						setMobRemoverChatRight(mobRemoverChatRight);
						getConfig().addProperty("mobRemoverChatRight", mobRemoverChatRight);
						saveConfig();
					}
				}, isMobRemoverChatRight());
		settings.add(mobRemoverChatRightBtn);

		final BooleanElement mobRemoverLastTimeHoverBtn = new BooleanElement("Mobremover Zeitstempel hover",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean mobRemoverLastTimeHover) {
						setMobRemoverLastTimeHover(mobRemoverLastTimeHover);
						getConfig().addProperty("mobRemoverLastTimeHover", mobRemoverLastTimeHover);
						saveConfig();
					}
				}, isMobRemoverLastTimeHover());
		settings.add(mobRemoverLastTimeHoverBtn);

		settings.add(new HeaderElement("Ignorierte Spieler"));
		final BooleanElement betterIgnoreListBtn = new BooleanElement("verbesserte Ignorierliste",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean betterIgnoreList) {
						setBetterIgnoreList(betterIgnoreList);
						getConfig().addProperty("betterIgnoreList", betterIgnoreList);
						saveConfig();
					}
				}, isBetterIgnoreList());
		settings.add(betterIgnoreListBtn);

		settings.add(new HeaderElement("Karten"));
		final BooleanElement clearMapCacheBtn = new BooleanElement("Cache automatisch leeren",
				new ControlElement.IconData("labymod/textures/settings/settings/directconnectinfo.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean clearMapCache) {
						setClearMapCache(clearMapCache);
						getConfig().addProperty("clearMapCache", clearMapCache);
						saveConfig();
					}
				}, isClearMapCache());
		settings.add(clearMapCacheBtn);

		settings.add(new HeaderElement("Auto Update Booster Status"));
		final BooleanElement updateBoosterStateBtn = new BooleanElement("Enabled",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean updateBoosterState) {
						setUpdateBoosterState(updateBoosterState);
						getConfig().addProperty("updateBoosterState", updateBoosterState);
						saveConfig();
					}
				}, isUpdateBoosterState());
		settings.add(updateBoosterStateBtn);

		settings.add(new HeaderElement("AntiMagicPrefix"));
		final BooleanElement ampEnabledBtn = new BooleanElement("Enabled", new ControlElement.IconData(Material.LEVER),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean ampEnabled) {
						setAMPEnabled(ampEnabled);
						getConfig().addProperty("ampEnabled", ampEnabled);
						saveConfig();
					}
				}, isAMPEnabled());
		settings.add(ampEnabledBtn);

		final String ampChatText = (getAMPChatReplacement().length() > 0) ? getAMPChatReplacement()
				: getDefaultAMPChatReplacement();
		final StringElement chatReplacementInput = new StringElement("Chat Replacement",
				new ControlElement.IconData(Material.BOOK_AND_QUILL), ampChatText, new Consumer<String>() {
					@Override
					public void accept(String replacement) {
						setAMPChatReplacement(replacement);
						getConfig().addProperty("chatReplacement", replacement);
						saveConfig();
					}
				});
		settings.add(chatReplacementInput);

		final String ampTabListText = (getAMPTablistReplacement().length() > 0) ? getAMPTablistReplacement()
				: getDefaultAMPTablistReplacement();
		final StringElement tablistReplacementInput = new StringElement("TabList Replacement",
				new ControlElement.IconData(Material.BOOK_AND_QUILL), ampTabListText, new Consumer<String>() {
					@Override
					public void accept(String replacement) {
						setAMPTablistReplacement(replacement);
						getConfig().addProperty("tablistReplacement", replacement);
						saveConfig();
					}
				});
		settings.add(tablistReplacementInput);

		settings.add(new HeaderElement("AntiMagicClanTag"));
		final BooleanElement ampClanEnabledBtn = new BooleanElement("Enabled",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean ampClanEnabled) {
						setAMPClanEnabled(ampClanEnabled);
						getConfig().addProperty("ampClanEnabled", ampClanEnabled);
						saveConfig();
					}
				}, isAMPClanEnabled());
		settings.add(ampClanEnabledBtn);

		settings.add(new HeaderElement("LabyChat"));
		final BooleanElement labyChatShowSubServerEnabledBtn = new BooleanElement("Spielmodus teilen",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean labyChatShowSubServerEnabled) {
						setLabyChatShowSubServerEnabled(labyChatShowSubServerEnabled);
						getConfig().addProperty("labyChatShowSubServerEnabled", labyChatShowSubServerEnabled);
						saveConfig();
					}
				}, isLabyChatShowSubServerEnabled());
		settings.add(labyChatShowSubServerEnabledBtn);

		settings.add(new HeaderElement("Automatisationen"));
		final BooleanElement autoPortalBtn = new BooleanElement("Portalraum beim Betreten",
				new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean autoPortal) {
				setAutoPortal(autoPortal);
				getConfig().addProperty("autoPortal", autoPortal);
				saveConfig();
			}
		}, isAutoPortl());
		settings.add(autoPortalBtn);
	}
}