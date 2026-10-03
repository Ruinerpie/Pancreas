# PeekView

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.PeekView
**ID:** peek-view
**Display name:** Peek View

----------------------------------------------------------------
## Purpose

Enables looking around freely in third-person camera perspective without modifying the player's movement or facing direction.

## User-visible description

"Look around freely in 3rd person without changing movement direction."

----------------------------------------------------------------
## Behavior

### Enable
Saves player's current camera yaw and pitch, switches perspective to third-person if needed, and redirects mouse inputs to independent camera rotation variables.

### Disable
Restores standard player-locked camera angles and detaches hooks.

### Per-tick / continuous behavior
Continuously maintains separate camera yaw/pitch angles during mouse look handling.

### Edge cases
- Changing perspective manually (F5): Synchronizes camera angles.
- Vehicle riding: Camera rotates independently around vehicle.

----------------------------------------------------------------
## Settings

### invert-x
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Invert horizontal rotation."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### invert-y
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Invert vertical rotation."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press (or hold)
- **Notes:** None

----------------------------------------------------------------
## Events

None - module is purely state-based or mixin-driven.

----------------------------------------------------------------
## Rendering

Modifies camera rotation in Camera setup mixin.

----------------------------------------------------------------
## Packets

None (movement packets continue using player body angles, not free look camera angles).

----------------------------------------------------------------
## Input

Intercepts mouse delta in `MouseHandlerMixin` while active.

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
- **Mixins:** CameraMixin, MouseHandlerMixin.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** Conflicts with Freecam if both active simultaneously.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Negligible (adds offset to camera angle calculation).
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.render.FreeLook` to `Categories.Extras`.
