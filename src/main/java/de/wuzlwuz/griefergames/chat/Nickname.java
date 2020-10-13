package de.wuzlwuz.griefergames.chat;

import net.labymod.utils.ModColor;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nickname extends Chat {
    private static Pattern msgUserGlobalChatRegex = Pattern
            .compile("^(?:\\[[^\\]]+\\]\\s)?([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16})\\s\\u00BB");
    private static Pattern nicknameMsgRegex = Pattern
            .compile("^\\[GrieferGames\\] Dein Name lautet nun ([A-Za-z\\-]+\\+?) \\u2503 (\\u007E)?(\\!?\\w{1,16}).$");

    @Override
    public String getName() {
        return "nickname";
    }

    @Override
    public boolean doAction(String unformatted, String formatted) {
        if (unformatted.trim().length() > 0) {
            Matcher nicknameMsg = nicknameMsgRegex.matcher(unformatted);
            if (nicknameMsg.find()) {
                getGG().setNickname(nicknameMsg.group(3));
            } else if (unformatted.trim().equalsIgnoreCase("[GrieferGames] Dein Name wurde zurückgesetzt.")) {
                getGG().setNickname("");
            }
        }

        return false;
    }

    @Override
    public boolean doActionHandleChatMessage(String unformatted, String formatted) {
        return doAction(unformatted, formatted);
    }

    @Override
    public boolean doActionModifyChatMessage(IChatComponent msg) {
        return getSettings().isRealnameClick();
    }

    @Override
    public IChatComponent modifyChatMessage(IChatComponent msg) {
        String unformatted = msg.getUnformattedText();

        Matcher userGlobalChat = msgUserGlobalChatRegex.matcher(unformatted);
        if (userGlobalChat.find()) {
            String userString = userGlobalChat.group(1);
            String nickname = getHelper().getPlayerName(userString);

            boolean doReplacement = true;
            for (IChatComponent msgPart : msg.getSiblings()) {
                String AMPText = getSettings().getAMPTablistReplacement();

                if (!AMPText.contains("%CLEAN%")) {
                    AMPText = getSettings().getDefaultAMPTablistReplacement();
                }

                AMPText = AMPText.replaceAll("%CLEAN%", userString);
                AMPText = "${REPSTART}" + AMPText + "${REPEND}";

                if (doReplacement &&
                        ((msgPart.getUnformattedText().trim().equalsIgnoreCase(userString)) ||
                                (getSettings().isAMPEnabled() &&
                                        msgPart.getUnformattedText().trim().equalsIgnoreCase(AMPText)) ||
                                (msgPart.getUnformattedText().trim().equalsIgnoreCase(nickname)))) {
                    ChatStyle appendMsgStyle = new ChatStyle();

                    IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") + "Click to see realname!");
                    appendMsgStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            hoverText));
                    appendMsgStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/realname " + nickname));

                    IChatComponent newMsg = msg.createCopy();

                    IChatComponent appendMsg = new ChatComponentText(" " + ModColor.cl("r") +
                            ModColor.cl("6") + "[" + ModColor.cl("f") + "RN" + ModColor.cl("6") +
                            "]").setChatStyle(appendMsgStyle);
                    newMsg.getSiblings().get(newMsg.getSiblings().indexOf(msgPart)).appendSibling
                            (appendMsg);

                    doReplacement = false;
                    msg = newMsg;
                }
            }
        }
        return msg;
    }
}
