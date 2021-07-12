package de.neocraftr.griefergames.modules;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.booster.Booster;
import de.neocraftr.griefergames.booster.BreakBooster;
import de.neocraftr.griefergames.booster.DropBooster;
import de.neocraftr.griefergames.booster.ExperienceBooster;
import de.neocraftr.griefergames.booster.FlyBooster;
import de.neocraftr.griefergames.booster.MobBooster;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ColorPicker;
import net.labymod.ingamegui.Module;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.main.DefaultValues;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ColorPickerCheckBoxBulkElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BoosterModule extends Module {

	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	private List<Booster> boosters = new ArrayList<>();

	private Color LIGHT_RED = new Color(255, 85, 85);
	private Color LIGHT_BLUE = new Color(85, 255, 255);

	public BoosterModule() {
		getGG().getApi().registerModule(this);
		boosters.add(new FlyBooster());
		boosters.add(new BreakBooster());
		boosters.add(new DropBooster());
		boosters.add(new ExperienceBooster());
		boosters.add(new MobBooster());
	}

	public void init() {
		super.init();
	}

	public void loadSettings() {
	}

	public void draw(int x, int y, int rightX) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();

		int slotHeight = getSlotHeight();
		for (Booster booster : this.boosters) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			String name = booster.getName();
			int count = booster.getCount();

			if (count > 0) {
				int durationColor = getColor("durationColor", DefaultValues.POTION_DURATION_COLOR);
				if(booster.doHighlightDuration()) {
					if(durationColor == LIGHT_RED.getRGB()) {
						durationColor = Color.WHITE.getRGB();
					} else {
						durationColor = LIGHT_RED.getRGB();
					}
				}

				if (rightX == -1) {
					int stringWidth = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(name + " ");
					LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(name, x + 25, y + 2,
							getColor("nameColor", DefaultValues.POTION_NAME_COLOR));

					if (booster.isStackable()) {
						LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow("x" + count,
								x + (25 + stringWidth), y + 2, getColor("ampColor", LIGHT_BLUE.getRGB()));
					}

					LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(booster.getDurationString(),
							x + 25, y + 12, durationColor);
				} else {
					int stringWidth = 0;
					String countStr = "x" + count;
					if (booster.isStackable()) {
						stringWidth = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(" " + countStr);
					}
					LabyMod.getInstance().getDrawUtils().drawRightStringWithShadow(name, rightX - (25 + stringWidth),
							y + 2, getColor("nameColor", DefaultValues.POTION_NAME_COLOR));

					if (booster.isStackable()) {
						stringWidth = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(countStr);
						LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow("x" + count,
								rightX - (25 + stringWidth), y + 2,
								getColor("ampColor", LIGHT_BLUE.getRGB()));
					}

					LabyMod.getInstance().getDrawUtils().drawRightStringWithShadow(booster.getDurationString(),
							rightX - 25, y + 12, durationColor);
				}

				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("griefergames/textures/icons/module_booster_sprite.png"));

				GlStateManager.color(1.0F, 1.0F, 1.0F);
				if (rightX == -1) {
					LabyMod.getInstance().getDrawUtils().drawTexturedModalRect(x, y + 2, booster.getIconIndex() * 18, 0, 18, 18);
				} else {
					LabyMod.getInstance().getDrawUtils().drawTexturedModalRect(rightX - 18, y + 2, booster.getIconIndex() * 18, 0, 18, 18);
				}
				y += slotHeight;
			}
		}
		GlStateManager.popMatrix();
	}

	public boolean isShown() {
		return getGG().getSettings().isModEnabled() && getGG().isOnGrieferGames();
	}

	protected boolean supportsRescale() {
		return true;
	}

	private int getColor(String key, int defaultColor) {
		Color color = ModColor.getColorByString(getAttributes().get(key));
		if (color == null) {
			return defaultColor;
		}
		return color.getRGB();
	}

	private int getSlotHeight() {
		return 23;
	}

	public void fillSubSettings(List<SettingsElement> settingsElements) {
		super.fillSubSettings(settingsElements);

		ColorPickerCheckBoxBulkElement colorPickerBulkElement = new ColorPickerCheckBoxBulkElement("");

		ColorPicker nameColorPicker = new ColorPicker(LanguageManager.translateOrReturnKey("module_gg_booster_settings_name"),
				ModColor.getColorByString((String) getAttributes().get("nameColor")),
				new ColorPicker.DefaultColorCallback() {
					public Color getDefaultColor() {
						return new Color(DefaultValues.POTION_NAME_COLOR);
					}
				}, 0, 0, 0, 0);

		nameColorPicker.setHasAdvanced(true);
		nameColorPicker.setHasDefault(false);
		nameColorPicker.setUpdateListener(new Consumer<Color>() {
			@Override
			public void accept(Color color) {
				BoosterModule.this.getAttributes().put("nameColor",
						String.valueOf(color == null ? -1 : color.getRGB()));
			}
		});
		colorPickerBulkElement.addColorPicker(nameColorPicker);

		ColorPicker amplifierColorPicker = new ColorPicker(LanguageManager.translateOrReturnKey("module_gg_booster_settings_amplifier"),
				ModColor.getColorByString((String) getAttributes().get("ampColor")),
				new ColorPicker.DefaultColorCallback() {
					public Color getDefaultColor() {
						return LIGHT_BLUE;
					}
				}, 0, 0, 0, 0);

		amplifierColorPicker.setHasAdvanced(true);
		amplifierColorPicker.setHasDefault(false);
		amplifierColorPicker.setUpdateListener(new Consumer<Color>() {
			@Override
			public void accept(Color color) {
				BoosterModule.this.getAttributes().put("ampColor", String.valueOf(color == null ? -1 : color.getRGB()));
			}
		});
		colorPickerBulkElement.addColorPicker(amplifierColorPicker);

		ColorPicker durationColorPicker = new ColorPicker(LanguageManager.translateOrReturnKey("module_gg_booster_settings_duration"),
				ModColor.getColorByString((String) getAttributes().get("durationColor")),
				new ColorPicker.DefaultColorCallback() {
					public Color getDefaultColor() {
						return new Color(DefaultValues.POTION_DURATION_COLOR);
					}
				}, 0, 0, 0, 0);

		durationColorPicker.setHasAdvanced(true);
		durationColorPicker.setHasDefault(false);
		durationColorPicker.setUpdateListener(new Consumer<Color>() {
			@Override
			public void accept(Color color) {
				BoosterModule.this.getAttributes().put("durationColor",
						String.valueOf(color == null ? -1 : color.getRGB()));
			}
		});
		colorPickerBulkElement.addColorPicker(durationColorPicker);

		settingsElements.add(colorPickerBulkElement);
	}

	public ControlElement.IconData getIconData() {
		return new ControlElement.IconData("griefergames/textures/icons/module_booster.png");
	}

	public double getHeight() {
		int size = 0;
		for (Booster booster : this.boosters) {
			if (booster.getCount() > 0) {
				size++;
			}
		}
		return scaleModuleSize(getSlotHeight() * size, false);
	}

	public double getWidth() {
		int maxWidth = 0;
		for (Booster booster : this.boosters) {
			if (booster.getCount() > 0) {
				String amplifierString = booster.getName();
				int width = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(booster.getDurationString());
				int width2 = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(amplifierString);

				int completeWidth = Math.max(width, width2) + 28;
				if (completeWidth > maxWidth) {
					maxWidth = completeWidth;
				}
			}
		}
		return scaleModuleSize(maxWidth, false);
	}

	public String getSettingName() {
		return "gg_booster";
	}

	public String getDescription() {
		return LanguageManager.translateOrReturnKey("module_gg_booster_description");
	}

	public int getSortingId() {
		return 10;
	}

	public ModuleCategory getCategory() {
		return getGG().getModuleCategory();
	}

	public void addBooster(String type, long duration) {
		for(Booster booster : this.boosters) {
			if(booster.getType().equalsIgnoreCase(type)) {
				booster.addBooster(duration);
			}
		}
	}

	public void setBooster(String type, int count, List<Long> durations) {
		for(Booster booster : this.boosters) {
			if(booster.getType().equalsIgnoreCase(type)) {
				booster.setBooster(count, durations);
			}
		}
	}

	public void removeBooster(String type) {
		for(Booster booster : this.boosters) {
			if(booster.getType().equalsIgnoreCase(type)) {
				booster.removeBooster();
			}
		}
	}

	public void resetBooster(String type) {
		for(Booster booster : this.boosters) {
			if(booster.getType().equalsIgnoreCase(type)) {
				booster.setCount(0);
				booster.getEndTimes().clear();
			}
		}
	}

	public void resetBoosters() {
		for(Booster booster : this.boosters) {
			booster.setCount(0);
			booster.getEndTimes().clear();
		}
	}
}