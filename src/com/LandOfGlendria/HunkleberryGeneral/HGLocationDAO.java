package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class HGLocationDAO {
	
	//the static result set
    private static final Map<String, HGLocation> lookupName = new HashMap<String, HGLocation>();
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger("Minecraft");


    HGLocationDAO() {
    }

    
    public static HGLocation put(final String name,final HGLocation location) {
        return lookupName.put(name,location);
    }
    
    public static HGLocation remove(final String name) {
        return lookupName.remove(name);
    }
    
    public static HGLocation getLocationDataByName(final String name) {
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
    
    public static HashSet<HGLocation> getLocationsByOwnerName(final String name) {
    	HashSet<HGLocation> ownerLocations = new HashSet<HGLocation>();
    	for (HGLocation location : lookupName.values()) {
    		if(location.playerName == name) {
    			ownerLocations.add(location);
            	location.lastAccess = System.currentTimeMillis();
    		}
    	}
        return ownerLocations;
    }
    
    public static void reloadLookup() {
    	lookupName.clear();
    	//from the properties accessor
        //for (HGCommandData command : values()) {
        //    lookupName.put(command.getCommand(), command);
        //}
    }
}
