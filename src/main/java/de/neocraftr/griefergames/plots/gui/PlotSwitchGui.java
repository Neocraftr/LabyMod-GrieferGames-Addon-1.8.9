package de.neocraftr.griefergames.plots.gui;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.CityBuild;
import de.neocraftr.griefergames.plots.Plot;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.main.ModTextures;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

// Some parts used from: net.labymod.user.gui.UserActionGui

public class PlotSwitchGui extends Gui {

    private float lockedYaw;
    private float lockedPitch;
    private boolean open;
    private boolean prevCrosshairState;
    private long menuOpenedAt;
    private List<Plot> plots;
    private Plot selectedPlot;

    public static void scaleResolution() {
        if(getGG() != null) {
            if(getGG().getPlotSwitchGui() != null) {
                if(!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return;
                getGG().getPlotSwitchGui().lockMouseMovementInCircle();
            }
        }
    }

    public void open() {
        if(this.open) return;

        CityBuild currentCityBuild = getGG().getHelper().cityBuildFromServerName(getGG().getSubServer(), null);
        if(currentCityBuild != null) {
            this.plots = getGG().getPlotManager().getPlotsForGui(currentCityBuild);
        } else {
            return;
        }

        EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if(player != null) {
            this.lockedYaw = player.rotationYaw;
            this.lockedPitch = player.rotationPitch;
        }

        this.prevCrosshairState = LabyMod.getInstance().getLabyModAPI().isCrosshairHidden();
        LabyMod.getInstance().getLabyModAPI().setCrosshairHidden(true);
        this.menuOpenedAt = System.currentTimeMillis();

        this.open = true;
    }

    private void close() {
        if(!this.open) return;

        EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if(player != null) {
            player.rotationYaw = this.lockedYaw;
            player.rotationPitch = this.lockedPitch;
        }

        LabyMod.getInstance().getLabyModAPI().setCrosshairHidden(this.prevCrosshairState);
        this.open = false;
    }

    public void tick() {
        if(this.open) {
            if(!LabyMod.getInstance().isInGame() || Minecraft.getMinecraft().currentScreen != null) {
                this.open = false;
                return;
            }

            if(!Keyboard.isKeyDown(getGG().getSettings().getPlotMenuKey())) {
                if(this.selectedPlot != null) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(this.selectedPlot.getCommand());
                }
                close();
            }
        } else if(getGG().getSettings().getPlotMenuKey() != -1 && Keyboard.isKeyDown(getGG().getSettings().getPlotMenuKey())
                && Minecraft.getMinecraft().currentScreen == null) {
            open();
        }
    }

    public void render() {
        if(!this.open) return;

        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if(player == null) return;

        double radiusMouseBorder = (double)LabyMod.getInstance().getDrawUtils().getHeight() / 4.0D / 3.0D;
        double midX = (double)draw.getWidth() / 2.0D;
        double midY = (double)draw.getHeight() / 2.0D;
        double lockedX = (double)this.lockedYaw;
        double lockedY = (double)this.lockedPitch;
        if (lockedY + radiusMouseBorder > 90.0D) {
            lockedY = (double)((float)(90.0D - radiusMouseBorder));
        }

        if (lockedY - radiusMouseBorder < -90.0D) {
            lockedY = (double)((float)(-90.0D + radiusMouseBorder));
        }

        double radius = (double)draw.getHeight() / 4.0D;
        double offsetX = lockedX - (double)player.rotationYaw;
        double offsetY = lockedY - (double)player.rotationPitch;
        double distance = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
        double cursorX = midX - offsetX * 1.5D;
        double cursorY = midY - offsetY * 1.5D;
        midX += offsetX;
        midY += offsetY;

        draw.drawCenteredString("§a§l"+LanguageManager.translateOrReturnKey("gui_gg_plots_title"), midX, midY - 10, radius / 70.0D);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        int index = 0;
        int amount = this.plots.size();
        this.selectedPlot = null;
        if (amount != 0) {
            for(Plot plot : plots) {
                this.drawUnit(plot, midX, midY, radius, amount, index, cursorX, cursorY, distance);
                index++;
            }
        } else {
            Plot infoMessage = new Plot(LanguageManager.translateOrReturnKey("gui_gg_plots_empty"), null, null);
            this.drawUnit(infoMessage, midX, midY, radius, 1, 0, cursorX, cursorY, distance);
        }

        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        if (offsetX == 0.0D && offsetY == 0.0D) {
            cursorX = (double)((int)cursorX);
            cursorY = (double)((int)cursorY);
        }

        draw.drawRect(cursorX, cursorY - 4.0D, cursorX + 1.0D, cursorY + 5.0D, 2147483647);
        draw.drawRect(cursorX - 4.0D, cursorY, cursorX + 5.0D, cursorY + 1.0D, 2147483647);
    }

