package com.antonymo.thereach.dimension;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class TheReachTeleporter implements ITeleporter {
    public static BlockPos thisPos = BlockPos.ZERO;
    public static boolean insideDimension = true;

    public TheReachTeleporter(BlockPos pos, boolean insideDim) {
        thisPos = pos;
        insideDimension = insideDim;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
//        return ITeleporter.super.placeEntity(entity, currentWorld, destWorld, yaw, repositionEntity);
        entity = repositionEntity.apply(false);

        double y = 61;

        if (!insideDimension) {
            y = thisPos.getY();
        }

        BlockPos destinationPos = new BlockPos(thisPos.getX(), y ,thisPos.getZ());

        int tries = 0;

        while (destWorld.getBlockState(destinationPos).getMaterial() != Material.AIR &&
                !destWorld.getBlockState(destinationPos).isReplaceable(Fluids.WATER) &&
                destWorld.getBlockState(destinationPos.up()).getMaterial() != Material.AIR &&
                !destWorld.getBlockState(destinationPos.up()).isReplaceable(Fluids.WATER) && tries < 25) {
            destinationPos = destinationPos.up();
            tries++;
        }

        entity.setPositionAndUpdate(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());

        if (insideDimension) {
            boolean doSetBlock = true;
            for (BlockPos checkPos : BlockPos.getAllInBoxMutable(destinationPos.down(5).west(5).north(5), destinationPos.up(5).east(5).south(5))) {
                if (destWorld.getBlockState(checkPos).getBlock().matchesBlock(Blocks.OBSIDIAN)) {
                    doSetBlock = false;
                }
            }

            if (doSetBlock) {
                destWorld.setBlockState(destinationPos, Blocks.OBSIDIAN.getDefaultState());
            }
        }

        return entity;
    }
}
