package com.gmail.nuclearcat1337.snitch_master.gui;

import com.gmail.nuclearcat1337.snitch_master.SnitchMaster;
import com.gmail.nuclearcat1337.snitch_master.api.SnitchListQualifier;
import com.gmail.nuclearcat1337.snitch_master.gui.controls.TextBox;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchList;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchLists;
import com.gmail.nuclearcat1337.snitch_master.util.Color;
import com.gmail.nuclearcat1337.snitch_master.util.IOHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by Mr_Little_Kitty on 9/18/2016.
 */
public class NewSnitchListGui extends GuiScreen
{
    private static final String CREATE_NEW_LIST_STRING = "Create New Snitch List";

    private SnitchMaster snitchMaster;
    private GuiScreen cancelToScreen;

    private TextBox nameBox;
    private TextBox redBox;
    private TextBox greenBox;
    private TextBox blueBox;
    private TextBox qualifierBox;

    private int nameBoxWidth;
    private int qualifierBoxWidth;
    private int rgbBoxWidth;
    private int qualifierStringWidth;
    private int createNewListStringWidth;
    private int buttonWidth;

    public NewSnitchListGui(GuiScreen cancelToScreen, SnitchMaster snitchMaster)
    {
        this.cancelToScreen = cancelToScreen;
        this.snitchMaster = snitchMaster;
    }

    public void initGui()
    {
        qualifierBoxWidth = mc.fontRendererObj.getStringWidth(SnitchList.MAX_NAME_CHARACTERS+"WWW"); //TODO---Idk this is kind of just an arbitrary width
        nameBoxWidth = qualifierBoxWidth;
        rgbBoxWidth = nameBoxWidth/3;
        qualifierStringWidth = mc.fontRendererObj.getStringWidth("Qualifier");
        createNewListStringWidth = mc.fontRendererObj.getStringWidth(CREATE_NEW_LIST_STRING);
        buttonWidth = (qualifierBoxWidth-GuiConstants.STANDARD_SEPARATION_DISTANCE)/3;

        int yPos = (this.height / 2) - (GuiConstants.STANDARD_BUTTON_HEIGHT*3) - (GuiConstants.STANDARD_SEPARATION_DISTANCE) ;
        int xPos = (this.width/2) - (nameBoxWidth/2);

        nameBox = new TextBox("",fontRendererObj,xPos,yPos,nameBoxWidth,GuiConstants.STANDARD_TEXTBOX_HEIGHT,false,false);
        nameBox.setMaxStringLength(20);
        nameBox.setFocused(true);

        yPos += (GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.SMALL_SEPARATION_DISTANCE);
        xPos = nameBox.xPosition;

        this.qualifierBox = new TextBox("",fontRendererObj,xPos,yPos,qualifierBoxWidth,GuiConstants.STANDARD_TEXTBOX_HEIGHT,false,false);
        qualifierBox.setMaxStringLength(100);

        yPos += (GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.SMALL_SEPARATION_DISTANCE);

        this.redBox = new TextBox("",fontRendererObj,xPos,yPos,rgbBoxWidth,GuiConstants.STANDARD_TEXTBOX_HEIGHT,true,false);
        redBox.setClamp(0,255);
        redBox.setMaxStringLength(3);

        yPos += (GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.SMALL_SEPARATION_DISTANCE);

        this.greenBox = new TextBox("",fontRendererObj,xPos,yPos,rgbBoxWidth,GuiConstants.STANDARD_TEXTBOX_HEIGHT,true,false);
        greenBox.setClamp(0,255);
        greenBox.setMaxStringLength(3);

        yPos += (GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.SMALL_SEPARATION_DISTANCE);

        this.blueBox = new TextBox("",fontRendererObj,xPos,yPos,rgbBoxWidth,GuiConstants.STANDARD_TEXTBOX_HEIGHT,true,false);
        blueBox.setClamp(0,255);
        blueBox.setMaxStringLength(3);

        this.buttonList.clear();

        xPos += (blueBox.width + GuiConstants.SMALL_SEPARATION_DISTANCE);

        this.buttonList.add(new GuiButton(1,xPos,yPos,buttonWidth,GuiConstants.STANDARD_BUTTON_HEIGHT,"Cancel"));

        xPos += (buttonWidth + GuiConstants.SMALL_SEPARATION_DISTANCE);

        this.buttonList.add(new GuiButton(2,xPos,yPos,buttonWidth,GuiConstants.STANDARD_BUTTON_HEIGHT,"Create"));

        super.initGui();
    }

