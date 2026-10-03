# ServerReturn

**Category:** Misc
**Class:** ruinerpie.pancreas.systems.modules.misc.ServerReturn
**ID:** server-return
**Display name:** Server Return

----------------------------------------------------------------
## Purpose

Automatically reconnects the player to the previous server after an unexpected disconnection occurs, displaying a countdown timer on the disconnect screen.

## User-visible description

"Automatically reconnects to server after disconnect."

----------------------------------------------------------------
## Behavior

### Enable
Enables automatic reconnect handling on disconnection screens.

### Disable
Disables reconnect timer.

### Per-tick / continuous behavior
Ticks down reconnect countdown timer while on `DisconnectedScreen`.

### Edge cases
- User manually presses "Back to Server List": Cancels reconnect timer.
- Singleplayer disconnect: Inactive.

----------------------------------------------------------------
## Settings

### delay
- **Type:** Double
- **Default:** 4.0
- **Range / modes:** min 1.0, max 60.0
- **Description:** "Delay in seconds before reconnecting."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Duration of countdown timer.

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

Draws countdown timer text on `DisconnectedScreen`.

----------------------------------------------------------------
## Packets

Initiates standard client connection handshake packets upon reconnect timer expiration.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Caches last connected `ServerData` instance.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** DisconnectedScreenMixin, ConnectScreen.

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

- Cost per tick / per frame when enabled: Negligible.
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.misc.AutoReconnect` to `Categories.Misc`.
