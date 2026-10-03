# AlertHub

**Category:** Misc
**Class:** ruinerpie.pancreas.systems.modules.misc.AlertHub
**ID:** alert-hub
**Display name:** Alert Hub

----------------------------------------------------------------
## Purpose

Monitors critical game events (such as Totem of Undying pops, low armor durability, and player combat status) and displays notifications.

## User-visible description

"Sends notifications about game events."

----------------------------------------------------------------
## Behavior

### Enable
Attaches event listeners for entity status and equipment changes.

### Disable
Detaches event listeners.

### Per-tick / continuous behavior
Purely event-driven.

### Edge cases
- Notifications must be routed through the PANCREAS notification system, never chat (per Chat Feedback Rule).

----------------------------------------------------------------
## Settings

None - this module currently declares no settings in Tweaks. Settings list UNKNOWN - requires confirmation.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

None in current stub/legacy base; event list UNKNOWN - requires confirmation.

----------------------------------------------------------------
## Rendering

Renders toast/notification banners via the PANCREAS notification system.

----------------------------------------------------------------
## Packets

None.

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

May check armor durability levels.

----------------------------------------------------------------
## Entity / world interaction

Tracks totem pops and entity status packets.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** Can distinguish friend vs foe totem pops.
- **With Loadouts:** Enabled state saved/restored per loadout.
- **With other modules:** None.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.) All messages must route to the PANCREAS notification system.

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Low (event-driven).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

- Settings list (e.g. totem-pops, low-armor threshold, visual style) - requires confirmation.
- Concrete event triggers to monitor - requires confirmation.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.misc.Notifier` to `Categories.Misc`.
