package com.gmail.nuclearcat1337.snitch_master.gui.tables;

import com.gmail.nuclearcat1337.snitch_master.gui.GuiConstants;
import com.gmail.nuclearcat1337.snitch_master.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TableTopGui<T> extends Screen {
	private static final int DONE_BUTTON_WIDTH = GuiConstants.SMALL_BUTTON_WIDTH * 3;

	protected final Screen parentScreen;
	protected Button doneButton;
	protected Button columnsButton;

	private Collection<T> items;

	private List<TableColumn<T>> allColumns;
	private List<TableColumn<T>> renderColumns;
	private List<TableColumn<T>> columnsToBoundsCheck;

	private final String title;
	private final int titleWidth;

	private TableGui<T> tableGui;

	public TableTopGui(Screen parentScreen, Collection<T> items, String title) {
		this.parentScreen = parentScreen;
		this.items = items;
		this.title = title;
		this.titleWidth = Minecraft.getInstance().fontRenderer.getStringWidth(title);
	}

	protected abstract void initializeButtons(int firstId);

	protected abstract Collection<Pair<TableColumn<T>, Boolean>> initializeColumns();

	public void saveColumns(List<TableColumn<T>> allColumns, List<TableColumn<T>> renderColumns) {

	}

	@Override
	public void init() {
		Collection<Pair<TableColumn<T>, Boolean>> initialColumns = initializeColumns();

		assert initialColumns != null && !initialColumns.isEmpty();

		allColumns = new ArrayList<>();
		renderColumns = new ArrayList<>();
		columnsToBoundsCheck = new ArrayList<>();

		for (Pair<TableColumn<T>, Boolean> pair : initialColumns) {
			allColumns.add(pair.getOne());

			if (pair.getTwo()) {
				renderColumns.add(pair.getOne());

				if (pair.getOne().doBoundsCheck()) {
					columnsToBoundsCheck.add(pair.getOne());
				}
			}
		}

		tableGui = new TableGui<T>(this, items, renderColumns);

		buttons.clear();

		int xPos = (this.width / 2) - DONE_BUTTON_WIDTH - (GuiConstants.STANDARD_SEPARATION_DISTANCE / 2);
		int yPos = this.height - GuiConstants.STANDARD_BUTTON_HEIGHT - GuiConstants.STANDARD_SEPARATION_DISTANCE;

		doneButton = new Button(0, xPos, yPos, DONE_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Back");

		int buttonWidth = minecraft.fontRenderer.getStringWidth("--Columns--");
		xPos -= ((GuiConstants.STANDARD_SEPARATION_DISTANCE * 4) + buttonWidth);

		columnsButton = new Button(1, xPos, yPos, buttonWidth, GuiConstants.STANDARD_BUTTON_HEIGHT, "Columns");

		buttons.add(doneButton);
		buttons.add(columnsButton);

		initializeButtons(3);

		super.init();
	}

	protected Collection<T> getItems() {
		return items;
	}

	public int getTableSize() {
		return tableGui.getSize();
	}

	public T getTableItem(int tableIndex) {
		return tableGui.getItemForSlotIndex(tableIndex);
	}

	public void swapTableItems(int tableIndex, int nextTableIndex) {
		tableGui.swapItems(tableIndex, nextTableIndex);
	}

	public void setRenderColumns(List<TableColumn<T>> allColumns, List<TableColumn<T>> renderColumns) {
		//We need both columns lists so that the column order can be changed
		this.allColumns = allColumns;
		this.renderColumns = renderColumns;

		columnsToBoundsCheck.clear();
		for (TableColumn<T> col : renderColumns)
			if (col.doBoundsCheck()) {
				columnsToBoundsCheck.add(col);
			}
		tableGui = new TableGui<T>(this, items, this.renderColumns);
	}

	@Override
	public void actionPerformed(Button button) {
		if (!button.enabled) {
			return;
		}
		switch (button.id) {
			case 0: //Done
				this.minecraft.displayGuiScreen(parentScreen);
				break;
			case 1:
				this.minecraft.displayGuiScreen(new TableColumnSelectorTop<T>(this, allColumns, renderColumns, "Select Columns"));
				break;
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//Draw the background, the actual table, and anything from out parent
		this.renderBackground();
		this.tableGui.render(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);

		//Create positioning info for drawing the title
		int yPos = 16 - (minecraft.fontRenderer.FONT_HEIGHT / 2);
		int xPos = (this.width / 2) - (titleWidth / 2);

		//Draw the title
		minecraft.fontRenderer.drawString(title, xPos, yPos, 16777215);

		if (mouseY >= tableGui.getTop() && mouseY <= tableGui.getBottom()) {
			int index = tableGui.getSlotIndexFromScreenCoords(mouseX, mouseY);
			if (index >= 0) {
				for (TableColumn<T> col : columnsToBoundsCheck) {
					Pair<Integer, Integer> bounds = tableGui.getBoundsForColumn(col);
					if (mouseX >= bounds.getOne() && mouseX <= bounds.getTwo()) {
						List<String> text = col.hover(tableGui.getItemForSlotIndex(index), xPos, yPos);
						if (text != null && !text.isEmpty()) {
							drawHoveringText(text, mouseX, mouseY);
						}
						break;
					}
				}
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		tableGui.mouseClicked(mouseX, mouseY, mouseEvent);

		//Hopefully this determines if they clicked in the header area
		if (mouseY >= tableGui.getTop() && mouseY <= tableGui.getBottom() + tableGui.headerPadding && tableGui.getSlotIndexFromScreenCoords(mouseX, mouseY) < 0) {
			for (TableColumn<T> col : renderColumns) {
				Pair<Integer, Integer> bounds = tableGui.getBoundsForColumn(col);
				if (mouseX >= bounds.getOne() && mouseX <= bounds.getTwo()) {
					tableGui.sortByColumn(col);
					break;
				}
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseEvent);
	}

	@Override
	public void mouseReleased(int arg1, int arg2, int arg3) {
		//This method is ESSENTIAL to the functioning of the scroll bar
		tableGui.mouseReleased(arg1, arg2, arg3);
		super.mouseReleased(arg1, arg2, arg3);
	}

	@Override
	public void handleMouseInput() {
		//This method is ESSENTIAL to the functioning of the scroll bar
		tableGui.handleMouseInput();
		try {
			super.handleMouseInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
