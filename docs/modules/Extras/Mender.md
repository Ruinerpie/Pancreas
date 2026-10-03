# MendLoop

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.MendLoop
**ID:** mend-loop
**Display name:** Mend Loop

----------------------------------------------------------------
## Purpose

Automatically replaces items in your offhand with damaged Mending items when the current offhand item reaches full durability.

## User-visible description

"Automatically replaces items in your offhand with mending when fully repaired."

----------------------------------------------------------------
## Behavior

### Enable
Attaches `TickEvent.Pre` listener.

### Disable
Detaches tick listener.

### Per-tick / continuous behavior
On `TickEvent.Pre`, checks durability of offhand item. If item damage is 0 (fully repaired) or `force` is enabled, scans main inventory for items with Mending enchantment that are damaged, and swaps one into the offhand slot. If no repairable items remain and `auto-disable` is true, toggles module off.

### Edge cases
- No damaged Mending items left: Auto-disables if configured, or idles.
- Inventory full: Offhand swap uses standard slot swapping.

----------------------------------------------------------------
## Settings

### blacklist
- **Type:** ItemList
- **Default:** empty
- **Range / modes:** item filter
- **Description:** "Item blacklist."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Items that will not be placed into offhand for mending.

### force
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Replaces item in offhand even if there is some other non-repairable item."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### auto-disable
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Automatically disables when there are no more items to repair."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Pre | NORMAL | Evaluates offhand durability and performs inventory swap if repaired |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends slot click / swap inventory packets.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

Reads main inventory, inspects enchantments (Mending) and damage values, swaps item into offhand.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** InvUtils.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** Often used alongside EXPThrower.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Low (scans 36 inventory slots when offhand item is full durability).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.player.AutoMend` to `Categories.Extras`.
