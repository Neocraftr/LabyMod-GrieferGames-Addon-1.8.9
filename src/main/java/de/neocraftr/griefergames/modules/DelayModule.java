package de.neocraftr.griefergames.modules;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

import java.util.concurrent.TimeUnit;

public class DelayModule extends SimpleModule {
    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }

    public DelayModule() {
        getGG().getApi().registerModule(this);
    }

    @Override
    public String getDisplayName() {
        return LanguageManager.translateOrReturnKey("module_gg_delay_displayName");
    }

    @Override
    public String getDisplayValue() {
        long remainingTime = getGG().getWaitTime() - System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(remainingTime) + "s";
    }

    @Override
    public String getDefaultValue() {
        return "?";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/module_delay.png"));
    }

    @Override
    public void loadSettings() {}

    @Override
    public String getSettingName() {
        return "gg_delay";
    }

    @Override
    public String getDescription() {
        return LanguageManager.translateOrReturnKey("module_gg_delay_description");
    }

    @Override
    public boolean isShown() {
        return getGG().getSettings().isModEnabled() && getGG().isOnGrieferGames() && getGG().getWaitTime() > System.currentTimeMillis() + 1000;
    }

    @Override
    public ModuleCategory getCategory() {
        return getGG().getModuleCategory();
    }

    @Override
    public int getSortingId() {
        return 60;
    }
}
