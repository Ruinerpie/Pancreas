# Scope

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.Scope
**ID:** scope
**Display name:** Scope

----------------------------------------------------------------
## Purpose

Provides smooth, cinematic camera zoom with configurable magnification factor and transition speed.

## User-visible description

"Smooth camera zoom with configurable FOV multiplier."

----------------------------------------------------------------
## Behavior

### Enable
Begins smooth interpolation towards target FOV reduction factor.

### Disable
Smoothly interpolates FOV back to default 1.0 multiplier before fully idling.

### Per-tick / continuous behavior
Interpolates current zoom factor towards target zoom factor each frame.

### Edge cases
- In-game FOV changes: Scales proportionally with base FOV setting.
- Spyglass active: Stacks smoothly or overrides.

----------------------------------------------------------------
## Settings

### zoom-factor
- **Type:** Double
- **Default:** 3.0
- **Range / modes:** min 1.0, max 10.0
- **Description:** "FOV reduction factor when zooming."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Divides normal FOV by this multiplier.

### smooth-speed
- **Type:** Double
- **Default:** 1.0
- **Range / modes:** min 0.1, max 5.0
- **Description:** "Transition speed."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Speed of interpolation between standard FOV and zoomed FOV.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** hold or toggle on press
- **Notes:** Frequently bound to 'C' or 'V'.

----------------------------------------------------------------
## Events

None - module is purely state-based or mixin-driven.

----------------------------------------------------------------
## Rendering

Modifies FOV calculation in `GameRendererMixin`.

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
- **Utils:** None.
- **Mixins:** GameRendererMixin.

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

- Cost per tick / per frame when enabled: Negligible (one math interpolation on frame render).
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.render.Zoom` to `Categories.Extras`.
