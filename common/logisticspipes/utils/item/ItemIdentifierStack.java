/** 
 * Copyright (c) Krapht, 2011
 * 
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package logisticspipes.utils.item;

import java.util.Comparator;
import java.util.LinkedList;

import logisticspipes.logisticspipes.IRoutedItem;
import logisticspipes.pipes.basic.CoreRoutedPipe.ItemSendMode;
import logisticspipes.utils.tuples.Triplet;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public final class ItemIdentifierStack implements Comparable<ItemIdentifierStack>{
	public static class orderedComparitor implements Comparator<ItemIdentifierStack>{
		@Override
		public int compare(ItemIdentifierStack o1, ItemIdentifierStack o2) {
			int c=o1._item.getItemID() - o2._item.getItemID();
			if(c!=0) return c;
			c=o1._item.itemDamage-o2._item.itemDamage;
			if(c!=0) return c;
			c=o1._item.uniqueID-o2._item.uniqueID;
			if(c!=0) return c;

			return o2.getStackSize()-o1.getStackSize();
		}		
	}
	public static class itemComparitor implements Comparator<ItemIdentifierStack>{

		@Override
		public int compare(ItemIdentifierStack o1, ItemIdentifierStack o2) {
			int c = o1._item.getItemID() - o2._item.getItemID();
			if(c != 0) return c;
			c = o1._item.itemDamage - o2._item.itemDamage;
			if(c != 0) return c;
			return o1._item.uniqueID - o2._item.uniqueID;
		}
		
	}
	public static class simpleItemComparitor implements Comparator<ItemIdentifierStack>{

		@Override
		public int compare(ItemIdentifierStack o1, ItemIdentifierStack o2) {
			int c = o1._item.getItemID() - o2._item.getItemID();
			if(c != 0) return c;
			return o1._item.itemDamage - o2._item.itemDamage;
		}
		
	}
	private final ItemIdentifier _item;
	private int stackSize;
	
	public static ItemIdentifierStack getFromStack(ItemStack stack){
		return new ItemIdentifierStack(ItemIdentifier.get(stack), stack.stackSize);
	}
	
	public ItemIdentifierStack(ItemIdentifier item, int stackSize){
		_item = item;
		this.setStackSize(stackSize);
	}
	
	public ItemIdentifier getItem(){
		return _item;
	}

	/**
	 * @return the stackSize
	 */
	public int getStackSize() {
		return stackSize;
	}

	/**
	 * @param stackSize the stackSize to set
	 */
	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

	public void lowerStackSize(int stackSize) {
		this.stackSize -= stackSize;
	}

	public ItemStack unsafeMakeNormalStack(){
		return _item.unsafeMakeNormalStack(stackSize);
	}

	public ItemStack makeNormalStack(){
		return _item.makeNormalStack(stackSize);
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof ItemIdentifierStack) {
			ItemIdentifierStack stack = (ItemIdentifierStack)that;
			return stack._item.equals(this._item) && stack.getStackSize() == this.getStackSize();
		}
		if ((that instanceof ItemIdentifier))
			throw new IllegalStateException("Comparison between ItemIdentifierStack and ItemIdentifier -- did you forget a .getItem() in your code?");

		return false;
	}
	
	@Override
	public int hashCode() {
		return _item.hashCode() ^ (1023 * this.getStackSize());
	}

	@Override
	public String toString() {
		return new StringBuilder(Integer.toString(getStackSize())).append("x ").append(_item.toString()).toString();
	}
	
	@Override
	public ItemIdentifierStack clone() {
		return new ItemIdentifierStack(_item, getStackSize());
	}
	
	public String getFriendlyName() {
		return getStackSize() + " " + _item.getFriendlyName();
	}
	
	public static LinkedList<ItemIdentifierStack> getListFromInventory(IInventory inv) {
		return getListFromInventory(inv, false);
	}
		
	public static LinkedList<ItemIdentifierStack> getListFromInventory(IInventory inv, boolean removeNull) {
		LinkedList<ItemIdentifierStack> list = new LinkedList<ItemIdentifierStack>();
		for(int i=0;i<inv.getSizeInventory();i++) {
			if(inv.getStackInSlot(i) == null) {
				if(!removeNull) {
					list.add(null);
				}
			} else {
				list.add(ItemIdentifierStack.getFromStack(inv.getStackInSlot(i)));
			}
		}
		return list;
	}

	public static LinkedList<ItemIdentifierStack> getListSendQueue(LinkedList<Triplet<IRoutedItem, ForgeDirection, ItemSendMode>> _sendQueue) {
		LinkedList<ItemIdentifierStack> list = new LinkedList<ItemIdentifierStack>();
		for(Triplet<IRoutedItem, ForgeDirection, ItemSendMode> part:_sendQueue) {
			if(part == null) {
				list.add(null);
			} else {
				boolean added = false;
				for(ItemIdentifierStack stack:list) {
					if(stack.getItem().equals(part.getValue1().getItemIdentifierStack().getItem())) {
						stack.setStackSize(stack.getStackSize() + part.getValue1().getItemIdentifierStack().stackSize);
						added = true;
						break;
					}
				}
				if(!added) {
					list.add(part.getValue1().getItemIdentifierStack().clone());
				}
			}
		}
		return list;
	}

	@Override
	public int compareTo(ItemIdentifierStack o) {
		int c= _item.compareTo(o._item);
		if(c==0)
			return getStackSize()-o.getStackSize();
		return c;
	}
}
