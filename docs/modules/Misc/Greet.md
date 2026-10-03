# GreetBot

**Category:** Misc
**Class:** ruinerpie.pancreas.systems.modules.misc.GreetBot
**ID:** greet-bot
**Display name:** Greet Bot

----------------------------------------------------------------
## Purpose

Sends a configured automated chat message to any player entering visual render distance.

## User-visible description

"Sends a specified message to any player that enters render distance."

----------------------------------------------------------------
## Behavior

### Enable
Attaches `EntityAddedEvent` listener and initializes tracked player set.

### Disable
Detaches `EntityAddedEvent` listener and clears tracked player set.

### Per-tick / continuous behavior
Purely event-driven when entities are added to the world.

### Edge cases
- Self player: Ignored.
- Friends: Filtered out if `ignore-friends` is true.
- Same player re-entering render distance: Tracked in set to avoid spamming repeatedly in the same session.

----------------------------------------------------------------
## Settings

### message
- **Type:** String
- **Default:** "Pancreas on Top!"
- **Range / modes:** any string
- **Description:** "The specified message sent to the player."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Placeholders like {player} can be substituted.

### ignore-friends
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "Will not send any messages to people friended."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Uses Crew/Friends system check.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| EntityAddedEvent | NORMAL | Checks if new entity is player, filters friends, and sends chat message |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends `ServerboundChatPacket` or calls `ChatUtils.sendPlayerMsg()`.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Monitors incoming `Player` entities added to client level.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** ChatUtils.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** Checks `Friends.get().isFriend(player)` when `ignore-friends` is enabled.
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

Migrated from legacy `ruinerpie.pancreas.systems.modules.misc.MessagePro` to `Categories.Misc`.
