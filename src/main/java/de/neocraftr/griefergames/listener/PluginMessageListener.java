package de.neocraftr.griefergames.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.neocraftr.griefergames.GrieferGames;
import io.netty.buffer.ByteBuf;
import net.labymod.api.events.PluginMessageEvent;
import net.labymod.main.LabyMod;
import net.minecraft.network.PacketBuffer;

import java.util.concurrent.TimeUnit;

public class PluginMessageListener implements PluginMessageEvent {

    JsonParser parser = new JsonParser();

    @Override
    public void receiveMessage(String channel, PacketBuffer packetBufferOrig) {
        if (!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return;

        if(!channel.equals("mysterymod:mm")) return;
        ByteBuf packetBuffer = packetBufferOrig.copy();

        if(packetBuffer.readableBytes() <= 0) return;
        String messageKey = getGG().getHelper().readStringFromBuffer(32767, packetBuffer);

        if(packetBuffer.readableBytes() <= 0) return;
        String jsonMessage = getGG().getHelper().readStringFromBuffer(32767, packetBuffer);

        //System.out.println("MysteryMod message: "+messageKey+" - "+jsonMessage);

        JsonElement message;
        try {
            message = parser.parse(jsonMessage);
        } catch(Exception err) {
            System.err.println("Error while parsing MysteryMod message: "+err.getMessage());
            return;
        }

        if(messageKey.equals("user_subtitle")) {
            JsonArray subtitleArray = message.getAsJsonArray();
            JsonObject subtitle = subtitleArray.get(0).getAsJsonObject();

            subtitle.addProperty("uuid", subtitle.get("targetId").getAsString());
            subtitle.addProperty("value", subtitle.get("text").getAsString());
            subtitle.addProperty("size", 1.2);

            LabyMod.getInstance().getEventManager().callServerMessage("account_subtitle", subtitleArray);
        }

        if(messageKey.equals("redstone")) {
            String redstoneState = message.getAsJsonObject().get("status").getAsString();
            getGG().setRedstoneActive(redstoneState.equals("0"));
        }

        if(messageKey.equals("countdown_create")) {
            JsonObject countdown = message.getAsJsonObject();
            String name = countdown.get("name").getAsString();
            TimeUnit timeUnit = TimeUnit.valueOf(countdown.get("unit").getAsString());
            long until = countdown.get("until").getAsLong();
            if(name.equals("ClearLag")) {
                getGG().setClearLagTime(System.currentTimeMillis() + timeUnit.toMillis(until));
            }
        }

        if(messageKey.equals("coins")) {
            int amount = message.getAsJsonObject().get("amount").getAsInt();
            if(getGG().getBalance() != amount) {
                getGG().setBalance(amount);
                getGG().getHelper().updateBalance("cash");
            }
        }

        if(messageKey.equals("bank")) {
            int amount = message.getAsJsonObject().get("amount").getAsInt();
            if(getGG().getBankBalance() != amount) {
                getGG().setBankBalance(amount);
                getGG().getHelper().updateBalance("bank");
            }
        }
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
