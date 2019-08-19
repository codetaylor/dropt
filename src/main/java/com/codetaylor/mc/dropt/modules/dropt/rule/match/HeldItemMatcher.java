package com.codetaylor.mc.dropt.modules.dropt.rule.match;

import com.codetaylor.mc.dropt.api.reference.EnumListType;
import com.codetaylor.mc.dropt.modules.dropt.rule.data.RuleMatchHarvesterHeldItem;
import com.codetaylor.mc.dropt.modules.dropt.rule.log.DebugFileWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Set;

public class HeldItemMatcher {

  public boolean matches(
      RuleMatchHarvesterHeldItem ruleMatchHarvesterHeldItem,
      ItemStack heldItemStack,
      IBlockState blockState,
      EntityPlayer harvester,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    Item item = heldItemStack.getItem();
    String toolClass = ruleMatchHarvesterHeldItem._toolClass;

    if (toolClass != null) {
      int harvestLevel = item.getHarvestLevel(heldItemStack, toolClass, harvester, blockState);

      // WHITELIST
      if (ruleMatchHarvesterHeldItem.type == EnumListType.WHITELIST) {
        Set<String> toolClasses = item.getToolClasses(heldItemStack);

        if (!toolClasses.contains(toolClass)) {

          if (debug) {
            logFile.debug("[MATCH] [!!] Held item doesn't have required tool class: " + toolClass);
          }
          return false;
        }

        if (debug) {
          logFile.debug("[MATCH] [OK] Held item has required tool class: " + toolClass);
        }

        if (harvestLevel < ruleMatchHarvesterHeldItem._minHarvestLevel
            || harvestLevel > ruleMatchHarvesterHeldItem._maxHarvestLevel) {

          if (debug) {
            logFile.debug("[MATCH] [!!] Harvest tool outside of level range");
          }
          return false;
        }

        if (debug) {
          logFile.debug("[MATCH] [OK] Harvest tool inside of level range");
        }

      } else { // BLACKLIST
        Set<String> toolClasses = item.getToolClasses(heldItemStack);

        boolean harvestLevelInRange = harvestLevel >= ruleMatchHarvesterHeldItem._minHarvestLevel
            && harvestLevel <= ruleMatchHarvesterHeldItem._maxHarvestLevel;
        boolean toolClassMatches = toolClasses.contains(toolClass);

        if (toolClassMatches && harvestLevelInRange) {

          if (debug) {
            logFile.debug("[MATCH] [!!] Held item is excluded: " + toolClass + ";" + harvestLevel);
          }
          return false;
        }

        if (debug) {
          logFile.debug("[MATCH] [OK] Held item isn't excluded: " + toolClass + ";" + harvestLevel);
        }
      }
    }

    return this.checkItemList(ruleMatchHarvesterHeldItem, heldItemStack, logFile, debug);
  }

  private boolean checkItemList(
      RuleMatchHarvesterHeldItem ruleMatchHarvesterHeldItem,
      ItemStack heldItemStack,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    if (ruleMatchHarvesterHeldItem._items.isEmpty()) {

      if (debug) {
        logFile.debug("[MATCH] [OK] No item entries to match");
      }
      return true;
    }

    if (ruleMatchHarvesterHeldItem.type == EnumListType.WHITELIST) {
      return this.checkAsWhitelist(ruleMatchHarvesterHeldItem, heldItemStack, logFile, debug);

    } else {
      return this.checkAsBlacklist(ruleMatchHarvesterHeldItem, heldItemStack, logFile, debug);
    }
  }

