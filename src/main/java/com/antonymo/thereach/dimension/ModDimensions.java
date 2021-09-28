package com.antonymo.thereach.dimension;

import com.antonymo.thereach.TheReach;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ModDimensions {
    public static RegistryKey<World> TheReachDim;

    public static void register() {
        TheReachDim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(TheReach.MOD_ID, "thereachdim"));
    }
}
