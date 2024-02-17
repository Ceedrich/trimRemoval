package net.trimremoval;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Trimremoval implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("trimremoval");

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Trimremoval");
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (player.isSpectator() ||
					!player.isSneaking() ||
					!world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.GRINDSTONE)
			) return ActionResult.PASS;

			ItemStack item = hand == Hand.MAIN_HAND ? player.getMainHandStack() : player.getOffHandStack();
			NbtCompound nbt = item.getNbt();

			if (nbt == null || !nbt.contains("Trim")) return ActionResult.PASS;

			if (player.isCreative()) {
				ItemStack item2 = item.copy();
				assert item2.getNbt() != null;
				item2.getNbt().remove("Trim");
				player.giveItemStack(item2);
			} else {
				nbt.remove("Trim");
			}
			return ActionResult.SUCCESS;
		});
	}
}