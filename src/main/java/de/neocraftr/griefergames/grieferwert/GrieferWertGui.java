package de.neocraftr.griefergames.grieferwert;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class GrieferWertGui extends GuiScreen {
    private static final int ENTRY_HEIGHT = 30, ENTRY_WIDTH = 300;

    private Scrollbar scrollbar;
    private ModTextField searchTextField;
    private GuiScreen lastScreen;
    private int hoveredIndex = -1;
    private List<GrieferWertItem> items;

    public GrieferWertGui(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;

        this.items = getGG().getGrieferWertManager().getItems();

        this.scrollbar = new Scrollbar(0);
    }

    @Override
    public void initGui() {
        super.initGui();

        this.scrollbar.init();
        this.scrollbar.setPosition(this.width / 2 + 152, 44, this.width / 2 + 156, this.height - 10 - 3);
        this.scrollbar.setSpeed(30);

        Keyboard.enableRepeatEvents(true);
        this.searchTextField = new ModTextField(-1, LabyModCore.getMinecraft().getFontRenderer(), this.width/2 - ENTRY_WIDTH/2, 10, ENTRY_WIDTH, 18);
        this.searchTextField.setFocused(true);
        this.searchTextField.setPlaceHolder(LanguageManager.translateOrReturnKey("gui_gg_gw_search"));

        this.buttonList.add(new GuiButton(1, 10, 10, 58, 20, "< "+ LanguageManager.translateOrReturnKey("button_done")));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.hoveredIndex = -1;

        double entryHeight = 0;
        for (int i = 0; i < items.size(); i++) {
            drawEntry(i, items.get(i), (entryHeight + 45.0D + this.scrollbar.getScrollY()), mouseX, mouseY);
            entryHeight += 2.0D + ENTRY_HEIGHT;
        }
        if(items.isEmpty()) {
            LabyMod.getInstance().getDrawUtils().drawCenteredString("§7"+LanguageManager.translateOrReturnKey("gui_gg_gw_empty"), this.width / 2.0D, 60.0D);
        }

        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(0, 41);
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(this.height - 10, this.height);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowTop(41.0D, 0.0D, this.width);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowBottom(this.height - 10.0D, 0.0D, this.width);

        this.scrollbar.setEntryHeight(entryHeight / items.size());
        this.scrollbar.update(items.size());
        this.scrollbar.draw();

        this.searchTextField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawEntry(int index, GrieferWertItem item, double y, int mouseX, int mouseY) {
        int x = this.width / 2 - ENTRY_WIDTH / 2;

        boolean hovered = (mouseX > x && mouseX < x + ENTRY_WIDTH && mouseY > y && mouseY < y + ENTRY_HEIGHT && mouseY > 41 && mouseY < this.height - 10);
        if (hovered) this.hoveredIndex = index;

        int backgroundColor = hovered ? ModColor.toRGB(80, 80, 80, 120) : ModColor.toRGB(70, 70, 70, 120);
        drawRect(x, (int) y, x + ENTRY_WIDTH, (int) y + ENTRY_HEIGHT, backgroundColor);

        int textX = 10;
        if(getGG().getSettings().isShowGWImages()) {
            LabyMod.getInstance().getDrawUtils().drawImageUrl(item.getImgUrl(), x + 3.0D, y + 3.0D, 255, 255, 24, 24);
            textX = 24 + 10;
        }

        List<String> lines = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(this.formatDescription(item), ENTRY_WIDTH - textX - 10, 2);
        if(lines.size() <= 1) {
            LabyMod.getInstance().getDrawUtils().drawString(lines.get(0), x + textX, y + 11.0D);
        } else {
            LabyMod.getInstance().getDrawUtils().drawString(lines.get(0), x + textX, y + 5.0D);
            LabyMod.getInstance().getDrawUtils().drawString(lines.get(1), x + textX, y + 17.0D);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoveredIndex != -1) {
            try {
                Desktop.getDesktop().browse(new URI(this.items.get(this.hoveredIndex).getUrl()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        this.searchTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        } else if(!this.searchTextField.isFocused()) {
            this.searchTextField.setFocused(true);
        }

        if(this.searchTextField.textboxKeyTyped(typedChar, keyCode)) {
            if(this.searchTextField.getText().trim().isEmpty()) {
                this.items = getGG().getGrieferWertManager().getItems();
            } else {
                this.items = getGG().getGrieferWertManager().searchByName(this.searchTextField.getText());
            }
        }
    }

    @Override
    public void updateScreen() {
        this.searchTextField.updateCursorCounter();
    }

    private String formatDescription(GrieferWertItem item) {
        String text = "§7" + item.getName() + " §8| §a";
        if(item.getPrice() == null) {
            text += LanguageManager.translateOrReturnKey("gui_gg_gw_noPrice");
        } else {
            text += item.getPrice() + (item.getPriceRange() != null ? " §8| §7"+item.getPriceRange() : "");
        }
        return text;
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
