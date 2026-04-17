package cc.happyareabean.jumppad.commands;

import cc.happyareabean.jumppad.commands.exception.handler.SimpleJumpPadExceptionHandler;
import cc.happyareabean.jumppad.object.JumpPadsDirection;
import cc.happyareabean.jumppad.suggestionprovider.JumpPadsDirectionSuggestion;
import cc.happyareabean.jumppad.suggestionprovider.ParticleTypeSuggestion;
import cc.happyareabean.jumppad.suggestionprovider.SoundTypeSuggestion;
import cc.happyareabean.jumppad.targetlocation.JumpPadsTargetLocation;
import cc.happyareabean.jumppad.targetlocation.JumpPadsTargetLocationParameterType;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.LampBuilderVisitor;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

import java.util.Arrays;

public class JumpPadsVisitor implements LampBuilderVisitor<BukkitCommandActor> {
    @Override
    public void visit(Lamp.@NotNull Builder<BukkitCommandActor> builder) {
        builder.suggestionProviders(providers -> {
            providers.addProviderForAnnotation(ParticleTypeSuggestion.class, annotation -> context ->
                    Arrays.stream(Particle.values()).map(Particle::name).toList());
            providers.addProviderForAnnotation(JumpPadsDirectionSuggestion.class, annotation -> context ->
                    Arrays.stream(JumpPadsDirection.values()).map(JumpPadsDirection::name).toList());
            providers.addProviderForAnnotation(SoundTypeSuggestion.class, annotation -> context ->
                    Registry.SOUND_EVENT.stream()
                            .filter(s -> Registry.SOUND_EVENT.getKey(s) != null)
                            .map(s -> Registry.SOUND_EVENT.getKey(s).toString())
                            .toList()
            );
        });

        builder.parameterTypes(type -> {
            type.addParameterType(JumpPadsTargetLocation.class, new JumpPadsTargetLocationParameterType());
        });

        builder.exceptionHandler(new SimpleJumpPadExceptionHandler());
    }
}
