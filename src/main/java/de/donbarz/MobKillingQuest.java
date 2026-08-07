package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

import static de.donbarz.PlayerQuests.getPlayerStat;

public class MobKillingQuest extends Quest {

    EntityType<?> questTarget;
    int amountGoal;
    int progress;

    public static final MapCodec<MobKillingQuest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntityType.CODEC.fieldOf("quest_target").forGetter(MobKillingQuest::get_quest_target),
            Codec.INT.fieldOf("progress").forGetter(MobKillingQuest::get_progress),
            Codec.INT.fieldOf("amount_goal").forGetter(MobKillingQuest::get_amount_goal)
    ).apply(instance, MobKillingQuest::new));

    public MobKillingQuest(EntityType<?> target, int amount, int progress) {
        this.questTarget = target;
        this.amountGoal = amount;
        this.progress = progress;
    }

    public MobKillingQuest(EntityType<?> target, int amount, ServerPlayer player) {
        this.questTarget = target;
        this.amountGoal = amount + getPlayerStat(target, player);
        this.progress = getPlayerStat(target, player);
    }

    public EntityType<?> get_quest_target () {
        return questTarget;
    }

    public int get_progress () {
        return progress;
    }

    public int get_amount_goal () {
        return amountGoal;
    }

    @Override
    public QuestType<?> getType() {
        return QuestTypes.MOB_KILLING_QUEST;
    }

    public Component questText = Component.literal("Kill");

}