    private void drawUnit(Plot plot, double midX, double midY, double radius, int amount, int index, double cursorX, double cursorY, double distance) {
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();

        long timePassed = System.currentTimeMillis() - (this.menuOpenedAt - 1072L);
        float animation = (float)(timePassed < 1572L ? Math.sin((float)timePassed / 1000.0F) : 1.0D);

        double tau = 6.28318530717958D;
        double destinationShift = 2;
        double shift = destinationShift / (double)(animation * animation);
        double xNext = midX + radius * 2.0D * Math.cos((double)(index + 1) * tau / (double)amount + shift);
        double yNext = midY + radius * 2.0D * Math.sin((double)(index + 1) * tau / (double)amount + shift);
        double xAfterNext = midX + radius * 2.0D * Math.cos((double)(index + 2) * tau / (double)amount + shift);
        double yAfterNext = midY + radius * 2.0D * Math.sin((double)(index + 2) * tau / (double)amount + shift);
        double x = midX + radius * Math.cos(((double)index + 1.5D) * tau / (double)amount + shift);
        double y = midY + radius * Math.sin(((double)index + 1.5D) * tau / (double)amount + shift);
        double finalDestX = midX + radius * Math.cos(((double)index + 1.5D) * tau / (double)amount + destinationShift);
        double finalDestY = midY + radius * Math.sin(((double)index + 1.5D) * tau / (double)amount + destinationShift);
        boolean insideOfTwo = cursorY > midY && index == 0 || cursorY < midY && index != 0;
        boolean inside = amount > 2 ? this.isInside(cursorX, cursorY, xAfterNext, yAfterNext, midX, midY, xNext, yNext) : (amount == 1 ? true : insideOfTwo);
        boolean hover = distance > 10.0D && inside && timePassed > 1500L;
        double buttonWidth = 256.0D;
        double textureWidth = 9.0D;
        double textureHeight = 9.0D;
        double size = radius / 70.0D;
        if (size < 3.0D) {
            size = 1.0D;
        }

        textureWidth *= size;
        textureHeight *= size;
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_MENU_POINT);
        if(plot.getCommand() == null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        } else if (hover) {
            GL11.glColor4f(0.2F, 1.0F, 0.2F, 1.0F);
        } else {
            GL11.glColor4f(0.23F, 0.7F, 1.0F, 1.0F);
        }

        if (finalDestX - midX > -10.0D && finalDestX - midX < 10.0D) {
            if (finalDestY > midY) {
                draw.drawTexture(x - (double)((int)(textureWidth / 2.0D)), y - 13.0D, buttonWidth, buttonWidth, textureWidth, textureHeight, 2.0F);
            } else {
                draw.drawTexture(x - (double)((int)(textureWidth / 2.0D)), y + 13.0D, buttonWidth, buttonWidth, textureWidth, textureHeight, 2.0F);
            }

            this.drawOptionTag(plot, x, y, hover, radius, 0);
        } else if (finalDestX > midX) {
            draw.drawTexture(x - 13.0D * size, y, buttonWidth, buttonWidth, textureWidth, textureHeight, 2.0F);

            this.drawOptionTag(plot, x, y, hover, radius, 1);
        } else {
            draw.drawTexture(x + 4.0D, y, buttonWidth, buttonWidth, textureWidth, textureHeight, 2.0F);

            this.drawOptionTag(plot, x, y, hover, radius, -1);
        }

