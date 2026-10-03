# TapLoop

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.TapLoop
**ID:** tap-loop
**Display name:** Tap Loop

----------------------------------------------------------------
## Purpose

Automatically simulates continuous left or right mouse clicks at a configurable tick rate.

## User-visible description

"Automatically clicks."

----------------------------------------------------------------
## Behavior

### Enable
Resets click tick timers and attaches `TickEvent.Post` listener.

### Disable
Detaches tick listener and resets click timers.

### Per-tick / continuous behavior
On `TickEvent.Post`, decrements left and right click delay counters. If delay elapsed and mouse button held (or simulated), calls `mc.startAttack()` for left clicks or `mc.startUseItem()` for right clicks.

### Edge cases
- In GUI screens: Handled by `while-in-screens` setting.
- Main menu: Inactive.

----------------------------------------------------------------
## Settings

### while-in-screens
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Whether to click while a screen is open."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### delay-left
- **Type:** Integer
- **Default:** 2
- **Range / modes:** min 0, max 60
- **Description:** "The amount of delay between left clicks in ticks."
- **Visible when:** always
- **On change:** nothing
- **Notes:** 0 = every tick.

### delay-right
- **Type:** Integer
- **Default:** 2
- **Range / modes:** min 0, max 60
- **Description:** "The amount of delay between right clicks in ticks."
- **Visible when:** always
- **On change:** nothing
- **Notes:** 0 = every tick.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Post | NORMAL | Decrements timers and triggers attack or item use actions |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends normal player interaction/attack packets via client Minecraft controller methods.

----------------------------------------------------------------
## Input

Checks GLFW mouse button state if configured.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Simulates user attack and use item on crosshair target.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** Does not override Minecraft targeting.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** None.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Minimal tick counter decrement.
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.combat.AutoClicker` to `Categories.Extras`.
