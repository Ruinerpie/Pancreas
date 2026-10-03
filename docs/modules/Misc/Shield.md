# PacketGuard

**Category:** Misc
**Class:** ruinerpie.pancreas.systems.modules.misc.PacketGuard
**ID:** packet-guard
**Display name:** Packet Guard

----------------------------------------------------------------
## Purpose

Intercepts and safely drops corrupted or oversized network packets that would otherwise trigger a client disconnect.

## User-visible description

"Attempts to prevent you from being disconnected by large packets."

----------------------------------------------------------------
## Behavior

### Enable
Enables packet exception suppression flag in client packet listeners.

### Disable
Disables exception suppression flag, allowing standard disconnection behavior on packet errors.

### Per-tick / continuous behavior
Purely event/mixin-driven during packet decoding and packet handler execution.

### Edge cases
- Critical connection handshake packets: Handled carefully to avoid stalling login phase.

----------------------------------------------------------------
## Settings

### catch-exceptions
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Drops corrupted packets."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Module exposes `catchExceptions()` query for network mixins.

### log-exceptions
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Logs caught exceptions."
- **Visible when:** catch-exceptions is true
- **On change:** nothing
- **Notes:** Prints caught packet exceptions to logger instead of crashing/disconnecting.

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

Intercepts incoming corrupted/oversized packets in `ClientPacketListenerMixin` and drops them before handler crashes.

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
- **Mixins:** ClientPacketListenerMixin, ConnectionMixin.

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

- Cost per tick / per frame when enabled: Effectively zero.
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Exposes `catchExceptions()` and `logExceptions()` helper methods queried by `ConnectionMixin`.
