package de.donbarz;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.commands.Commands;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SnacksQuestMod implements ModInitializer {
    final static String MOD_ID = "snacks_quest_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // quest data is attached to player and set to persist through player death and server restart
    public static final AttachmentType<PlayerQuests> PLAYER_QUEST_ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MOD_ID, "active_player_quests"),
            builder -> builder
                    .initializer(() -> new PlayerQuests(new ArrayList<>()))
                    .persistent(PlayerQuests.CODEC.orElse(new PlayerQuests(new ArrayList<>()))) // if the old data is corrupted
                    .copyOnDeath()
    );

    @Override
    public void onInitialize() {

        // registering the command
        // has two subcommands: "add" for receiving a new random quest and "list" for listing current progress on all active quests
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("quest")
                    .then(Commands.literal("add").executes(context -> {
                        PlayerQuests.giveRandomQuest(context.getSource().getPlayerOrException());
                        return 1;
            }))
                    .then(Commands.literal("list").executes(context -> {
                        PlayerQuests.viewQuestProgress(context.getSource().getPlayerOrException());
                        return 1;
                    })));
        });

        // registering server ticks for updating/ checking quest progress
        ServerTickEvents.END_SERVER_TICK.register((world) -> {
            for (ServerPlayer p : world.getPlayerList().getPlayers()) {
                PlayerQuests.update(p);
            }
        });
    }
}
