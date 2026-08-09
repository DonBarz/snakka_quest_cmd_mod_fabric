package de.donbarz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

import static de.donbarz.SnacksQuestMod.*;

public class PlayerQuests {

    // saves a field of quests
    public static final Codec<PlayerQuests> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Quest.CODEC.listOf().fieldOf("active_quests").forGetter(PlayerQuests::getActiveQuests)
                    ).apply(instance, PlayerQuests::new));

    ArrayList<Quest> activeQuests;

    public PlayerQuests(List<Quest> quests) {
        activeQuests = new ArrayList<>(quests);
    }

    public ArrayList<Quest> getActiveQuests () {return this.activeQuests;}

    // runs every tick
    // checks if any quests are fulfilled
    public static void update (ServerPlayer player) {
        if (!player.hasAttached(PLAYER_QUEST_ATTACHMENT)) {player.setAttached(PLAYER_QUEST_ATTACHMENT, new PlayerQuests(new ArrayList<>()));}

        PlayerQuests curr = player.getAttachedOrThrow(PLAYER_QUEST_ATTACHMENT);

        List<Quest> finishedQuests = new ArrayList<>();

        for (Quest q : curr.activeQuests) {
            if (getPlayerStat(q.getTarget().statTarget(), player) >= q.getAmountGoal()) {
                player.sendSystemMessage(Component.literal("Finished a quest: ")
                        .append(q.getQuestText().copy())
                        .append(" " + (q.getAmountGoal() - q.getProgress()) + " * ")
                        .append(q.getTarget().name()));
                finishedQuests.add(q);
            }
        }

        for (Quest fQ : finishedQuests) {curr.activeQuests.remove(fQ);}

        player.setAttached(PLAYER_QUEST_ATTACHMENT, curr);
    }

    // quest progress is based off player statistics
    // returns value of the players statistic specified by the type and target
    public static int getPlayerStat (Object target, ServerPlayer player) {
        ServerStatsCounter statsCounter = player.getStats();

        if (target instanceof Block) {
            return statsCounter.getValue(Stats.BLOCK_MINED.get((Block) target)); // heureka
        } else if (target instanceof EntityType<?>) {
            return statsCounter.getValue(Stats.ENTITY_KILLED.get((EntityType<?>) target)); // heureka
        }

        return 0;
    }

    // shows the executing player progress on all their quests
    public static void viewQuestProgress (ServerPlayer player) {
        if (!player.hasAttached(PLAYER_QUEST_ATTACHMENT)) {player.setAttached(PLAYER_QUEST_ATTACHMENT, new PlayerQuests(new ArrayList<>()));}

        PlayerQuests curr = player.getAttachedOrThrow(PLAYER_QUEST_ATTACHMENT);

        player.sendSystemMessage(Component.literal("Active quests: "));

        for (Quest q : curr.activeQuests) {
            player.sendSystemMessage(Component.literal("")
                    .append(q.getQuestText().copy())
                    .append(" ")
                    .append(q.getTarget().name())
                    .append(" (")
                    .append((getPlayerStat(q.target.statTarget(), player) - q.getProgress()) + "/" + (q.getAmountGoal() - q.getProgress()) + ")"));

        }

        player.setAttached(PLAYER_QUEST_ATTACHMENT, curr);
    }

    // gives the player executing the command a new quest from the specified set
    public static void giveRandomQuest (ServerPlayer player) {

        // heres where to add new quests
        final Quest[] possibleQuests = new Quest[]{
                new BlockMiningQuest(Blocks.STONE, 16, player),
                new BlockMiningQuest(Blocks.DIRT, 32, player),
                new BlockMiningQuest(Blocks.OBSIDIAN, 4, player),
                new BlockMiningQuest(Blocks.TNT, 9, player),
                new BlockMiningQuest(Blocks.AIR, 32, player),

                new MobKillingQuest(EntityTypes.ZOMBIE, 5, player),
                new MobKillingQuest(EntityTypes.SKELETON, 5, player),
                new MobKillingQuest(EntityTypes.IRON_GOLEM, 1, player),
                new MobKillingQuest(EntityTypes.SHEEP, 10, player),
                new MobKillingQuest(EntityTypes.ITEM_FRAME, 5, player)
        };

        if (!player.hasAttached(PLAYER_QUEST_ATTACHMENT)) {
            player.setAttached(PLAYER_QUEST_ATTACHMENT, new PlayerQuests(new ArrayList<>()));
        }

        PlayerQuests curr = player.getAttachedOrThrow(PLAYER_QUEST_ATTACHMENT);

        Quest newQuest = possibleQuests[(int) (Math.random() * possibleQuests.length)];

        player.sendSystemMessage(Component.literal("New quest: ")
                .append(newQuest.getQuestText().copy())
                .append(" " + (newQuest.getAmountGoal() - newQuest.getProgress()) + " * ")
                .append(newQuest.getTarget().name()));

        curr.activeQuests.add(newQuest);

        player.setAttached(PLAYER_QUEST_ATTACHMENT, curr);
    }
}
