package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

import static de.donbarz.PlayerQuests.getPlayerStat;

public class MobKillingQuest extends Quest {
    private static final Component QUEST_TEXT = Component.literal("Kill ");

    public static final MapCodec<MobKillingQuest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MobQuestTarget.CODEC.fieldOf("quest_target").forGetter(MobKillingQuest::get_quest_target),
            Codec.INT.fieldOf("amount_goal").forGetter(MobKillingQuest::get_amount_goal),
            Codec.INT.fieldOf("progress").forGetter(MobKillingQuest::get_progress)
    ).apply(instance, MobKillingQuest::new));

    public MobKillingQuest(MobQuestTarget target, int amount, int progress) {
        super(target, amount, progress, QUEST_TEXT);
    }

    public MobKillingQuest(EntityType<?> target, int amount, ServerPlayer player) {
        this(new MobQuestTarget(target), amount + getPlayerStat(target, player), getPlayerStat(target, player));
    }

    public MobQuestTarget get_quest_target () {
        return (MobQuestTarget) this.target;
    }

    public int get_progress () {
        return this.progress;
    }

    public int get_amount_goal () {
        return this.amountGoal;
    }

    @Override
    public QuestType<?> getType() {
        return QuestTypes.MOB_KILLING_QUEST;
    }


}
