/** 
 * Copyright (c) Krapht, 2011
 * 
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package logisticspipes.routing;

import logisticspipes.interfaces.routing.IProvideItems;
import logisticspipes.utils.item.ItemIdentifier;

public class LogisticsPromise {
	public ItemIdentifier item;
	public int numberOfItems;
	public IProvideItems sender;

	public LogisticsPromise(ItemIdentifier item, int numberOfItems, IProvideItems sender) {
		this.item = item;
		this.numberOfItems = numberOfItems;
		this.sender = sender;
	}
	
	public LogisticsPromise copy() {
		return new LogisticsPromise(item, numberOfItems, sender);
	}
}
