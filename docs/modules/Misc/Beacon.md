# BeaconPlus

**Category:** Misc
**Class:** ruinerpie.pancreas.systems.modules.misc.BeaconPlus
**ID:** beacon-plus
**Display name:** Beacon Plus

----------------------------------------------------------------
## Purpose

Streamlines beacon interaction by automatically selecting the primary beacon effect and optionally closing the screen immediately after paying.

## User-visible description

"Enhances beacon screens and streamlines beacon effect selection."

----------------------------------------------------------------
## Behavior

### Enable
Attaches beacon screen enhancement hooks.

### Disable
Detaches hooks.

### Per-tick / continuous behavior
Purely GUI event/mixin-driven during beacon screen interaction.

### Edge cases
- Low pyramid levels: Selects best available effect for current beacon level.

----------------------------------------------------------------
## Settings

### auto-select
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Automatically selects primary effect based on beacon pyramid status."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Selects Speed / Haste / Resistance depending on pyramid tier.

### close-on-payment
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Automatically confirms and closes screen once payment item is placed."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Sends confirm packet and closes container screen.

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

Sends `ServerboundSetBeaconPacket` automatically.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

Detects payment item in beacon slot.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** BeaconScreenMixin.

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

- Cost per tick / per frame when enabled: Zero overhead outside beacon screens.
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.misc.BetterBeacons` to `Categories.Misc`.
