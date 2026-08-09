package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import static de.donbarz.PlayerQuests.getPlayerStat;

public class BlockMiningQuest extends Quest {
    private static final Component QUEST_TEXT = Component.literal("Mine ");

    public static final MapCodec<BlockMiningQuest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            // Up to 16 fields can be declared here
            BlockQuestTarget.CODEC.fieldOf("quest_target").forGetter(BlockMiningQuest::get_quest_target),
            Codec.INT.fieldOf("amount_goal").forGetter(BlockMiningQuest::get_amount_goal),
            Codec.INT.fieldOf("progress").forGetter(BlockMiningQuest::get_progress)
    ).apply(instance, BlockMiningQuest::new));

    public BlockMiningQuest(BlockQuestTarget target, int amount, int progress) {
        super(target, amount, progress, QUEST_TEXT);
    }

    public BlockMiningQuest(Block target, int amount, ServerPlayer player) {
        this(new BlockQuestTarget(target), amount + getPlayerStat(target, player), getPlayerStat(target, player));
    }

    public BlockQuestTarget get_quest_target () {
        return (BlockQuestTarget) this.target;
    }

    public int get_progress () {
        return this.progress;
    }

    public int get_amount_goal () {
        return this.amountGoal;
    }

    @Override
    public QuestType<?> getType() {
        return QuestTypes.BLOCK_MINING_QUEST;
    }
}
