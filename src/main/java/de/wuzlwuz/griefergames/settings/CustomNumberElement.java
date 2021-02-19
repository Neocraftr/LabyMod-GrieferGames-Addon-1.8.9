package de.wuzlwuz.griefergames.settings;

// Improved version of the LabyMod NumberElement

import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.Module;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;

public class CustomNumberElement extends ControlElement {
    private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    private static final int CHECK_DELAY = 700;

    private Integer currentValue;
    private Consumer<Integer> changeListener;
    private GuiTextField textField;
    private Consumer<Integer> callback;
    private int minValue;
    private int maxValue;
    private boolean hoverUp;
    private boolean hoverDown;
    private int steps;
    private long fastTickerCounterValue;
    private long lastNumberCheck;

    public CustomNumberElement(String displayName, final String configEntryName, ControlElement.IconData iconData) {
        super(displayName, configEntryName, iconData);
        this.minValue = 0;
        this.maxValue = 2147483647;
        this.steps = 1;
        this.fastTickerCounterValue = 0L;
        this.lastNumberCheck = -1L;
        if (!configEntryName.isEmpty()) {
            try {
                this.currentValue = (Integer) ModSettings.class.getDeclaredField(configEntryName).get(LabyMod.getSettings());
            } catch (IllegalAccessException var5) {
                var5.printStackTrace();
            } catch (NoSuchFieldException var6) {
                var6.printStackTrace();
            }
        }

        if (this.currentValue == null) {
            this.currentValue = this.minValue;
        }

        this.changeListener = new Consumer<Integer>() {
            public void accept(Integer accepted) {
                try {
                    Field f = ModSettings.class.getDeclaredField(configEntryName);
                    if (f.getType().equals(Integer.TYPE)) {
                        f.set(LabyMod.getSettings(), accepted);
                    } else {
                        f.set(LabyMod.getSettings(), String.valueOf(accepted));
                    }
                } catch (Exception var3) {
                    var3.printStackTrace();
                }

                if (CustomNumberElement.this.callback != null) {
                    CustomNumberElement.this.callback.accept(accepted);
                }

            }
        };
        this.createTextfield();
    }

    public CustomNumberElement(String displayName, ControlElement.IconData iconData, int currentValue) {
        super(displayName, (String)null, iconData);
        this.minValue = 0;
        this.maxValue = 2147483647;
        this.steps = 1;
        this.fastTickerCounterValue = 0L;
        this.lastNumberCheck = -1L;
        this.currentValue = currentValue;
        this.changeListener = new Consumer<Integer>() {
            public void accept(Integer accepted) {
                if (CustomNumberElement.this.callback != null) {
                    CustomNumberElement.this.callback.accept(accepted);
                }

            }
        };
        this.createTextfield();
    }

    public CustomNumberElement(final Module module, ControlElement.IconData iconData, String displayName, final String attribute) {
        super(module, iconData, displayName);
        this.minValue = 0;
        this.maxValue = 2147483647;
        this.steps = 1;
        this.fastTickerCounterValue = 0L;
        this.lastNumberCheck = -1L;

        try {
            String attr = module.getAttributes().get(attribute);
            this.currentValue = attr == null ? this.minValue : Integer.parseInt(attr);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        if (this.currentValue == null) {
            this.currentValue = this.minValue;
        }

        this.changeListener = new Consumer<Integer>() {
            public void accept(Integer accepted) {
                module.getAttributes().put(attribute, String.valueOf(accepted));
                module.loadSettings();
                if (CustomNumberElement.this.callback != null) {
                    CustomNumberElement.this.callback.accept(accepted);
                }

            }
        };
        this.createTextfield();
    }

    public CustomNumberElement(String configEntryName, ControlElement.IconData iconData) {
        this(configEntryName, configEntryName, iconData);
    }

    public CustomNumberElement setMinValue(int minValue) {
        this.minValue = minValue;
        if (this.currentValue < this.minValue) {
            this.currentValue = this.minValue;
        }

        return this;
    }

    public CustomNumberElement setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
        }

