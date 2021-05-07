package de.neocraftr.griefergames.chat;

import net.labymod.ingamechat.tools.filter.Filters;
import net.labymod.main.LabyMod;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mention extends Chat {
    private Pattern msgUserGlobalChatRegex = Pattern.compile("^(\\[[^\\]]+\\])? ?([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16})\\s[\\u00BB:]\\s(.+)");

    @Override
    public String getName() {
        return "mentions";
    }


    @Override
    public boolean doActionReceiveMessage(String formatted, String unformatted) {
        if(!getSettings().isHighlightMentions() && !getSettings().isMentionSound()) return false;
        if(unformatted.trim().length() <= 0) return false;

        Matcher matcher = msgUserGlobalChatRegex.matcher(unformatted);
        if(matcher.find()) {
            if(!matcher.group(3).equalsIgnoreCase(LabyMod.getInstance().getPlayerName())) {
                for(String word : matcher.group(4).split(" ")) {
                    if(word.equalsIgnoreCase(LabyMod.getInstance().getPlayerName().toLowerCase())) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean receiveMessage(String formatted, String unformatted) {
        short r = (short) getSettings().getMentionsColor().getRed();
        short g = (short) getSettings().getMentionsColor().getGreen();
        short b = (short) getSettings().getMentionsColor().getBlue();
        String soundPath = getHelper().getSoundPath(getSettings().getMentionSound());
        Filters.Filter filter = new Filters.Filter("GG Addon Mention", new String[] {unformatted}, new String[0],
                getSettings().isMentionSound(), soundPath, getSettings().isHighlightMentions(), r, g, b,
                false, false, false, "Global");

        LabyMod.getInstance().getChatToolManager().getFilters().add(filter);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LabyMod.getInstance().getChatToolManager().getFilters().remove(filter);
            }
        }, 2000);

        return false;
    }
}
