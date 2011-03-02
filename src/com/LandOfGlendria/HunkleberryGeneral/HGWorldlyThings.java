package com.LandOfGlendria.HunkleberryGeneral;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    
    public Map<String, HGLocation> getLocationList(String prefix) throws NumberFormatException, UnsupportedEncodingException {
    	//iterate over props looking for keys that start with loc.
    	Map<String, HGLocation> locations = new HashMap<String, HGLocation>();
    	//HashSet<HGLocation> locations = new HashSet<HGLocation>();
    	
    	for (Enumeration<Object> e = worldlyProperties.keys(); e.hasMoreElements(); /**/) {
    		String key = (String) e.nextElement();
    		//String value = worldlyProperties.getProperty(key);
    		//System.out.println(key + " = " + value);
    	
    	
    	
    	//Iterator<Object> iter = worldlyProperties.keySet().iterator();
    	//for (String key = null; iter.hasNext(); key = (String) iter.next()) {
    		if (key.startsWith(prefix)) {
    			//populate locations
    			String locArgs = worldlyProperties.getProperty(key);
    			if (locArgs != null && !locArgs.isEmpty()) {
    				String[] parts = locArgs.split(",");
    				if (parts.length == 9) {
    					HGLocation location = new HGLocation(
    							URLDecoder.decode(parts[0],HGStatics.UTF8),	//playername
    							URLDecoder.decode(parts[1],HGStatics.UTF8),	  //locationname
    							URLDecoder.decode(parts[2],HGStatics.UTF8),	  //worldname
    							Integer.parseInt(URLDecoder.decode(parts[3],HGStatics.UTF8)),	  //x
    							Integer.parseInt(URLDecoder.decode(parts[4],HGStatics.UTF8)),	  //y
    							Integer.parseInt(URLDecoder.decode(parts[5],HGStatics.UTF8)),	  //z
    							Float.parseFloat(URLDecoder.decode(parts[6],HGStatics.UTF8)),	  //pitch
    							Float.parseFloat(URLDecoder.decode(parts[7],HGStatics.UTF8)),	  //yaw
    							Long.parseLong(URLDecoder.decode(parts[8],HGStatics.UTF8)));
    					locations.put(parts[0]+parts[1],location);
    				}
    			}

    		}
    	}
    	return locations;
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
