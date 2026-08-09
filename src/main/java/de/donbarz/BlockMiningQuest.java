package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import static de.donbarz.PlayerQuests.getPlayerStat;

public class BlockMiningQuest extends Quest {

    BlockQuestTarget questTarget;
    int amountGoal;
    int progress;

    public static final MapCodec<BlockMiningQuest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            // Up to 16 fields can be declared here
            BlockQuestTarget.CODEC.fieldOf("quest_target").forGetter(BlockMiningQuest::get_quest_target),
            Codec.INT.fieldOf("amount_goal").forGetter(BlockMiningQuest::get_amount_goal),
            Codec.INT.fieldOf("progress").forGetter(BlockMiningQuest::get_progress)
    ).apply(instance, BlockMiningQuest::new));

    public BlockMiningQuest(BlockQuestTarget target, int amount, int progress) {
        this.questTarget = target;
        this.amountGoal = amount;
        this.progress = progress;
    }

    public BlockMiningQuest(Block target, int amount, ServerPlayer player) {
        this.questTarget = new BlockQuestTarget(target);
        this.amountGoal = amount + getPlayerStat(target, player);
        this.progress = getPlayerStat(target, player);
    }

    public BlockQuestTarget get_quest_target () {
        return questTarget;
    }

    public int get_progress () {
        return progress;
    }

    public int get_amount_goal () {
        return amountGoal;
    }

    private String blockMiningQuestString;

    @Override
    public QuestType<?> getType() {
        return QuestTypes.BLOCK_MINING_QUEST;
    }

    public Component questText = Component.literal("Mine");
}
