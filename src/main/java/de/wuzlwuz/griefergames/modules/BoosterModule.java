package de.wuzlwuz.griefergames.modules;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.booster.Booster;
import de.wuzlwuz.griefergames.booster.BreakBooster;
import de.wuzlwuz.griefergames.booster.DropBooster;
import de.wuzlwuz.griefergames.booster.ExperienceBooster;
import de.wuzlwuz.griefergames.booster.FlyBooster;
import de.wuzlwuz.griefergames.booster.MobBooster;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ColorPicker;
import net.labymod.ingamegui.Module;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.main.DefaultValues;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ColorPickerCheckBoxBulkElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BoosterModule extends Module {
	private List<Booster> dummyBooster = new ArrayList<Booster>();

	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	public BoosterModule() {
		dummyBooster.add(new BreakBooster(1));
		dummyBooster.add(new DropBooster(4));
		dummyBooster.add(new ExperienceBooster(6));
		dummyBooster.add(new FlyBooster(1));
		dummyBooster.add(new MobBooster(3));
		getGG().getApi().registerModule(this);
	}

	public void init() {
		super.init();
	}

	public void loadSettings() {
	}

	public void draw(int x, int y, int rightX) {
		Collection<Booster> boosters = getActiveBoosters();
		if (boosters.size() == 0) {
			return;
		}
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();

		int slotHeight = getSlotHeight(boosters);
		for (Booster booster : boosters) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			String name = booster.getName();
			Integer count = booster.getCount();
			boolean showCount = booster.getShowCount();

			if (count > 0) {
				int durationColor = getColor("durationColor", DefaultValues.POTION_DURATION_COLOR);
				if(booster.doHighlightDuration()) {
					if(durationColor == new Color(255, 85, 85).getRGB()) {
						durationColor = new Color(255, 255, 255).getRGB();
					} else {
						durationColor = new Color(255, 85, 85).getRGB();
					}
				}

				if (rightX == -1) {
					int stringWidth = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(name + " ");
					LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(name, x + 28, y - 1,
							getColor("nameColor", DefaultValues.POTION_NAME_COLOR));

					if (showCount) {
						LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow("x" + count.toString(),
								x + (28 + stringWidth), y - 1, getColor("ampColor", new Color(85, 255, 255).getRGB()));
					}

					LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(booster.getDurationString(),
							x + 28, y + 9, durationColor);
				} else {
					int stringWidth = 0;
					String countStr = "x" + count.toString();
					if (showCount) {
						stringWidth = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(" " + countStr);
					}
					LabyMod.getInstance().getDrawUtils().drawRightStringWithShadow(name, rightX - (28 + stringWidth),
							y - 1, getColor("nameColor", DefaultValues.POTION_NAME_COLOR));

					if (showCount) {
						stringWidth = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(countStr);
						LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow("x" + count.toString(),
								rightX - (28 + stringWidth), y - 1,
								getColor("ampColor", new Color(85, 255, 255).getRGB()));
					}

					LabyMod.getInstance().getDrawUtils().drawRightStringWithShadow(booster.getDurationString(),
							rightX - 28, y + 9, durationColor);
				}

				Minecraft.getMinecraft().getTextureManager()
						.bindTexture(new ResourceLocation("griefergames/textures/icons/module_booster_sprite.png"));

				GlStateManager.color(1.0F, 1.0F, 1.0F);
				if (rightX == -1) {
					LabyMod.getInstance().getDrawUtils().drawTexturedModalRect(x, y, booster.getIconIndex() * 18, 0, 18,
							18);
				} else {
					LabyMod.getInstance().getDrawUtils().drawTexturedModalRect(rightX - 18, y,
							booster.getIconIndex() * 18, 0, 18, 18);
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

	private Collection<Booster> getActiveBoosters() {
		if (getGG().isShowBoosterDummy()) {
			return this.dummyBooster;
		}
		return getGG().getBoosters();
	}

	private int getColor(String key, int defaultColor) {
		Color color = ModColor.getColorByString((String) getAttributes().get(key));
		if (color == null) {
			return defaultColor;
		}
		return color.getRGB();
	}

	private int getSlotHeight(Collection<Booster> boosters) {
		return 23;
	}

	public void fillSubSettings(List<SettingsElement> settingsElements) {
		super.fillSubSettings(settingsElements);

		ColorPickerCheckBoxBulkElement colorPickerBulkElement = new ColorPickerCheckBoxBulkElement("Colors");

		ColorPicker nameColorPicker = new ColorPicker("Name",
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

		ColorPicker amplifierColorPicker = new ColorPicker("Amplifier",
				ModColor.getColorByString((String) getAttributes().get("ampColor")),
				new ColorPicker.DefaultColorCallback() {
					public Color getDefaultColor() {
						return new Color(new Color(85, 255, 255).getRGB());
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

		ColorPicker durationColorPicker = new ColorPicker("Duration",
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
		return new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/module_booster.png"));
	}

	public double getHeight() {
		Collection<Booster> boosters = getActiveBoosters();
		int size = 0;
		for (Booster booster : boosters) {
			if (booster.getCount() > 0) {
				size++;
			}
		}
		return (short) (int) scaleModuleSize(getSlotHeight(boosters) * size, false);
	}

	public double getWidth() {
		Collection<Booster> boosters = getActiveBoosters();

		int maxWidth = 0;
		for (Booster booster : boosters) {
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
		return (short) (int) scaleModuleSize(maxWidth, false);
	}

	public String getSettingName() {
		return "gg_booster";
	}

	public String getDescription() {
		return "";
	}

	public int getSortingId() {
		return 10;
	}

	public ModuleCategory getCategory() {
		return getGG().getModuleCategory();
	}
}