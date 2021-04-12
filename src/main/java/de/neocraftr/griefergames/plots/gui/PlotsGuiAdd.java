package de.neocraftr.griefergames.plots.gui;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.CityBuild;
import de.neocraftr.griefergames.plots.Plot;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.DropDownElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class PlotsGuiAdd extends GuiScreen {
    private static final int TEXTFIELD_WIDTH = 200;

    private PlotsGui lastScreen;
    private int editIndex;
    private ModTextField nameField;
    private ModTextField commandField;
    private DropDownMenu<CityBuild> citybuildDropDownMenu;
    private DropDownElement<CityBuild> citybuildDropDown;
    private GuiButton buttonDone;

    public PlotsGuiAdd(PlotsGui lastScreen, int editIndex) {
        this.lastScreen = lastScreen;
        this.editIndex = editIndex;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        this.nameField = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().fontRenderer, this.width/2 - TEXTFIELD_WIDTH/2, this.height/2 - 70, TEXTFIELD_WIDTH, 20);
        this.commandField = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().fontRenderer, this.width/2 - TEXTFIELD_WIDTH/2, this.height/2 - 25, TEXTFIELD_WIDTH, 20);

        this.citybuildDropDownMenu = new DropDownMenu<CityBuild>(LanguageManager.translateOrReturnKey("gui_gg_plots_citybuild"), 0, 0, 0, 0).fill(CityBuild.values());
        this.citybuildDropDown = new DropDownElement<CityBuild>(LanguageManager.translateOrReturnKey("gui_gg_plots_citybuild"), this.citybuildDropDownMenu);
        this.citybuildDropDown.init();
        this.citybuildDropDown.setChangeListener(citybuild -> {
            this.buttonDone.enabled =
                    !this.nameField.getText().trim().isEmpty() &&
                    !this.commandField.getText().trim().isEmpty() &&
                    !this.commandField.getText().trim().equals("/");
        });

        if(this.editIndex != -1) {
            Plot plot = this.lastScreen.plots.get(editIndex);
            this.nameField.setText(plot.getName());
            this.commandField.setText(plot.getCommand());
            this.citybuildDropDownMenu.setSelected(plot.getCityBuild());
        } else {
            this.commandField.setText("/");
            this.citybuildDropDownMenu.setSelected(this.lastScreen.citybuildDropDownMenu.getSelected());
        }

        this.nameField.setFocused(true);
        this.nameField.setCursorPositionEnd();

        this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 + 3, this.height / 2 + 65, 98, 20, LanguageManager.translateOrReturnKey("button_save")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 101, this.height / 2 + 65, 98, 20, LanguageManager.translateOrReturnKey("button_cancel")));
        this.buttonDone.enabled = false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        if(this.commandField.textboxKeyTyped(typedChar, keyCode)) {
            if(!this.commandField.getText().startsWith("/")) {
                this.commandField.setText("/"+this.commandField.getText());
                this.commandField.setCursorPositionEnd();
            }
        }

        if(keyCode == Keyboard.KEY_ESCAPE) {
            this.lastScreen.selectedIndex = -1;
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }

        if(keyCode == Keyboard.KEY_RETURN) {
            if(this.buttonDone.enabled) {
                saveSettings();
                this.lastScreen.selectedIndex = -1;
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            }
        }

        if(keyCode == Keyboard.KEY_TAB) {
            if(this.nameField.isFocused()) {
                this.nameField.setFocused(false);
                this.commandField.setFocused(true);
                this.commandField.setCursorPositionEnd();
            } else {
                this.commandField.setFocused(false);
                this.nameField.setFocused(true);
                this.nameField.setCursorPositionEnd();
            }
        }

        this.buttonDone.enabled =
                !this.nameField.getText().trim().isEmpty() &&
                !this.commandField.getText().trim().isEmpty() &&
                !this.commandField.getText().trim().equals("/");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.commandField.mouseClicked(mouseX, mouseY, mouseButton);
        this.citybuildDropDown.onClickDropDown(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick);
        this.citybuildDropDown.mouseClickMove(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.citybuildDropDown.mouseRelease(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.citybuildDropDown.onScrollDropDown();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(this.citybuildDropDownMenu.isOpen()) return;

        switch (button.id) {
            case 0:
                saveSettings();
                this.lastScreen.selectedIndex = -1;
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            case 1:
                this.lastScreen.selectedIndex = -1;
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.nameField.updateCursorCounter();
        this.commandField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        LabyMod.getInstance().getDrawUtils().drawString(LanguageManager.translateOrReturnKey("gui_gg_plots_name")+":", this.width / 2.0D - TEXTFIELD_WIDTH / 2.0D, this.height / 2.0D - 85.0D);
        this.nameField.drawTextBox();

        LabyMod.getInstance().getDrawUtils().drawString(LanguageManager.translateOrReturnKey("gui_gg_plots_command")+":", this.width / 2.0D - TEXTFIELD_WIDTH / 2.0D, this.height / 2.0D - 40.0D);
        this.commandField.drawTextBox();

        this.citybuildDropDown.draw(this.width / 2 - TEXTFIELD_WIDTH / 2, this.height / 2 + 5,
                this.width / 2 + TEXTFIELD_WIDTH / 2 + 2, this.height / 2 + 5 + 35, mouseX, mouseY);
    }

    private void saveSettings() {
        if (this.editIndex != -1) {
            Plot plot = this.lastScreen.plots.get(editIndex);
            plot.setName(this.nameField.getText());
            plot.setCommand(this.commandField.getText());
            plot.setCityBuild(this.citybuildDropDownMenu.getSelected());
        } else {
            Plot plot = new Plot(this.nameField.getText(), this.commandField.getText(), this.citybuildDropDownMenu.getSelected());
            getGG().getPlotManager().getPlots().add(plot);
        }
        getGG().getPlotManager().savePlots();
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