    public void updateScreen()
    {
        nameBox.updateCursorCounter();
        qualifierBox.updateCursorCounter();
        redBox.updateCursorCounter();
        greenBox.updateCursorCounter();
        blueBox.updateCursorCounter();
        super.updateScreen();
    }

    public void keyTyped(char par1, int par2) throws IOException
    {
        if(nameBox.isFocused())
        {
            if(par2 == Keyboard.KEY_TAB)
            {
                qualifierBox.setFocused(true);
                nameBox.setFocused(false);
            }
            nameBox.textboxKeyTyped(par1, par2);
        }
        else if(qualifierBox.isFocused())
        {
            if(par2 == Keyboard.KEY_TAB)
            {
                redBox.setFocused(true);
                qualifierBox.setFocused(false);
            }
            qualifierBox.textboxKeyTyped(par1, par2);
        }
        else if(redBox.isFocused())
        {
            if(par2 == Keyboard.KEY_TAB)
            {
                greenBox.setFocused(true);
                redBox.setFocused(false);
            }
            redBox.textboxKeyTyped(par1, par2);
        }
        else if(greenBox.isFocused())
        {
            if(par2 == Keyboard.KEY_TAB)
            {
                blueBox.setFocused(true);
                greenBox.setFocused(false);
            }
            greenBox.textboxKeyTyped(par1, par2);
        }
        else if(blueBox.isFocused())
        {
            if(par2 == Keyboard.KEY_TAB)
            {
                nameBox.setFocused(true);
                blueBox.setFocused(false);
            }
            blueBox.textboxKeyTyped(par1, par2);
        }
        super.keyTyped(par1,par2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        int constYValue = (GuiConstants.STANDARD_TEXTBOX_HEIGHT/2) - mc.fontRendererObj.FONT_HEIGHT/2;
        int constXValue = GuiConstants.SMALL_SEPARATION_DISTANCE + qualifierStringWidth;

        mc.fontRendererObj.drawString("Blue",blueBox.xPosition-constXValue,blueBox.yPosition + constYValue,16777215);
        mc.fontRendererObj.drawString("Green",greenBox.xPosition-constXValue,greenBox.yPosition + constYValue,16777215);
        mc.fontRendererObj.drawString("Red",redBox.xPosition-constXValue,redBox.yPosition + constYValue,16777215);
        mc.fontRendererObj.drawString("Qualifier",qualifierBox.xPosition-constXValue,qualifierBox.yPosition + constYValue,16777215);
        mc.fontRendererObj.drawString("Name",nameBox.xPosition-constXValue,nameBox.yPosition + constYValue,16777215);

        int yPos = nameBox.yPosition - GuiConstants.STANDARD_SEPARATION_DISTANCE - mc.fontRendererObj.FONT_HEIGHT;
        int xPos = nameBox.xPosition + (nameBoxWidth/2) - (createNewListStringWidth/2);

        mc.fontRendererObj.drawString(CREATE_NEW_LIST_STRING,xPos,yPos,16777215);

        this.nameBox.drawTextBox();
        this.qualifierBox.drawTextBox();
        this.redBox.drawTextBox();
        this.greenBox.drawTextBox();
        this.blueBox.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(int one, int two, int three) throws IOException
    {
        this.nameBox.mouseClicked(one,two,three);
        this.qualifierBox.mouseClicked(one,two,three);
        this.redBox.mouseClicked(one,two,three);
        this.blueBox.mouseClicked(one,two,three);
        this.greenBox.mouseClicked(one,two,three);
        super.mouseClicked(one,two,three);
    }

    public void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(cancelToScreen);
                break;
            case 2:
                String name = nameBox.getText();
                if(name == null)
                    break;
                Integer red = redBox.clamp();
                if(red == null)
                    break;
                Integer green = greenBox.clamp();
                if(green == null)
                    break;
                Integer blue = blueBox.clamp();
                if(blue == null)
                    break;
                String qualifier = qualifierBox.getText();
                if(qualifier == null || !SnitchListQualifier.isSyntaxValid(qualifier))
                    break;

                SnitchLists lists = snitchMaster.getSnitchLists();
                if(lists.doesListWithNameExist(name))
                    break;

                SnitchList list = new SnitchList(new SnitchListQualifier(qualifier),true,name,new Color(red,green,blue));
                lists.addSnitchList(list);

                //Handles any things that need to happen when lists change. (saving, etc)
                lists.snitchListChanged();

                Minecraft.getMinecraft().displayGuiScreen(new EditSnitchListsGui(null,snitchMaster,snitchMaster.getSnitchLists().asCollection(),true, "All Snitch Lists"));
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
