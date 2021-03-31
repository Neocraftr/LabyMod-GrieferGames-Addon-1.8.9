package de.neocraftr.griefergames.settings;

import net.labymod.gui.elements.ColorPicker;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ColorPickerCheckBoxBulkElement;
import net.labymod.utils.Consumer;

import java.awt.*;

public class ColorElement extends ColorPickerCheckBoxBulkElement {

    // Not finished

    private ColorPicker colorPicker;
    private Consumer<Color> callback;

    public ColorElement(String displayName, IconData iconData, Color color, Consumer<Color> callback) {
        super(displayName);
        super.iconData = iconData;

        colorPicker = new ColorPicker("", color, null, 0, 0, 20, 20);
        colorPicker.setHasAdvanced(true);

        this.callback = callback;
    }

    @Override
    public boolean onClickBulkEntry(int mouseX, int mouseY, int mouseButton) {
        colorPicker.mouseClicked(mouseX, mouseY, mouseButton);

        if(colorPicker.isHoverAdvancedButton() || colorPicker.isHoverSlider()) {
            if(callback != null) callback.accept(colorPicker.getSelectedColor());
        }

        return true;
    }

    @Override
    public int getEntryHeight() {
        return 23;
    }

    @Override
    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {


        colorPicker.setX(maxX - colorPicker.getWidth() - 2);
        colorPicker.setY(y + 1);

        colorPicker.drawColorPicker(mouseX, mouseY);

        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, colorPicker.getSelectedColor().getRGB());
    }

    public void setClickCallback(Consumer<Color> callback) {
        this.callback = callback;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }
}