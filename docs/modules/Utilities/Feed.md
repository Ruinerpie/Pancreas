# FeedLoop

**Category:** Utilities
**Class:** ruinerpie.pancreas.systems.modules.utilities.FeedLoop
**ID:** feed-loop
**Display name:** Feed Loop

----------------------------------------------------------------
## Purpose

Automatically selects food from your hotbar or offhand and eats when hunger or health drops below configured thresholds.

## User-visible description

"Automatically consumes food when hungry or low health."

----------------------------------------------------------------
## Behavior

### Enable
Attaches `TickEvent.Pre` listener.

### Disable
Releases use key if currently eating and detaches tick listener.

### Per-tick / continuous behavior
On `TickEvent.Pre`, checks player hunger level. If hunger <= `hunger-threshold` (or health low), finds the best food item in hotbar or offhand (evaluating hunger replenishment and saturation), switches slot, and holds use key until consumed.

### Edge cases
- No food available: Does nothing.
- In combat: Handled by `pause-on-combat` setting.
- Bad food (chorus fruit, spider eyes, rotten flesh): Filtered out.

----------------------------------------------------------------
## Settings

### hunger-threshold
- **Type:** Integer
- **Default:** 16
- **Range / modes:** min 1, max 20
- **Description:** "Hunger level at which eating starts."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Vanilla max hunger is 20.

### pause-on-combat
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Pause eating during combat."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Pauses eating if player attacked an entity recently.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Pre | NORMAL | Evaluates hunger level, selects best food item, and holds use key |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends standard item use and slot selection packets.

----------------------------------------------------------------
## Input

Holds `mc.options.keyUse.setDown(true)` while eating, releases when finished.

----------------------------------------------------------------
## Inventory

Finds best food item in hotbar or offhand, switches active hotbar slot.

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
- **With other modules:** Yields priority to AutoGap when eating golden apples.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Low (checks hunger int and 9 hotbar slots).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.player.AutoEat` to `Categories.Utilities`.
