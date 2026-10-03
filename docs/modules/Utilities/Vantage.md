# F5Plus

**Category:** Utilities
**Class:** ruinerpie.pancreas.systems.modules.utilities.F5Plus
**ID:** f5-plus
**Display name:** F5 Plus

----------------------------------------------------------------
## Purpose

Customizes the third-person camera perspective distance and allows the camera to clip through solid blocks without obstruction.

## User-visible description

"Customizes third-person camera distance and behavior."

----------------------------------------------------------------
## Behavior

### Enable
Enables camera distance multiplier and clipping override hooks in Camera mixin.

### Disable
Restores default vanilla 4.0 block camera distance and block collision checks.

### Per-tick / continuous behavior
Purely render/mixin-driven during camera setup calculation each frame.

### Edge cases
- Camera clipping: Blocks between player and camera do not force camera zoom-in if `clip` is enabled.

----------------------------------------------------------------
## Settings

### distance
- **Type:** Double
- **Default:** 4.0
- **Range / modes:** min 1.0, max 15.0
- **Description:** "Third person camera distance."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Distance in blocks from player eye position.

### clip
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Camera clips through blocks."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Disables block raycast obstruction in Camera.setup().

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

Modifies camera position vector in Camera mixin.

----------------------------------------------------------------
## Packets

None.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Bypasses camera block collision raycasting when `clip` is active.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** CameraMixin.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** FreeLook.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Negligible.
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

- Rename: `CameraTweaks` -> `F5Plus` per Task 5.6. Confirm if kebab ID changes from `"camera-tweaks"` to `"f5-plus"` - requires confirmation.

----------------------------------------------------------------
## Implementation notes

Renamed from `CameraTweaks` to `F5Plus` in Task 5.6 locked rename table. Migrated from legacy `ruinerpie.pancreas.systems.modules.render.CameraTweaks` to `Categories.Utilities`.
