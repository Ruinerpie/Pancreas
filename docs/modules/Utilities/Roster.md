# TabPlus

**Category:** Utilities
**Class:** ruinerpie.pancreas.systems.modules.utilities.TabPlus
**ID:** tab-plus
**Display name:** Tab Plus

----------------------------------------------------------------
## Purpose

Expands the player tab list to show up to 500 players simultaneously and renders exact numeric latency (ping in ms) beside player names.

## User-visible description

"Enhances player tab list display and limits."

----------------------------------------------------------------
## Behavior

### Enable
Enables tab list count override and latency rendering mixin hooks.

### Disable
Restores vanilla 80-player tab list limit and ping bar icons.

### Per-tick / continuous behavior
Purely render/mixin-driven when tab list overlay is open.

### Edge cases
- Large servers (200+ players): Tab columns scale appropriately.

----------------------------------------------------------------
## Settings

### tab-size
- **Type:** Integer
- **Default:** 100
- **Range / modes:** min 1, max 500
- **Description:** "Maximum players shown in tab list."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Overrides vanilla 80-entry limit.

### show-ping
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Display numeric ping beside player names."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Renders latency in milliseconds with latency-based coloring (green, yellow, red).

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

Renders numeric text for ping in player tab list overlay (`PlayerTabOverlayMixin`).

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

Reads latency from `PlayerInfo.getLatency()`.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** PlayerTabOverlayMixin.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** Highlights friends in tab list with custom color.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** NameProtect.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Negligible (only renders when tab key is held).
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

- Rename: `BetterTab` -> `TabPlus` per Task 5.6. Confirm if kebab ID changes from `"better-tab"` to `"tab-plus"` - requires confirmation.

----------------------------------------------------------------
## Implementation notes

Renamed from `BetterTab` to `TabPlus` in Task 5.6 locked rename table. Migrated from legacy `ruinerpie.pancreas.systems.modules.render.BetterTab` to `Categories.Utilities`.
