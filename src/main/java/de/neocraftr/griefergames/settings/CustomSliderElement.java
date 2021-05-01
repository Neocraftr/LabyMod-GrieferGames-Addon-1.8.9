package de.neocraftr.griefergames.settings;

import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

// LabyMod SliderElement with custom value overrides

public class CustomSliderElement extends ControlElement {
    public static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    private Integer currentValue;
    private Consumer<Integer> changeListener;
    private Consumer<Integer> callback;
    private int minValue;
    private int maxValue;
    private boolean dragging;
    private boolean hover;
    private int dragValue;
    private int steps;

    public CustomSliderElement(String displayName, IconData iconData, int currentValue) {
        super(displayName, null, iconData);
        this.minValue = 0;
        this.maxValue = 10;
        this.steps = 1;
        this.currentValue = currentValue;
        this.changeListener = new Consumer<Integer>() {
            public void accept(Integer accepted) {
                if (CustomSliderElement.this.callback != null) {
                    CustomSliderElement.this.callback.accept(accepted);
                }

            }
        };
    }

    public CustomSliderElement setMinValue(int minValue) {
        this.minValue = minValue;
        if (this.currentValue < this.minValue) {
            this.currentValue = this.minValue;
        }

        return this;
    }

    public CustomSliderElement setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
        }

        return this;
    }

    public CustomSliderElement setRange(int min, int max) {
        this.setMinValue(min);
        this.setMaxValue(max);
        return this;
    }

    public CustomSliderElement setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    private String getDisplayValue(Integer number) {
        if(number <= 0) return LanguageManager.translateOrReturnKey("gg_off");

        return number + "s";
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        int width = this.getObjectWidth();
        if (this.displayName != null) {
            draw.drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(buttonTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        double maxSliderPos = (double)maxX - 2.0D;
        double sliderWidth = (double)(width - 8);
        double sliderWidthBackground = (double)width;
        double minSliderPos = maxSliderPos - (double)width;
        double totalValueDiff = (double)(this.maxValue - this.minValue);
        double currentValue = (double)this.currentValue;
        double pos = minSliderPos + sliderWidth / totalValueDiff * (currentValue - (double)this.minValue);
        draw.drawTexturedModalRect(minSliderPos, (double)(y + 1), 0.0D, 46.0D, sliderWidthBackground / 2.0D, 20.0D);
        draw.drawTexturedModalRect(minSliderPos + sliderWidthBackground / 2.0D, (double)(y + 1), 200.0D - sliderWidthBackground / 2.0D, 46.0D, sliderWidthBackground / 2.0D, 20.0D);
        this.hover = mouseX > x && mouseX < maxX - 2 && mouseY > y + 1 && mouseY < maxY;
        draw.drawTexturedModalRect(pos, (double)(y + 1), 0.0D, 66.0D, 4.0D, 20.0D);
        draw.drawTexturedModalRect(pos + 4.0D, (double)(y + 1), 196.0D, 66.0D, 4.0D, 20.0D);
        if (!this.isMouseOver()) {
            this.mouseRelease(mouseX, mouseY, 0);
        } else {
            double mouseToMinSlider = (double)mouseX - minSliderPos;
            double finalValue = (double)this.minValue + totalValueDiff / sliderWidth * (mouseToMinSlider - 1.0D);
            if (this.dragging) {
                this.dragValue = (int)finalValue;
                this.mouseClickMove(mouseX, mouseY, 0);
            }
        }

        draw.drawCenteredString(this.getDisplayValue(this.currentValue), minSliderPos + sliderWidthBackground / 2.0D, (double)(y + 7));
    }

    public void unfocus(int mouseX, int mouseY, int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hover) {
            this.dragging = true;
        }

    }

    public void mouseRelease(int mouseX, int mouseY, int mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        if (this.dragging) {
            this.dragging = false;
            this.currentValue = (int)((double)this.dragValue / (double)this.steps) * this.steps;
            if (this.currentValue > this.maxValue) {
                this.currentValue = this.maxValue;
            }

            if (this.currentValue < this.minValue) {
                this.currentValue = this.minValue;
            }

            this.changeListener.accept(this.currentValue);
        }

    }

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton) {
        super.mouseClickMove(mouseX, mouseY, mouseButton);
        if (this.dragging) {
            this.currentValue = (int)Math.round((double)this.dragValue / (double)this.steps * (double)this.steps);
            if (this.currentValue > this.maxValue) {
                this.currentValue = this.maxValue;
            }

            if (this.currentValue < this.minValue) {
                this.currentValue = this.minValue;
            }

            this.changeListener.accept(this.currentValue);
        }

    }

    public CustomSliderElement addCallback(Consumer<Integer> callback) {
        this.callback = callback;
        return this;
    }

    public int getObjectWidth() {
        return 50;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }
}
