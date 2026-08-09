package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;

public class BlockQuestTarget extends QuestTarget{
    private final Block target;

    public static final Codec<BlockQuestTarget> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            // Up to 16 fields can be declared here
            Block.CODEC.fieldOf("target").forGetter(BlockQuestTarget::get_target)
    ).apply(instance, BlockQuestTarget::new));

    public BlockQuestTarget (Block target) {
        this.target = target;
    }

    @Override
    Object statTarget() {
        return target;
    }

    @Override
    String name() {
        return target.getName().toString();
    }

    public Block get_target () {
        return target;
    }
}
