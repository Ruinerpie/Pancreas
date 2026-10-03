# Pancreas - Porting Issues & Infrastructure Log (Round 9)

This document tracks all 51 modules ported in Round 9, notes on behavior adaptations, zero-chat / zero-client-name compliance, and the status of the six modules requiring future rendering/simulator infrastructure.

---

## 1. Executive Summary
- **Total Modules Ported:** 51 modules across 4 categories (Misc: 3, Cheats: 16, Extras: 16, Utilities: 15).
- **Cumulative Active Tweaks:** 81 modules registered in `Modules.java`.
- **Terminology:** 100% migrated to "Tweak" for all user-facing interfaces and HUD widgets.
- **Branding & Chat Rules:**
  - Zero chat messages emitted on tweak toggle, enable, disable, or setting change.
  - Zero legacy client name occurrences anywhere in default settings, code comments, or user-facing strings.
  - Prohibited combat auras (`KillAura`, `CrystalAura`, `AnchorAura`, `BedAura`) remain completely excluded.

---

## 2. Infrastructure-Blocked Modules (Part 5 Stubs)

The following six modules have full setting definitions, lifecycle methods, and public query APIs implemented, with their complex rendering bodies stubbed with `// TODO` markers awaiting dedicated engine pipelines:

### 1. `TipPlus` (`tip-plus`) [Utilities]
- **Status:** Settings, keybind detection, and lifecycle ported.
- **Blocked On:**
  - `ContainerTooltipComponent` (3x9 shulker/chest grid view)
  - `MapTooltipComponent` (live map preview)
  - `BookTooltipComponent` & `BannerTooltipComponent`
  - `BundleTooltipComponent` & `EntityTooltipComponent`
  - `TextTooltipComponent`, `EChestMemory`, `ByteCountDataOutput`, and `ContainerInventoryScreen`

### 2. `FrostGlass` (`frost-glass`) [Utilities]
- **Status:** Blur intensity, screen filter settings, and lifecycle ported.
- **Blocked On:**
  - GPU texture view + uniform buffer pipeline
  - Dual blur shaders (`BLUR_DOWN`, `BLUR_UP`, `BLUR_PASSTHROUGH`)
  - `MeshRenderer` and `FixedUniformStorage`

### 3. `TagPlus` (`tag-plus`) [Utilities]
- **Status:** All entity filter, player info, and rendering settings ported.
- **Blocked On:**
  - `NametagUtils.to2D()` / screen coordinate transformation matrix
  - `RenderUtils.drawItem()` 2D billboard pass
  - `TextRenderer` with `beginBig()` / scale font rendering

### 4. `OverlayPlus` (`overlay-plus`) [Utilities]
- **Status:** All 38 toggle settings categorized across Overlay, HUD, World, and Entity groups ported.
- **Blocked On:**
  - ~15 individual Mixins into vanilla render pipelines (GameRenderer overlays, LevelRenderer weather/fog/particles, EntityRenderDispatcher layers, Gui elements).

### 5. `ArcLines` (`arc-lines`) [Extras]
- **Status:** Projectile filter list, simulation step settings, and visual color settings ported.
- **Blocked On:**
  - `ProjectileEntitySimulator` step-by-step ballistic velocity calculator
  - `SimulationStep` collision and trajectory raycasting pipeline

### 6. `DropPhys` (`drop-phys`) [Extras]
- **Status:** Settings and public query API ported.
- **Blocked On:**
  - `ItemStackRenderStateAccessor`
  - `LayerRenderStateAccessor`
  - MC 26 `ItemStackRenderState` flat item rotation and surface resting API

### 7. `RouteMark` (`route-mark`) [Utilities]
- **Status:** Empty module stub registered.
- **Blocked On:**
  - Deferred pending completion of visual rendering infrastructure and waypoint storage systems.

---

## 3. Adaptation & Architecture Decisions

1. **MC 26 Container & Slot Input:**
   - MC 26 renamed `ClickType` to `net.minecraft.world.inventory.ContainerInput`. All slot click and shift-click operations across `PackSort`, `RefillLoop`, `ChestFlip`, and `SmeltLoop` use `MultiPlayerGameMode.handleContainerInput(...)`.

2. **Equipment & Component System:**
   - MC 26 replaced legacy `ArmorItem` with `DataComponents.EQUIPPABLE` and `net.minecraft.world.item.equipment.Equippable`. `GearUp` evaluates equipment slots and defense ratings directly through the component registry.

3. **Tool Evaluation:**
   - Tool classification in `RapidMine` and `ToolMatch` uses vanilla `ItemTags.PICKAXES` and dynamic item damage components (`DataComponents.DAMAGE`) rather than obsolete item class heirarchies.

4. **Crew Integration:**
   - All references to legacy friends systems are routed directly through `Crew.get()` (`isFriend(UUID)`, `isFriend(String)`), utilizing `Crew.CREW_COLOR` (`#00FFB4`).

5. **Notification Routing:**
   - All informational alerts, warning prompts, and status messages route cleanly through `NotificationManager.get().info()` and `.warning()`, presenting unobtrusive animated toasts on the HUD.
