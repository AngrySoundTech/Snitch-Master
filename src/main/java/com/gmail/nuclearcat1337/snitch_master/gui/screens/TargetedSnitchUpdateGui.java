package com.gmail.nuclearcat1337.snitch_master.gui.screens;

import com.gmail.nuclearcat1337.snitch_master.gui.GuiConstants;
import com.gmail.nuclearcat1337.snitch_master.gui.controls.TextBox;
import com.gmail.nuclearcat1337.snitch_master.handlers.ChatSnitchParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class TargetedSnitchUpdateGui extends Screen {
	private static final String START_BOX_TEXT = "Start Index";
	private static final String STOP_BOX_TEXT = "Stop Index";

	private static final int BOX_AND_BUTTON_WIDTH = GuiConstants.MEDIUM_BUTTON_WIDTH;

	private final Screen cancelToScreen;
	private final ChatSnitchParser chatParser;

	private TextBox startIndexBox;
	private TextBox stopIndexBox;

	private final int startTextWidth;
	private final int stopTextWidth;

	public TargetedSnitchUpdateGui(Screen cancelToScreen, ChatSnitchParser chatParser) {
		this.cancelToScreen = cancelToScreen;
		this.chatParser = chatParser;

		startTextWidth = Minecraft.getInstance().fontRenderer.getStringWidth(START_BOX_TEXT);
		stopTextWidth = Minecraft.getInstance().fontRenderer.getStringWidth(STOP_BOX_TEXT);
	}

	public void initGui() {
		int yPos = (this.height / 2) - (GuiConstants.STANDARD_TEXTBOX_HEIGHT / 2);
		int xPos = (this.width / 2) - GuiConstants.STANDARD_SEPARATION_DISTANCE / 2 - BOX_AND_BUTTON_WIDTH;

		startIndexBox = new TextBox("", fontRenderer, xPos, yPos, BOX_AND_BUTTON_WIDTH, GuiConstants.STANDARD_TEXTBOX_HEIGHT, true, false, 3);
		startIndexBox.setClamp(0, 999);
		startIndexBox.changeFocus(true);

		xPos += (startIndexBox.width + GuiConstants.STANDARD_SEPARATION_DISTANCE);

		stopIndexBox = new TextBox("", fontRenderer, xPos, yPos, BOX_AND_BUTTON_WIDTH, GuiConstants.STANDARD_TEXTBOX_HEIGHT, true, false, 3);
		stopIndexBox.setClamp(0, 999);

		yPos += GuiConstants.STANDARD_TEXTBOX_HEIGHT + GuiConstants.STANDARD_SEPARATION_DISTANCE;
		xPos -= (startIndexBox.width + GuiConstants.STANDARD_SEPARATION_DISTANCE);

		this.buttonList.clear();
		this.buttonList.add(new Button(1, xPos, yPos, BOX_AND_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Cancel"));

		xPos += (startIndexBox.width + GuiConstants.STANDARD_SEPARATION_DISTANCE);
		this.buttonList.add(new Button(2, xPos, yPos, BOX_AND_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Update"));

		super.initGui();
	}

	public void updateScreen() {
		startIndexBox.updateCursorCounter();
		stopIndexBox.updateCursorCounter();
		super.updateScreen();
	}

	public void keyTyped(char par1, int par2) throws IOException {
		if (startIndexBox.isFocused()) {
			if (par2 == GLFW.GLFW_KEY_TAB) {
				stopIndexBox.changeFocus(true);
				startIndexBox.changeFocus(false);
			} else {
				startIndexBox.textboxKeyTyped(par1, par2);
			}
		}
		else if (stopIndexBox.isFocused()) {
			if (par2 == GLFW.GLFW_KEY_TAB) {
				startIndexBox.changeFocus(true);
				stopIndexBox.changeFocus(false);
			} else {
				stopIndexBox.textboxKeyTyped(par1, par2);
			}
		}
		super.keyTyped(par1, par2);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		int textYHeight = (GuiConstants.STANDARD_TEXTBOX_HEIGHT / 2) - (mc.fontRenderer.FONT_HEIGHT / 2) - GuiConstants.STANDARD_TEXTBOX_HEIGHT;

		//Draw the text above the start index text box
		mc.fontRenderer.drawString(START_BOX_TEXT, startIndexBox.x + (startIndexBox.width / 2) - (startTextWidth / 2), startIndexBox.y + textYHeight, 16777215);

		//Draw the text above the stop index text box
		mc.fontRenderer.drawString(STOP_BOX_TEXT, stopIndexBox.x + (stopIndexBox.width / 2) - (stopTextWidth / 2), startIndexBox.y + textYHeight, 16777215);

		this.startIndexBox.drawTextBox();
		this.stopIndexBox.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void mouseClicked(int one, int two, int three) throws IOException {
		this.startIndexBox.mouseClicked(one, two, three);
		this.stopIndexBox.mouseClicked(one, two, three);
		super.mouseClicked(one, two, three);
	}

	public void actionPerformed(Button button) {
		switch (button.id) {
			case 1:
				Minecraft.getInstance().displayGuiScreen(cancelToScreen);
				break;
			case 2:
				Integer minIndex = startIndexBox.clamp();
				if (minIndex == null) {
					return;
				}

				Integer maxIndex = stopIndexBox.clamp();
				if (maxIndex == null) {
					maxIndex = -1;
				}

				chatParser.updateSnitchList(minIndex, maxIndex);

				Minecraft.getInstance().displayGuiScreen(null);
				break;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
