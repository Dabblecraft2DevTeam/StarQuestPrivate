
package net.countercraft.movecraft.cardboardbox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.countercraft.movecraft.cardboardbox.meta.CardboardItemMeta;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaBook;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaBook2;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaEnchantment;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaFirework;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaFireworkEffect;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaLeatherArmor;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaMap;
import net.countercraft.movecraft.cardboardbox.meta.CardboardMetaSkull;
import net.countercraft.movecraft.cardboardbox.utils.CardboardEnchantment;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A serializable ItemStack
 */
public class CardboardBox implements Serializable {

	private static final long serialVersionUID = 729890133797629668L;

	private final int type, amount;
	private final short damage;
	private String name;
	private List<String> lore;

	private final HashMap<CardboardEnchantment, Integer> enchants;
	private CardboardItemMeta meta;

	public CardboardBox(ItemStack item) {

		this.type = item.getTypeId();
		this.amount = item.getAmount();
		this.damage = item.getDurability();

		if (item.hasItemMeta()) {
			ItemMeta m = item.getItemMeta();
			System.out.println("has item meta: " + item.getType());
			if (m.hasDisplayName()) {
				this.name = m.getDisplayName();
			}

			if (m.hasLore()) {
				this.lore = m.getLore();
			}

		}

		HashMap<CardboardEnchantment, Integer> map = new HashMap<CardboardEnchantment, Integer>();

		Map<Enchantment, Integer> enchantments = item.getEnchantments();

		for (Enchantment enchantment : enchantments.keySet()) {
			map.put(new CardboardEnchantment(enchantment), enchantments.get(enchantment));
		}

		this.enchants = map;

		// I call the below switch after the regular item parsing because
		// enchantment books register as having enchantments

		// IDs of items that have meta. Future items will need to be added here
		switch (item.getTypeId()) {
			case 386: // Book and quill
			case 387: // Written Book
				System.out.println("Creating new cardboard meta book.");
				this.meta = (new CardboardMetaBook2(item));
				break;
			case 403: // Enchanted Book
				this.meta = (new CardboardMetaEnchantment(item));
				break;
			case 402: // firework star
				if (!item.hasItemMeta())
					break;
				this.meta = (new CardboardMetaFireworkEffect(item));
				break;
			case 401: // firework
				this.meta = (new CardboardMetaFirework(item));
				break;
			case 298: // Leather armor
			case 299:
			case 300:
			case 301:
				this.meta = (new CardboardMetaLeatherArmor(item));
				break;
			case 358: // map
				this.meta = (new CardboardMetaMap(item));
				break;
			case 397: // player head
				this.meta = (new CardboardMetaSkull(item));
				break;
			default:
				this.meta = null;
				break;

		}

	}

	public ItemStack unbox() {

		ItemStack item = new ItemStack(type, amount, damage);

		// These metas below will never be null because of the if/else during
		// the packing

		ItemFactory factory = Bukkit.getServer().getItemFactory();
		ItemMeta itemMeta = factory.getItemMeta(Material.getMaterial(item.getTypeId()));

		// Should only have one specific item meta at a time
		if ((this.meta != null)) {
			itemMeta = this.meta.unbox();
		}
		if (this.name != null) {
			itemMeta.setDisplayName(this.name);
		}
		if (this.lore != null) {
			itemMeta.setLore(this.lore);
		}
		// Apply item meta
		item.setItemMeta(itemMeta);

		HashMap<Enchantment, Integer> map = new HashMap<Enchantment, Integer>();

		for (CardboardEnchantment cEnchantment : enchants.keySet()) {
			map.put(cEnchantment.unbox(), enchants.get(cEnchantment));
		}

		item.addUnsafeEnchantments(map);

		return item;
	}

}