# RunLock

**Category:** Utilities
**Class:** ruinerpie.pancreas.systems.modules.utilities.RunLock
**ID:** run-lock
**Display name:** Run Lock

----------------------------------------------------------------
## Purpose

Maintains sprinting automatically whenever the player moves forward, with advanced modes for combat and water movement.

## User-visible description

"Automatically sprints."

----------------------------------------------------------------
## Behavior

### Enable
Attaches tick and packet listeners.

### Disable
Stops forcing sprint key down and detaches listeners.

### Per-tick / continuous behavior
On `TickEvent.Post`, if forward movement is detected and hunger allows, sets `mc.options.keySprint.setDown(true)` or calls `mc.player.setSprinting(true)`.

### Edge cases
- Colliding horizontally: Pauses sprint if vanilla strict mechanics enabled.
- Attacking entities: Handles sweep attacks / crits via `unsprint-on-hit` and keeps sprint via `keep-sprint`.
- Submerged in water: Handled via `unsprint-in-water` setting.

----------------------------------------------------------------
## Settings

### sprint-mode
- **Type:** Enum
- **Default:** Strict
- **Range / modes:** Strict, Rage
- **Description:** "What mode of sprinting."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Strict adheres to vanilla forward movement checks; Rage forces sprinting packet.

### keep-sprint
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Whether to keep sprinting after attacking."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Intercepts attack packet and maintains sprint state.

### unsprint-on-hit
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Whether to stop sprinting before attacking, to ensure you get crits and sweep attacks."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Briefly cancels sprint before attack packet dispatch.

### unsprint-in-water
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Whether to stop sprinting when in water."
- **Visible when:** sprint-mode is Rage
- **On change:** nothing
- **Notes:** Avoids water slowdown in rage mode.

### sprint-while-stationary
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Sprint even when not moving."
- **Visible when:** sprint-mode is Rage
- **On change:** nothing
- **Notes:** Forces sprint packet continuously.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Post | NORMAL | Updates player sprint state based on movement and settings |
| PacketEvent.Send | HIGH | Intercepts ServerboundAttackPacket and manages unsprint on hit |
| PacketEvent.Sent | NORMAL | Restores sprint packet state after attack |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends and intercepts `ServerboundPlayerCommandPacket` (`START_SPRINTING`, `STOP_SPRINTING`).

----------------------------------------------------------------
## Input

Manipulates `mc.options.keySprint.setDown(true/false)`.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Checks player hunger, fluid state (water), and movement inputs.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** AutoJump, InMove.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Negligible.
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.movement.Sprint` to `Categories.Utilities`.
