# InMove

**Category:** Utilities
**Class:** ruinerpie.pancreas.systems.modules.utilities.InMove
**ID:** in-move
**Display name:** In Move

----------------------------------------------------------------
## Purpose

Enables moving, jumping, sprinting, sneaking, and rotating the player look direction while user interface screens and inventories are open.

## User-visible description

"Allows you to perform various actions while in GUIs."

----------------------------------------------------------------
## Behavior

### Enable
Attaches key and render listeners.

### Disable
Cleans up movement key press states (resets jump, sneak, sprint key down flags) and detaches listeners.

### Per-tick / continuous behavior
Listens to `KeyInputEvent` and updates movement keys even when a screen is open. In `Render3DEvent`, rotates camera if arrow keys are held.

### Edge cases
- Chat screen / Anvil text inputs: Movement keys are disabled to allow normal text typing.
- Creative inventory search tab: Pauses movement when search tab is active.
- Container drag-clicking: Mouse interactions inside GUI are respected.

----------------------------------------------------------------
## Settings

### guis
- **Type:** Enum
- **Default:** Inventory
- **Range / modes:** GUI, Inventory, Both
- **Description:** "Which GUIs to move in."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Selects whether movement works in normal GUIs, inventories only, or both.

### jump
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Allows you to jump while in GUIs."
- **Visible when:** always
- **On change:** resets jump key down if changed to false
- **Notes:** None

### sneak
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Allows you to sneak while in GUIs."
- **Visible when:** always
- **On change:** resets sneak key down if changed to false
- **Notes:** None

### sprint
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Allows you to sprint while in GUIs."
- **Visible when:** always
- **On change:** resets sprint key down if changed to false
- **Notes:** None

### arrows-rotate
- **Type:** Boolean
- **Default:** true
- **Range / modes:** N/A
- **Description:** "Allows you to use your arrow keys to rotate while in GUIs."
- **Visible when:** always
- **On change:** nothing
- **Notes:** Uses Arrow Left/Right/Up/Down to look around.

### rotate-speed
- **Type:** Double
- **Default:** 4.0
- **Range / modes:** min 0.5, max 10.0
- **Description:** "Rotation speed while in GUIs."
- **Visible when:** arrows-rotate is true
- **On change:** nothing
- **Notes:** Degrees rotated per tick.

----------------------------------------------------------------
## Keybind

- **Default:** unbound
- **Behavior:** toggle on press
- **Notes:** None

----------------------------------------------------------------
## Events

| Event | Priority | What it does |
|---|---|---|
| KeyInputEvent | NORMAL | Forwards WASD, Space, Shift, Ctrl keys to Minecraft movement inputs when screen is active |
| MouseClickEvent | NORMAL | Checks screen state and permits mouse actions |
| Render3DEvent | NORMAL | Rotates player yaw and pitch when arrow keys are pressed |

----------------------------------------------------------------
## Rendering

None directly; rotates camera view on render frame.

----------------------------------------------------------------
## Packets

Standard player movement and rotation packets sent continuously.

----------------------------------------------------------------
## Input

Intercepts GLFW key events and updates `KeyMapping.setDown(...)` for movement keys.

----------------------------------------------------------------
## Inventory

Allows normal inventory container item clicks while moving.

----------------------------------------------------------------
## Entity / world interaction

None.

----------------------------------------------------------------
## Dependencies

- **Other modules:** None.
- **Utils:** Input.
- **Mixins:** CreativeModeInventoryScreenAccessor.

----------------------------------------------------------------
## Interactions

- **With Crew (friends):** None.
- **With Loadouts:** Settings persisted per loadout.
- **With other modules:** SafeEdge, Sprint.

----------------------------------------------------------------
## Chat feedback

None. (Hard requirement. Do not change without owner approval.)

----------------------------------------------------------------
## Performance

- Cost per tick / per frame when enabled: Low (key status checks on active screen).
- Cost when disabled: Zero runtime cost (listeners detached).
- Allocations in hot paths: None.
- Anything scanned unnecessarily: None.

----------------------------------------------------------------
## UNKNOWN

- Rename: `GUIMove` -> `InMove` per Task 5.6. Confirm if kebab ID changes from `"gui-move"` to `"in-move"` - requires confirmation.

----------------------------------------------------------------
## Implementation notes

Renamed from `GUIMove` to `InMove` in Task 5.6 locked rename table. Migrated from legacy `ruinerpie.pancreas.systems.modules.movement.GUIMove` to `Categories.Utilities`.
