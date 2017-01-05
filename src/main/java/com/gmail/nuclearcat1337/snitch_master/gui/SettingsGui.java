package com.gmail.nuclearcat1337.snitch_master.gui;

import com.gmail.nuclearcat1337.snitch_master.Settings;
import com.gmail.nuclearcat1337.snitch_master.SnitchMaster;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

/**
 * Created by Mr_Little_Kitty on 12/29/2016.
 */
public class SettingsGui extends GuiScreen
{
    private final GuiScreen backToScreen;
    private final Settings settings;

    private GuiButton chatSpamButton;
    private GuiButton quietTimeButton;
    private GuiButton renderTextButton;

    public SettingsGui(GuiScreen backToScreen)
    {
        this.backToScreen = backToScreen;
        //This is a hack so that this class can be used with the Forge "config" button
        this.settings = SnitchMaster.instance.getSettings();
    }

    public void initGui()
    {
        this.buttonList.clear();

        int xPos = (this.width/2) - (GuiConstants.LONG_BUTTON_WIDTH / 2);
        int yPos = (this.height/2) - ( ((GuiConstants.STANDARD_BUTTON_HEIGHT*3) + (GuiConstants.STANDARD_SEPARATION_DISTANCE*2))/2 );

        //Set the drawXPos variable for drawing 2 half sized buttons
        int drawXPos = xPos;
        int halfWidth = (GuiConstants.LONG_BUTTON_WIDTH/2) - (GuiConstants.STANDARD_SEPARATION_DISTANCE/2);

        renderTextButton = new GuiButton(1, drawXPos, yPos, halfWidth, GuiConstants.STANDARD_BUTTON_HEIGHT, "");
        updateRenderTextButton();
        this.buttonList.add(renderTextButton);

        //Increment it for drawing the second button
        //drawXPos += (halfWidth + GuiConstants.STANDARD_SEPARATION_DISTANCE);

        yPos = yPos + GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.STANDARD_SEPARATION_DISTANCE;

        quietTimeButton = new GuiButton(3, xPos, yPos, GuiConstants.LONG_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "");
        updateQuietTimeButton();
        this.buttonList.add(quietTimeButton);

        yPos = yPos + GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.STANDARD_SEPARATION_DISTANCE;

        chatSpamButton = new GuiButton(4, xPos, yPos, GuiConstants.LONG_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "");
        updateChatSpamButton();
        this.buttonList.add(chatSpamButton);

        yPos = yPos + GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.STANDARD_SEPARATION_DISTANCE;

        this.buttonList.add(new GuiButton(0, xPos, yPos, GuiConstants.LONG_BUTTON_WIDTH, GuiConstants.STANDARD_BUTTON_HEIGHT, "Done"));
    }

    public void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0: //"Done"
                this.mc.displayGuiScreen(backToScreen);
                break;
            case 1: //"Render Text"
                nextRenderTextState();
                updateRenderTextButton();
                settings.saveSettings();
                break;
            case 2:
                break;
            case 3: //"Quiet Time"
                nextQuietTimeState();
                updateQuietTimeButton();
                settings.saveSettings();
                break;

            case 4: //"Updating Snitches Spam: "
                nextChatSpamState();
                updateChatSpamButton();
                settings.saveSettings();
                break;
        }
    }

    private void nextRenderTextState()
    {
        Boolean state = (Boolean)settings.getValue(Settings.RENDER_TEXT_KEY);
        state = state ? Boolean.FALSE : Boolean.TRUE;
        settings.setValue(Settings.RENDER_TEXT_KEY,state);
    }

    private void updateRenderTextButton()
    {
        Boolean state = (Boolean)settings.getValue(Settings.RENDER_TEXT_KEY);
        String text =  state ? "Render Text On" : "Render Text Off";
        renderTextButton.displayString = text;
    }

    private void nextQuietTimeState()
    {
        Settings.QuietTimeState quietTimeState = (Settings.QuietTimeState) settings.getValue(Settings.QUIET_TIME_KEY);
        if(quietTimeState == Settings.QuietTimeState.OFF)
            quietTimeState = Settings.QuietTimeState.HIDE_COORDINATES;
        else if(quietTimeState == Settings.QuietTimeState.HIDE_COORDINATES)
            quietTimeState = Settings.QuietTimeState.HIDE_NAME_AND_COORDINATES;
        else if(quietTimeState == Settings.QuietTimeState.HIDE_NAME_AND_COORDINATES)
            quietTimeState = Settings.QuietTimeState.OFF;
        settings.setValue(Settings.QUIET_TIME_KEY,quietTimeState);
    }

    private void updateQuietTimeButton()
    {
        String quietTimeText = "Quiet Time: ";
        Settings.QuietTimeState quietTimeState = (Settings.QuietTimeState) settings.getValue(Settings.QUIET_TIME_KEY);
        if(quietTimeState == Settings.QuietTimeState.OFF)
            quietTimeText += "Off";
        else if(quietTimeState == Settings.QuietTimeState.HIDE_COORDINATES)
            quietTimeText += "Hide Location";
        else if(quietTimeState == Settings.QuietTimeState.HIDE_NAME_AND_COORDINATES)
            quietTimeText += "Hide Location & Name";
        quietTimeButton.displayString = quietTimeText;
    }

    private void nextChatSpamState()
    {
        Settings.ChatSpamState chatSpamState = (Settings.ChatSpamState) settings.getValue(Settings.CHAT_SPAM_KEY);
        if(chatSpamState == Settings.ChatSpamState.ON)
            chatSpamState = Settings.ChatSpamState.OFF;
        else if(chatSpamState == Settings.ChatSpamState.OFF)
            chatSpamState = Settings.ChatSpamState.PAGENUMBERS;
        else if(chatSpamState == Settings.ChatSpamState.PAGENUMBERS)
            chatSpamState = Settings.ChatSpamState.ON;
        settings.setValue(Settings.CHAT_SPAM_KEY,chatSpamState);
    }

    private void updateChatSpamButton()
    {
        String jaListSpamText = "Updating Snitches Spam: ";
        Settings.ChatSpamState chatSpamState = (Settings.ChatSpamState) settings.getValue(Settings.CHAT_SPAM_KEY);
        if(chatSpamState == Settings.ChatSpamState.ON)
            jaListSpamText += "On";
        else if(chatSpamState == Settings.ChatSpamState.OFF)
            jaListSpamText += "Off";
        else if(chatSpamState == Settings.ChatSpamState.PAGENUMBERS)
            jaListSpamText += "Page Numbers";
        chatSpamButton.displayString = jaListSpamText;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
