# ClickGuard

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.ClickGuard
**ID:** click-guard
**Display name:** Click Guard

----------------------------------------------------------------
## Purpose

Prevents accidental interaction with specified blocks or entities (e.g. chests, crafting tables, armor stands, villagers) by cancelling clicks.

## User-visible description

"Blocks interactions with certain types of inputs."

----------------------------------------------------------------
## Behavior

### Enable
Attaches listeners for block and entity interaction/attack events.

### Disable
Detaches event listeners from event bus.

### Per-tick / continuous behavior
Purely event-driven on player interaction events.

### Edge cases
- Main menu: Inactive.
- Creative mode: Operates identically.

----------------------------------------------------------------
## Settings

### block-mine
- **Type:** BlockList
- **Default:** empty
- **Range / modes:** block filter
- **Description:** "Cancels block mining."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### block-interact
- **Type:** BlockList
- **Default:** empty
- **Range / modes:** block filter
- **Description:** "Cancels block interaction."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### entity-hit
- **Type:** EntityTypeList
- **Default:** empty
- **Range / modes:** entity filter
- **Description:** "Cancel entity hitting."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### entity-interact
- **Type:** EntityTypeList
- **Default:** empty
- **Range / modes:** entity filter
- **Description:** "Cancel entity interaction."
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
| StartBreakingBlockEvent | NORMAL | Cancels event if targeted block is in block-mine list |
| InteractBlockEvent | NORMAL | Cancels event if targeted block is in block-interact list |
| AttackEntityEvent | NORMAL | Cancels attack if target entity is in entity-hit list |
| InteractEntityEvent | NORMAL | Cancels interaction if target entity is in entity-interact list |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Cancels client-side interaction packet dispatch.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Checks block type at target position and entity type of target entity against configured filters.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** Friend protection can optionally be handled via entity-hit filter.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** None.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Effectively zero (event-driven).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.player.NoInteract` to `Categories.Extras`.
