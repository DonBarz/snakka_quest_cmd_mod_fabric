package de.donbarz;

import net.minecraft.network.chat.Component;

// dynamically set quest target and text component
abstract class QuestTarget {
    abstract Object statTarget();
    abstract Component name();
}