        return this;
    }

    public CustomNumberElement setRange(int min, int max) {
        this.setMinValue(min);
        this.setMaxValue(max);
        return this;
    }

    public CustomNumberElement setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    public void createTextfield() {
        this.textField = new GuiTextField(-2, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, this.getObjectWidth(), 20);
        this.updateValue();
        this.textField.setFocused(false);
    }

    private void updateValue() {
        this.textField.setText(String.valueOf(this.currentValue));
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        int width = this.getObjectWidth();
        if (this.textField != null) {
            LabyModCore.getMinecraft().setTextFieldXPosition(this.textField, maxX - width - 2);
            LabyModCore.getMinecraft().setTextFieldYPosition(this.textField, y + 1);
            this.textField.drawTextBox();
            LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
            DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            Minecraft.getMinecraft().getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            this.hoverUp = mouseX > maxX - 15 && mouseX < maxX - 15 + 11 && mouseY > y + 2 && mouseY < y + 2 + 7;
            this.hoverDown = mouseX > maxX - 15 && mouseX < maxX - 15 + 11 && mouseY > y + 12 && mouseY < y + 12 + 7;
            draw.drawTexture((double)(maxX - 15), (double)(y + 2), 99.0D, this.hoverUp ? 37.0D : 5.0D, 11.0D, 7.0D, 11.0D, 7.0D);
            draw.drawTexture((double)(maxX - 15), (double)(y + 12), 67.0D, this.hoverDown ? 52.0D : 20.0D, 11.0D, 7.0D, 11.0D, 7.0D);
            if (this.isMouseOver() && this.fastTickerCounterValue != 0L) {
                if (this.fastTickerCounterValue > 0L && this.fastTickerCounterValue + 80L < System.currentTimeMillis()) {
                    this.fastTickerCounterValue = System.currentTimeMillis();
                    if (this.currentValue < this.maxValue) {
                        this.currentValue = this.currentValue + this.steps;
                        this.updateValue();
                    }
                }

                if (this.fastTickerCounterValue < 0L && this.fastTickerCounterValue - 80L > System.currentTimeMillis() * -1L) {
                    this.fastTickerCounterValue = System.currentTimeMillis() * -1L;
                    if (this.currentValue > this.minValue) {
                        this.currentValue = this.currentValue - this.steps;
                        this.updateValue();
                    }
                }
            } else {
                this.mouseRelease(mouseX, mouseY, 0);
            }

        }

        if(this.lastNumberCheck != -1 && this.lastNumberCheck < System.currentTimeMillis()) {
            this.lastNumberCheck = -1;
            String currentText = this.textField.getText();

            int newNumber = getNumberOrMin(currentText);

            if (newNumber > this.maxValue) {
                newNumber = this.maxValue;
            }
            if (newNumber < this.minValue) {
                newNumber = this.minValue;
            }

            this.textField.setText(String.valueOf(newNumber));
            this.changeListener.accept(newNumber);
            this.currentValue = newNumber;
        }
    }

    public void unfocus(int mouseX, int mouseY, int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        this.textField.setFocused(false);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoverUp && this.currentValue < this.maxValue) {
            this.currentValue = this.currentValue + this.steps;
            this.updateValue();
            this.fastTickerCounterValue = System.currentTimeMillis() + 500L;
        }

        if (this.hoverDown && this.currentValue > this.minValue) {
            this.currentValue = this.currentValue - this.steps;
            this.updateValue();
            this.fastTickerCounterValue = System.currentTimeMillis() * -1L - 500L;
        }

        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
            this.updateValue();
        }

        if (this.currentValue < this.minValue) {
            this.currentValue = this.minValue;
            this.updateValue();
        }

        this.textField.mouseClicked(mouseX, mouseY, 0);
    }

    public void mouseRelease(int mouseX, int mouseY, int mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        if (this.fastTickerCounterValue != 0L) {
            this.fastTickerCounterValue = 0L;
            this.changeListener.accept(this.currentValue);
        }

    }

    public void keyTyped(char typedChar, int keyCode) {
        String preValue = this.textField.getText();
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            String currentText = this.textField.getText();

            if(!currentText.equals(preValue)) {
                if (isNumber(currentText)) {
                    this.lastNumberCheck = System.currentTimeMillis() + CHECK_DELAY;
                } else {
                    this.textField.setText(preValue);
                }
            }

        }

    }

    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
    }

    public GuiTextField getTextField() {
        return this.textField;
    }

    public CustomNumberElement addCallback(Consumer<Integer> callback) {
        this.callback = callback;
        return this;
    }

    public int getObjectWidth() {
        return 50;
    }

    public Integer getCurrentValue() {
        return this.currentValue;
    }

    private boolean isNumber(String text) {
        if(text.isEmpty() || text.equals("-")) return true;

        try {
            Integer.parseInt(text);
        } catch(NumberFormatException ignored) {
            return false;
        }

        return true;
    }

    private int getNumberOrMin(String text) {
        try {
            return Integer.parseInt(text);
        } catch(NumberFormatException ignored) {
            return this.minValue;
        }
    }
}
