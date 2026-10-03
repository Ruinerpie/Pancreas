# RotLock

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.RotLock
**ID:** rot-lock
**Display name:** Rot Lock

----------------------------------------------------------------
## Purpose

Prevents the multiplayer server from forcibly changing the client player's look direction (yaw and pitch) during teleports or knockback.

## User-visible description

"Prevents server from forcibly changing player rotation/look direction."

----------------------------------------------------------------
## Behavior

### Enable
Attaches packet listener for incoming position/teleport packets.

### Disable
Detaches packet listener.

### Per-tick / continuous behavior
Purely event-driven when server position updates arrive.

### Edge cases
- Dimension change: May need to allow rotation sync on initial dimension load to face spawn.
- Respawn: Preserves client angles or allows default orientation.

----------------------------------------------------------------
## Settings

### preserve-yaw
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Keeps current player yaw when teleported or rotated by server."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### preserve-pitch
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Keeps current player pitch when rotated by server."
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
| PacketEvent.Receive | NORMAL | Intercepts ClientboundPlayerPositionPacket and replaces packet yaw/pitch with player's current client angles |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

- Packet: `ClientboundPlayerPositionPacket`
- Direction: Incoming
- Action: Modifies packet `yaw` and `pitch` fields via accessor before packet processing.
- Desync risk: Slight rubberband if server enforces strict rotation acknowledgment.

----------------------------------------------------------------
## Input

None.

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
- **Mixins:** ClientboundPlayerPositionPacketAccessor.

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

- Cost per tick / per frame when enabled: Zero per-tick cost (event-driven).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.player.NoRotate` to `Categories.Extras`.
