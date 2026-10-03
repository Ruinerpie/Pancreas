# BladeSwitch

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.BladeSwitch
**ID:** blade-switch
**Display name:** Blade Switch

----------------------------------------------------------------
## Purpose

Automatically selects the most effective weapon in your hotbar immediately before attacking an entity.

## User-visible description

"Finds the best weapon to use in your hotbar."

----------------------------------------------------------------
## Behavior

### Enable
Attaches `AttackEntityEvent` listener.

### Disable
Detaches event listener.

### Per-tick / continuous behavior
Purely event-driven on attack events.

### Edge cases
- No weapons in hotbar: Leaves current item selected.
- Target entity is invalid or dead: Does not switch.
- Durability near break: Handled by `anti-break` setting.

----------------------------------------------------------------
## Settings

### threshold
- **Type:** Integer
- **Default:** 4
- **Range / modes:** min 1, max 20
- **Description:** "If the non-preferred weapon produces this much damage this will favor it over your preferred weapon."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Damage difference threshold.

### anti-break
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Prevents you from breaking your weapon."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Skips weapons with durability at or below threshold.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| AttackEntityEvent | HIGH | Calculates weapon damage across hotbar against target entity and switches hotbar slot before attack resolves |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends slot change packets (`ServerboundSetCarriedItemPacket`).

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

Reads hotbar slots (0-8) and switches active hotbar slot.

----------------------------------------------------------------
## Entity / world interaction

Inspects target entity attributes to calculate best weapon damage.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** DamageUtils, InvUtils.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** Only triggers on attackable entities.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** None.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Zero per-tick cost (event-driven).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.combat.AutoWeapon` to `Categories.Extras`.
