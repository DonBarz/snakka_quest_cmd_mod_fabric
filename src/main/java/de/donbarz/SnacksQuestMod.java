package de.donbarz;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SnacksQuestMod implements ModInitializer {
    final static String MOD_ID = "snacks_quest_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final AttachmentType<PlayerQuests> PLAYER_QUEST_ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MOD_ID, "active_player_quests"),
            builder -> builder
                    .initializer(() -> new PlayerQuests(new ArrayList<>())) // The default value of the Attachment, if one has not been set.
                    .persistent(PlayerQuests.CODEC) // Dictates how this Attachment's data should be saved and loaded.
                    .copyOnDeath() // Dictates that this Attachment should persist even after the entity dies or converts.
    );

    @Override
    public void onInitialize() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("quest").executes(context -> {
                PlayerQuests.giveRandomQuest(context.getSource().getPlayerOrException());
                return 1;
            }));
        });

        ServerTickEvents.END_SERVER_TICK.register((world) -> {
            for (ServerPlayer p : world.getPlayerList().getPlayers()) {
                PlayerQuests.update(p);
            }
        });
    }
}
