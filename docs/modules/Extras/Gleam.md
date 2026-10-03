# ItemGlow

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.ItemGlow
**ID:** item-glow
**Display name:** Item Glow

----------------------------------------------------------------
## Purpose

Draws a colored overlay highlight over specified items when viewing container screens and inventories.

## User-visible description

"Highlights selected items when in guis"

----------------------------------------------------------------
## Behavior

### Enable
Enables slot rendering hook.

### Disable
Disables slot rendering hook.

### Per-tick / continuous behavior
Purely render-driven during GUI screen slot rendering.

### Edge cases
- Creative inventory: Handles creative inventory tabs and search screen.
- Screen closed: Inactive.

----------------------------------------------------------------
## Settings

### items
- **Type:** ItemList
- **Default:** empty
- **Range / modes:** item filter
- **Description:** "Items to highlight."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Items to search for in containers.

### color
- **Type:** Color
- **Default:** SettingColor(225, 25, 255, 50)
- **Range / modes:** RGBA color picker
- **Description:** "The color to highlight the items with."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Semi-transparent box drawn over slot bounds.

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

Draws filled 2D rectangle in GUI container screen space over matching item slots.

----------------------------------------------------------------
## Packets

None.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

Reads item type in current container slot during screen rendering.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** SettingColor.
- **Mixins:** AbstractContainerScreenMixin.

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

- Cost per tick / per frame when enabled: Minimal (checks slot item stack against item set during GUI render).
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.render.ItemHighlight` to `Categories.Extras`.
