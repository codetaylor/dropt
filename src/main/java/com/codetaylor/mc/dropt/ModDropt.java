package com.codetaylor.mc.dropt;

import com.codetaylor.mc.athenaeum.module.ModuleManager;
import com.codetaylor.mc.dropt.api.DroptAPI;
import com.codetaylor.mc.dropt.api.api.IDroptDropBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptRuleBuilder;
import com.codetaylor.mc.dropt.modules.dropt.ModuleDropt;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(
    modid = ModDropt.MOD_ID,
    version = ModDropt.VERSION,
    name = ModDropt.NAME
    //@@DEPENDENCIES@@
)
public class ModDropt {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final String VERSION = Reference.VERSION;
  public static final String NAME = Reference.NAME;

  @Mod.Instance
  @SuppressWarnings("unused")
  public static ModDropt INSTANCE;

  private final ModuleManager moduleManager;

  public ModDropt() {

    this.moduleManager = new ModuleManager(MOD_ID);
    this.moduleManager.registerModules(
        ModuleDropt.class
    );
  }

  @Mod.EventHandler
  public void onConstructionEvent(FMLConstructionEvent event) {

    this.moduleManager.onConstructionEvent();
    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onInitializationEvent(FMLInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onLoadCompleteEvent(FMLLoadCompleteEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerAboutToStartEvent(FMLServerAboutToStartEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartingEvent(FMLServerStartingEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartedEvent(FMLServerStartedEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppingEvent(FMLServerStoppingEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppedEvent(FMLServerStoppedEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

}