  private boolean checkAsWhitelist(
      RuleMatchHarvesterHeldItem ruleMatchHarvesterHeldItem,
      ItemStack heldItemStack,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    Item heldItem = heldItemStack.getItem();
    int metadata = heldItemStack.getMetadata();

    for (ItemStack itemStack : ruleMatchHarvesterHeldItem._items) {

      if (itemStack.getItem() != heldItem) {

        if (debug) {
          logFile.debug(String.format(
              "[MATCH] [!!] Held item mismatch: (match) %s != %s (candidate)",
              itemStack.getItem(),
              heldItem
          ));
        }
        continue;

      } else if (debug) {
        logFile.debug(String.format(
            "[MATCH] [OK] Held item match: (match) %s == %s (candidate)",
            itemStack.getItem(),
            heldItem
        ));
      }

      if ((itemStack.getMetadata() == OreDictionary.WILDCARD_VALUE) || (itemStack.getMetadata() == metadata)) {

        if (debug) {
          logFile.debug(String.format(
              "[MATCH] [OK] Held item meta match: (match) %d == %d (candidate)",
              itemStack.getMetadata(),
              metadata
          ));
          logFile.debug("[MATCH] [OK] Found held item match in whitelist");
        }

      } else if (debug) {
        logFile.debug(String.format(
            "[MATCH] [!!] Held item meta mismatch: (match) %d != %d (candidate)",
            itemStack.getMetadata(),
            metadata
        ));
        continue;
      }

      if (itemStack.getTagCompound() != null) {

        if (heldItemStack.getTagCompound() == null) {

          if (debug) {
            logFile.debug(String.format(
                "[MATCH] [!!] Held item tag mismatch: (match) %s != %s (candidate)",
                itemStack.getTagCompound(),
                heldItemStack.getTagCompound()
            ));
          }
          continue;
        }

        boolean result = itemStack.getTagCompound().equals(heldItemStack.getTagCompound());

        if (debug) {

          if (result) {
            logFile.debug("[MATCH] [OK] Item NBT matches: " + itemStack.getTagCompound());

          } else {
            logFile.debug(String.format(
                "[MATCH] [!!] Held item tag mismatch: (match) %s != %s (candidate)",
                itemStack.getTagCompound(),
                heldItemStack.getTagCompound()
            ));
          }
        }

        return result;

      } else if (debug) {
        logFile.debug("[MATCH] [OK] Match has no tag");
        return true;
      }
    }

    if (debug) {
      logFile.debug("[MATCH] [!!] Unable to find heldItemMainHand match in whitelist");
    }
    return false;
  }

  private boolean checkAsBlacklist(
      RuleMatchHarvesterHeldItem ruleMatchHarvesterHeldItem,
      ItemStack heldItemStack,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    Item heldItem = heldItemStack.getItem();
    int metadata = heldItemStack.getMetadata();

    for (ItemStack itemStack : ruleMatchHarvesterHeldItem._items) {

      if (itemStack.getItem() != heldItem) {

        if (debug) {
          logFile.debug(String.format(
              "[MATCH] [OK] Held item item mismatch: (match) %s != %s (candidate)",
              itemStack.getItem(),
              heldItem
          ));
        }
        continue;

      } else if (debug) {
        logFile.debug(String.format(
            "[MATCH] [!!] Held item item match: (match) %s == %s (candidate)",
            itemStack.getItem(),
            heldItem
        ));
      }

      if ((itemStack.getMetadata() == OreDictionary.WILDCARD_VALUE) || (itemStack.getMetadata() == metadata)) {

        if (debug) {
          logFile.debug(String.format(
              "[MATCH] [!!] Held item meta match: (match) %d == %d (candidate)",
              itemStack.getMetadata(),
              metadata
          ));
          logFile.debug("[MATCH] [!!] Found heldItemMainHand match in blacklist");
        }
        continue;

      } else if (debug) {
        logFile.debug(String.format(
            "[MATCH] [OK] Held item meta mismatch: (match) %d != %d (candidate)",
            itemStack.getMetadata(),
            metadata
        ));
      }

      if (itemStack.getTagCompound() != null) {

        if (heldItemStack.getTagCompound() == null) {

          if (debug) {
            logFile.debug(String.format(
                "[MATCH] [OK] Held item tag mismatch: (match) %s != %s (candidate)",
                itemStack.getTagCompound(),
                heldItemStack.getTagCompound()
            ));
          }
          return true;
        }

        boolean result = itemStack.getTagCompound().equals(heldItemStack.getTagCompound());

        if (debug) {

          if (result) {
            logFile.debug("[MATCH] [!!] Item NBT matches: " + itemStack.getTagCompound());

          } else {
            logFile.debug(String.format(
                "[MATCH] [OK] Held item tag mismatch: (match) %s != %s (candidate)",
                itemStack.getTagCompound(),
                heldItemStack.getTagCompound()
            ));
          }
        }

        return !result;

      } else if (debug) {
        logFile.debug("[MATCH] [OK] Match has no tag");
        return true;
      }
    }

    if (debug) {
      logFile.debug("[MATCH] [OK] Unable to find heldItemMainHand match in blacklist");
    }
    return true;
  }
}
