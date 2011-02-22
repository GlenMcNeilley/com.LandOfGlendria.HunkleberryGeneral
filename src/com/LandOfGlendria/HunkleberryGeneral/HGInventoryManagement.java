package com.LandOfGlendria.HunkleberryGeneral;

import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HGInventoryManagement
{

	private final ItemStack emptyStack;

	public HGInventoryManagement()
	{
		emptyStack = new ItemStack(Material.AIR, 0);
	}

	public void clearPlayerInventory(Player player)
	{
		ItemStack stacks[] = player.getInventory().getContents();
		int index = 0;
		ItemStack aitemstack[];
		int j = (aitemstack = stacks).length;
		for(int i = 0; i < j; i++)
		{
			ItemStack stack = aitemstack[i];
			if(stack.getAmount() > 0 && index > 8)
			{
				stacks[index].setType(Material.AIR);
				stacks[index].setAmount(0);
			}
			index++;
		}

		player.getInventory().setContents(stacks);
	}

	public void clearEntirePlayerInventory(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setBoots(emptyStack);
		player.getInventory().setChestplate(emptyStack);
		player.getInventory().setHelmet(emptyStack);
		player.getInventory().setLeggings(emptyStack);
	}

	public void listPlayerInventory(Player player)
	{
		ItemStack stacks[] = player.getInventory().getContents();
		int index = 0;
		ItemStack aitemstack[];
		int j = (aitemstack = stacks).length;
		for(int i = 0; i < j; i++)
		{
			ItemStack stack = aitemstack[i];
			if(stack.getAmount() > 0)
			{
				player.sendMessage((new StringBuilder(String.valueOf(index))).append(stack.toString()).toString());
			}
			index++;
		}

	}

	public int getAvailableSpace(Player player, int item, byte data)
	{
		CraftItemStack inventoryStacks[] = (CraftItemStack[])player.getInventory().getContents();
		CraftItemStack stackToAdd = new CraftItemStack(item, 1, (short)0, Byte.valueOf(data));
		int available = 0;
		int index = 0;
		CraftItemStack acraftitemstack[];
		int j = (acraftitemstack = inventoryStacks).length;
		for(int i = 0; i < j; i++)
		{
			CraftItemStack inventoryStack = acraftitemstack[i];
			byte inventoryData = 0;
			if(inventoryStack.getData() != null)
			{
				inventoryData = inventoryStack.getData().getData();
			}
			if(inventoryStack.getAmount() == 0 && inventoryStack.getType() == Material.AIR)
			{
				available += stackToAdd.getMaxStackSize();
			} else
			if(inventoryStack.getType() == stackToAdd.getType() && inventoryData == data)
			{
				available += inventoryStack.getMaxStackSize() - inventoryStack.getAmount();
			}
			index++;
		}

		return available;
	}

	public int addItemToInventory(Player player, int item, int amount)
	{
		return player.getInventory().addItem(new ItemStack[] {
			new ItemStack(item, amount)
		}).size();
	}

	public int addItemToInventory(Player player, int item, int amount, byte data)
	{
		return player.getInventory().addItem(new ItemStack[] {
			new ItemStack(item, amount, (short)0, Byte.valueOf(data))
		}).size();
	}

	public int addItemToInventory(Player player, Material mat, int amount)
	{
		return player.getInventory().addItem(new ItemStack[] {
			new ItemStack(mat, amount)
		}).size();
	}

	public int addItemToInventory(Player player, Material mat, int amount, byte data)
	{
		return player.getInventory().addItem(new ItemStack[] {
			new ItemStack(mat, amount, (short)0, Byte.valueOf(data))
		}).size();
	}
}
