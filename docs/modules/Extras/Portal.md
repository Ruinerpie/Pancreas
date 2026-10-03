# PortalTune

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.PortalTune
**ID:** portal-tune
**Display name:** Portal Tune

----------------------------------------------------------------
## Purpose

Allows opening inventories, chat, and other GUIs while standing inside nether portals, and disables the nausea screen warp effect.

## User-visible description

"Allows interacting with GUIs normally while standing inside a Nether Portal."

----------------------------------------------------------------
## Behavior

### Enable
Enables mixin overrides for portal GUI restriction checks and nausea visual rendering.

### Disable
Restores vanilla portal GUI blocking behavior and visual nausea effect.

### Per-tick / continuous behavior
Purely mixin/state-driven.

### Edge cases
- In nether portal countdown: Screens remain open until actual teleport happens.

----------------------------------------------------------------
## Settings

### allow-gui
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Allows opening inventories and chat while inside a portal."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Overrides `LocalPlayer.canTakeDamage()` or portal GUI check.

### no-nausea
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Prevents the portal distortion and nausea screen effect."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Cancels portal overlay rendering.

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

Cancels portal distortion and portal overlay rendering in `GuiMixin` / `GameRenderer`.

----------------------------------------------------------------
## Packets

None.

----------------------------------------------------------------
## Input

Allows normal key handling in screens while inside portals.

----------------------------------------------------------------
## Inventory

Allows normal inventory opening and container interaction while standing inside portal blocks.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** LocalPlayerMixin, GuiMixin.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** None.

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

Migrated from legacy `ruinerpie.pancreas.systems.modules.movement.Portals` to `Categories.Extras`.
