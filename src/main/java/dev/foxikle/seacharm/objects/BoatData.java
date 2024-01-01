package dev.foxikle.seacharm.objects;

import org.bukkit.block.data.BlockData;
import org.joml.Matrix4f;

public record BoatData(Matrix4f transformations, BlockData blockData) {
}
