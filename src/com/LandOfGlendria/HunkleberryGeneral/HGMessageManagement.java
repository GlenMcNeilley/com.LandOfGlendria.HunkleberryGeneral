package com.LandOfGlendria.HunkleberryGeneral;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HGMessageManagement {

	private static final Logger log = Logger.getLogger("Minecraft");

	public HGMessageManagement() {
	}

	public String[] messageSegmenter(String message, int notUSed) {
		char chars[] = message.toCharArray();
		int defaultWidth = HGStatics.MAX_MESSAGE_LENGTH.intValue();
		int minLength = Math.abs(defaultWidth / 2);
		int lastChar = chars.length - 1;
		int lookingAtChar = 0;
		int lastGood = 0;
		int visibleCount = 0;
		int startingAt = 0;
		int messageCount = 0;
		int lastPossible = 0;
		String messages[] = new String[lastChar / minLength + 1];
		String lastColor = new String();
		while (lookingAtChar < lastChar) {
			startingAt = lookingAtChar;
			visibleCount = 0;
			lastPossible = lookingAtChar;
			lastGood = lookingAtChar;
			StringBuffer currentMessage = new StringBuffer();
			currentMessage.append(lastColor);
			while (visibleCount < defaultWidth + 1 && lookingAtChar < lastChar + 1) {
				switch (chars[lookingAtChar]) {
				case 32: // ' '
					lastGood = lookingAtChar;
					visibleCount++;
					lookingAtChar++;
					break;

				case 44: // ','
					lastGood = lookingAtChar;
					visibleCount++;
					lookingAtChar++;
					break;

				case 45: // '-'
					lastGood = lookingAtChar;
					visibleCount++;
					lookingAtChar++;
					break;

				case 46: // '.'
					lastGood = lookingAtChar;
					visibleCount++;
					lookingAtChar++;
					break;

				case 167:
					lastColor = String.copyValueOf(chars, lookingAtChar, 2);
					lookingAtChar += 2;
					break;

				default:
					visibleCount++;
					lastPossible = lookingAtChar;
					lookingAtChar++;
					break;
				}
			}
			if (lookingAtChar >= lastChar) {
				lastGood = lastChar;
				lastPossible = lastChar;
			}
			if (visibleCount > minLength) {
				currentMessage.append(chars, startingAt, (lastGood - startingAt) + 1);
				lookingAtChar = lastGood + 1;
			} else {
				currentMessage.append(chars, startingAt, (lastPossible - startingAt) + 1);
				lookingAtChar = lastPossible + 1;
			}
			messages[messageCount] = currentMessage.toString();
			messageCount++;
		}
		return messages;
	}

	public void sendSegmented(Player player, String message) {
		if (message.length() <= HGStatics.MAX_MESSAGE_LENGTH.intValue()) {
			player.sendMessage(message);
		} else {
			String lines[] = messageSegmenter(message, 0);
			String as[];
			int j = (as = lines).length;
			for (int i = 0; i < j; i++) {
				String line = as[i];
				if (line != null) {
					player.sendMessage(line);
				}
			}

		}
	}

	public void sendNegativeMessage(Player player, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append(HGStatics.ERROR_COLOR);
		sb.append(message);
		sendSegmented(player, sb.toString());
	}

	public void sendPositiveMessage(Player player, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append(HGStatics.POSITIVE_COLOR);
		sb.append(message);
		sendSegmented(player, sb.toString());
	}

	public String formatInvalidArgs(String arg, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append("Invalid Argument [");
		sb.append(arg);
		sb.append("]: ");
		sb.append(message);
		sb.append(".");
		return sb.toString();
	}

	public String formatInvalidArgs(int arg, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append("Invalid Argument [");
		sb.append((new Integer(arg)).toString());
		sb.append("]: ");
		sb.append(message);
		sb.append(".");
		return sb.toString();
	}

	public void sendColorUsage(Player player, String command, String arguments) {
		StringBuffer sb = new StringBuffer();
		sb.append("Usage: ");
		sb.append(HGStatics.COMMAND_COLOR);
		sb.append("/");
		sb.append(command);
		sb.append(" ");
		sb.append(HGStatics.ARGUMENTS_COLOR);
		sb.append(arguments);
		sendSegmented(player, sb.toString());
	}

	public void sendColorErrorUsage(Player player, String error, String command, String arguments, String helpCommand) {
		sendSegmented(player, (new StringBuilder(String.valueOf(HGStatics.ERROR_COLOR))).append(error).toString());
		StringBuffer sb = new StringBuffer();
		sb.append("Usage: ");
		sb.append(HGStatics.COMMAND_COLOR);
		sb.append("/");
		sb.append(command);
		sb.append(" ");
		sb.append(HGStatics.ARGUMENTS_COLOR);
		sb.append(arguments);
		sb.append(HGStatics.NO_COLOR);
		sb.append(" See ");
		sb.append(HGStatics.WARNING_COLOR);
		sb.append("/");
		sb.append(helpCommand);
		sb.append(" ");
		sb.append(command);
		sb.append(HGStatics.NO_COLOR);
		sb.append(" for more information.");
		sendSegmented(player, sb.toString());
	}

	public void sendCommandList(Player player,HGCommandData cmd) {
		StringBuffer sb = new StringBuffer();
		for (HGCommandData command : HGCommandData.values()) {
			sb.append(HGStatics.POSITIVE_COLOR);
			sb.append("[");
			sb.append(HGStatics.WARNING_COLOR);
			sb.append("/");
			sb.append(command.getCommand());
			sb.append(HGStatics.POSITIVE_COLOR);
			sb.append("] ");
		}
		sendPositiveMessage(player,sb.toString());
		return;
	}
	public void sendColorHelp(Player player, HGCommandData command, boolean allowed) {
		StringBuffer sb = new StringBuffer();
		sb.append("Usage: ");
		sb.append(HGStatics.COMMAND_COLOR);
		sb.append("/");
		sb.append(command.getCommand());
		sb.append(" ");
		sb.append(HGStatics.ARGUMENTS_COLOR);
		sb.append(command.getCommandArgs());
		sb.append(" ");
		sb.append(HGStatics.NO_COLOR);
		sb.append(command.getCommandUsage());
		sb.append(" ");
		if (command.getServerAllowed().booleanValue()) {
			if (allowed) {
				sb.append(HGStatics.POSITIVE_COLOR);
				sb.append("This command is enabled and you have permission to use it.");
			} else {
				sb.append(HGStatics.ERROR_COLOR);
				sb.append("This command is enabled but you don't have permission to use it.");
			}
		} else {
			sb.append(HGStatics.ERROR_COLOR);
			sb.append(" This command is disabled.");
		}
		sendSegmented(player, sb.toString());
	}

	public String concatinateRemainingArgs(String stringArray[], int index) {
		int indexCount = 0;
		String message = new String("");
		String as[];
		int j = (as = stringArray).length;
		for (int i = 0; i < j; i++) {
			String cat = as[i];
			if (indexCount > index) {
				message = message.concat(" ");
			}
			if (indexCount >= index) {
				message = message.concat(cat);
			}
			indexCount++;
		}

		return message;
	}

	public String sendColorList(Player player) {
		StringBuffer sb = new StringBuffer();
		int indexCount = 0;	
		for (ChatColor color : ChatColor.values()) {
			sb.append(color);
			sb.append("[");
			sb.append(indexCount);
			sb.append("] ");
			indexCount++;
		}
		sendPositiveMessage(player,sb.toString());
		return null;
	}

	public void info(String message) {
		log.info((new StringBuilder(String.valueOf(HGStatics.LOG_PREFIX))).append(message).toString());
	}

	public void warn(String message) {
		log.info((new StringBuilder(String.valueOf(HGStatics.LOG_PREFIX))).append(message).toString());
	}

	public void severe(String message) {
		log.info((new StringBuilder(String.valueOf(HGStatics.LOG_PREFIX))).append(message).toString());
	}

}
