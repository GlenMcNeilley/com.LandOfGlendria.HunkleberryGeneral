//TODO: add material data info/standable blocks/itemData to null if not expressed
//TODO: add player target to inv commands
//TODO: format inv list

package com.LandOfGlendria.HunkleberryGeneral;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HGInventoryManagement {

	private final ItemStack emptyStack;
	private HGMessageManagement msg;

	public HGInventoryManagement(HGMessageManagement msg) {
		this.msg = msg;
		emptyStack = new ItemStack(Material.AIR, 0);
	}

	public void clearPlayerInventory(Player player) {
		ItemStack stacks[] = player.getInventory().getContents();
		int index = 0;
		ItemStack aitemstack[];
		int j = (aitemstack = stacks).length;
		for (int i = 0; i < j; i++) {
			ItemStack stack = aitemstack[i];
			if (stack.getAmount() > 0 && index > 8) {
				stacks[index].setType(Material.AIR);
				stacks[index].setAmount(0);
			}
			index++;
		}

		player.getInventory().setContents(stacks);
	}

	public void clearEntirePlayerInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setBoots(emptyStack);
		player.getInventory().setChestplate(emptyStack);
		player.getInventory().setHelmet(emptyStack);
		player.getInventory().setLeggings(emptyStack);
	}

	public void listPlayerInventory(Player player,Player resolvedPlayer) {
		ItemStack stacks[] = resolvedPlayer.getInventory().getContents();
		int index = 0;
		ItemStack aitemstack[];
		StringBuffer sb = new StringBuffer();
		int j = (aitemstack = stacks).length;
		for (int i = 0; i < j; i++) {
			ItemStack stack = aitemstack[i];
			if (stack.getAmount() > 0) {
				sb.append("[");
				sb.append(index+1);
				sb.append("][");
				sb.append(HGStatics.WARNING_COLOR);
				sb.append(stack.getType().toString());
				sb.append(HGStatics.NO_COLOR);
				sb.append("x");
				sb.append(HGStatics.ERROR_COLOR);
				sb.append(stack.getAmount());
				sb.append(HGStatics.POSITIVE_COLOR);
				sb.append("] ");
			}
			index++;
		}
		msg.info(sb.toString());
		msg.sendPositiveMessage(player,sb.toString());
	}

	public int getAvailableSpace(Player player, int item) {
		ItemStack inventoryStacks[] = player.getInventory().getContents();
		int available = 0;
		for (ItemStack inventoryStack : inventoryStacks) {
			if (inventoryStack == null) {
				available += 64;
			} else if (inventoryStack.getTypeId() == item) {
				available += 64 - inventoryStack.getAmount();
			}
		}

		return available;
	}

	public int addItemToInventory(Player player, int item, int amount) {
		return player.getInventory().addItem(new ItemStack[] { new ItemStack(item, amount) }).size();
	}

	public static int addItemToInventory(Player player, String item, int amount) {
		Material mat = Material.matchMaterial(item);
		if (mat != null) {
			return player.getInventory().addItem(new ItemStack[] { new ItemStack(mat, amount) }).size();
		}
		return -1;
	}

	public static int setItemInHand(Player player, String item, int amount) {
		Material mat = Material.matchMaterial(item);
		if (mat != null) {
			player.setItemInHand(new ItemStack(mat, amount));
		}
		return 1;
	}

	public int addItemToInventory(Player player, int item, int amount, byte data) {
		if (data == -1) {
			return addItemToInventory(player,item,amount);
		} else {
		return player.getInventory().addItem(new ItemStack[] { new ItemStack(item, amount, (short) 0, Byte.valueOf(data)) }).size();
		}
	}

	public int addItemToInventory(Player player, Material mat, int amount) {
		return player.getInventory().addItem(new ItemStack[] { new ItemStack(mat, amount) }).size();
	}

	public int addItemToInventory(Player player, Material mat, int amount, byte data) {
		return player.getInventory().addItem(new ItemStack[] { new ItemStack(mat, amount, (short) 0, Byte.valueOf(data)) }).size();
	}
}
