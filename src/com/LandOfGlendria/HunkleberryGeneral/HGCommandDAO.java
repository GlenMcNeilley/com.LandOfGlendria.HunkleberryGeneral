
package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HGCommandDAO {

	//the static result set
    private static final Map<String, HGCommand> lookupName = new HashMap<String, HGCommand>();
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger("Minecraft");

    public HGCommandDAO() {
    	for (HGCommand command : HGCommand.values()) {
    		lookupName.put(getCommand(command), command);
    	}
    }
	
	/**
	 * @return the resolved command string
	 */
	public String getCommand(HGCommand cmd) {
		if (!(cmd.commandAlias == null) && !cmd.commandAlias.isEmpty()) {
			return cmd.commandAlias;
		} else {
			return cmd.defaultCommand;
		}
	}

	/**
	 * @return the commandPermissions
	 */
	public String getPermissions(HGCommand cmd) {
		return cmd.permissions;
	}

	/**
	 * @param commandPermissions the commandPermissions to set
	 */
	public void setPermissions(HGCommand cmd,String permissions) {
		cmd.permissions = permissions;
	}

	/**
	 * @return the opsOnly
	 */
	public Boolean getOpsOnly(HGCommand cmd) {
		return cmd.opsOnly;
	}

	/**
	 * @param opsOnly the opsOnly to set
	 */
	public void setOpsOnly(HGCommand cmd,Boolean opsOnly) {
		cmd.opsOnly = opsOnly;
	}

	/**
	 * @return the commandAlias
	 */
	public String getCommandAlias(HGCommand cmd) {
		return cmd.commandAlias;
	}

	/**
	 * @param commandAlias the commandAlias to set
	 */
	public void setCommandAlias(HGCommand cmd,String commandAlias) {
		cmd.commandAlias = ((commandAlias == null) ? "" : commandAlias);
	}

	/**
	 * @return the serverAllowed
	 */
	public Boolean getServerAllowed(HGCommand cmd) {
		return cmd.serverAllowed;
	}

	/**
	 * @param serverAllowed the serverAllowed to set
	 */
	public void setServerAllowed(HGCommand cmd,Boolean serverAllowed) {
		cmd.serverAllowed = serverAllowed;
	}

	/**
	 * @return the defaultCommand
	 */
	public String getDefaultCommand(HGCommand cmd) {
		return cmd.defaultCommand;
	}

	/**
	 * @return the commandArgs
	 */
	public String getCommandArgs(HGCommand cmd) {
		return cmd.commandArgs;
	}

	/**
	 * @return the commandUsage
	 */
	public String getCommandUsage(HGCommand cmd) {
		return cmd.commandUsage;
	}
	
//	public String getPermissionsForProperty() {
//		return ((getPermissions() != null) ? getPermissions() : "");
//	}
//	
//	public String getOpsOnlyForProperty() {
//		return ((getOpsOnly() != null) ? getOpsOnly() : "").toString();
//	}
//	
//	public String getCommandAliasForProperty() {
//		return ((getCommandAlias() != null) ? getCommandAlias() : "");
//	}
//	
//	public String getServerAllowedForProperty() {
//		return ((getServerAllowed() != null) ? getServerAllowed() : "").toString();
//	}
    
    public static HGCommand getCommandDataByName(final String name) {
        return lookupName.get(name);
    }
    
    public void reloadLookup() {
    	lookupName.clear();
        for (HGCommand command : HGCommand.values()) {
            lookupName.put(getCommand(command), command);
        }
    }
    
}
