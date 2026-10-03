package ruinerpie.pancreas.theme;

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

public final class Theme {
    
    public static final Tint background = new Tint(0x1A, 0x1A, 0x1E, 235);      
    public static final Tint surface = new Tint(0x2B, 0x2B, 0x30, 255);         
    public static final Tint surfaceAlt = new Tint(0x35, 0x35, 0x3B, 255);      
    public static final Tint surfaceRaised = new Tint(0x3E, 0x3E, 0x44, 255);   
    public static final Tint border = new Tint(0x14, 0x14, 0x1A, 255);          
    public static final Tint borderLight = new Tint(0x4A, 0x4A, 0x52, 255);     

    public static final Tint text = new Tint(0xE8, 0xE4, 0xDA, 255);            
    public static final Tint textMuted = new Tint(0xA8, 0xA2, 0x9A, 255);       
    public static final Tint textDisabled = new Tint(0x6E, 0x6A, 0x64, 255);    
    public static final Tint textAccent = new Tint(0xFF, 0xF4, 0xD6, 255);      

    public static final Tint accentRed = new Tint(0xE5, 0x39, 0x35, 255);       
    public static final Tint accentPurple = new Tint(0x65, 0x00, 0xFF, 255);    
    public static final Tint accentMinecraft = new Tint(0x5D, 0x9B, 0x3C, 255); 
    public static final Tint accentGold = new Tint(0xF2, 0xB2, 0x33, 255);      
    public static final Tint accentDiamond = new Tint(0x4A, 0xED, 0xD9, 255);   
    public static final Tint accentLapis = new Tint(0x3B, 0x6F, 0xCC, 255);     
    public static final Tint accentEmerald = new Tint(0x17, 0xDD, 0x62, 255);   
    public static final Tint accentNetherite = new Tint(0x4A, 0x4A, 0x4A, 255); 

    public static final Tint warning = accentGold;
    public static final Tint success = accentEmerald;
    public static final Tint error = accentRed;
    public static final Tint info = accentDiamond;
    public static final Tint selection = accentLapis;
    public static final Tint focusRing = accentLapis;
    public static final Tint moduleOn = accentEmerald;
    public static final Tint moduleOff = accentNetherite;

    public static final Tint categoryCheats = accentRed;
    public static final Tint categoryExtras = accentPurple;
    public static final Tint categoryMisc = accentMinecraft;
    public static final Tint categoryUtilities = accentGold;

    public static final Tint hover = borderLight;
    public static final Tint selected = selection;
    public static final Tint ACCENT_RED = accentRed;
    public static final Tint ACCENT_PURPLE = accentPurple;
    public static final Tint BACKGROUND = background;
    public static final Tint CARD_BG = surface;
    public static final Tint TEXT = text;
    public static final Tint TEXT_MUTED = textMuted;

    public static Tint getCategoryColor(Group category) {
        if (category == null) return accentPurple;
        return switch (category) {
            case Cheats -> categoryCheats;
            case Extras -> categoryExtras;
            case Misc -> categoryMisc;
            case Utilities -> categoryUtilities;
        };
    }

    public static int withAlpha(Tint color, float alphaFactor) {
        int a = Math.clamp((int) (color.a * alphaFactor), 0, 255);
        return (a << 24) | (color.getPacked() & 0x00FFFFFF);
    }
}