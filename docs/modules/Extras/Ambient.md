# SkyTune

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.SkyTune
**ID:** sky-tune
**Display name:** Sky Tune

----------------------------------------------------------------
## Purpose

Customizes the client-side atmospheric coloring and sky visual rendering.

## User-visible description

"Customizes client-side world atmosphere and colors."

----------------------------------------------------------------
## Behavior

### Enable
Attaches module state. Forces level renderer sky color update.

### Disable
Reverts sky color rendering to default Minecraft atmospheric calculations.

### Per-tick / continuous behavior
Purely render/event-driven via mixin hooks into sky rendering.

### Edge cases
- Dimension changes: Correctly reapplies custom color or defers to dimension type.
- Disconnect: Cleans up temporary render states.

----------------------------------------------------------------
## Settings

### custom-sky
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Enable custom sky coloring."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### sky-color
- **Type:** Color
- **Default:** SettingColor(100, 50, 200, 255)
- **Range / modes:** RGBA color picker
- **Description:** "Custom sky color."
- **Visible when:** custom-sky is true
- **On change:** nothing
- **Notes:** Packed RGBA value read by sky rendering mixin.

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

Overrides sky clear color and ambient sky vertex colors in screen/world render pipelines.

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

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** SettingColor.
- **Mixins:** LevelRendererMixin (sky color hook).

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

- Cost per tick / per frame when enabled: Minimal (replaces sky color lookup).
- Cost when disabled: Zero runtime cost (condition branch in mixin returns early).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.render.Ambience` to `Categories.Extras`.
