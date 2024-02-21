package com.gotmemes.dmtcheat;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import org.apache.commons.lang3.StringUtils;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


@Mod(
        modid = dmtcheat.MODID,
        name = dmtcheat.NAME,
        version = dmtcheat.VERSION,
        clientSideOnly= true,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class dmtcheat {
    public static final String MODID = "dmtcheat";
    public static final String NAME = "Draw My Thing Cheat";
    public static final String VERSION = "1.0";
	
	private static final String THEME_MSG = EnumChatFormatting.AQUA + "The theme is " + EnumChatFormatting.YELLOW;
    private static final String WORDLIST_URL = "https://gotmemes.github.io/gtbcheat/wordlist.txt";
    private String message = "nothing";
    
    public static void preInit(FMLPreInitializationEvent event) {}

    @EventHandler()
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new Cmd());
    }

    public static void postInit(FMLPostInitializationEvent event) {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onChatReceive(ClientChatReceivedEvent e) {
        message = e.message.getUnformattedTextForChat();
    }

    private String getWordList() throws Exception{
        URL url = new URL(WORDLIST_URL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = reader.readLine();
        reader.close();
        return line;
    }

    private ArrayList<String> findWords(String hint) throws Exception {
        String[] wordList = getWordList().split(",");
        ArrayList<String> matches = new ArrayList<String>();
        int numWords = StringUtils.countMatches(hint, " ") + 1;
        int wordLength = hint.length();
		
        for (String word : wordList) {
            if (word.length() == wordLength){
                boolean match = true;
				
                for (int j = 0; j < word.length(); j++) {
                    if (!(hint.charAt(j) == '_' && !(word.charAt(j) == ' ') || hint.charAt(j) == word.charAt(j))) {
                        match = false;
                        break;
                    }
                }
				
                if (match) {
                    matches.add(word);
                }
            }
        }
		
        return matches;
    }

    class Cmd extends CommandBase {
        @Override
        public String getCommandName() {
            return "gtb";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "gtb";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) throws CommandException{
            if (message.contains(THEME_MSG)) {
                String hint = message.replace(THEME_MSG, "");
				
                try {
					ArrayList<String> matches = findWords(hint);
                    String list = " ";

                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "GTB Cheat (" + String.valueOf(matches.size()) + " matches):"));
					
                    for (String choices : matches){
                        list += EnumChatFormatting.AQUA + choices + ", ";
                    }
					
                    list = list.substring(0, list.length() - 2);
                    sender.addChatMessage(new ChatComponentText(list));
					
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "GTB Cheat: " + EnumChatFormatting.GRAY + "Nothing to find."));
            }
        }

        @Override
        public boolean canCommandSenderUseCommand(ICommandSender sender) {
            return true;
        }
    }
}


