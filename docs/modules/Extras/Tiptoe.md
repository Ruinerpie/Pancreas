# Stealth

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.Stealth
**ID:** stealth
**Display name:** Stealth

----------------------------------------------------------------
## Purpose

Automatically keeps the player sneaking continuously without needing to hold down the shift key.

## User-visible description

"Sneaks for you"

----------------------------------------------------------------
## Behavior

### Enable
Sets `mc.options.keyShift.setDown(true)` and attaches module state.

### Disable
Releases sneak key `mc.options.keyShift.setDown(false)` and detaches module.

### Per-tick / continuous behavior
Keeps sneak input flag set down on client player input.

### Edge cases
- Flying: Lowers player altitude if flying.
- Dismounting vehicle: Prevents accidental dismount if guarded.

----------------------------------------------------------------
## Settings

None - this module has no settings.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

None - module is purely state-based or mixin-driven.

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Standard serverbound player command packets for sneaking (`PRESS_SHIFT_KEY`).

----------------------------------------------------------------
## Input

Holds `mc.options.keyShift.setDown(true)`.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Enabled state saved/restored per loadout.
- **With other modules:** Works with SafeEdge and InMove.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Zero overhead.
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.movement.Sneak` to `Categories.Extras`.
