package gvlfm78.plugin.OldCombatMechanics.module;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import com.codingforcookies.armourequip.ArmourEquipEvent;
import com.comphenix.example.Attributes;

import gvlfm78.plugin.OldCombatMechanics.OCMMain;
import gvlfm78.plugin.OldCombatMechanics.utilities.ArmourValues;
import gvlfm78.plugin.OldCombatMechanics.utilities.reflection.ItemData;

public class ModuleOldArmourStrength extends Module {

	private OCMMain plugin;
    public ModuleOldArmourStrength(OCMMain plugin) {
        super(plugin, "old-armour-strength");
        this.plugin = plugin;
    }

    @EventHandler
    public void onArmourEquip(ArmourEquipEvent e) {

        debug("Test999", e.getPlayer());
        ItemStack newPiece = e.getNewArmourPiece();

        if (newPiece != null && newPiece.getType() != Material.AIR) {
            Player p = e.getPlayer();
            debug("Test123", p);

            e.setNewArmourPiece(apply(newPiece));

        }
    }

    private ItemStack apply(ItemStack is) {

        if (ItemData.hasMark(is, "ArmorModifier")) {
            return is;
        }

        String slot = "";
        String type = is.getType().toString().toLowerCase();
        if(type.contains("helmet"))
        	slot = "head";
        else if(type.contains("chestplate"))
        	slot = "chest";
        else if(type.contains("leggings"))
        	slot = "legs";
        else if(type.contains("boots"))
        	slot = "feet";
        
        FileConfiguration config = plugin.getConfig();
        Double toughness = config.getDouble("old-armour-strength.toughness");
        
        ArmourValues av = new ArmourValues(plugin);
        double strength = av.getValue(is.getType());
        
        Attributes attributes = new Attributes(is);

        attributes.add(Attributes.Attribute.newBuilder().name("Armor").type(Attributes.AttributeType.GENERIC_ARMOR_TOUGHNESS).amount(toughness).slot(slot).build());
        attributes.add(Attributes.Attribute.newBuilder().name("Armor").type(Attributes.AttributeType.GENERIC_ARMOR).amount(strength).slot(slot).build());

        ItemData.mark(is, "ArmorModifier");

        return is;

    }

}