# SunDial

**Category:** Cheats
**Class:** ruinerpie.pancreas.systems.modules.cheats.SunDial
**ID:** sun-dial
**Display name:** Sun Dial

----------------------------------------------------------------
## Purpose

Overrides the client-side world time to a user-defined fixed value, ignoring incoming server time synchronization packets.

## User-visible description

"Makes you able to set a custom time."

----------------------------------------------------------------
## Behavior

### Enable
Caches the server's current world game time (`oldTime = mc.level.getGameTime()`) and attaches packet and tick listeners.

### Disable
Restores the original world game time on the level (`mc.level.getLevelData().setGameTime(oldTime)`) and detaches listeners.

### Per-tick / continuous behavior
On `TickEvent.Post`, continuously applies `mc.level.getLevelData().setGameTime(time.get().longValue())`.

### Edge cases
- Main menu (no world loaded): Inactive.
- World / Dimension change: Resets and captures new level game time.
- Disconnect / reconnect: Restores server time on disconnect.

----------------------------------------------------------------
## Settings

### time
- **Type:** Double
- **Default:** 0.0
- **Range / modes:** sliderRange -20000 to 20000
- **Description:** "The specified time to be set."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Cast to `long` before applying to `LevelData`.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| PacketEvent.Receive | NORMAL | Intercepts ClientboundSetTimePacket, records server gameTime, and cancels the packet |
| TickEvent.Post | NORMAL | Updates client level data game time to configured time setting value |

----------------------------------------------------------------
## Rendering

None directly; alters client world lighting through time manipulation.

----------------------------------------------------------------
## Packets

- Packet: `ClientboundSetTimePacket`
- Direction: Incoming
- Action: Intercepted and cancelled to prevent server time override.
- Desync risk: None (purely client visual time).

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Modifies client-side `LevelData.setGameTime(...)`.

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

- Cost per tick / per frame when enabled: Negligible (one integer setter per tick).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `Categories.Render` to `Categories.Cheats`.
