package io.github.abilliontrillionstars.movesthemind.forge.mixin;

import io.github.abilliontrillionstars.movesthemind.Movesthemind;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

// scuffed workaround for https://github.com/architectury/architectury-loom/issues/189
@Mixin(net.minecraft.data.Main.class)
public class MixinDatagenMain {
    @WrapMethod(method = "main", remap = false)
    private static void movesthemind$systemExitAfterDatagenFinishes(String[] strings, Operation<Void> original) {
        try {
            original.call((Object) strings);
        } catch (Throwable throwable) {
            Movesthemind.LOGGER.error("Datagen failed!", throwable);
            System.exit(1);
        }
        Movesthemind.LOGGER.info("Datagen succeeded, terminating.");
        System.exit(0);
    }
}
