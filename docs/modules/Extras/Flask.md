# BottleToss

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.BottleToss
**ID:** bottle-toss
**Display name:** Bottle Toss

----------------------------------------------------------------
## Purpose

Automatically throws experience bottles from your hotbar toward the ground to rapidly repair Mending gear.

## User-visible description

"Automatically throws XP bottles from your hotbar."

----------------------------------------------------------------
## Behavior

### Enable
Attaches `TickEvent.Pre` listener.

### Disable
Detaches tick listener and restores pitch angle.

### Per-tick / continuous behavior
On `TickEvent.Pre`, searches hotbar for `Items.EXPERIENCE_BOTTLE`. If present, aims downward (pitch 90) and uses item.

### Edge cases
- No XP bottles in hotbar: Does nothing.
- In menus: Pauses throwing unless configured otherwise.

----------------------------------------------------------------
## Settings

None - this module has no settings.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Pre | NORMAL | Selects experience bottle from hotbar, looks down, and sends use item action |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends `ServerboundMovePlayerPacket.Rot` and `ServerboundUseItemPacket`.

----------------------------------------------------------------
## Input

Simulates right-click use action.

----------------------------------------------------------------
## Inventory

Finds `Items.EXPERIENCE_BOTTLE` in hotbar and switches selected slot.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** InvUtils, Rotations.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Enabled state saved/restored per loadout.
- **With other modules:** Often paired with AutoMend.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Low (scans 9 hotbar slots per tick).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

None.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.player.EXPThrower` to `Categories.Extras`.
