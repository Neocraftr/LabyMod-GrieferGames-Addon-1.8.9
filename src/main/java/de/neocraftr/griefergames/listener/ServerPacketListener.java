package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.minecraft.network.play.server.S3EPacketTeams;

import java.util.Timer;
import java.util.TimerTask;

public class ServerPacketListener implements Consumer<Object> {

    @Override
    public void accept(Object packet) {
        if(packet instanceof S3EPacketTeams) {
            S3EPacketTeams scoreboardPacket = (S3EPacketTeams) packet;
            String name = scoreboardPacket.func_149312_c();
            String value = scoreboardPacket.func_149311_e();

            if(name.equals("server_value")) {
                value = ModColor.removeColor(value).toLowerCase();
                if(value.trim().equals("") || value.contains("lade")) return;
                if(!getGG().getSubServer().equals(value)) {
                    getGG().setSubServer(value);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getGG().callSubServerEvent(getGG().getSubServer());
                        }
                    }, 1000);
                }
            }
        }
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
