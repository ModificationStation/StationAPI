package net.rek.oldlogo.gui.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;

public class LogoConfig {
	public LogoConfig() {
		enabled = true;
		File configFile = new File((Minecraft.getMinecraftDir()) + "/config/oldCustomLogo.cfg");
		if(!configFile.exists())
		{
			try
			{
				new File((Minecraft.getMinecraftDir()) + "/config/").mkdirs();
				configFile.createNewFile();
				BufferedWriter configWriter = new BufferedWriter(new FileWriter(configFile));
				configWriter.write(				   "// Config file for customising the animated logo."
						+ System.lineSeparator() + "// If you would like to disable this mod you can do so in ModLoader.cfg"
						+ System.lineSeparator() + "// "
						+ System.lineSeparator() + "// To customise the logo, first create a key below with block IDs and characters to match the ascii."
						+ System.lineSeparator() + "// This X=1 means that any block denoted with an X in the image section will be a stone block in the main menu."
						+ System.lineSeparator() + "X=1" 
            			+ System.lineSeparator() + "//Y=35-6" 
            			+ System.lineSeparator() + "// You can use blocks with metadata with ':' or '-' so for example: Y=35-6 would attribute Y with pink wool." 
            			+ System.lineSeparator() + "// "
            			+ System.lineSeparator() + "#" 
            			+ System.lineSeparator() + "// Below this # symbol is where the logo art is recorded."
            			+ System.lineSeparator() + "// If you want to use a blank line you must put at least 1 space in." 
            			+ System.lineSeparator() + "// If you use a symbol below that has not been assigned a block, it will give the torch block (which does not look good)."
            			+ System.lineSeparator() + "// " 
            			+ System.lineSeparator() + "X   X X X   X XXX XXX XXX XXX XXX XXX"
            			+ System.lineSeparator() + "XX XX X XX  X X   X   X X X X X    X "
            			+ System.lineSeparator() + "X X X X X X X XX  X   XX  XXX XX   X " 
            			+ System.lineSeparator() + "X   X X X  XX X   X   X X X X X    X " 
            			+ System.lineSeparator() + "X   X X X   X XXX XXX X X X X X    X "
            		);
				configWriter.close();
			}
			catch(Exception exception) { }
		}
		try
		{
			BufferedReader configReader = new BufferedReader(new FileReader(configFile));
			String s;
			Boolean readImage = false;
			while ((s = configReader.readLine()) != null)
			{
				if(s.charAt(0) == '/' && s.charAt(1) == '/') {continue;} //Ignore comments
        	
				if(readImage) {
					image.add(s);
					if(s.length() > maxLineLength)
                	{
						maxLineLength = s.length();
                	}
				}
				else if(s.contains("="))
				{
					String as[] = s.split("=");
					chars.add(as[0].charAt(0));
					if(as[1].contains(":"))
					{
						String as1[] = as[1].split(":");
						blockID.add(Integer.parseInt(as1[0]));
						metaData.add(Integer.parseInt(as1[1]));
					} 
					else if(as[1].contains("-"))
					{
						String as1[] = as[1].split("-");
						blockID.add(Integer.parseInt(as1[0]));
						metaData.add(Integer.parseInt(as1[1]));
					}
					else
					{
						blockID.add(Integer.parseInt(as[1]));
						metaData.add(0);
					}
				}
				else if(s.equals("#"))
				{
					readImage = true;
				}
        	}
        	configReader.close();
    	}
    catch(Exception exception) { }
	
	}
	
	public static String[] getLogo()
    {
            String as[] = new String[image.size()];
            for(int i = 0; i < image.size(); i++)
            {
                as[i] = image.get(i);
            }

            return as;
    }
	
	public static Boolean enabled()
    {
    	return enabled;
    }
	
    public static int getLogoWidth() {
    	return maxLineLength;
    }

    public static Block getBlock(int i, int j)
    {
        for(int k = 0; k < blockID.size(); k++)
        {
            if(image.get(i).charAt(j) == chars.get(k))
            {
            	if (blockID.get(k) < 255)
            	{
            		return Block.blocksList[blockID.get(k)];
            	}
            }
        }
        return null;
    }

    public static int getMetaData(int i, int j)
    {
        for(int k = 0; k < blockID.size(); k++)
        {
            if(image.get(i).charAt(j) == chars.get(k))
            {
                return metaData.get(k);
            }
        }
        return 0;
    }
    
	private static List<String> image = new ArrayList<String>();
    private static List<Integer> blockID = new ArrayList<Integer>();
    private static List<Integer> metaData = new ArrayList<Integer>();
    private static List<Character> chars = new ArrayList<Character>();
    private static int maxLineLength = 0;
    private static boolean enabled = false;
}
