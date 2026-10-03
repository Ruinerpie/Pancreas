package ruinerpie.pancreas.tweaks.extras;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;
import ruinerpie.pancreas.saved.*;
import ruinerpie.pancreas.stalk.*;
import ruinerpie.pancreas.theme.*;
import ruinerpie.pancreas.screens.*;
import ruinerpie.pancreas.screens.widgets.*;
import ruinerpie.pancreas.hud.*;
import ruinerpie.pancreas.hud.parts.*;
import ruinerpie.pancreas.tweaks.cheats.*;
import ruinerpie.pancreas.tweaks.extras.*;
import ruinerpie.pancreas.tweaks.misc.*;
import ruinerpie.pancreas.tweaks.utilities.*;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class Guard extends Feature {
    public enum HandMode {
        Mainhand,
        Offhand,
        Both,
        None
    }

    public enum ListMode {
        WhiteList,
        BlackList
    }

    public enum InteractMode {
        Hit,
        Interact,
        Both,
        None
    }

    private final ValueGroup sgBlocks = settings.createGroup("Blocks");
    private final ValueGroup sgEntities = settings.createGroup("Entities");

    private final Value<List<Block>> blockMine = sgBlocks.add(new BlockListValue.Builder()
        .name("block-mine")
        .description("Cancels block mining.")
        .build()
    );

    private final Value<ListMode> blockMineMode = sgBlocks.add(new ChoiceValue.Builder<ListMode>()
        .name("block-mine-mode")
        .description("List mode to use for block mine.")
        .defaultValue(ListMode.BlackList)
        .build()
    );

    private final Value<List<Block>> blockInteract = sgBlocks.add(new BlockListValue.Builder()
        .name("block-interact")
        .description("Cancels block interaction.")
        .build()
    );

    private final Value<ListMode> blockInteractMode = sgBlocks.add(new ChoiceValue.Builder<ListMode>()
        .name("block-interact-mode")
        .description("List mode to use for block interact.")
        .defaultValue(ListMode.BlackList)
        .build()
    );

    private final Value<HandMode> blockInteractHand = sgBlocks.add(new ChoiceValue.Builder<HandMode>()
        .name("block-interact-hand")
        .description("Cancels block interaction if performed by this hand.")
        .defaultValue(HandMode.None)
        .build()
    );

    private final Value<List<EntityType<?>>> entityHit = sgEntities.add(new EntityListValue.Builder()
        .name("entity-hit")
        .description("Cancel entity hitting.")
        .onlyAttackable()
        .build()
    );

    private final Value<ListMode> entityHitMode = sgEntities.add(new ChoiceValue.Builder<ListMode>()
        .name("entity-hit-mode")
        .description("List mode to use for entity hit.")
        .defaultValue(ListMode.BlackList)
        .build()
    );

    private final Value<List<EntityType<?>>> entityInteract = sgEntities.add(new EntityListValue.Builder()
        .name("entity-interact")
        .description("Cancel entity interaction.")
        .onlyAttackable()
        .build()
    );

    private final Value<ListMode> entityInteractMode = sgEntities.add(new ChoiceValue.Builder<ListMode>()
        .name("entity-interact-mode")
        .description("List mode to use for entity interact.")
        .defaultValue(ListMode.BlackList)
        .build()
    );

    private final Value<HandMode> entityInteractHand = sgEntities.add(new ChoiceValue.Builder<HandMode>()
        .name("entity-interact-hand")
        .description("Cancels entity interaction if performed by this hand.")
        .defaultValue(HandMode.None)
        .build()
    );

    private final Value<InteractMode> friends = sgEntities.add(new ChoiceValue.Builder<InteractMode>()
        .name("friends")
        .description("Team cancel mode.")
        .defaultValue(InteractMode.None)
        .build()
    );

    private final Value<InteractMode> babies = sgEntities.add(new ChoiceValue.Builder<InteractMode>()
        .name("babies")
        .description("Baby entity cancel mode.")
        .defaultValue(InteractMode.None)
        .build()
    );

    private final Value<InteractMode> nametagged = sgEntities.add(new ChoiceValue.Builder<InteractMode>()
        .name("nametagged")
        .description("Nametagged entity cancel mode.")
        .defaultValue(InteractMode.None)
        .build()
    );

    public Guard() {
        super(Group.Extras, "guard", "Blocks interactions with certain types of inputs.");
    }

    @Listen(priority = Order.HIGH)
    private void onStartBreakingBlockEvent(StartBreakBlock event) {
        if (!shouldAttackBlock(event.blockPos)) event.cancel();
    }

    @Listen
    private void onInteractBlock(InteractBlock event) {
        if (!shouldInteractBlock(event.result, event.hand)) event.cancel();
    }

    @Listen(priority = Order.HIGH)
    private void onAttackEntity(AttackEntity event) {
        if (!shouldAttackEntity(event.entity)) event.cancel();
    }

    @Listen
    private void onInteractEntity(InteractEntity event) {
        if (!shouldInteractEntity(event.entity, event.hand)) event.cancel();
    }

    private boolean shouldAttackBlock(BlockPos blockPos) {
        if (mc.level == null) return true;
        if (blockMineMode.get() == ListMode.WhiteList &&
            blockMine.get().contains(mc.level.getBlockState(blockPos).getBlock())) {
            return false;
        }

        return blockMineMode.get() != ListMode.BlackList ||
            !blockMine.get().contains(mc.level.getBlockState(blockPos).getBlock());
    }

    private boolean shouldInteractBlock(BlockHitResult hitResult, InteractionHand hand) {
        if (mc.level == null) return true;
        if (blockInteractHand.get() == HandMode.Both ||
            (blockInteractHand.get() == HandMode.Mainhand && hand == InteractionHand.MAIN_HAND) ||
            (blockInteractHand.get() == HandMode.Offhand && hand == InteractionHand.OFF_HAND)) {
            return false;
        }

        if (blockInteractMode.get() == ListMode.BlackList &&
            blockInteract.get().contains(mc.level.getBlockState(hitResult.getBlockPos()).getBlock())) {
            return false;
        }

        return blockInteractMode.get() != ListMode.WhiteList ||
            blockInteract.get().contains(mc.level.getBlockState(hitResult.getBlockPos()).getBlock());
    }

    private boolean shouldAttackEntity(Entity entity) {
        if ((friends.get() == InteractMode.Both || friends.get() == InteractMode.Hit) &&
            entity instanceof Player player && !Team.get().shouldAttack(player)) {
            return false;
        }

        if ((babies.get() == InteractMode.Both || babies.get() == InteractMode.Hit) &&
            entity instanceof Animal animal && animal.isBaby()) {
            return false;
        }

        if ((nametagged.get() == InteractMode.Both || nametagged.get() == InteractMode.Hit) && entity.hasCustomName())
            return false;

        if (entityHitMode.get() == ListMode.BlackList &&
            entityHit.get().contains(entity.getType())) {
            return false;
        } else return entityHitMode.get() != ListMode.WhiteList ||
            entityHit.get().contains(entity.getType());
    }

    private boolean shouldInteractEntity(Entity entity, InteractionHand hand) {
        if (entityInteractHand.get() == HandMode.Both ||
            (entityInteractHand.get() == HandMode.Mainhand && hand == InteractionHand.MAIN_HAND) ||
            (entityInteractHand.get() == HandMode.Offhand && hand == InteractionHand.OFF_HAND)) {
            return false;
        }

        if ((friends.get() == InteractMode.Both || friends.get() == InteractMode.Interact) &&
            entity instanceof Player player && !Team.get().shouldAttack(player)) {
            return false;
        }

        if ((babies.get() == InteractMode.Both || babies.get() == InteractMode.Interact) &&
            entity instanceof Animal animal && animal.isBaby()) {
            return false;
        }

        if ((nametagged.get() == InteractMode.Both || nametagged.get() == InteractMode.Interact) && entity.hasCustomName())
            return false;

        if (entityInteractMode.get() == ListMode.BlackList &&
            entityInteract.get().contains(entity.getType())) {
            return false;
        } else return entityInteractMode.get() != ListMode.WhiteList ||
            entityInteract.get().contains(entity.getType());
    }
}