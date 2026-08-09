package de.donbarz;

import net.minecraft.network.chat.Component;

abstract class QuestTarget {
    abstract Object statTarget();
    abstract Component name();
}
