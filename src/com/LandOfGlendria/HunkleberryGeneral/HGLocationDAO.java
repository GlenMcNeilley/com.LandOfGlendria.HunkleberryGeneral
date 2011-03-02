package com.LandOfGlendria.HunkleberryGeneral;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

public class HGLocationDAO {
	
	//the static result set
    private static Map<String, HGLocation> lookupName = new HashMap<String, HGLocation>();
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger("Minecraft");
	private HGWorldlyThings worldly;

    public HGLocationDAO(HGWorldlyThings worldly) {
    	this.worldly = worldly;
    	try {
			//log.info("looking for loc.");
			lookupName = worldly.getLocationList("loc.");
			//log.info("looking for loc. found " + lookupName.size());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    public HGLocation put(final String name,final HGLocation location) {
        return lookupName.put(name,location);
    }
    
    public HGLocation remove(final String name) {
        return lookupName.remove(name);
    }
    
    public HGLocation getLocationDataByName(final String name) {
        HGLocation result = lookupName.get(name);
        if (result != null) {
        	result.lastAccess = System.currentTimeMillis();
        }
        return result;
    }
    
    public static int getLocationsCountByOwnerName(final String name) {
    	int count = 0;
    	for (HGLocation location : lookupName.values()) {
    		if(location.playerName == name) {
    			count++;
    		}
    	}
        return count;
    }
    
    public HashSet<HGLocation> getLocationsByOwnerName(final String name) {
    	HashSet<HGLocation> ownerLocations = new HashSet<HGLocation>();
    	for (HGLocation location : lookupName.values()) {
    		//log.info("looking for name " + name + " found name " + location.playerName + ".");
    		if(location.playerName.equals(name)) {
    			ownerLocations.add(location);
    			location.lastAccess = System.currentTimeMillis();
    		}
    	}
    	return ownerLocations;
    }

    public String saveLocationDataByOwner(final HashSet<HGLocation> ownerLocations) {
    	//add keys,values to worldly, properties, save properties
    	try {
    		for (HGLocation location : ownerLocations) {
    			String key = new String(URLEncoder.encode("loc." + location.playerName + location.locationName,HGStatics.UTF8));
    			String value;

    			value = URLEncoder.encode(location.playerName, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(location.locationName, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(location.worldName, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(""+location.x, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(""+location.y, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(""+location.z, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(""+location.pitch, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(""+location.yaw, HGStatics.UTF8) + ","
    			+ URLEncoder.encode(""+location.lastAccess, HGStatics.UTF8);
    			worldly.setWorldlyProperty(key,value);
    			worldly.saveConfigFileProperties();
    		}
    	} catch (UnsupportedEncodingException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}	

    	return null;
    }

    public static void reloadLookup() {
    	lookupName.clear();
    	//from the properties accessor
    	//for (HGCommandData command : values()) {
    	//    lookupName.put(command.getCommand(), command);
        //}
    }
}
