# ChatS

**Category:** Misc
**Class:** ruinerpie.pancreas.systems.modules.misc.ChatS
**ID:** chat-s
**Display name:** Chat S

----------------------------------------------------------------
## Purpose

Automates repeating a custom chat message at a configured tick interval.

## User-visible description

"Automates repeating chat messages on a timer."

----------------------------------------------------------------
## Behavior

### Enable
Resets tick timer and attaches `TickEvent.Post` listener.

### Disable
Detaches tick listener and resets timer.

### Per-tick / continuous behavior
On `TickEvent.Post`, increments timer. When timer reaches configured `delay`, dispatches the configured `message` to chat and resets timer to 0.

### Edge cases
- Main menu / Singleplayer without cheats: Inactive.
- Disconnect: Timer resets.

----------------------------------------------------------------
## Settings

### message
- **Type:** String
- **Default:** "Pancreas on Top!"
- **Range / modes:** any text
- **Description:** "Message to send."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Text dispatched to chat.

### delay
- **Type:** Integer
- **Default:** 100
- **Range / modes:** min 1, max 1000
- **Description:** "Delay in ticks between messages."
- **Visible when:** always
- **On change:** nothing
- **Notes:** 20 ticks = 1 second.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Post | NORMAL | Increments timer and sends message when interval is reached |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends standard `ServerboundChatPacket`.

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
- **Utils:** ChatUtils.
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

- Cost per tick / per frame when enabled: Minimal (one integer increment per tick).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

- Rename: `Spam` -> `ChatS` per Task 5.6. Confirm if kebab ID changes from `"spam"` to `"chat-s"` - requires confirmation.

----------------------------------------------------------------
## Implementation notes

Renamed from `Spam` to `ChatS` in Task 5.6 locked rename table.
