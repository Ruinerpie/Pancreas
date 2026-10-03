# SoundMute

**Category:** Misc
**Class:** ruinerpie.pancreas.systems.modules.misc.SoundMute
**ID:** sound-mute
**Display name:** Sound Mute

----------------------------------------------------------------
## Purpose

Silences and prevents playback of user-selected client-side sound effects (e.g. portal whoosh, bats, pistons).

## User-visible description

"Cancels and mutes selected sound effects."

----------------------------------------------------------------
## Behavior

### Enable
Enables sound event interception filter in sound engine mixin.

### Disable
Disables sound event filter, allowing all sound playback.

### Per-tick / continuous behavior
Purely event/mixin-driven whenever a sound effect is queued for playback.

### Edge cases
- Sound identifiers matching blocked list are immediately discarded before audio buffer allocation.

----------------------------------------------------------------
## Settings

### blocked-sounds
- **Type:** SoundEventList
- **Default:** empty
- **Range / modes:** sound event selector
- **Description:** "Mute selected sound effects."
- **Visible when:** always
- **On change:** nothing
- **Notes:** List of `SoundEvent` or `Identifier` instances to mute.

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

None (operates at client SoundEngine level).

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
- **Mixins:** SoundEngineMixin / SoundManagerMixin.

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

- Cost per tick / per frame when enabled: Negligible (one HashSet contains check per played sound).
- Cost when disabled: Zero runtime cost.
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.misc.SoundBlocker` to `Categories.Misc`.
