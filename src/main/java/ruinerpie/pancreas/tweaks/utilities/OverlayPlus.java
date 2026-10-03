package ruinerpie.pancreas.tweaks.utilities;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;
import ruinerpie.pancreas.saved.*;
import ruinerpie.pancreas.stalk.*;
import ruinerpie.pancreas.theme.*;
import ruinerpie.pancreas.screens.*;
import ruinerpie.pancreas.screens.widgets.*;
import ruinerpie.pancreas.hud.*;
import ruinerpie.pancreas.hud.parts.*;
import ruinerpie.pancreas.tweaks.cheats.*;
import ruinerpie.pancreas.tweaks.extras.*;
import ruinerpie.pancreas.tweaks.misc.*;
import ruinerpie.pancreas.tweaks.utilities.*;

public class OverlayPlus extends Feature {
    public enum BannerMode {
        Everything,
        Pillar,
        None
    }

    private final ValueGroup sgOverlay = settings.createGroup("Overlay");
    private final ValueGroup sgHUD = settings.createGroup("HUD");
    private final ValueGroup sgWorld = settings.createGroup("World");
    private final ValueGroup sgEntity = settings.createGroup("Entity");

    public final Value<Boolean> portalOverlay = sgOverlay.add(new FlagValue.Builder().name("portal-overlay").description("Disables Nether portal screen swirl.").defaultValue(false).build());
    public final Value<Boolean> spyglassOverlay = sgOverlay.add(new FlagValue.Builder().name("spyglass-overlay").description("Disables circular spyglass scope vignette.").defaultValue(false).build());
    public final Value<Boolean> nausea = sgOverlay.add(new FlagValue.Builder().name("nausea").description("Disables nausea warp screen effect.").defaultValue(false).build());
    public final Value<Boolean> pumpkinOverlay = sgOverlay.add(new FlagValue.Builder().name("pumpkin-overlay").description("Disables carved pumpkin face HUD mask.").defaultValue(false).build());
    public final Value<Boolean> powderedSnowOverlay = sgOverlay.add(new FlagValue.Builder().name("powdered-snow-overlay").description("Disables freezing frost screen edges.").defaultValue(false).build());
    public final Value<Boolean> fireOverlay = sgOverlay.add(new FlagValue.Builder().name("fire-overlay").description("Disables first-person burning screen flame overlay.").defaultValue(false).build());
    public final Value<Boolean> liquidOverlay = sgOverlay.add(new FlagValue.Builder().name("liquid-overlay").description("Disables underwater and lava screen tint overlays.").defaultValue(false).build());
    public final Value<Boolean> inWallOverlay = sgOverlay.add(new FlagValue.Builder().name("in-wall-overlay").description("Disables suffocating block view texture.").defaultValue(false).build());
    public final Value<Boolean> vignette = sgOverlay.add(new FlagValue.Builder().name("vignette").description("Disables dark corner vignette shading.").defaultValue(false).build());
    public final Value<Boolean> guiBackground = sgOverlay.add(new FlagValue.Builder().name("gui-background").description("Disables default dark background tint in screens.").defaultValue(false).build());
    public final Value<Boolean> totemAnimation = sgOverlay.add(new FlagValue.Builder().name("totem-animation").description("Disables floating Totem pop animation.").defaultValue(false).build());
    public final Value<Boolean> eatingParticles = sgOverlay.add(new FlagValue.Builder().name("eating-particles").description("Disables food bite particles.").defaultValue(false).build());
    public final Value<Boolean> enchantmentGlint = sgOverlay.add(new FlagValue.Builder().name("enchantment-glint").description("Disables animated purple enchantment sheen.").defaultValue(false).build());

    public final Value<Boolean> bossBar = sgHUD.add(new FlagValue.Builder().name("boss-bar").description("Hides boss health bars.").defaultValue(false).build());
    public final Value<Boolean> scoreboard = sgHUD.add(new FlagValue.Builder().name("scoreboard").description("Hides sidebar objective scoreboards.").defaultValue(false).build());
    public final Value<Boolean> crosshair = sgHUD.add(new FlagValue.Builder().name("crosshair").description("Hides screen center crosshair.").defaultValue(false).build());
    public final Value<Boolean> title = sgHUD.add(new FlagValue.Builder().name("title").description("Hides screen title / subtitle announcements.").defaultValue(false).build());
    public final Value<Boolean> heldItemName = sgHUD.add(new FlagValue.Builder().name("held-item-name").description("Hides held item name tooltip popup above hotbar.").defaultValue(false).build());
    public final Value<Boolean> obfuscation = sgHUD.add(new FlagValue.Builder().name("obfuscation").description("Decodes random magic character text in chat/signs.").defaultValue(false).build());
    public final Value<Boolean> potionIcons = sgHUD.add(new FlagValue.Builder().name("potion-icons").description("Hides active status effect icons on HUD.").defaultValue(false).build());
    public final Value<Boolean> messageSignatureIndicator = sgHUD.add(new FlagValue.Builder().name("message-signature-indicator").description("Hides chat message verification status indicators.").defaultValue(false).build());

