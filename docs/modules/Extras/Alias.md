# AliasName

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.AliasName
**ID:** alias-name
**Display name:** Alias Name

----------------------------------------------------------------
## Purpose

Replaces the local player's username and masks player skins client-side in chat, tab list, and nametags for privacy and recording.

## User-visible description

"Hide player names and skins."

----------------------------------------------------------------
## Behavior

### Enable
Enables name replacement filters and skin override state.

### Disable
Cleans up name replacement cache and restores standard skin texturing.

### Per-tick / continuous behavior
Intercepts name component rendering and skin texture lookups via mixins.

### Edge cases
- Chat history: Replaces existing instances in visible chat lines.
- Tab list: Modifies displayed name component.

----------------------------------------------------------------
## Settings

### name-protect
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Hides your name client-side."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### name
- **Type:** String
- **Default:** "seasnail"
- **Range / modes:** any string
- **Description:** "Name to be replaced with."
- **Visible when:** name-protect is true
- **On change:** nothing
- **Notes:** Client-side alias.

### skin-protect
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Make players become Steves."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Replaces skin texture with default Steve asset.

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

Modifies text components during text rendering and skin texture references during entity rendering.

----------------------------------------------------------------
## Packets

None (purely client-side string substitution).

----------------------------------------------------------------
## Input

None.

----------------------------------------------------------------
## Inventory

None.

----------------------------------------------------------------
## Entity / world interaction

Modifies player name component formatting and player skin textures.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** None.
- **Mixins:** PlayerInfoMixin, ChatComponentMixin, SkinManagerMixin.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** Friends' names can be preserved or protected.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** BetterChat, TabPlus.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Negligible string replace on chat arrival and tab rendering.
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

- Default alias name string (currently "seasnail" in Tweaks, recommend changing to "PANCREAS" or user config) - requires confirmation.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.misc.NameProtect` to `Categories.Extras`.
