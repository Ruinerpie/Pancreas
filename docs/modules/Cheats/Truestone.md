# TrueBlocks

**Category:** Cheats
**Class:** ruinerpie.pancreas.systems.modules.cheats.TrueBlocks
**ID:** true-blocks
**Display name:** True Blocks

----------------------------------------------------------------
## Purpose

Synchronizes broken and placed blocks with the server to prevent ghost blocks from appearing during rapid mining or block placement.

## User-visible description

"Attempts to prevent ghost blocks arising."

----------------------------------------------------------------
## Behavior

### Enable
Attaches listeners for `BreakBlockEvent` and `PlaceBlockEvent`. No background threads or persistent allocations.

### Disable
Detaches event listeners from the event bus. Cleans up all event state.

### Per-tick / continuous behavior
Purely event-driven. Performs zero per-tick or per-frame computations.

### Edge cases
- Main menu (no world loaded): Inactive.
- Local singleplayer server: `mc.isLocalServer()` check skips block-breaking logic to prevent double destruction.
- World / Dimension change: Handled cleanly; listeners remain passive until block interactions occur.
- Disconnect / reconnect: Handled cleanly.

----------------------------------------------------------------
## Settings

### breaking
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Whether to apply for block breaking actions."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Cancels client event and invokes playerWillDestroy on server.

### placing
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Whether to apply for block placement actions."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Cancels client-side premature block placement confirmation.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| BreakBlockEvent | NORMAL | Cancels client break event on remote servers and triggers block playerWillDestroy |
| PlaceBlockEvent | NORMAL | Cancels client place block event if placing setting is enabled |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

None directly sent; cancels client-side block interaction events.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Queries target `BlockState` at `event.blockPos` and calls `playerWillDestroy(mc.level, event.blockPos, blockState, mc.player)`.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** None.

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

- Cost per tick / per frame when enabled: Effectively zero (event-driven).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.world.NoGhostBlocks` and legacy category `Categories.World` to `Categories.Cheats`.
