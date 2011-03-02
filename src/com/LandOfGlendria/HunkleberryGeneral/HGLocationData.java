package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class HGLocationData {
	
	protected String playerName;
	protected String locationName;
	protected String worldName;
	protected int x;
	protected int y;
	protected int z;
	protected float pitch;
	protected float yaw;
	protected long lastAccess;
	
    private static final Map<String, HGLocationData> lookupName = new HashMap<String, HGLocationData>();
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger("Minecraft");


    HGLocationData(
    		String ownerName,
    		String locationName,
    		String worldName,
    		int x,
    		int y,
    		int z,
    		float pitch,
    		float yaw,
    		long lastAccess){
    	this.playerName = ownerName;
    	this.locationName = locationName;
    	this.worldName = worldName;
    	this.x = x;
    	this.y = y;
    	this.z = z;
    	this.pitch = pitch;
    	this.yaw = yaw;
    	this.lastAccess = lastAccess;
    }

    
    public static HGLocationData put(final String name,final HGLocationData location) {
        return lookupName.put(name,location);
    }
    
    public static HGLocationData remove(final String name) {
        return lookupName.remove(name);
    }
    
    public static HGLocationData getLocationDataByName(final String name) {
        HGLocationData result = lookupName.get(name);
        if (result != null) {
        	result.lastAccess = System.currentTimeMillis();
        }
        return result;
    }
    
    public static int getLocationsCountByOwnerName(final String name) {
    	int count = 0;
    	for (HGLocationData location : lookupName.values()) {
    		if(location.playerName == name) {
    			count++;
    		}
    	}
        return count;
    }
    
    public static HashSet<HGLocationData> getLocationsByOwnerName(final String name) {
    	HashSet<HGLocationData> ownerLocations = new HashSet<HGLocationData>();
    	for (HGLocationData location : lookupName.values()) {
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
