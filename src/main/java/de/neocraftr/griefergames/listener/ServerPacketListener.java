package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.minecraft.network.play.server.S3EPacketTeams;

public class ServerPacketListener implements Consumer<Object> {

    @Override
    public void accept(Object packet) {
        if(packet instanceof S3EPacketTeams) {
            S3EPacketTeams scoreboardPacket = (S3EPacketTeams) packet;
            String name = scoreboardPacket.func_149312_c();
            String value = scoreboardPacket.func_149311_e();

            if(name.equals("server_value")) {
                value = ModColor.removeColor(value);
                if(value.trim().equals("") || value.contains("Lade")) return;
                if(!getGG().getSubServer().equals(value)) {
                    getGG().callSubServerEvent(getGG().getSubServer(), value);
                    getGG().setSubServer(value);
                }
            }
        }
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
