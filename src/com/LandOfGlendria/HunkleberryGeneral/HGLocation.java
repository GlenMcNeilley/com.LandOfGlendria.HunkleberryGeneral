package com.LandOfGlendria.HunkleberryGeneral;

public class HGLocation {
	protected String playerName;
	protected String locationName;
	protected String worldName;
	protected int x;
	protected int y;
	protected int z;
	protected float pitch;
	protected float yaw;
	protected long lastAccess;

    HGLocation(
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
}
