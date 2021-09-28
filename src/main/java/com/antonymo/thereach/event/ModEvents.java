package com.antonymo.thereach.event;

import com.antonymo.thereach.TheReach;
import com.antonymo.thereach.dimension.ModDimensions;
import com.antonymo.thereach.dimension.TheReachTeleporter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {
    @SubscribeEvent
    public void onNoYouAreNotGonnaDie(LivingDamageEvent event)
    {
        LivingEntity target = event.getEntityLiving();

        if (!(target instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) target;
        if (player.world.isRemote()) {
            return;
        }

        MinecraftServer server = player.world.getServer();
        if (server == null) {
            return;
        }

        TheReach.LOGGER.debug(target.getHealth() + "/" + target.getMaxHealth() +
                " - " + event.getAmount() + " -> " + (target.getHealth() - event.getAmount()));

        if (event.getAmount() < target.getHealth())
            return;

        event.setAmount(0);
        player.heal(player.getMaxHealth());

        if (player.world.getDimensionKey() == ModDimensions.TheReachDim) {
            ServerWorld overWorld = server.getWorld(World.OVERWORLD);
            if (overWorld != null) {
                player.changeDimension(overWorld, new TheReachTeleporter(player.getPosition(), false));
            }
        } else {
            ServerWorld theReachDim = server.getWorld(ModDimensions.TheReachDim);
            if (theReachDim != null) {
                player.changeDimension(theReachDim, new TheReachTeleporter(player.getPosition(), true));
            }
        }
    }
}
