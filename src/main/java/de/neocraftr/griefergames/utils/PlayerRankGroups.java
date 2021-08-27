package de.neocraftr.griefergames.utils;

import java.util.Arrays;
import java.util.List;

public class PlayerRankGroups {

    private final List<String> vanishRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
            "obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "youtuber+", "yt+");
    private final List<String> vanishDefaultOnRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
            "obergeier", "developer", "deppelopfer", "dev", "moderator", "mod");
    private final List<String> godModeRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
            "obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter", "sup");
    private final List<String> auraRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
            "obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "youtuber+", "yt+");
    private final List<String> flyRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
            "obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter", "sup", "youtuber+", "yt+");
    private final List<String> noPrefixRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
            "obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter", "sup", "youtuber+", "yt+",
            "youtuber", "yt");

    public boolean hasVanish(String playerRank) {
        return vanishRanks.contains(playerRank);
    }

    public boolean getDefaultVanishState(String playerRank) {
        return vanishDefaultOnRanks.contains(playerRank);
    }

    public boolean hasGodMode(String playerRank) {
        return godModeRanks.contains(playerRank);
    }

    public boolean hasAura(String playerRank) {
        return auraRanks.contains(playerRank);
    }

    public boolean hasFly(String playerRank) {
        return flyRanks.contains(playerRank);
    }

    public boolean canHavePrefix(String playerRank) {
        return !noPrefixRanks.contains(playerRank);
    }
}
