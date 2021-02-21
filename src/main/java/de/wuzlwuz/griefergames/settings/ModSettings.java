package de.wuzlwuz.griefergames.settings;

import java.awt.*;
import java.util.List;

import com.google.gson.JsonObject;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.enums.EnumLanguages;
import de.wuzlwuz.griefergames.enums.EnumRealnameShown;
import de.wuzlwuz.griefergames.enums.EnumSounds;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ColorPicker;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.util.ResourceLocation;

public class ModSettings {
	public static final String DEFAULT_AMP_REPLACEMENT_CHAT = "[AMP] %CLEAN%",
			DEFAULT_AMP_REPLACEMENT_TABLIST = "[AMP] %CLEAN%",
			DEFAULT_AFK_NICKNAME = "AFK_%name%";

	private TextElement infoText;

	private boolean modEnabled = true;
	private EnumLanguages language = EnumLanguages.GAMELANGUAGE;
	private String overrideRank = null;
	private boolean showChatTime = false;
	private boolean chatTimeShortFormat = true;
	private boolean chatTimeAfterMessage = false;
	private boolean privateChatRight = true;
	private boolean plotChatRight = true;
	private EnumSounds privateChatSound = EnumSounds.NONE;
	private EnumRealnameShown realname = EnumRealnameShown.DEFAULT;
	// private boolean realnameClick = false;
	private boolean msgDisplayNameClick = true;
	private boolean filterDuplicateMessages = false;
	private Integer filterDuplicateMessagesTime = 5;
	private boolean cleanBlanks = false;
	private boolean cleanSupremeBlanks = false;
	private boolean highlightMentions = true;
	private Color mentionsColor = new Color(121, 178, 255);
	private EnumSounds mentionSound = EnumSounds.NONE;
	private boolean afkNick = false;
	private String afkNickname = DEFAULT_AFK_NICKNAME;
	private int afkTime = 15;
	private boolean afkMsgAnswear = false;
	private String afkMsgText = "Ich bin momentan AFK ;)";
	private boolean payChatRight = true;
	private boolean payAchievement = false;
	private boolean payMarker = false;
	private boolean payHover = false;
	private boolean bankChatRight = true;
	private boolean bankAchievement = false;
	private boolean itemRemoverChatRight = false;
	private boolean itemRemoverLastTimeHover = true;
	private boolean mobRemoverChatRight = false;
	private boolean mobRemoverLastTimeHover = true;
	private boolean betterIgnoreList = true;
	private boolean ampEnabled = true;
	private boolean ampClanEnabled = false;
	private String ampChatReplacement = DEFAULT_AMP_REPLACEMENT_CHAT;
	private String ampTablistReplacement = DEFAULT_AMP_REPLACEMENT_TABLIST;
	private boolean preventCommandFailure = false;
	private boolean markTPAMsg = true;
	private boolean cleanVoteMsg = false;
	private boolean cleanNewsMsg = false;
	private boolean updateBoosterState = true;
	private boolean clearMapCache = false;
	private boolean labyChatShowSubServerEnabled = true;
	private boolean discordShowSubServerEnabled = true;
	private boolean autoPortal = false;
	private boolean hideBoosterMenu = true;
	private boolean autoUpdate = true;
	private boolean vanishOnJoin = false;
	private boolean flyOnJoin = false;
	private boolean logTransactions = true;

