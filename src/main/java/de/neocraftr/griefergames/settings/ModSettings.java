package de.neocraftr.griefergames.settings;

import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.JsonObject;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.EnumLanguages;
import de.neocraftr.griefergames.enums.EnumRealnameShown;
import de.neocraftr.griefergames.enums.EnumSounds;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ColorPicker;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.LabyModAddonsGui;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ModSettings {
	public static final String DEFAULT_AMP_REPLACEMENT_CHAT = "[AMP]",
			DEFAULT_AMP_REPLACEMENT_TABLIST = "[AMP]",
			DEFAULT_AFK_NICKNAME = "AFK_%name%",
			DEFAULT_CHATTIME_FORMAT = "&8[&3%h%&7:&3%m%&7:&3%s%&8]";

	private TextElement infoText;

	private boolean modEnabled;
	private EnumLanguages language;
	private String overrideRank;
	private boolean showChatTime;
	private boolean chatTimeAfterMessage;
	private String chatTimeFormat;
	private boolean privateChatRight;
	private boolean plotChatRight;
	private EnumSounds privateChatSound;
	private EnumRealnameShown realname;
	//private boolean realnameClick;
	private boolean msgDisplayNameClick;
	private boolean filterDuplicateMessages;
	private Integer filterDuplicateMessagesTime;
	private boolean cleanBlanks;
	private boolean cleanSupremeBlanks;
	private boolean highlightMentions;
	private Color mentionsColor;
	private EnumSounds mentionSound;
	private boolean afkNick;
	private String afkNickname;
	private int afkTime;
	private boolean afkMsgAnswear;
	private String afkMsgText;
	private boolean payChatRight;
	private boolean payAchievement;
	private boolean payMarker;
	private boolean payHover;
	private boolean bankChatRight;
	private boolean bankAchievement;
	private boolean itemRemoverChatRight;
	private boolean itemRemoverLastTimeHover;
	private boolean mobRemoverChatRight;
	private boolean mobRemoverLastTimeHover;
	private boolean betterIgnoreList;
	private boolean ampEnabled;
	private boolean ampClanEnabled;
	private String ampChatReplacement;
	private String ampTablistReplacement;
	private boolean preventCommandFailure;
	private boolean markTPAMsg;
	private boolean cleanVoteMsg;
	private boolean cleanNewsMsg;
	private boolean updateBoosterState;
	private boolean clearMapCache;
	private boolean labyChatShowSubServerEnabled;
	private boolean discordShowSubServerEnabled;
	private boolean autoPortal;
	private boolean hideBoosterMenu;
	private boolean autoUpdate;
	private boolean vanishOnJoin;
	private boolean flyOnJoin;
	private boolean logTransactions;
	private boolean showPrefixInDisplayName;

	public void loadConfig() {
		// comnversion of old amp replacement
		if(getConfig().has("tablistReplacement")) {
			String tablistReplacement = getConfig().get("tablistReplacement").getAsString();
			if(tablistReplacement.toLowerCase().contains("%clean%")) {
				getConfig().addProperty("tablistReplacement",
						tablistReplacement.replace("%clean%", "").replace("%CLEAN%", "").trim());
				saveConfig();
			}
		}
		if(getConfig().has("chatReplacement")) {
			String chatReplacement = getConfig().get("chatReplacement").getAsString();
			if(chatReplacement.toLowerCase().contains("%clean%")) {
				getConfig().addProperty("chatReplacement",
						chatReplacement.replace("%clean%", "").replace("%CLEAN%", "").trim());
				saveConfig();
			}
		}

		modEnabled = getConfig().has("modEnabled") ?
				getConfig().get("modEnabled").getAsBoolean() : true;

		language = getConfig().has("language") ?
				EnumLanguages.valueOf(getConfig().get("language").getAsString()) : EnumLanguages.GAMELANGUAGE;

		overrideRank = getConfig().has("overrideRank") ?
				getConfig().get("overrideRank").getAsString() : null;

		showChatTime = getConfig().has("showChatTime") ?
				getConfig().get("showChatTime").getAsBoolean() : false;

		chatTimeAfterMessage = getConfig().has("chatTimeAfterMessage") ?
				getConfig().get("chatTimeAfterMessage").getAsBoolean() : false;

		privateChatRight = getConfig().has("privateChatRight") ?
				getConfig().get("privateChatRight").getAsBoolean() : true;

		privateChatSound = getConfig().has("privateChatSound") ?
				EnumSounds.valueOf(getConfig().get("privateChatSound").getAsString()) : EnumSounds.NONE;

		msgDisplayNameClick = getConfig().has("msgDisplayNameClick") ?
				getConfig().get("msgDisplayNameClick").getAsBoolean() : true;

		filterDuplicateMessages = getConfig().has("filterDuplicateMessages") ?
				getConfig().get("filterDuplicateMessages").getAsBoolean() : false;

		filterDuplicateMessagesTime = getConfig().has("filterDuplicateMessagesTime") ?
				getConfig().get("filterDuplicateMessagesTime").getAsInt() : 5;

		cleanBlanks = getConfig().has("cleanBlanks") ?
				getConfig().get("cleanBlanks").getAsBoolean() : true;

		cleanSupremeBlanks = getConfig().has("cleanSupremeBlanks") ?
				getConfig().get("cleanSupremeBlanks").getAsBoolean() : true;

		highlightMentions = getConfig().has("highlightMentions") ?
				getConfig().get("highlightMentions").getAsBoolean() : true;

		mentionsColor = getConfig().has("mentionsColor") ?
				new Color(getConfig().get("mentionsColor").getAsInt()) : new Color(121, 178, 255);

		mentionSound = getConfig().has("mentionSound") ?
				EnumSounds.valueOf(getConfig().get("mentionSound").getAsString()) : EnumSounds.NONE;

		payChatRight = getConfig().has("payChatRight") ?
				getConfig().get("payChatRight").getAsBoolean() : true;

		payAchievement = getConfig().has("payAchievement") ?
				getConfig().get("payAchievement").getAsBoolean() : false;

		payMarker = getConfig().has("payMarker") ?
				getConfig().get("payMarker").getAsBoolean() : false;

		payHover = getConfig().has("payHover") ?
				getConfig().get("payHover").getAsBoolean() : false;

		afkNick = getConfig().has("afkNick") ?
				getConfig().get("afkNick").getAsBoolean() : false;

		afkMsgAnswear = getConfig().has("afkMsgAnswear") ?
				getConfig().get("afkMsgAnswear").getAsBoolean() : false;

		afkMsgText = getConfig().has("afkMsgText") ?
				getConfig().get("afkMsgText").getAsString() : "Ich bin momentan AFK ;)";

		afkNickname = getConfig().has("afkNickname") && !getConfig().get("afkNickname").getAsString().trim().isEmpty() ?
				getConfig().get("afkNickname").getAsString() : DEFAULT_AFK_NICKNAME;

		afkTime = getConfig().has("afkTime") ?
				getConfig().get("afkTime").getAsInt() : 15;

		bankChatRight = getConfig().has("bankChatRight") ?
				getConfig().get("bankChatRight").getAsBoolean() : true;

		bankAchievement = getConfig().has("bankAchievement") ?
				getConfig().get("bankAchievement").getAsBoolean() : false;

		plotChatRight = getConfig().has("plotChatRight") ?
				getConfig().get("plotChatRight").getAsBoolean() : false;

		realname = getConfig().has("realname") ?
				EnumRealnameShown.valueOf(getConfig().get("realname").getAsString()) : EnumRealnameShown.BOTH;

		//realnameClick = getConfig().has("realnameClick") ?
		//		getConfig().get("realnameClick").getAsBoolean() ? false;

		itemRemoverChatRight = getConfig().has("itemRemoverChatRight") ?
				getConfig().get("itemRemoverChatRight").getAsBoolean() : false;

		itemRemoverLastTimeHover = getConfig().has("itemRemoverLastTimeHover") ?
				getConfig().get("itemRemoverLastTimeHover").getAsBoolean() : true;

		mobRemoverChatRight = getConfig().has("mobRemoverChatRight") ?
				getConfig().get("mobRemoverChatRight").getAsBoolean() : false;

		mobRemoverLastTimeHover = getConfig().has("mobRemoverLastTimeHover") ?
				getConfig().get("mobRemoverLastTimeHover").getAsBoolean() : true;

		betterIgnoreList = getConfig().has("betterIgnoreList") ?
				getConfig().get("betterIgnoreList").getAsBoolean() : true;

		ampEnabled = getConfig().has("ampEnabled") ?
				getConfig().get("ampEnabled").getAsBoolean() : true;

		ampClanEnabled = getConfig().has("ampClanEnabled") ?
				getConfig().get("ampClanEnabled").getAsBoolean() : false;

		ampChatReplacement = getConfig().has("chatReplacement") && !getConfig().get("chatReplacement").getAsString().trim().isEmpty() ?
				getConfig().get("chatReplacement").getAsString() : DEFAULT_AMP_REPLACEMENT_CHAT;

		ampTablistReplacement = getConfig().has("tablistReplacement") && !getConfig().get("tablistReplacement").getAsString().trim().isEmpty() ?
				getConfig().get("tablistReplacement").getAsString() : DEFAULT_AMP_REPLACEMENT_TABLIST;

		chatTimeFormat = getConfig().has("chatTimeFormat") && !getConfig().get("chatTimeFormat").getAsString().trim().isEmpty() ?
				getConfig().get("chatTimeFormat").getAsString() : DEFAULT_CHATTIME_FORMAT;

		preventCommandFailure = getConfig().has("preventCommandFailure") ?
				getConfig().get("preventCommandFailure").getAsBoolean() : true;

		markTPAMsg = getConfig().has("markTPAMsg") ?
				getConfig().get("markTPAMsg").getAsBoolean() : true;

		cleanVoteMsg = getConfig().has("cleanVoteMsg") ?
				getConfig().get("cleanVoteMsg").getAsBoolean() : true;

		cleanNewsMsg = getConfig().has("cleanNewsMsg") ?
				getConfig().get("cleanNewsMsg").getAsBoolean() : false;

		updateBoosterState = getConfig().has("updateBoosterState") ?
				getConfig().get("updateBoosterState").getAsBoolean() : true;

		clearMapCache = getConfig().has("clearMapCache") ?
				getConfig().get("clearMapCache").getAsBoolean() : false;

		labyChatShowSubServerEnabled = getConfig().has("labyChatShowSubServerEnabled") ?
				getConfig().get("labyChatShowSubServerEnabled").getAsBoolean() : true;

		discordShowSubServerEnabled = getConfig().has("discordShowSubServerEnabled") ?
				getConfig().get("discordShowSubServerEnabled").getAsBoolean() : true;

		autoPortal = getConfig().has("autoPortal") ?
				getConfig().get("autoPortal").getAsBoolean() : false;

		hideBoosterMenu = getConfig().has("hideBoosterMenu") ?
				getConfig().get("hideBoosterMenu").getAsBoolean() : false;

		autoUpdate = getConfig().has("autoUpdate") ?
				getConfig().get("autoUpdate").getAsBoolean() : true;

		vanishOnJoin = getConfig().has("vanishOnJoin") ?
				getConfig().get("vanishOnJoin").getAsBoolean() : false;

		flyOnJoin = getConfig().has("flyOnJoin") ?
				getConfig().get("flyOnJoin").getAsBoolean() : false;

		logTransactions = getConfig().has("logTransactions") ?
				getConfig().get("logTransactions").getAsBoolean() : false;

		showPrefixInDisplayName = getConfig().has("showPrefixInDisplayName") ?
				getConfig().get("showPrefixInDisplayName").getAsBoolean() : true;
	}

	public void fillSettings(final List<SettingsElement> settings) {
		// Addon enabled
		final BooleanElement modEnabledBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_addonEnabled"),
			new ControlElement.IconData("labymod/textures/buttons/accept.png"), new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					modEnabled = value;
					getConfig().addProperty("modEnabled", value);
					saveConfig();
				}
			}, modEnabled);
		settings.add(modEnabledBtn);

		// Auto update
		final BooleanElement autoUpdateBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_autoUpdate"),
				new ControlElement.IconData("labymod/textures/buttons/update.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				autoUpdate = value;
				getConfig().addProperty("autoUpdate", value);
				saveConfig();
				updateInfo();
			}
		}, autoUpdate);
		settings.add(autoUpdateBtn);

		final ButtonElement resetSettingsBtn = new ButtonElement(LanguageManager.translateOrReturnKey("settings_gg_resetSettings"),
				LanguageManager.translateOrReturnKey("settings_gg_resetSettingsBtn"),
				new ControlElement.IconData("griefergames/textures/icons/trash.png"), null);
		resetSettingsBtn.setClickCallback(new Runnable() {
			@Override
			public void run() {
				if(resetSettingsBtn.getButtonText().equals(LanguageManager.translateOrReturnKey("settings_gg_resetSettingsConfirm"))) {
					// Perform reset
					resetSettingsBtn.setButtonText(LanguageManager.translateOrReturnKey("settings_gg_resetSettingsBtn"));
					resetSettingsBtn.setEnabled(false);

					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							getConfig().entrySet().clear();
							saveConfig();

							loadConfig();
							getGG().loadTranslations();
							reinitSettings();
						}
					}, 700);
				} else {
					// Display confirmation
					resetSettingsBtn.setButtonText(LanguageManager.translateOrReturnKey("settings_gg_resetSettingsConfirm"));
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							if(resetSettingsBtn.isEnabled()) {
								resetSettingsBtn.setButtonText(LanguageManager.translateOrReturnKey("settings_gg_resetSettingsBtn"));
							}
						}
					}, 3000);
				}
			}
		});
		settings.add(resetSettingsBtn);

		// Language
		final DropDownMenu<EnumLanguages> languageDropDownMenu = new DropDownMenu<EnumLanguages>(
				LanguageManager.translateOrReturnKey("settings_gg_addonLanguage"), 0, 0, 0, 0).fill(EnumLanguages.values());

		final DropDownElement<EnumLanguages> languageDropDown = new DropDownElement<EnumLanguages>(
				LanguageManager.translateOrReturnKey("settings_gg_addonLanguage"), languageDropDownMenu);

		languageDropDownMenu.setSelected(language);

		languageDropDown.setChangeListener(new Consumer<EnumLanguages>() {
			@Override
			public void accept(EnumLanguages value) {
				if(language != value) {
					language = value;
					getConfig().addProperty("language", value.name());
					saveConfig();

					getGG().loadTranslations();
					reinitSettings();
				}
			}
		});
		settings.add(languageDropDown);

		// Category: Chat
		settings.add(new HeaderElement(""));
		final ListContainerElement chatCategory = new ListContainerElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_category_chat"),
				new ControlElement.IconData("labymod/textures/settings/settings/second_chat.png"));
		settings.add(chatCategory);

		// Click to answear
		final BooleanElement msgDisplayNameClickBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_clickToAnswer"),
				new ControlElement.IconData("labymod/textures/settings/settings/chatshortcuts.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				msgDisplayNameClick = value;
				getConfig().addProperty("msgDisplayNameClick", value);
				saveConfig();
			}
		}, msgDisplayNameClick);
		chatCategory.getSubSettings().add(msgDisplayNameClickBtn);

		// Block incorrect commands
		final BooleanElement preventCommandFailureBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_blockIncorrectCommands"),
				new ControlElement.IconData("labymod/textures/chat/gui_editor.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				preventCommandFailure = value;
				getConfig().addProperty("preventCommandFailure", value);
				saveConfig();
			}
		}, preventCommandFailure);
		chatCategory.getSubSettings().add(preventCommandFailureBtn);

		// Better ignore list
		final BooleanElement betterIgnoreListBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_betterIgnoreList"),
				new ControlElement.IconData("labymod/textures/settings/settings/publicserverlist.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				betterIgnoreList = value;
				getConfig().addProperty("betterIgnoreList", value);
				saveConfig();
			}
		}, betterIgnoreList);
		chatCategory.getSubSettings().add(betterIgnoreListBtn);

		// Show prefix in display name
		final BooleanElement showPrefixInDisplayNameBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_showPrefixInDisplayName"),
				new ControlElement.IconData("labymod/textures/settings/settings/showmyname.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				showPrefixInDisplayName = value;
				getConfig().addProperty("showPrefixInDisplayName", value);
				saveConfig();
			}
		}, showPrefixInDisplayName);
		chatCategory.getSubSettings().add(showPrefixInDisplayNameBtn);

		// Title: Chat highlight
		chatCategory.getSubSettings().add(new HeaderElement(""));
		chatCategory.getSubSettings().add(new HeaderElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_heads_chatHighlight")));

		// Plotchat 2nd chat
		final BooleanElement plotChatRightBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_plotChatRight"),
			new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					plotChatRight = value;
					getConfig().addProperty("plotChatRight", value);
					saveConfig();
				}
			}, plotChatRight);
		chatCategory.getSubSettings().add(plotChatRightBtn);

		// Private messages 2nd chat
		final BooleanElement privateChatRightBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_privateChatRight"),
			new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					privateChatRight = value;
					getConfig().addProperty("privateChatRight", value);
					saveConfig();
				}
			}, privateChatRight);
		chatCategory.getSubSettings().add(privateChatRightBtn);

		// Private messages sound
		final DropDownMenu<EnumSounds> privateChatSoundDropDownMenu = new DropDownMenu<EnumSounds>(
				LanguageManager.translateOrReturnKey("settings_gg_privateMessageSound"), 0, 0, 0, 0).fill(EnumSounds.values());

		final DropDownElement<EnumSounds> privateChatSoundDropDown = new DropDownElement<EnumSounds>(
				LanguageManager.translateOrReturnKey("settings_gg_privateMessageSound"), privateChatSoundDropDownMenu);

		privateChatSoundDropDownMenu.setSelected(privateChatSound);

		privateChatSoundDropDown.setChangeListener(new Consumer<EnumSounds>() {
			@Override
			public void accept(EnumSounds value) {
				privateChatSound = value;
				getConfig().addProperty("privateChatSound", value.name());
				saveConfig();
				if (value != EnumSounds.NONE) {
					LabyModCore.getMinecraft().playSound(new ResourceLocation(getGG().getHelper().getSoundPath(value)), 1.0F);
				}
			}
		});
		chatCategory.getSubSettings().add(privateChatSoundDropDown);

		// Realname position
		final DropDownMenu<EnumRealnameShown> realnameDropDownMenu = new DropDownMenu<EnumRealnameShown>(
				LanguageManager.translateOrReturnKey("settings_gg_showRealname"), 0, 0, 0, 0).fill(EnumRealnameShown.values());

		final DropDownElement<EnumRealnameShown> realnameDropDown = new DropDownElement<EnumRealnameShown>(
				LanguageManager.translateOrReturnKey("settings_gg_showRealname"), realnameDropDownMenu);

		// Set selected entry
		realnameDropDownMenu.setSelected(realname);

		// Listen on changes
		realnameDropDown.setChangeListener(new Consumer<EnumRealnameShown>() {
			@Override
			public void accept(EnumRealnameShown value) {
				realname = value;
				getConfig().addProperty("realname", value.name());
				saveConfig();
			}
		});
		chatCategory.getSubSettings().add(realnameDropDown);

		// Highlight mentions
		final BooleanElement highlightMentionsBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_highlightMentions"),
			new ControlElement.IconData("labymod/textures/misc/featured.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					highlightMentions = value;
					getConfig().addProperty("highlightMentions", value);
					saveConfig();
				}
			}, highlightMentions);
		chatCategory.getSubSettings().add(highlightMentionsBtn);

		// Mentions color
		final Color[] prevMentionsColor = {mentionsColor};
		final ColorPickerCheckBoxBulkElement mentionsColorBulkElement = new ColorPickerCheckBoxBulkElement("");
		final ColorPicker mentionsColorPicker = new ColorPicker(LanguageManager.translateOrReturnKey("settings_gg_mentionColor"), mentionsColor, null, 0, 0, 0, 0);
		mentionsColorPicker.setHasAdvanced(true);
		mentionsColorPicker.setUpdateListener(new Consumer<Color>() {
			@Override
			public void accept(Color value) {
				if(prevMentionsColor[0].getRGB() != value.getRGB()) {
					prevMentionsColor[0] = value;
					mentionsColor = value;
					getConfig().addProperty("mentionsColor", value.getRGB());
					saveConfig();
				}
			}
		});
		mentionsColorBulkElement.addColorPicker(mentionsColorPicker);
		chatCategory.getSubSettings().add(mentionsColorBulkElement);

		// Mentions sound
		final DropDownMenu<EnumSounds> mentionsSoundDropDownMenu = new DropDownMenu<EnumSounds>(
				LanguageManager.translateOrReturnKey("settings_gg_mentionSound"), 0, 0, 0, 0).fill(EnumSounds.values());

		final DropDownElement<EnumSounds> mentionsSoundDropDown = new DropDownElement<EnumSounds>(
				LanguageManager.translateOrReturnKey("settings_gg_mentionSound"), mentionsSoundDropDownMenu);

		mentionsSoundDropDownMenu.setSelected(mentionSound);

		mentionsSoundDropDown.setChangeListener(new Consumer<EnumSounds>() {
			@Override
			public void accept(EnumSounds value) {
				mentionSound = value;
				getConfig().addProperty("mentionSound", value.name());
				saveConfig();
				if (value != EnumSounds.NONE) {
					LabyModCore.getMinecraft().playSound(new ResourceLocation(getGG().getHelper().getSoundPath(value)), 1.0F);
				}
			}
		});
		chatCategory.getSubSettings().add(mentionsSoundDropDown);

		// Highlight tpa messages
		final BooleanElement markTPAMsgBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_highlightTpaMessages"),
			new ControlElement.IconData("labymod/textures/misc/featured.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					markTPAMsg = value;
					getConfig().addProperty("markTPAMsg", value);
					saveConfig();
				}
			}, markTPAMsg);
		chatCategory.getSubSettings().add(markTPAMsgBtn);

		// ItemRemover 2nd chat
		final BooleanElement itemRemoverChatRightBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_clearlagChatRight"),
			new ControlElement.IconData("labymod/textures/misc/blocked.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					itemRemoverChatRight = value;
					getConfig().addProperty("itemRemoverChatRight", value);
					saveConfig();
				}
			}, itemRemoverChatRight);
		chatCategory.getSubSettings().add(itemRemoverChatRightBtn);

		// ItemRemover timestamp
		final BooleanElement itemRemoverLastTimeHoverBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_clearlagTimeHover"),
				new ControlElement.IconData("labymod/textures/settings/settings/afecentityinterval.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				itemRemoverLastTimeHover = value;
				getConfig().addProperty("itemRemoverLastTimeHover", value);
				saveConfig();
			}
		}, itemRemoverLastTimeHover);
		chatCategory.getSubSettings().add(itemRemoverLastTimeHoverBtn);

		// MobRemover 2nd chat
		final BooleanElement mobRemoverChatRightBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_mobRemoverChatRight"),
			new ControlElement.IconData("labymod/textures/misc/blocked.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					mobRemoverChatRight = value;
					getConfig().addProperty("mobRemoverChatRight", value);
					saveConfig();
				}
			}, mobRemoverChatRight);
		chatCategory.getSubSettings().add(mobRemoverChatRightBtn);

		// MobRemover timestamp
		final BooleanElement mobRemoverLastTimeHoverBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_mobRemoverTimeHover"),
				new ControlElement.IconData("labymod/textures/settings/settings/afecentityinterval.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				mobRemoverLastTimeHover = value;
				getConfig().addProperty("mobRemoverLastTimeHover", value);
				saveConfig();
			}
		}, mobRemoverLastTimeHover);
		chatCategory.getSubSettings().add(mobRemoverLastTimeHoverBtn);

		// Title: Block messages
		chatCategory.getSubSettings().add(new HeaderElement(""));
		chatCategory.getSubSettings().add(new HeaderElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_heads_chatBlock")));

		// Block vote messages
		final BooleanElement cleanVoteMsgBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_deleteVoteMessages"),
			new ControlElement.IconData("labymod/textures/misc/partner_crown.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					cleanVoteMsg = value;
					getConfig().addProperty("cleanVoteMsg", value);
					saveConfig();
				}
			}, cleanVoteMsg);
		chatCategory.getSubSettings().add(cleanVoteMsgBtn);

		// Block news
		final BooleanElement cleanNewsMsgBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_deleteNewsMessages"),
			new ControlElement.IconData("labymod/textures/settings/guicategory/other.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					cleanNewsMsg = value;
					getConfig().addProperty("cleanNewsMsg", value);
					saveConfig();
				}
			}, cleanNewsMsg);
		chatCategory.getSubSettings().add(cleanNewsMsgBtn);

		// Remove blanks
		final BooleanElement cleanBlanksBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_cleanBlanks"),
				new ControlElement.IconData("labymod/textures/settings/settings/chatanimation.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				cleanBlanks = value;
				getConfig().addProperty("cleanBlanks", value);
				saveConfig();
			}
		}, cleanBlanks);
		chatCategory.getSubSettings().add(cleanBlanksBtn);

		// Remove supreme blanks
		final BooleanElement cleanSupremeBlanksBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_cleanSupremeBlanks"),
				new ControlElement.IconData("labymod/textures/settings/settings/chatanimation.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				cleanSupremeBlanks = value;
				getConfig().addProperty("cleanSupremeBlanks", value);
				saveConfig();
			}
		}, cleanSupremeBlanks);
		chatCategory.getSubSettings().add(cleanSupremeBlanksBtn);

		// Block duplicate messages
		final BooleanElement filterDuplicateMessagesBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_filterDuplicateMessages"),
				new ControlElement.IconData("labymod/textures/settings/settings/autotext.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				filterDuplicateMessages = value;
				getConfig().addProperty("filterDuplicateMessages", filterDuplicateMessages);
				saveConfig();
			}
		}, filterDuplicateMessages);
		chatCategory.getSubSettings().add(filterDuplicateMessagesBtn);

		// Duplicate messages time
		final CustomNumberElement filterDuplicateMessagesTimeNumber = new CustomNumberElement(LanguageManager.translateOrReturnKey("settings_gg_filterDuplicateMessagesTime"),
				new ControlElement.IconData("labymod/textures/settings/settings/afecentityinterval.png"), filterDuplicateMessagesTime);
		filterDuplicateMessagesTimeNumber.setMinValue(3);
		filterDuplicateMessagesTimeNumber.setMaxValue(60);
		filterDuplicateMessagesTimeNumber.addCallback(new Consumer<Integer>() {
			@Override
			public void accept(Integer value) {
				filterDuplicateMessagesTime = value;
				getConfig().addProperty("filterDuplicateMessagesTime", value);
				saveConfig();
			}
		});
		chatCategory.getSubSettings().add(filterDuplicateMessagesTimeNumber);

		// Title: Magic prefix
		chatCategory.getSubSettings().add(new HeaderElement(""));
		chatCategory.getSubSettings().add(new HeaderElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_heads_magic_prefix")));

		// Anti magic clantag
		final BooleanElement ampClanEnabledBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_antiMagicClanTagEnabled"),
				new ControlElement.IconData("labymod/textures/settings/settings/particlefix.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				ampClanEnabled = value;
				getConfig().addProperty("ampClanEnabled", value);
				saveConfig();
			}
		}, ampClanEnabled);
		chatCategory.getSubSettings().add(ampClanEnabledBtn);

		// Anti magic prefix
		final BooleanElement ampEnabledBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_antiMagicPrefixEnabled"),
				new ControlElement.IconData("labymod/textures/settings/settings/particlefix.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				ampEnabled = value;
				getConfig().addProperty("ampEnabled", value);
				saveConfig();
			}
		}, ampEnabled);
		chatCategory.getSubSettings().add(ampEnabledBtn);

		// Magic prefix chat replacement
		final StringElement chatReplacementInput = new StringElement(LanguageManager.translateOrReturnKey("settings_gg_antiMagicPrefixChatReplacement"),
				new ControlElement.IconData("labymod/textures/settings/settings/keyplayermenu.png"), ampChatReplacement, new Consumer<String>() {
			@Override
			public void accept(String value) {
				ampChatReplacement = value;
				getConfig().addProperty("chatReplacement", value);
				saveConfig();
			}
		});
		chatCategory.getSubSettings().add(chatReplacementInput);

		// Magic prefix tablist replacement
		final StringElement tablistReplacementInput = new StringElement(LanguageManager.translateOrReturnKey("settings_gg_antiMagicPrefixTablistReplacement"),
				new ControlElement.IconData("labymod/textures/settings/settings/oldtablist.png"), ampTablistReplacement, new Consumer<String>() {
			@Override
			public void accept(String value) {
				ampTablistReplacement = value;
				getConfig().addProperty("tablistReplacement", value);
				saveConfig();
			}
		});
		chatCategory.getSubSettings().add(tablistReplacementInput);

		// Title: Chat time
		chatCategory.getSubSettings().add(new HeaderElement(""));
		chatCategory.getSubSettings().add(new HeaderElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_heads_chatTime")));

		// Show chat time
		final BooleanElement showChatTimeBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_displayChatTime"),
				new ControlElement.IconData("labymod/textures/settings/settings/afecentityinterval.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				showChatTime = value;
				getConfig().addProperty("showChatTime", value);
				saveConfig();
			}
		}, showChatTime);
		chatCategory.getSubSettings().add(showChatTimeBtn);

		// Chat time after message
		final BooleanElement chatTimeAfterMessageBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_chatTimeAfterMessage"),
				new ControlElement.IconData("labymod/textures/settings/settings/discordallowjoining.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				chatTimeAfterMessage = value;
				getConfig().addProperty("chatTimeAfterMessage", value);
				saveConfig();
			}
		}, chatTimeAfterMessage);
		chatCategory.getSubSettings().add(chatTimeAfterMessageBtn);

		// Chat time format
		final StringElement chatTimeFormatSetting = new StringElement(LanguageManager.translateOrReturnKey("settings_gg_chatTimeFormat"),
				new ControlElement.IconData("labymod/textures/chat/gui_editor.png"), chatTimeFormat, new Consumer<String>() {
			@Override
			public void accept(String value) {
				chatTimeFormat = value;
				getConfig().addProperty("chatTimeFormat", value);
				saveConfig();
			}
		});
		chatCategory.getSubSettings().add(chatTimeFormatSetting);

		// Category: Payment
		final ListContainerElement paymentCategory = new ListContainerElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_category_payment"),
				new ControlElement.IconData("labymod/textures/misc/economy_cash.png"));
		settings.add(paymentCategory);

		// Log transactions
		final BooleanElement logTransactionsBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_logTransactions"),
			new ControlElement.IconData("labymod/textures/settings/settings/sendanonymousstatistics.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					logTransactions = value;
					getConfig().addProperty("logTransactions", value);
					saveConfig();
				}
			}, logTransactions);
		paymentCategory.getSubSettings().add(logTransactionsBtn);

		// Open transactions file
		final ButtonElement openTransactionsLogBtn = new ButtonElement(LanguageManager.translateOrReturnKey("settings_gg_openTransactions"),
				LanguageManager.translateOrReturnKey("settings_gg_openTransactionsBtn"), new ControlElement.IconData("labymod/textures/settings/category/ingame_gui.png"),
				new Runnable() {
					@Override
					public void run() {
						getGG().getFileManager().openTransactionsFile();
					}
				});
		paymentCategory.getSubSettings().add(openTransactionsLogBtn);

		// Payment 2nd chat
		final BooleanElement payChatRightBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_payChatRight"),
			new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					payChatRight = value;
					getConfig().addProperty("payChatRight", value);
					saveConfig();
				}
			}, payChatRight);
		paymentCategory.getSubSettings().add(payChatRightBtn);

		// Payment achievement
		final BooleanElement payAchievementBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_payAchievement"),
				new ControlElement.IconData("labymod/textures/settings/settings/alertsonlinestatus.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				payAchievement = value;
				getConfig().addProperty("payAchievement", value);
				saveConfig();
			}
		}, payAchievement);
		paymentCategory.getSubSettings().add(payAchievementBtn);

		// Payment marker
		final BooleanElement payMarkerBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_payHighlight"),
				new ControlElement.IconData("labymod/textures/buttons/checkbox.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				payMarker = value;
				getConfig().addProperty("payMarker", value);
				saveConfig();
			}
		}, payMarker);
		paymentCategory.getSubSettings().add(payMarkerBtn);

		// Payment hover message
		final BooleanElement payHoverBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_payHover"),
				new ControlElement.IconData("labymod/textures/buttons/sign_search.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				payHover = value;
				getConfig().addProperty("payHover", value);
				saveConfig();
			}
		}, payHover);
		paymentCategory.getSubSettings().add(payHoverBtn);

		// Bank 2nd chat
		final BooleanElement bankChatRightBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_bankChatRight"),
			new ControlElement.IconData("labymod/textures/settings/settings/chatpositionright.png"),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					bankChatRight = value;
					getConfig().addProperty("bankChatRight", value);
					saveConfig();
				}
			}, bankChatRight);
		paymentCategory.getSubSettings().add(bankChatRightBtn);

		final BooleanElement bankAchievementBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_bankAchievement"),
				new ControlElement.IconData("labymod/textures/settings/settings/alertsonlinestatus.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				bankAchievement = value;
				getConfig().addProperty("bankAchievement", value);
				saveConfig();
			}
		}, bankAchievement);
		paymentCategory.getSubSettings().add(bankAchievementBtn);

		// Category: Automations
		final ListContainerElement automationsCategory = new ListContainerElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_category_automations"),
				new ControlElement.IconData("labymod/textures/chat/autotext.png"));
		settings.add(automationsCategory);

		// Auto update booster state
		final BooleanElement updateBoosterStateBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_updateBoosterStatusEnabled"),
				new ControlElement.IconData("labymod/textures/settings/settings/afecdistancedetection.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				updateBoosterState = value;
				getConfig().addProperty("updateBoosterState", value);
				saveConfig();
			}
		}, updateBoosterState);
		automationsCategory.getSubSettings().add(updateBoosterStateBtn);

		// Hide booster menu
		final BooleanElement hodeBoosterMenuBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_hideBoosterMenu"),
				new ControlElement.IconData("labymod/textures/misc/blocked.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				hideBoosterMenu = value;
				getConfig().addProperty("hideBoosterMenu", value);
				saveConfig();
			}
		}, hideBoosterMenu);
		automationsCategory.getSubSettings().add(hodeBoosterMenuBtn);

		// Clear map cache
		final BooleanElement clearMapCacheBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_clearMapCache"),
			new ControlElement.IconData(Material.MAP),
			new Consumer<Boolean>() {
				@Override
				public void accept(Boolean value) {
					clearMapCache = value;
					getConfig().addProperty("clearMapCache", clearMapCache);
					saveConfig();
				}
			}, clearMapCache);
		automationsCategory.getSubSettings().add(clearMapCacheBtn);

		// Portal on join
		final BooleanElement autoPortalBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_portalOnJoin"),
				new ControlElement.IconData("griefergames/textures/icons/portal.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				autoPortal = value;
				getConfig().addProperty("autoPortal", value);
				saveConfig();
			}
		}, autoPortal);
		automationsCategory.getSubSettings().add(autoPortalBtn);

		// Vanish on join
		final BooleanElement vanishOnJoinBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_vanishOnJoin"),
				new ControlElement.IconData("griefergames/textures/icons/module_vanish.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				vanishOnJoin = value;
				getConfig().addProperty("vanishOnJoin", value);
				saveConfig();
			}
		}, vanishOnJoin);
		automationsCategory.getSubSettings().add(vanishOnJoinBtn);

		// Fly on join
		final BooleanElement flyOnJoinBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_flyOnJoin"),
				new ControlElement.IconData("griefergames/textures/icons/booster_fliegen.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				flyOnJoin = value;
				getConfig().addProperty("flyOnJoin", value);
				saveConfig();
			}
		}, flyOnJoin);
		automationsCategory.getSubSettings().add(flyOnJoinBtn);

		// Title: AFK
		automationsCategory.getSubSettings().add(new HeaderElement(""));
		automationsCategory.getSubSettings().add(new HeaderElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_heads_afk")));

		// AFK time
		final CustomNumberElement afkTimeSetting = new CustomNumberElement(LanguageManager.translateOrReturnKey("settings_gg_afkTime"),
				new ControlElement.IconData("labymod/textures/settings/settings/afecplayerinterval.png"), afkTime);
		afkTimeSetting.setMinValue(0);
		afkTimeSetting.setMaxValue(60);
		afkTimeSetting.addCallback(new Consumer<Integer>() {
			@Override
			public void accept(Integer value) {
				afkTime = value;
				getConfig().addProperty("afkTime", value);
				saveConfig();
			}
		});
		automationsCategory.getSubSettings().add(afkTimeSetting);

		// AFK nick
		final BooleanElement afkNickBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_afkNick"),
				new ControlElement.IconData("labymod/textures/settings/modules/afk_timer.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean value) {
						afkNick = value;
						getConfig().addProperty("afkNick", value);
						saveConfig();
					}
				}, afkNick);
		automationsCategory.getSubSettings().add(afkNickBtn);

		// AFK nickname
		final StringElement afkNicknameSetting = new StringElement(LanguageManager.translateOrReturnKey("settings_gg_afkNickname"),
				new ControlElement.IconData("labymod/textures/settings/settings/afechideplayernames.png"), afkNickname, new Consumer<String>() {
			@Override
			public void accept(String value) {
				afkNickname = value;
				getConfig().addProperty("afkNickname", value);
				saveConfig();
			}
		});
		automationsCategory.getSubSettings().add(afkNicknameSetting);

		// AFK answear
		final BooleanElement afkMsgAnswearBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_afkMsgAnswear"),
				new ControlElement.IconData("labymod/textures/settings/modules/afk_timer.png"),
				new Consumer<Boolean>() {
					@Override
					public void accept(Boolean value) {
						afkMsgAnswear = value;
						getConfig().addProperty("afkMsgAnswear", value);
						saveConfig();
					}
				}, afkMsgAnswear);
		automationsCategory.getSubSettings().add(afkMsgAnswearBtn);

		// AFK message
		final StringElement afkMsgTextSetting = new StringElement(LanguageManager.translateOrReturnKey("settings_gg_afkMsgText"),
				new ControlElement.IconData(Material.BOOK_AND_QUILL), afkMsgText, new Consumer<String>() {
			@Override
			public void accept(String value) {
				afkMsgText = value;
				getConfig().addProperty("afkMsgText", value);
				saveConfig();
			}
		});
		automationsCategory.getSubSettings().add(afkMsgTextSetting);

		// Category: Friends
		final ListContainerElement friendsCategory = new ListContainerElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_category_friends"),
				new ControlElement.IconData("labymod/textures/settings/modules/online_players.png"));
		settings.add(friendsCategory);

		// Show citybuild in LabyChat
		final BooleanElement labyChatShowSubServerEnabledBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_labyChatEnabled"),
				new ControlElement.IconData("labymod/textures/settings/settings/motd.png"), new Consumer<Boolean>() {
					@Override
					public void accept(Boolean value) {
						labyChatShowSubServerEnabled = value;
						getConfig().addProperty("labyChatShowSubServerEnabled", value);
						saveConfig();
						if(value) {
							getGG().getHelper().updateLabyChatSubServer("lobby");
						} else {
							getGG().getHelper().updateLabyChatSubServer("reset");
						}
					}
				}, labyChatShowSubServerEnabled);
		friendsCategory.getSubSettings().add(labyChatShowSubServerEnabledBtn);

		// Show citybuild in Discord
		final BooleanElement discordShowSubServerEnabledBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_discordEnabled"),
				new ControlElement.IconData("labymod/textures/settings/settings/discordrichpresence.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				discordShowSubServerEnabled = value;
				getConfig().addProperty("discordShowSubServerEnabled", value);
				saveConfig();
				if(value) {
					getGG().getHelper().updateDiscordSubServer("lobby");
				} else {
					getGG().getHelper().updateDiscordSubServer("reset");
				}
			}
		}, discordShowSubServerEnabled);
		friendsCategory.getSubSettings().add(discordShowSubServerEnabledBtn);

		String commandsInfoText = "§7"+LanguageManager.translateOrReturnKey("settings_gg_cmdinfo");
		commandsInfoText += "\n§e/resetincome §8- §7"+LanguageManager.translateOrReturnKey("settings_gg_cmdinfo_resetincome");
		settings.add(new TextElement(commandsInfoText));

		infoText = new TextElement("");
		updateInfo();
		settings.add(infoText);
	}

	private void updateInfo() {
		String text = "§7Version: §a"+GrieferGames.VERSION;
		if(getGG().getUpdater().isUpdateAvailable()) {
			text += " §c(";
			if(autoUpdate) {
				if(getGG().getUpdater().canDoUpdate()) {
					text += LanguageManager.translateOrReturnKey("message_gg_updateReady");
				} else {
					text += LanguageManager.translateOrReturnKey("message_gg_updateFailed");
				}
			} else {
				text += LanguageManager.translateOrReturnKey("message_gg_updateAvailable");
			}
			text += ")";
		}

		text += "\n§7GitHub: §ahttps://github.com/Neocraftr/LabyMod-GrieferGames-Addon-1.8.9";
		infoText.setText(text);
	}

	public void reinitSettings() {
		List<SettingsElement> subSettings = getGG().getAddonnfo().getAddonElement().getSubSettings();
		subSettings.clear();
		fillSettings(subSettings);

		if(Minecraft.getMinecraft().currentScreen instanceof LabyModAddonsGui) {
			Minecraft.getMinecraft().currentScreen.initGui();
		}
	}

	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	private JsonObject getConfig() {
		return getGG().getConfig();
	}
	private void saveConfig() {
		getGG().saveConfig();
	}

	public boolean isModEnabled() {
		return this.modEnabled;
	}

	public EnumLanguages getLanguage() {
		return language;
	}

	public String getOverrideRank() {
		return overrideRank;
	}

	public boolean isShowChatTime() {
		return this.showChatTime;
	}

	public boolean isChatTimeAfterMessage() {
		return chatTimeAfterMessage;
	}

	public String getChatTimeFormat() {
		return chatTimeFormat;
	}

	public boolean isPrivateChatRight() {
		return this.privateChatRight;
	}

	public boolean isPlotChatRight() {
		return this.plotChatRight;
	}

	public EnumSounds getPrivateChatSound() {
		return this.privateChatSound;
	}

	public boolean isPrivateChatSound() {
		return privateChatSound != EnumSounds.NONE;
	}

	public EnumRealnameShown getRealname() {
		return this.realname;
	}

	public boolean isRealnameRight() {
		return realname == EnumRealnameShown.SECONDCHAT || realname == EnumRealnameShown.BOTH;
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

	public boolean isMsgDisplayNameClick() {
		return this.msgDisplayNameClick;
	}

	public boolean isPreventCommandFailure() {
		return this.preventCommandFailure;
	}

	public boolean isFilterDuplicateMessages() {
		return this.filterDuplicateMessages;
	}

	public Integer getFilterDuplicateMessagesTime() {
		return filterDuplicateMessagesTime;
	}

	public boolean isCleanBlanks() {
		return this.cleanBlanks;
	}

	public boolean isCleanSupremeBlanks() {
		return this.cleanSupremeBlanks;
	}

	public boolean isHighlightMentions() {
		return highlightMentions;
	}

	public Color getMentionsColor() {
		return mentionsColor;
	}

	public EnumSounds getMentionSound() {
		return mentionSound;
	}

	public boolean isMentionSound() {
		return mentionSound != EnumSounds.NONE;
	}

	public void setMentionSound(EnumSounds mentionSound) {
		this.mentionSound = mentionSound;
	}

	public boolean isPayChatRight() {
		return this.payChatRight;
	}

	public boolean isPayAchievement() {
		return this.payAchievement;
	}

	public boolean isPayMarker() {
		return this.payMarker;
	}

	public boolean isPayHover() {
		return this.payHover;
	}

	public boolean isAfkNick() {
		return afkNick;
	}

	public boolean isAfkMsgAnswear() {
		return afkMsgAnswear;
	}

	public String getAfkMsgText() {
		return afkMsgText;
	}

	public String getAfkNickname() {
		return afkNickname;
	}

	public int getAfkTime() {
		return afkTime;
	}

	public boolean isBankChatRight() {
		return this.bankChatRight;
	}

	public boolean isBankAchievement() {
		return this.bankAchievement;
	}

	public boolean isItemRemoverChatRight() {
		return this.itemRemoverChatRight;
	}

	public boolean isItemRemoverLastTimeHover() {
		return this.itemRemoverLastTimeHover;
	}

	public boolean isMobRemoverChatRight() {
		return this.mobRemoverChatRight;
	}

	public boolean isMobRemoverLastTimeHover() {
		return this.mobRemoverLastTimeHover;
	}

	public boolean isBetterIgnoreList() {
		return this.betterIgnoreList;
	}

	public boolean isAMPEnabled() {
		return this.ampEnabled;
	}

	public boolean isAMPClanEnabled() {
		return this.ampClanEnabled;
	}

	public String getAMPChatReplacement() {
		return ampChatReplacement;
	}

	public String getAMPTablistReplacement() {
		return ampTablistReplacement;
	}

	public boolean isMarkTPAMsg() {
		return markTPAMsg;
	}

	public boolean isCleanVoteMsg() {
		return cleanVoteMsg;
	}

	public boolean isCleanNewsMsg() {
		return cleanNewsMsg;
	}

	public boolean isUpdateBoosterState() {
		return updateBoosterState;
	}

	public boolean isClearMapCache() {
		return clearMapCache;
	}

	public boolean isLabyChatShowSubServerEnabled() {
		return labyChatShowSubServerEnabled;
	}

	public boolean isDiscordShowSubServerEnabled() {
		return discordShowSubServerEnabled;
	}

	public boolean isAutoPortl() {
		return autoPortal;
	}

	public boolean isHideBoosterMenu() {
		return hideBoosterMenu;
	}

	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	public boolean isVanishOnJoin() {
		return vanishOnJoin;
	}

	public boolean isFlyOnJoin() {
		return flyOnJoin;
	}

	public boolean isLogTransactions() {
		return logTransactions;
	}

	public boolean isShowPrefixInDisplayName() {
		return showPrefixInDisplayName;
	}
}