# GoldBite

**Category:** Extras
**Class:** ruinerpie.pancreas.systems.modules.extras.GoldBite
**ID:** gold-bite
**Display name:** Gold Bite

----------------------------------------------------------------
## Purpose

Automatically selects and consumes Golden Apples or Enchanted Golden Apples based on health threshold or potion effect expiration.

## User-visible description

"Automatically eats Gaps or E-Gaps."

----------------------------------------------------------------
## Behavior

### Enable
Attaches tick and crosshair target listeners.

### Disable
Releases use key and detaches listeners.

### Per-tick / continuous behavior
On `TickEvent.Pre`, checks player health and potion effect durations (Regeneration, Fire Resistance, Absorption). If criteria met, searches hotbar/offhand for Golden Apple, switches slot, and holds use key until consumed.

### Edge cases
- No apples in inventory: Does nothing.
- Inventory full: Offhand eating preferred if already equipped.
- Baritone pathing: Can pause pathing during consumption.

----------------------------------------------------------------
## Settings

### allow-egap
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Allow eating E-Gaps over Gaps if found."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### always
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "If it should always eat."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### pause-baritone
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Pause baritone when eating."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Pauses PathManagers pathing while eating. Note: legacy pause-auras setting and all aura references removed per Round 7 Rule 1.6.

### before-expiry
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "If it should eat before potion effects expire."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### expiry-threshold
- **Type:** Integer
- **Default:** 60
- **Range / modes:** min 0, max 100
- **Description:** "Time in ticks before the potion effect expires to start eating."
- **Visible when:** before-expiry is true
- **On change:** nothing
- **Notes:** None

### potions-regeneration
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "If it should eat when Regeneration runs out."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### potions-fire-resistance
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "If it should eat when Fire Resistance runs out. Requires E-Gaps."
- **Visible when:** allow-egap is true
- **On change:** nothing
- **Notes:** None

### potions-absorption
- **Type:** Boolean
- **Default:** false
- **Range / modes:** N/A
- **Description:** "If it should eat when Absorption runs out. Requires E-Gaps."
- **Visible when:** allow-egap is true
- **On change:** nothing
- **Notes:** None

### health-enabled
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "If it should eat when health drops below threshold."
- **Visible when:** always
- **On change:** nothing
- **Notes:** None

### health-threshold
- **Type:** Integer
- **Default:** 20
- **Range / modes:** min 1, max 36
- **Description:** "Health threshold to eat at. Includes absorption."
- **Visible when:** health-enabled is true
- **On change:** nothing
- **Notes:** Evaluates health + absorption amount.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| TickEvent.Pre | NORMAL | Evaluates health and effects, equips apple, holds use key |
| ItemUseCrosshairTargetEvent | NORMAL | Preserves valid crosshair interaction target while eating |

----------------------------------------------------------------
## Rendering

None.

----------------------------------------------------------------
## Packets

Sends standard item use packets and slot change packets.

----------------------------------------------------------------
## Input

Manipulates `mc.options.keyUse.setDown(true/false)`.

----------------------------------------------------------------
## Inventory

Finds `Items.ENCHANTED_GOLDEN_APPLE` or `Items.GOLDEN_APPLE` in hotbar/offhand, switches active hotbar slot.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None directly; Task 8.2 removes aura references.
- **Utils:** InvUtils, PathManagers.
- **Mixins:** None.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** Pauses PathManagers pathing when `pause-baritone` is true.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Low (checks player health and active status effects).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

- Removal of `pause-auras` setting per Task 8.2 out-of-scope mandate - requires confirmation.

----------------------------------------------------------------
## Implementation notes

Migrated from legacy `ruinerpie.pancreas.systems.modules.combat.AutoGap` to `Categories.Extras`.