	public void loadConfig() {
		if (getConfig().has("modEnabled"))
			modEnabled = getConfig().get("modEnabled").getAsBoolean();

		if (getConfig().has("language"))
			language = EnumLanguages.valueOf(getConfig().get("language").getAsString());

		if(getConfig().has("overrideRank"))
			overrideRank = getConfig().get("overrideRank").getAsString();

		if (getConfig().has("showChatTime"))
			showChatTime = getConfig().get("showChatTime").getAsBoolean();

		if (getConfig().has("chatTimeAfterMessage"))
			chatTimeAfterMessage = getConfig().get("chatTimeAfterMessage").getAsBoolean();

		if (getConfig().has("chatTimeShortFormat"))
			chatTimeShortFormat = getConfig().get("chatTimeShortFormat").getAsBoolean();

		if (getConfig().has("privateChatRight"))
			privateChatRight = getConfig().get("privateChatRight").getAsBoolean();

		if (getConfig().has("privateChatSound"))
			privateChatSound = EnumSounds.valueOf(getConfig().get("privateChatSound").getAsString());

		if (getConfig().has("msgDisplayNameClick"))
			msgDisplayNameClick = getConfig().get("msgDisplayNameClick").getAsBoolean();

		if (getConfig().has("filterDuplicateMessages"))
			filterDuplicateMessages = getConfig().get("filterDuplicateMessages").getAsBoolean();

		if (getConfig().has("filterDuplicateMessagesTime"))
			 filterDuplicateMessagesTime = getConfig().get("filterDuplicateMessagesTime").getAsInt();

		if (getConfig().has("cleanBlanks"))
			cleanBlanks = getConfig().get("cleanBlanks").getAsBoolean();

		if (getConfig().has("cleanSupremeBlanks"))
			cleanSupremeBlanks = getConfig().get("cleanSupremeBlanks").getAsBoolean();

		if(getConfig().has("highlightMentions"))
			highlightMentions = getConfig().get("highlightMentions").getAsBoolean();

		if(getConfig().has("mentionsColor"))
			mentionsColor = new Color(getConfig().get("mentionsColor").getAsInt());

		if(getConfig().has("mentionSound"))
			mentionSound = EnumSounds.valueOf(getConfig().get("mentionSound").getAsString());

		if (getConfig().has("payChatRight"))
			payChatRight = getConfig().get("payChatRight").getAsBoolean();

		if (getConfig().has("payAchievement"))
			payAchievement = getConfig().get("payAchievement").getAsBoolean();

		if (getConfig().has("payMarker"))
			payMarker = getConfig().get("payMarker").getAsBoolean();

		if (getConfig().has("payHover"))
			payHover = getConfig().get("payHover").getAsBoolean();

		if(getConfig().has("afkNick"))
			afkNick = getConfig().get("afkNick").getAsBoolean();

		if(getConfig().has("afkMsgAnswear"))
			afkMsgAnswear = getConfig().get("afkMsgAnswear").getAsBoolean();

		if(getConfig().has("afkMsgText"))
			afkMsgText = getConfig().get("afkMsgText").getAsString();

		if(getConfig().has("afkNickname"))
			afkNickname = getConfig().get("afkNickname").getAsString();

		if(getConfig().has("afkTime"))
			afkTime = getConfig().get("afkTime").getAsInt();

		if (getConfig().has("bankChatRight"))
			bankChatRight = getConfig().get("bankChatRight").getAsBoolean();

		if (getConfig().has("bankAchievement"))
			bankAchievement = getConfig().get("bankAchievement").getAsBoolean();

		if (getConfig().has("plotChatRight"))
			plotChatRight = getConfig().get("plotChatRight").getAsBoolean();

		if (getConfig().has("realname"))
			realname = EnumRealnameShown.valueOf(getConfig().get("realname").getAsString());

		/*
		 * if (getConfig().has("realnameClick"))
		 * setRealnameClick(getConfig().get("realnameClick").getAsBoolean());
		 */

		if (getConfig().has("itemRemoverChatRight"))
			itemRemoverChatRight = getConfig().get("itemRemoverChatRight").getAsBoolean();

		if (getConfig().has("itemRemoverLastTimeHover"))
			itemRemoverLastTimeHover = getConfig().get("itemRemoverLastTimeHover").getAsBoolean();

		if (getConfig().has("mobRemoverChatRight"))
			mobRemoverChatRight = getConfig().get("mobRemoverChatRight").getAsBoolean();

		if (getConfig().has("mobRemoverLastTimeHover"))
			mobRemoverLastTimeHover = getConfig().get("mobRemoverLastTimeHover").getAsBoolean();

		if (getConfig().has("betterIgnoreList"))
			betterIgnoreList = getConfig().get("betterIgnoreList").getAsBoolean();

		if (getConfig().has("ampEnabled"))
			ampEnabled = getConfig().get("ampEnabled").getAsBoolean();

		if (getConfig().has("ampClanEnabled"))
			ampClanEnabled = getConfig().get("ampClanEnabled").getAsBoolean();

		if (getConfig().has("chatReplacement"))
			ampChatReplacement = getConfig().get("chatReplacement").getAsString();

		if (getConfig().has("tablistReplacement"))
			ampTablistReplacement = getConfig().get("tablistReplacement").getAsString();

		if (getConfig().has("preventCommandFailure"))
			preventCommandFailure = getConfig().get("preventCommandFailure").getAsBoolean();

		if (getConfig().has("markTPAMsg"))
			markTPAMsg = getConfig().get("markTPAMsg").getAsBoolean();

		if (getConfig().has("cleanVoteMsg"))
			cleanVoteMsg = getConfig().get("cleanVoteMsg").getAsBoolean();

		if (getConfig().has("cleanNewsMsg"))
			cleanNewsMsg = getConfig().get("cleanNewsMsg").getAsBoolean();

		if (getConfig().has("updateBoosterState"))
			updateBoosterState = getConfig().get("updateBoosterState").getAsBoolean();

		if (getConfig().has("clearMapCache"))
			clearMapCache = getConfig().get("clearMapCache").getAsBoolean();

		if (getConfig().has("labyChatShowSubServerEnabled"))
			labyChatShowSubServerEnabled = getConfig().get("labyChatShowSubServerEnabled").getAsBoolean();
		if (getConfig().has("discordShowSubServerEnabled"))
			discordShowSubServerEnabled = getConfig().get("discordShowSubServerEnabled").getAsBoolean();

		if (getConfig().has("autoPortal"))
			autoPortal = getConfig().get("autoPortal").getAsBoolean();

		if (getConfig().has("hideBoosterMenu"))
			hideBoosterMenu = getConfig().get("hideBoosterMenu").getAsBoolean();

		if (getConfig().has("autoUpdate"))
			autoUpdate = getConfig().get("autoUpdate").getAsBoolean();

		if (getConfig().has("vanishOnJoin"))
			vanishOnJoin = getConfig().get("vanishOnJoin").getAsBoolean();

		if (getConfig().has("flyOnJoin"))
			flyOnJoin = getConfig().get("flyOnJoin").getAsBoolean();

		if (getConfig().has("logTransactions"))
			logTransactions = getConfig().get("logTransactions").getAsBoolean();
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

		// Language
		final DropDownMenu<EnumLanguages> languageDropDownMenu = new DropDownMenu<EnumLanguages>(
				LanguageManager.translateOrReturnKey("settings_gg_addonLanguage"), 0, 0, 0, 0).fill(EnumLanguages.values());

		final DropDownElement<EnumLanguages> languageDropDown = new DropDownElement<EnumLanguages>(
				LanguageManager.translateOrReturnKey("settings_gg_addonLanguage"), languageDropDownMenu);

		languageDropDownMenu.setSelected(language);

		languageDropDown.setChangeListener(new Consumer<EnumLanguages>() {
			@Override
			public void accept(EnumLanguages value) {
				language = value;
				getConfig().addProperty("language", value.name());
				saveConfig();
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
					System.out.println(value);
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
				mobRemoverChatRight = value;
				getConfig().addProperty("mobRemoverLastTimeHover", value);
				saveConfig();
			}
		}, mobRemoverChatRight);
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

		// Chat time short format
		final BooleanElement chatTimeShortFormatBtn = new BooleanElement(LanguageManager.translateOrReturnKey("settings_gg_chatTimeShortVersion"),
				new ControlElement.IconData("labymod/textures/settings/modules/range.png"), new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				chatTimeShortFormat = value;
				getConfig().addProperty("chatTimeShortFormat", value);
				saveConfig();
			}
		}, chatTimeShortFormat);
		chatCategory.getSubSettings().add(chatTimeShortFormatBtn);

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
		chatCategory.getSubSettings().add(new HeaderElement(""));
		chatCategory.getSubSettings().add(new HeaderElement("§b§l"+LanguageManager.translateOrReturnKey("settings_gg_heads_afk")));

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
		chatCategory.getSubSettings().add(afkTimeSetting);

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
		chatCategory.getSubSettings().add(afkNickBtn);

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
		chatCategory.getSubSettings().add(afkNicknameSetting);

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
		chatCategory.getSubSettings().add(afkMsgAnswearBtn);

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
		chatCategory.getSubSettings().add(afkMsgTextSetting);

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

		// Show yitybuild in Discord
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


		infoText = new TextElement("");
		updateInfo();
		settings.add(infoText);
	}

	private void updateInfo() {
		String text = "§7Version: §e"+GrieferGames.VERSION;
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

	public boolean isChatTimeShortFormat() {
		return chatTimeShortFormat;
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
}