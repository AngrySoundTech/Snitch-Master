package com.gmail.nuclearcat1337.snitch_master.gui.snitchliststable;

import com.gmail.nuclearcat1337.snitch_master.gui.GuiConstants;
import com.gmail.nuclearcat1337.snitch_master.gui.tables.TableColumn;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchList;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class SnitchListControlsColumn implements TableColumn<SnitchList> {
	private static final int ARROW_BUTTON_WIDTH = 20;
	private static final int ON_OFF_BUTTON_WIDTH = 30;
	private static final int ENTRY_WIDTH = ARROW_BUTTON_WIDTH + GuiConstants.SMALL_SEPARATION_DISTANCE + ON_OFF_BUTTON_WIDTH + GuiConstants.SMALL_SEPARATION_DISTANCE + ARROW_BUTTON_WIDTH;

	private static Minecraft mc;

	private final SnitchListsTable table;
	private final SnitchManager manager;

	public SnitchListControlsColumn(SnitchListsTable table, SnitchManager manager) {
		mc = Minecraft.getInstance();
		this.table = table;
		this.manager = manager;
	}

	@Override
	public Button[] prepareEntry(SnitchList list) {
		Button[] buttons = new Button[3];
		buttons[0] = new Button(0, 0, 0, ARROW_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "/\\");
		buttons[1] = new Button(1, 0, 0, ON_OFF_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, list.shouldRenderSnitches() ? "On" : "Off");
		buttons[2] = new Button(2, 0, 0, ARROW_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "\\/");
		return buttons;
	}

	@Override
	public String getColumnName() {
		return "Controls";
	}

	@Override
	public boolean doBoundsCheck() {
		return true;
	}

	@Override
	public void clicked(SnitchList list, boolean leftClick, int xPos, int yPos, Button[] buttons, Screen parentScreen, int slotIndex) {
		if (!leftClick) {
			return;
		}

		if (buttons[0].mouseClicked(xPos, yPos, 0)) { //Up arrow button
			table.swapTableItems(slotIndex, slotIndex - 1);
			list.increaseRenderPriority();
			manager.saveSnitchLists();
		} else if (buttons[1].mouseClicked(xPos, yPos, 0)) { //Render toggle button
			list.setShouldRenderSnitches(!list.shouldRenderSnitches());
			manager.saveSnitchLists();
		} else if (buttons[2].mouseClicked(xPos, yPos, 0)) { //Down arrow button
			table.swapTableItems(slotIndex, slotIndex + 1);
			list.decreaseRenderPriority();
			manager.saveSnitchLists();
		}
	}

	@Override
	public void released(SnitchList list, int xPos, int yPos, Button[] buttons, Screen parentScreen, int slotIndex) {
		buttons[0].mouseReleased(xPos, yPos, 0);
		buttons[1].mouseReleased(xPos, yPos, 0);
		buttons[2].mouseReleased(xPos, yPos, 0);
	}

	@Override
	public void draw(SnitchList list, int xPosition, int yPosition, int columnWidth, int slotHeight, Button[] buttons, int slotIndex, int mouseX, int mouseY) {
		yPosition = yPosition + ((slotHeight - GuiConstants.STANDARD_BUTTON_HEIGHT) / 2);

		buttons[0].y = yPosition;
		buttons[1].y = yPosition;
		buttons[2].y = yPosition;

		int xPos = xPosition + (columnWidth / 2) - (ENTRY_WIDTH / 2);

		buttons[0].x = xPos;

		xPos += buttons[0].getWidth() + GuiConstants.SMALL_SEPARATION_DISTANCE;

		buttons[1].setMessage(list.shouldRenderSnitches() ? "On" : "Off");
		buttons[1].x = xPos;

		xPos += buttons[1].getWidth() + GuiConstants.SMALL_SEPARATION_DISTANCE;

		buttons[2].x = xPos;

		buttons[0].drawButton(mc, mouseX, mouseY, 0);
		buttons[1].drawButton(mc, mouseX, mouseY, 0);
		buttons[2].drawButton(mc, mouseX, mouseY, 0);
	}

	@Override
	public int getDrawWidth(SnitchList list) {
		return ENTRY_WIDTH;
	}

	@Override
	public List<String> hover(SnitchList item, int xPos, int yPos) {
		return null;
	}

	@Override
	public boolean canSort() {
		return false;
	}

	@Override
	public int compare(SnitchList o1, SnitchList o2) {
		return 0;
	}
}

