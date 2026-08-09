package de.donbarz;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class QuestTypes {
    public static final QuestType<BlockMiningQuest> BLOCK_MINING_QUEST = register("block_mining_quest", new QuestType<>(BlockMiningQuest.CODEC));
    public static final QuestType<MobKillingQuest> MOB_KILLING_QUEST = register("mob_killing_quest", new QuestType<>(MobKillingQuest.CODEC));

    public static <T extends Quest> QuestType<T> register(String id, QuestType<T> questType) {
        return Registry.register(QuestType.REGISTRY, Identifier.fromNamespaceAndPath(SnacksQuestMod.MOD_ID, id), questType);
    }
}