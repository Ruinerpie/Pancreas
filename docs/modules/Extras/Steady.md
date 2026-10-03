# AimLock

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.AimLock
**ID:** aim-lock
**Display name:** Aim Lock

----------------------------------------------------------------
## Purpose

Locks the player's look direction (yaw and pitch) to fixed specified angles.

## User-visible description

"Changes/locks your yaw and pitch."

----------------------------------------------------------------
## Behavior

### Enable
Attaches `TickEvent.Post` listener.

### Disable
Detaches tick listener.

### Per-tick / continuous behavior
On `TickEvent.Post`, sets `mc.player.setYRot(yawAngle.get().floatValue())` and `mc.player.setXRot(pitchAngle.get().floatValue())`.

### Edge cases
- Main menu: Inactive.
- In GUIs: Does not interfere with menu mouse clicks.

----------------------------------------------------------------
## Settings

### yaw-angle
- **Type:** Double
- **Default:** 0.0
- **Range / modes:** min -180.0, max 180.0
- **Description:** "Yaw angle in degrees."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### pitch-angle
- **Type:** Double
- **Default:** 0.0
- **Range / modes:** min -90.0, max 90.0
- **Description:** "Pitch angle in degrees."
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
| TickEvent.Post | NORMAL | Continuously forces player yaw and pitch to setting values |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Player position/rotation packets reflect the locked angles automatically.

----------------------------------------------------------------
## Input

Overrides manual mouse look while enabled.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** Overridden by temporary combat/interaction rotations if active.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Negligible (two float setters per tick).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.player.Rotation` to `Categories.Extras`.