    public final Value<Boolean> weather = sgWorld.add(new FlagValue.Builder().name("weather").description("Disables rain and snow weather rendering.").defaultValue(false).build());
    public final Value<Boolean> worldBorder = sgWorld.add(new FlagValue.Builder().name("world-border").description("Hides world border walls.").defaultValue(false).build());
    public final Value<Boolean> blindness = sgWorld.add(new FlagValue.Builder().name("blindness").description("Cancels blindness effect vision fog.").defaultValue(false).build());
    public final Value<Boolean> darkness = sgWorld.add(new FlagValue.Builder().name("darkness").description("Cancels Warden darkness effect pulsing.").defaultValue(false).build());
    public final Value<Boolean> fog = sgWorld.add(new FlagValue.Builder().name("fog").description("Disables atmospheric distance fog.").defaultValue(false).build());
    public final Value<Boolean> enchantmentTableBook = sgWorld.add(new FlagValue.Builder().name("enchantment-table-book").description("Hides spinning book on enchanting tables.").defaultValue(false).build());
    public final Value<Boolean> signText = sgWorld.add(new FlagValue.Builder().name("sign-text").description("Hides text on signs.").defaultValue(false).build());
    public final Value<Boolean> blockBreakParticles = sgWorld.add(new FlagValue.Builder().name("block-break-particles").description("Disables block breaking particles.").defaultValue(false).build());
    public final Value<Boolean> blockBreakOverlay = sgWorld.add(new FlagValue.Builder().name("block-break-overlay").description("Hides vanilla block cracking textures.").defaultValue(false).build());
    public final Value<Boolean> beaconBeams = sgWorld.add(new FlagValue.Builder().name("beacon-beams").description("Hides vertical beacon light beams.").defaultValue(false).build());
    public final Value<Boolean> fallingBlocks = sgWorld.add(new FlagValue.Builder().name("falling-blocks").description("Hides falling sand and gravel entities.").defaultValue(false).build());
    public final Value<Boolean> caveCulling = sgWorld.add(new FlagValue.Builder().name("cave-culling").description("Disables cave occlusion culling.").defaultValue(false).onChanged(b -> reloadRenderer()).build());
    public final Value<Boolean> mapMarkers = sgWorld.add(new FlagValue.Builder().name("map-markers").description("Hides player markers on maps.").defaultValue(false).build());
    public final Value<Boolean> mapContents = sgWorld.add(new FlagValue.Builder().name("map-contents").description("Hides map pixel imagery.").defaultValue(false).build());
    public final Value<BannerMode> banners = sgWorld.add(new ChoiceValue.Builder<BannerMode>().name("banners").description("Banner rendering mode.").defaultValue(BannerMode.Everything).build());
    public final Value<Boolean> fireworkExplosions = sgWorld.add(new FlagValue.Builder().name("firework-explosions").description("Disables firework particle explosions.").defaultValue(false).build());
    public final Value<Boolean> barrierInvisibility = sgWorld.add(new FlagValue.Builder().name("barrier-invisibility").description("Reveals invisible barrier blocks.").defaultValue(false).build());
    public final Value<Boolean> textureRotations = sgWorld.add(new FlagValue.Builder().name("texture-rotations").description("Disables randomized block texture orientations.").defaultValue(false).onChanged(b -> reloadRenderer()).build());

    public final Value<Boolean> dropSpawnPackets = sgEntity.add(new FlagValue.Builder().name("drop-spawn-packets").description("Drops entity spawn packets to prevent client entity spawns.").defaultValue(false).build());
    public final Value<Boolean> armor = sgEntity.add(new FlagValue.Builder().name("armor").description("Hides armor models on living entities.").defaultValue(false).build());
    public final Value<Boolean> invisibility = sgEntity.add(new FlagValue.Builder().name("invisibility").description("Renders invisible entities as translucent.").defaultValue(false).build());
    public final Value<Boolean> glowing = sgEntity.add(new FlagValue.Builder().name("glowing").description("Disables entity outline glowing effect.").defaultValue(false).build());
    public final Value<Boolean> spawnerEntities = sgEntity.add(new FlagValue.Builder().name("spawner-entities").description("Hides miniature spinning mob inside mob spawners.").defaultValue(false).build());
    public final Value<Boolean> deadEntities = sgEntity.add(new FlagValue.Builder().name("dead-entities").description("Hides dying entity death animations.").defaultValue(false).build());
    public final Value<Boolean> nametags = sgEntity.add(new FlagValue.Builder().name("nametags").description("Hides vanilla 3D overhead nametags.").defaultValue(false).build());

    public OverlayPlus() {
        super(Group.Utilities, "clear", "Disables selected client overlays, HUD elements, world effects, and entity layers.");
    }

    private void reloadRenderer() {
        if (mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        }
    }

    public boolean noWeather() { return isActive() && weather.get(); }
    public boolean noFog() { return isActive() && fog.get(); }
    public boolean noBlindness() { return isActive() && blindness.get(); }
    public boolean noVignette() { return isActive() && vignette.get(); }
    public boolean noPumpkin() { return isActive() && pumpkinOverlay.get(); }
}