        GL11.glLineWidth(2.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.VOID);
    }

    private void drawOptionTag(Plot plot, double x, double y, boolean hover, double radius, int alignment) {
        String displayName = plot.getName();
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        int stringWidth = draw.getStringWidth(displayName);
        int tagPadding = hover  && plot.getCommand() != null ? 3 : 2;
        int outlineColor = hover && plot.getCommand() != null ? 2147483647 : -2147483648;
        int backgroundColor = ModColor.toRGB(20, 20, 20, 100);
        double size = radius / 70.0D;
        if (size < 3.0D) {
            size = 1.0D;
        }

        int tagHeight = (int) size * 9;
        stringWidth = (int)((double)stringWidth * size);
        double tagX = x;
        double tagY = y + (double)tagHeight;
        if (alignment < 0) {
            tagX = x - (double)stringWidth;
        } else if (alignment == 0) {
            tagX = x - (double)stringWidth / 2.0D;
        }

        draw.drawRect(tagX - (double)tagPadding, tagY - (double)tagHeight - (double)tagPadding, tagX + (double)stringWidth + (double)tagPadding, tagY + (double)tagPadding, backgroundColor);

        draw.drawRectBorder(tagX - (double)tagPadding - 1.0D, tagY - (double)tagHeight - (double)tagPadding - 1.0D, tagX + (double)stringWidth + (double)tagPadding + 1.0D, tagY + (double)tagPadding + 1.0D, outlineColor, 1.0D);

        switch(alignment) {
            case -1:
                draw.drawRightString(displayName, x, y, size);
                break;
            case 0:
                draw.drawCenteredString(displayName, x, y, size);
                break;
            case 1:
                draw.drawString(displayName, x, y, size);
        }

        if (hover && plot.getCommand() != null) {
            this.selectedPlot = plot;
        }

    }

    private double sign(double px1, double py1, double px2, double py2, double px3, double py3) {
        return (px1 - px3) * (py2 - py3) - (px2 - px3) * (py1 - py3);
    }

    private boolean isInside(double pointX, double pointY, double px1, double py1, double px2, double py2, double px3, double py3) {
        boolean b1 = this.sign(pointX, pointY, px1, py1, px2, py2) < 0.0D;
        boolean b2 = this.sign(pointX, pointY, px2, py2, px3, py3) < 0.0D;
        boolean b3 = this.sign(pointX, pointY, px3, py3, px1, py1) < 0.0D;
        return b1 == b2 && b2 == b3;
    }

    public void lockMouseMovementInCircle() {
        if(!this.open) return;
        EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if (player == null) return;

        double radius = (double)LabyMod.getInstance().getDrawUtils().getHeight() / 4.0D / 3.0D;
        float centerX = this.lockedYaw;
        float centerY = this.lockedPitch;
        if ((double)centerY + radius > 90.0D) {
            centerY = (float)(90.0D - radius);
        }

        if ((double)centerY - radius < -90.0D) {
            centerY = (float)(-90.0D + radius);
        }

        float newX = player.rotationYaw;
        float newY = player.rotationPitch;
        double distanceX = centerX - newX;
        double distanceY = centerY - newY;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        if (distance > radius) {
            double fromOriginToObjectX = newX - centerX;
            double fromOriginToObjectY = newY - centerY;
            double multiplier = radius / distance;
            fromOriginToObjectX *= multiplier;
            fromOriginToObjectY *= multiplier;
            centerX = (float)((double)centerX + fromOriginToObjectX);
            centerY = (float)((double)centerY + fromOriginToObjectY);
            player.rotationYaw = centerX;
            player.prevRotationYaw = centerX;
            player.rotationPitch = centerY;
            player.prevRotationPitch = centerY;
        }
    }

    private static GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
