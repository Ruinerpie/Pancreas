# LeapPlus

**Category:** Utilities
**Class:** ruinerpie.pancreas.systems.modules.utilities.LeapPlus
**ID:** leap-plus
**Display name:** Leap Plus

----------------------------------------------------------------
## Purpose

Automatically triggers jumps whenever the player is moving, sprinting, or on the ground, with optional custom vertical velocity.

## User-visible description

"Automatically jumps."

----------------------------------------------------------------
## Behavior

### Enable
Attaches `TickEvent.Pre` listener.

### Disable
Detaches tick listener.

### Per-tick / continuous behavior
On `TickEvent.Pre`, checks if player is on ground, not shifting, and satisfies movement condition (`jump-if`). If valid, either invokes standard `jumpFromGround()` or modifies vertical velocity to `velocity-height`.

### Edge cases
- Player shifting: Suppressed.
- In liquids: Suppressed.
- In cobwebs: Suppressed.

----------------------------------------------------------------
## Settings

### mode
- **Type:** Enum
- **Default:** Jump
- **Range / modes:** Jump, Velocity
- **Description:** "The method of jumping."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Jump uses vanilla jump mechanic; Velocity applies custom delta movement.

### jump-if
- **Type:** Enum
- **Default:** Always
- **Range / modes:** Sprinting, Walking, Always
- **Description:** "Jump if."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Condition requiring player movement or sprint state.

### velocity-height
- **Type:** Double
- **Default:** 0.25
- **Range / modes:** min 0.0, sliderMax 2.0
- **Description:** "The distance that velocity mode moves you."
- **Visible when:** mode is Velocity
- **On change:** nothing
- **Notes:** Custom Y delta applied to movement vector.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Pre | NORMAL | Evaluates jump condition and triggers jump or sets vertical velocity |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Movement packets reflect adjusted position and velocity.

----------------------------------------------------------------
## Input

None (invokes movement logic directly).

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Checks `mc.player.onGround()`, `mc.player.isShiftKeyDown()`.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** IVec3 interface.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** Synergizes with Sprint.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Minimal (checks boolean flags).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.movement.AutoJump` to `Categories.Utilities`.
