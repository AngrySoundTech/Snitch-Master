package com.gmail.nuclearcat1337.snitch_master.gui.screens;

import com.gmail.nuclearcat1337.snitch_master.Settings;
import com.gmail.nuclearcat1337.snitch_master.gui.GuiConstants;
import com.gmail.nuclearcat1337.snitch_master.gui.controls.TextBox;
import com.gmail.nuclearcat1337.snitch_master.handlers.QuietTimeHandler;
import com.gmail.nuclearcat1337.snitch_master.util.QuietTimeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;

public class QuietTimeGui extends Screen {
	private static final int BUTTON_WIDTH = GuiConstants.MEDIUM_BUTTON_WIDTH;

	private final Screen cancelToScreen;
	private final Settings settings;

	private TextBox messageBox;

	public QuietTimeGui(Screen cancelToScreen, Settings settings) {
		this.cancelToScreen = cancelToScreen;
		this.settings = settings;
	}

	@Override
	public void init() {
		final int startingYPos = (this.height / 2) - (GuiConstants.STANDARD_TEXTBOX_HEIGHT / 2) - (GuiConstants.STANDARD_SEPARATION_DISTANCE / 2);
		final int startingXPos = (this.width / 2) - GuiConstants.STANDARD_SEPARATION_DISTANCE / 2 - BUTTON_WIDTH;

		int line2YPos = startingYPos + (GuiConstants.STANDARD_BUTTON_HEIGHT) + GuiConstants.STANDARD_SEPARATION_DISTANCE;
		int column2XPos = startingXPos + BUTTON_WIDTH + GuiConstants.STANDARD_SEPARATION_DISTANCE;

		int centerXPos = (this.width / 2) - (BUTTON_WIDTH / 2);
		int lowerYPos = line2YPos + (GuiConstants.STANDARD_BUTTON_HEIGHT * 2) + GuiConstants.STANDARD_SEPARATION_DISTANCE;

		int upperYPos = startingYPos - GuiConstants.STANDARD_SEPARATION_DISTANCE - GuiConstants.STANDARD_TEXTBOX_HEIGHT;

		messageBox = new TextBox("", font, centerXPos, upperYPos, BUTTON_WIDTH, GuiConstants.STANDARD_TEXTBOX_HEIGHT, false, false, 100);
		setTextBoxContent();
		messageBox.setEnabled(false);

		this.buttons.clear();

		this.buttons.add(new Button(3, startingXPos, startingYPos, BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Normal"));

		this.buttons.add(new Button(4, startingXPos, line2YPos, BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Gjum Special"));

		this.buttons.add(new Button(5, column2XPos, startingYPos, BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Hide Coordinates"));

		this.buttons.add(new Button(6, column2XPos, line2YPos, BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Hide Coords/Name"));

		this.buttons.add(new Button(1, startingXPos, lowerYPos, BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Cancel"));

		this.buttons.add(new Button(2, column2XPos, lowerYPos, BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Done"));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();

		messageBox.drawTextBox();

		super.render(mouseX, mouseY, partialTicks);
	}

	public void actionPerformed(Button button) {
		switch (button.id) {
			case 1:
				Minecraft.getInstance().displayGuiScreen(cancelToScreen);
				break;
			case 2:
				Minecraft.getInstance().displayGuiScreen(null);
				break;
			case 3:
				this.settings.setValue(QuietTimeHandler.QUIET_TIME_CONFIG_KEY, QuietTimeConfig.NORMAL);
				this.settings.saveSettings();
				setTextBoxContent();
				break;
			case 4:
				this.settings.setValue(QuietTimeHandler.QUIET_TIME_CONFIG_KEY, QuietTimeConfig.GJUM_SPECIAL);
				this.settings.saveSettings();
				setTextBoxContent();
				break;
			case 5:
				this.settings.setValue(QuietTimeHandler.QUIET_TIME_CONFIG_KEY, QuietTimeConfig.HIDE_COORDS);
				this.settings.saveSettings();
				setTextBoxContent();
				break;
			case 6:
				this.settings.setValue(QuietTimeHandler.QUIET_TIME_CONFIG_KEY, QuietTimeConfig.HIDE_COORDS_AND_NAME);
				this.settings.saveSettings();
				setTextBoxContent();
				break;
		}
	}

	private void setTextBoxContent() {
		QuietTimeConfig config = (QuietTimeConfig) settings.getValue(QuietTimeHandler.QUIET_TIME_CONFIG_KEY);
		int hash = config.hashCode();

		if (hash == QuietTimeConfig.NORMAL.hashCode() && config.equals(QuietTimeConfig.NORMAL)) {
			messageBox.setText("Normal");
		} else if (hash == QuietTimeConfig.GJUM_SPECIAL.hashCode() && config.equals(QuietTimeConfig.GJUM_SPECIAL)) {
			messageBox.setText("Gjum Special");
		} else if (hash == QuietTimeConfig.HIDE_COORDS.hashCode() && config.equals(QuietTimeConfig.HIDE_COORDS)) {
			messageBox.setText("Hide Coordinates");
		} else if (hash == QuietTimeConfig.HIDE_COORDS_AND_NAME.hashCode() && config.equals(QuietTimeConfig.HIDE_COORDS_AND_NAME)) {
			messageBox.setText("Hide Coords/Name");
		} else {
			messageBox.setText("Custom config? Idk.");
		}
		messageBox.setCursorPosition(0);
	}
}
