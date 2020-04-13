package de.wuzlwuz.griefergames.gui;

import net.labymod.utils.ModColor;
import net.minecraft.client.gui.GuiScreen;

public class vanishHelperGui extends GuiScreen {
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		this.drawCenteredString(this.fontRendererObj, ModColor.RED + "Achtung, du bist im Vanish!", this.width / 2,
				this.height / 2, 16777215);

		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
					mc.displayGuiScreen(null);
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		};

		thread.start();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
