package de.neocraftr.griefergames.plots.gui;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.CityBuild;
import de.neocraftr.griefergames.plots.Plot;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class PlotsGui extends GuiScreen {
    private static final int ENTRY_HEIGHT = 40, ENTRY_WIDTH = 220;

    private Scrollbar scrollbar;
    private GuiScreen lastScreen;
    private int hoveredIndex = -1;
    private GuiButton buttonEdit;
    private GuiButton buttonRemove;
    public DropDownMenu<CityBuild> citybuildDropDownMenu;
    private DropDownElement<CityBuild> citybuildDropDown;
    public List<Plot> plots;
    public int selectedIndex = -1;

    public PlotsGui(GuiScreen lastScreen, CityBuild initialCityBuild) {
        this.lastScreen = lastScreen;

        this.scrollbar = new Scrollbar(0);

        this.citybuildDropDownMenu = new DropDownMenu<CityBuild>(LanguageManager.translateOrReturnKey("gui_gg_plots_citybuild"), 0, 0, 0, 0).fill(CityBuild.values());
        this.citybuildDropDown = new DropDownElement<CityBuild>(LanguageManager.translateOrReturnKey("gui_gg_plots_citybuild"), this.citybuildDropDownMenu);
        this.citybuildDropDownMenu.setSelected(initialCityBuild);
        this.citybuildDropDown.setChangeListener(new Consumer<CityBuild>() {
            @Override
            public void accept(CityBuild cityBuild) {
                PlotsGui.this.plots = getGG().getPlotManager().getPlots(cityBuild);
                PlotsGui.this.selectedIndex = -1;
                PlotsGui.this.buttonEdit.enabled = false;
                PlotsGui.this.buttonRemove.enabled = false;
            }
        });
    }

    @Override
    public void initGui() {
        super.initGui();

        this.plots = getGG().getPlotManager().getPlots(this.citybuildDropDownMenu.getSelected());

        this.scrollbar.init();
        this.scrollbar.setPosition(this.width / 2 + 142, 44, this.width / 2 + 146, this.height - 32 - 3);
        this.scrollbar.setSpeed(10);

        this.buttonList.add(this.buttonRemove = new GuiButton(1, this.width / 2 - 120, this.height - 26, 75, 20, LanguageManager.translateOrReturnKey("button_remove")));
        this.buttonList.add(this.buttonEdit = new GuiButton(2, this.width / 2 - 37, this.height - 26, 75, 20, LanguageManager.translateOrReturnKey("button_edit")));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 120 - 75, this.height - 26, 75, 20, LanguageManager.translateOrReturnKey("button_add")));
        this.buttonList.add(new GuiButton(4, 10, 10, 58, 20, "< "+ LanguageManager.translateOrReturnKey("button_done")));

        this.citybuildDropDown.init();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 1:
                final GuiScreen lastScreen = (Minecraft.getMinecraft()).currentScreen;;
                Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                    @Override
                    public void confirmClicked(boolean result, int id) {
                        if (result) {
                            getGG().getPlotManager().getPlots().removeIf(plot -> plot == plots.get(selectedIndex));
                            getGG().getPlotManager().savePlots();
                        }
                        Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                        selectedIndex = -1;
                    }
                }, LanguageManager.translateOrReturnKey("gui_gg_plots_delete"), "§c"+plots.get(this.selectedIndex).getName(), 1));
                break;
            case 2:
                Minecraft.getMinecraft().displayGuiScreen(new PlotsGuiAdd(this, this.selectedIndex));
                break;
            case 3:
                Minecraft.getMinecraft().displayGuiScreen(new PlotsGuiAdd(this, -1));
                break;
            case 4:
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.hoveredIndex = -1;

        double entryHeight = 0;
        for (int i = 0; i < plots.size(); i++) {
            drawEntry(i, plots.get(i), (entryHeight + 45.0D + this.scrollbar.getScrollY()), mouseX, mouseY);
            entryHeight += 1.0D + ENTRY_HEIGHT;
        }
        if(plots.isEmpty()) {
            LabyMod.getInstance().getDrawUtils().drawCenteredString("§7"+LanguageManager.translateOrReturnKey("gui_gg_plots_empty"), this.width / 2.0D, 60.0D);
        }

        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(0, 41);
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(this.height - 32, this.height);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowTop(41.0D, 0.0D, this.width);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowBottom(this.height - 32.0D, 0.0D, this.width);
        LabyMod.getInstance().getDrawUtils().drawCenteredString("§b§l"+LanguageManager.translateOrReturnKey("gui_gg_plots_title"), this.width / 2.0D, 25.0D);

        this.scrollbar.setEntryHeight(entryHeight / plots.size());
        this.scrollbar.update(plots.size());
        this.scrollbar.draw();

        this.buttonEdit.enabled = (this.selectedIndex != -1);
        this.buttonRemove.enabled = (this.selectedIndex != -1);

        this.citybuildDropDown.draw(this.width - 10 - 100, 3, this.width - 10, 3 + 35, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawEntry(int index, Plot plot, double y, int mouseX, int mouseY) {
        int x = this.width / 2 - ENTRY_WIDTH / 2;

        boolean hovered = (mouseX > x && mouseX < x + ENTRY_WIDTH && mouseY > y && mouseY < y + ENTRY_HEIGHT && mouseX > 32 && mouseY < this.height - 32);
        if (hovered) this.hoveredIndex = index;

        int borderColor = (this.selectedIndex == index) ? ModColor.toRGB(240, 240, 240, 240) : Integer.MIN_VALUE;
        int backgroundColor = hovered ? ModColor.toRGB(50, 50, 50, 120) : ModColor.toRGB(40, 40, 40, 120);

        drawRect(x, (int) y, x + ENTRY_WIDTH, (int) y + ENTRY_HEIGHT, backgroundColor);
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x, y, x + ENTRY_WIDTH, (int) (y + ENTRY_HEIGHT), borderColor, 1.0D);

        LabyMod.getInstance().getDrawUtils().drawString("§e"+plot.getName(), x + 10.0D, y + 10.0D);
        LabyMod.getInstance().getDrawUtils().drawString(plot.getCommand(), x + 10.0D, y + 22.0D);

        drawRect(x + ENTRY_WIDTH - 1, (int) y + ENTRY_HEIGHT - 1, x + ENTRY_WIDTH - 50, (int) y + ENTRY_HEIGHT - 16, ModColor.toRGB(85, 255, 85, 60));
        drawCenteredString(plot.getCityBuild().name(), x + ENTRY_WIDTH - 50.0D / 2.0D, y + ENTRY_HEIGHT - 12.0D, ModColor.toRGB(230, 230, 230, 100));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoveredIndex != -1)
            this.selectedIndex = this.hoveredIndex;
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        this.citybuildDropDown.onClickDropDown(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        this.citybuildDropDown.mouseClickMove(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        this.citybuildDropDown.mouseRelease(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
        this.citybuildDropDown.onScrollDropDown();
    }

    private void drawCenteredString(String text, double x, double y, int color) {
        int width = LabyMod.getInstance().getDrawUtils().getStringWidth(text);
        LabyMod.getInstance().getDrawUtils().getFontRenderer().drawString(text, (int) x - width / 2, (int) y, color);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
