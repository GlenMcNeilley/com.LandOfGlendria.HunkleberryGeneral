package com.LandOfGlendria.HunkleberryGeneral;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.bukkit.plugin.Plugin;

// Referenced classes of package com.LandOfGlendria.HunkleberryGeneral:
//            HGStatics, HGMessageManagement, HGConfig

public class HGWorldlyThings
{

    private Properties worldlyProperties;
    public HGMessageManagement msg;
    public HGConfig cfg;
    public File worldlyPropertiesFile;
    public static HashSet<String> worldLoadSet = new HashSet<String>();
    public static String worldLoadArray[];

    public HGWorldlyThings(Plugin plugin, HGMessageManagement msg, HGConfig cfg)
    {
        this.msg = msg;
        this.cfg = cfg;
        worldlyProperties = new Properties();
        worldlyPropertiesFile = new File(HGStatics.WORLDLY_PROPERTIES);
        try
        {
            manageWorldlyPropertyFiles();
        }
        catch(Exception e)
        {
            msg.severe((new StringBuilder("Caught ")).append(e.getClass().getName()).append(" in HGWorldlyThings().").toString());
            e.printStackTrace();
        }
    }

    private void populateWorldAutoLoadList()
    {
        if(worldlyProperties.containsKey(HGStatics.WORLDS_AUTOLOAD_KEY))
        {
            String worldsToLoad = worldlyProperties.getProperty(HGStatics.WORLDS_AUTOLOAD_KEY);
            worldLoadArray = worldsToLoad.split("/");
            worldLoadSet.addAll(Arrays.asList(worldLoadArray));
            worldLoadSet.remove("");
        }
    }
    private void populateWorldLocationList()
    {
//        Enumeration e = worldlyProperties.propertyNames();
//
//        while (e.hasMoreElements()) {
//          String key = (String) e.nextElement();
//          if (key.startsWith("loc.")) {
//        	  
//          }
//          String value = worldlyProperties.getProperty(key);
//          
//          
//        }
//
//        if(worldlyProperties.containsKey(HGStatics.WORLDS_AUTOLOAD_KEY))
//        {
//            String worldsToLoad = worldlyProperties.getProperty(HGStatics.WORLDS_AUTOLOAD_KEY);
//            worldLoadArray = worldsToLoad.split("/");
//            worldLoadSet.addAll(Arrays.asList(worldLoadArray));
//            worldLoadSet.remove("");
//        }
    }

    private void setWorldAutoLoadList()
    {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Iterator<String> iterator = worldLoadSet.iterator(); iterator.hasNext();)
        {
            String worldName = (String)iterator.next();
            if(count > 0)
            {
                sb.append("/");
            }
            sb.append(worldName);
            count++;
        }

        worldlyProperties.put(HGStatics.WORLDS_AUTOLOAD_KEY, sb.toString());
    }

    public final Object[] getWorldAutoLoadArray()
    {
        return worldLoadSet.toArray();
    }

    public boolean addAutoLoadWorld(String worldName)
    {
        return worldLoadSet.add(worldName);
    }

    public boolean deleteAutoLoadWorld(String worldName)
    {
        return worldLoadSet.remove(worldName);
    }

    public String getWorldlyProperty(String key)
        throws IOException
    {
        String prop = worldlyProperties.getProperty(key);
        return prop;
    }

    public void setWorldlyProperty(String key, String value)
        throws IOException
    {
        worldlyProperties.setProperty(key, value);
        saveConfigFileProperties();
    }

    public boolean removeWorldlyProperty(String key)
        throws IOException
    {
        if(worldlyProperties.containsKey(key))
        {
            if(worldlyProperties.remove(key) != null)
            {
                saveConfigFileProperties();
                return true;
            } else
            {
                return false;
            }
        } else
        {
            return false;
        }
    }

    public void manageWorldlyPropertyFiles()
    {
        try
        {
            cfg.getPropertiesFromFile(worldlyPropertiesFile, worldlyProperties, HGStatics.WORLDLY_MESSAGE);
            populateWorldAutoLoadList();
            populateWorldLocationList();
            saveConfigFileProperties();
        }
        catch(IOException e)
        {
            msg.severe("Error reading/writing worldly properties files.");
            e.printStackTrace();
        }
    }

    public void saveConfigFileProperties()
        throws IOException
    {
        setWorldAutoLoadList();
        cfg.saveConfigFileProperties(worldlyPropertiesFile, worldlyProperties, HGStatics.WORLDLY_MESSAGE);
    }

    

}
