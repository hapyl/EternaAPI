package me.hapyl.eterna.module.player.tablist;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.util.CollectionUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Static textures for {@link TablistEntry}.
 *
 * <p>
 * You can generate textures with signature at <a href="https://mineskin.org/">MineSkin</a>.
 * </p>
 */
public final class EntryTexture implements Skin {
    
    /**
     * Equivalent to {@link NamedTextColor#BLACK}.
     */
    @NotNull
    public static final EntryTexture BLACK;
    
    /**
     * Equivalent to {@link NamedTextColor#DARK_BLUE}.
     */
    @NotNull
    public static final EntryTexture DARK_BLUE;
    
    /**
     * Equivalent to {@link NamedTextColor#DARK_GREEN}.
     */
    @NotNull
    public static final EntryTexture DARK_GREEN;
    
    /**
     * Equivalent to {@link NamedTextColor#DARK_AQUA}.
     */
    @NotNull
    public static final EntryTexture DARK_AQUA;
    
    /**
     * Equivalent to {@link NamedTextColor#DARK_RED}.
     */
    @NotNull
    public static final EntryTexture DARK_RED;
    
    /**
     * Equivalent to {@link NamedTextColor#DARK_PURPLE}.
     */
    @NotNull
    public static final EntryTexture DARK_PURPLE;
    
    /**
     * Equivalent to {@link NamedTextColor#GOLD}.
     */
    @NotNull
    public static final EntryTexture GOLD;
    
    /**
     * Equivalent to {@link NamedTextColor#GRAY}.
     */
    @NotNull
    public static final EntryTexture GRAY;
    
    /**
     * Equivalent to {@link NamedTextColor#DARK_GRAY}.
     */
    @NotNull
    public static final EntryTexture DARK_GRAY;
    
    /**
     * Equivalent to {@link NamedTextColor#BLUE}.
     */
    @NotNull
    public static final EntryTexture BLUE;
    
    /**
     * Equivalent to {@link NamedTextColor#GREEN}.
     */
    @NotNull
    public static final EntryTexture GREEN;
    
    /**
     * Equivalent to {@link NamedTextColor#AQUA}.
     */
    @NotNull
    public static final EntryTexture AQUA;
    
    /**
     * Equivalent to {@link NamedTextColor#RED}.
     */
    @NotNull
    public static final EntryTexture RED;
    
    /**
     * Equivalent to {@link NamedTextColor#LIGHT_PURPLE}.
     */
    @NotNull
    public static final EntryTexture LIGHT_PURPLE;
    
    /**
     * Equivalent to {@link NamedTextColor#YELLOW}.
     */
    @NotNull
    public static final EntryTexture YELLOW;
    
    /**
     * Equivalent to {@link NamedTextColor#WHITE}.
     */
    @NotNull
    public static final EntryTexture WHITE;
    
    // Maps default minecraft colors to a texture
    private static final Map<NamedTextColor, EntryTexture> chatColorMapped;
    
    // Stores cached player textures to reduce reflection calls
    private static final Map<Player, EntryTexture> cachedPlayerTextures;
    
    static {
        chatColorMapped = Maps.newHashMap();
        cachedPlayerTextures = Maps.newHashMap();
        
        BLACK = of(
                NamedTextColor.BLACK,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTczMDQwMiwKICAicHJvZmlsZUlkIiA6ICI1NzgzZWMxNDgxMDI0ZDJmOTk4N2JhNGZhNWNlMmFmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJCbHVlUGhlbml4NDMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjY2NzhlNjNmNzRmMWM3NzA0YmFiOTA1MTQwNzRmMGNkMTA1NGE5ZjYyYTYyMTEwMjk0MWYzZmYzNGU5MGI2YSIKICAgIH0KICB9Cn0=",
                "fCAPSPJO4zJKnLTESSFgm3bynVxDrwRfpOW+qsqZnCEfm8glh/y7IWhJu0EBDkGYsjWr+6R3xH4FFByNY/asQlCQcAeywv/NWfU1aXRr0d+RLOHTt9i/lFlVWgZl9YanHaBEx53PCEgibjl6V1dMg9d0vl20MkplRg3bTmWHZrLzW7yZq2lHJY6ne6y1MLnltIvm8WaoNGRJLopE5pnC7A8P7SGowWGUAd6PFe2NHcAeTN+7oFpwaxYLCi8SRNhTI6tDsPgKjVtinGu+d0bQYHcNUT1VDofQcZ8g3NlJ+c7XT+VjSQs1Nr/ON/dSaUjtrWNXS1sB8JIXf0snEGUlS84Va3zGhYakdBU3+wkG+a/kstMTmYA2GhVSGgq71aMlNDFpT1PEXt7UP8JmmUZkfyIECpNvJ8AxxWWbblWkyxzoHG2EK1Lrw/9s9wvg0WSiOhnm7HNND+eTaEFWDhBDKrU0l3WuLr6coltUU5K7nv9XiUEVfjaG580u2PgHxSsnW9id1b+unM8ciX7YlGOT02/iaq7HJ2bkHYrVhGwiClywGF7TdErhs85TvLIqyRKv5E7sZlDD10wmb3wqppKV70pY/E8FhlBwtlIEbAXyTcrvNYYtd3Kl/hVyaYiglpNUJJPFW43wCdtYI4TZ3Phe0gEUiQSeOI+6zE0RXGbufEg="
        );
        
        DARK_BLUE = of(
                NamedTextColor.DARK_BLUE,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTc5MjY0MiwKICAicHJvZmlsZUlkIiA6ICIzODhhMzgxMmRhZTQ0NDFkYWI4MjJjODZmNWY0YjNmYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJFbE1henppdGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQ1OGViZDAwZDhmYTgxOWEyZDcwZjVhOWJiZWY3MDc0ZDFkYWEwN2RlNTNjYzJlNTZiYTUyMmQyZTExZjdmMiIKICAgIH0KICB9Cn0=",
                "rgWJP0MV2DrpPuuzD8dmCcVPBf5pPM/7Z4Hu6lpXL/CymFGV7IRndV5YXo1iXyAiKASAnl1odlWzwsI64aT6W3Oxmb8l6uWE9pRhBGFx2lEijV8ub9iNUvJAIDRyVmLj576Pyu1rtPS3rBw6eJWlTU6FPWpEQb87z6WIk8J6ev35Nxu7bCVEDWzt3zOQgD4U51JxUngtHLD23IU9zH/TQToJiANpv0MCseDYEXlS76LovzAChFHmtv/P4oE9+7Qh4ekbBari5Hfz9Jl4Gt50KGrgqt9d18GDV5mySm/00/vtQ2z8r542twxe59a94jouavC4G+VuyHPt5ZBuEOtSksmcmgK+VMo16/vIN9FJ/bXVQ0/+atWwDovm4L57R68eytiFm5zobNpMVgnjqsZUW0Hfo8eX8jIfTBBPAgtM+uASz5XMFWRiR5zyG/guPwoKUk68WwNmD37qsKOdPY4pyytWZ2V8o6W6sxuN8wmZg96b6r1UrXOCIE19zgAxBOzozOjdl7QS54taB+tmbVR3lcXqnO1qmMFiFySeGRaf9vNfZLPJ7MF2gtT43p0V51VZqZ6bEw6jlXqk6LEI7KVcT+djs7tlTFJZ8W57orJe8CKSnDRlYMd2phIE+1x8RNFN3pFW6+x+KvNaTN70B+vLG36O1u46ARlDIG/dOiDbz6E="
        );
        
        DARK_GREEN = of(
                NamedTextColor.DARK_GREEN,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTgzMjU0MSwKICAicHJvZmlsZUlkIiA6ICIzMjNiYjlkYzkwZWU0Nzk5YjUxYzE3NjRmZDRhNjI3OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOcGllIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM5NDE4Mzg1MGQ2ZTI0OTk2Nzk4MGQ0ZWIyYjE5MGI5NDA0OWEwZmRiMjQ2ZjRmMTcxM2UxMzM0YWFlOGU5OTMiCiAgICB9CiAgfQp9",
                "KHF869dycUt54WVXq61u0iff3tfiMOj8GoVV1TeTUPoBBzM0L2M4J8vd2S53MW8sNKuxWVyJGQGBA09R3qOZDY+gnWgucUtjp/ULyotermDBYOs1gxVmu5nXvNJsLHt8P54PGh/f1woWXFTNHh7Qo/nxAuxdHlKb0ygvV+A1b2PGd6Wz3hxgBBmuufJgkUx3GaO1+cTQh9odZ8mE7c+28BNkt3/V6xHnU+rQuTZkqeCSlXeqi5o3eg9vcZrp6hELEe1APrtT+F5K3EqzVei/GhiyD60D3oUh7/kivELS9ljH0JIdhVkt79lktJhRw+hKgjHy0w5DzAGMvRbAk3n1cwWSHz88gP8FdCNRPPjyXjrD7DGy60uKDt3NsFGpvcCy68ALM5uPZlDVLuDabkhnt24+X5ftL2AYfZ4hbf2Qy+yudSSy0488hZjyMxw8/v7cEZRVZhAxXFiN/xxpNyC6NlX0K5wHkPwn0Lhxu8N3UXZk5YIzE+/SEDHS6Ex9Tm93j7Cp1M0IkTwQYrxCKhgAy4/wOkM6KHZAUu7lD0gYS6a4cYJC40TkLX4ETBNTFPyTuEVOEvPDNhyATgxhOULdor+IGd0qM2syYRt1T5tsiIcYPnu6g7LrAgUt5li8J38V5gR8C3eMX5SIddFq2OKH3WRAD38z56IQIJeslrscdlA="
        );
        
        DARK_AQUA = of(
                NamedTextColor.DARK_AQUA,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTc3Mjg3OCwKICAicHJvZmlsZUlkIiA6ICI2OTU3YjQyOTZiOTI0NGJkOGQzNWJiZGU4M2JiZDI3OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJjb2xpblBBUEEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM0OWU4MWFlNzU4ZWM3ZjFhNDg4NjY1OWFhZDJmMGViMjA0MWYyNzU1NDhhNzczZjkwNGU3Zjg5ZjI5NWYiCiAgICB9CiAgfQp9",
                "p6KiqBSbe7hKWhubJ4HOqfWbRvUGSKyiLjVZDL0/+t+WnpVBqmtbEuI7lyWU6V3/EgmCmY9/1haAqZedr4C/h0auqq2Iuv7tN0yt8yPaSw3ZMWGsygmJukgV/xy8GZEyUoOsgLQQVjNznWLj2s9VNyj1PM0XjsV0+/lv6uACyCBD8M/Jmn3oMt1E1i2VSCoUrlLFzr/e+ePVq5vwDvA8kTCExeKtXb9KyizWQqDQs+Wj8G/I98VnvWxosHPi0Co4O1pRGmTzHTKMTfE5wfRL7eVmHfXfE6HTTllS0vW/wo6uOqzT/b/YpQkaV57FR/QD4x8iAHmI1nBpX17rUdkmjWF1bD4+o0B27HV4lfiD8cUnmOSU5WzTKFUnQA9O3xoHN6/6fk5KgdtvVCoCUaBJgQvxtsNQPAENq+D4Rz6Vv80P2oMDJywsFB2sUiXGJvYBsU4Ox1Xby03SJt66mau522fzTPRGYfaWx+iQKZFbXekqpVZudGXBQ72zQe0qbD2SrJmrwYpimbtez6VKFfKMsnC2UtblKY0ycTrnEhq3T6psms0ZDYjekgdGgpGs+Q/Wp/keXkh4t6SX+EU8j63eK4WBlomu0qMCSM3FZ4FMv4AkkKW6Oh3oLB21TayiagNqo08ncKOj1V3nMdvBy+9u1iu8bI4BEmAZtZz3aFU5exw="
        );
        
        DARK_RED = of(
                NamedTextColor.DARK_RED,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTg3NDE2NCwKICAicHJvZmlsZUlkIiA6ICI2OGVmMmM5NTc5NjM0MjE4YjYwNTM5YWVlOTU3NWJiNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVNdWx0aUFjb3VudCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81YmRlZmVmYzRiOWM1ODQ1YTI1OGEzNjNlNjhkNWY1MzFhMjEyNDk4M2E1ZjZkYzYzOTE5NDE3YWQzNjk3YjQzIgogICAgfQogIH0KfQ==",
                "apXqzjU5OPrRVwf+ZbjAM7gWubchGnihAw6MGO2KATb6Z1m/8zhSHM7s2X1Wj1qCHidVTRSHKSxOT3bdVE++oeiiHPpV3JjhdXuLtvoCju4NpXi72w/QoNnmpGl7Y9oaNdFq2nc9s6YKizGEwVgirR/TZt/i6oD+pFKlSDCj9QnS7tamiBUQdjHGXr1rdaXu8UrCewrCd2MNHPjZHQJVNtFH5qEgodOPXCOdQk8U46c40Q9EQYNurp1Fdrq2OrK/slyuKdEoarZ2t9iWgIkVONrlr0iyyV2BCqJv8r4WQL3Z0429CflrDvpF8eSqX94KW+aKhepS5lF/NbihU90zOJtEJ3CztI2qD9FGF5lVVyBBKeuOVGrW19+jI33AtVJOcTWhm46XiqdFRqHDGnnay3ATRGSsJpXDugLqaBd+qTzUJZykAk3ZuOO5sf+l52Kj496IjQweUseMkqCjjkGbH1vhJr91Kp25yc7Jz8GhowovvzITZYW0LiTlYL9BLNzLZ7aK5OpKGVmkvb8Q51wGdUO9A5TQTJpRG7N/XKa9dR7hx+4QallE20Z/Hq2r/kvakPdm+GhrECw+eGRbfwQUoJZkIrAcjeJ1DOymNGaM3PEdxH0bIs1//gbseXQVSuGDMOq0Uqn8H5oI+C9E29a/BesR1Ia6NnfAa1ZNPVAAA9o="
        );
        
        DARK_PURPLE = of(
                NamedTextColor.DARK_PURPLE,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTg1MTcyMiwKICAicHJvZmlsZUlkIiA6ICJjYjYxY2U5ODc4ZWI0NDljODA5MzliNWYxNTkwMzE1MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWb2lkZWRUcmFzaDUxODUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRjYjllMmE5MGRmMTY4MjdkMzM0Y2FhOTg3N2E2MTBlZDNjZDUyOWFmZWUyOTgwODcxNzljMGI0YjUwODhkMSIKICAgIH0KICB9Cn0=",
                "J+q+Qw2t0O+P6PyAFS+ZiCjMIuzTZ3rJjRdqbR/uStFIZvnvDEcIoF0oxBcFmpLRaupv2hJzoaM+aXeOwI4DjbrlL/DwIw2eaCa8FzUaD+8TRfzMA4RajCyOL3Y4p7Y6TASa6YVhfzwWgvUAGRXQcsq33avb1UxxdVzfpR+XCv+AzbM69sVcbUt6tWK+WsSS2xKR54vP6k6xFBj2bL+ukynqcEy51fz77T4rr0vTyHuK5uAAb10UqbqX3EGjVYnGKy9EutTpYiAFcgnc8Rx6XN6mI5CEsJ/eSxVSeeVbSVMLCiwZY3PYScx7vuJtlW6VqqGDJyNizsMlMXJfO8eZa2fUSDXinzbtmmpq8sqsuIvaHPqJvraCF1476/Ro51eIbUHErK7Aatbbd2lxSzvwbGLe6vJXckxjtzUJjm7FJcDlPaX95TU9HlQBenH8aj9kLzuDmgV+SSh1z/mU45ubdbrglSnLWMqFPWYB5FSotcdTZzgQjzojHHdHBUSxRC0PpNBPy+AeD8PyidI8SR/78PJSPdZpB4h6laEq+FLJXEPNxD3wYl0vU8M3fY80+Lg+RaurSCXZWZkJeF15GTfyA084Gb7Y9E7VI5ohh4Ir3yA/1Ad8nFgjd1vgfXn3Q4HUaSqfMXG9UBBUE+/CNhF1q/WySKmBWxwf4J+n9JpG9QU="
        );
        
        GOLD = of(
                NamedTextColor.GOLD,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTg5Nzg3OCwKICAicHJvZmlsZUlkIiA6ICJmYzFhOTdlNTgxM2Y0NDI2YTNmZTI4ZjJiNDc1ZjA4ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJnZXRPbmxpbmVQbGF5ZXJzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U5YzAzODZlMDk5MWRhMmNjMjg3NjAwNGUyNzNjN2NkNjdmNzgxYzg2MGZkNTVhZDIzMDQ5YTU5ZDU4NTgyZmUiCiAgICB9CiAgfQp9",
                "FUkK02lvwpogQqVCOIdvwJB81fxTuPswKDS6NQC/6xS1zk7NzVX2EVRkpwRoteDhjPW8JZF1c54cELTBIsPBFtpY5yAHHng4LEKGtxSItbCbDEBg07N8GVFA45PMOvPHQ4xW1XdcAR5N050ByJZaqjMx96AfKlJEI00ampLJmQaqgafaUKggKVG7552fupEIig5+6czaFiub/lWq8Skj7Uqqyyjqe0AEG2q7FCdcd57XeoUOJVuOj5qJnT9B1kXHbYh+oYsgcf5JWPNZq30v6aVRYiCXt4m16g1eexcgJiQ/MYWXOg3DIX1WtrR5Kz/voEokR0IuTVC/uKiCZQVZZ6DsTpukeEgton3GnA574l8mGNr3J/UEy75mgXZfnjM3V9122Xh00R3qCSnYM5dZA1Pechwo0kJectxXDsDMsaiRAH8fTW1YpeizxF2jy1euYRJkMXKIh1c19uOcjyihWtj5djaiuYXi1H54wT9qE+qTuNvvDQL+eEzJ+shsJAYxHUnVhMag2D9FZSGjywbG+6/v5hNHyT2t8NHItgWzQS1vLGbOgOx0i6clRacPy3RFB2BjEBES0XSQhN3Tq5AmD6rHqix2q9z1rJ5RsbZ5eYBseuodUW0U6Zu8wlppTJAmAFPsFG3WtccCpL0hdxtaCpba52xNZF28sxOmzxySV/c="
        );
        
        GRAY = of(
                NamedTextColor.GRAY,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTkyMTk2MywKICAicHJvZmlsZUlkIiA6ICI5ZTA5YzM4ZGUzZTY0MDA2OTAwYzAwZTJiOTQ3ZTQwMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ4RFRPTUFTX1lUIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JlYmNhMzg0YTNmNDY3Y2FkYzUwMzQxODQ1ZTM2NGViZjlkZDc4YzEzNjk4ZTk4YmFiNGU0ZGJlMDY1M2Q3ZGMiCiAgICB9CiAgfQp9",
                "X+jlJJ0Lc/bbyCmm993IZX9A3kHEAwwJHkpga6zeFa+CsGvTr/rFO/z8deWaSuwoAvPQOZ66lo61si4qKPxQC7M60S/NS/cs3KA3Hs9v+IuytCme5FojxqJ0lDSZT+zIpaVdBprQpQLac6/evKKSo8qXY0ypYC8Z1ivzKOtn299fxEryPZi9f+3/CX6Qum5+t6ErWRHFR3tioz7eRWejp1kZVzr758gtEAOko6Q26+mRxRTpg3WR0/o1+9Rp99igPyaEhwZGHHG7Ji9GZDMllTIET6tjvu24CQdWZI2wuExPKV8PzwTnj1FsP+alCtpnTIJGqecIp/QX8JNUAe361qzp049Xh2tEYXHjW5/FuYzVEhW9FeqQRpR0knj6GIzH/wDedRztMNZQh0YdcLZ3SNbilCUSRAJhufyNzxiJN5Fz/2uSReTxe2I5tSK1uM6mZAjDDA5riPHCYw2aYKH+U7F/PR/tvR7laZaRU1YKrFT9phoayoznWmfiXeTL4yTryBTZRQ9zKemx3V4Zt0gJiWN+BDq3r93klucPAYjgnVv5QhR0j3nP0lsp1tGFKtKnAfCShuKSVtVmLMJzhMM3Ch11ec7rsoe0hwOTabxKPJSpgyMSrc/ICcxQH08l4FXq/lk1NWYCGOf9LnzTk35LLjnF+ufDWjoIr75S39ccvAs="
        );
        
        DARK_GRAY = of(
                NamedTextColor.DARK_GRAY,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTgxMjM2MywKICAicHJvZmlsZUlkIiA6ICJkZDNjZGJiOTE2M2Q0NzgyOGQ0YmZkODZmYWE4NGY5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJFTFdhbEtlUkpBSkEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUzZWMyN2RiNzViYWNhYjkyN2U0ZmY5OGE5N2Q0YWZiNWYwZjE0NzcyZDc3YWY2Y2I2NzQ1ZGRkZjMzNDhhMyIKICAgIH0KICB9Cn0=",
                "X/hd4jg3gYUrNmLXawvoi7D+ZK/u8FclyShFDj7EX1JepaNMnUp+Xor3QnEl6LqQq8v2021VxD63QlA58HPGZN2qUo9EmUgU3fkOjsjx9lZK/Kc1LH39pqohBFbqkrg3qk/9DMbZWH7dul82USsml+dd4N+5YE63JGPycKifEBjSQNijgMvX12sQ3yz8aDdrwmXdMeEQODPEOw1O+5oQjnYWTkA0KGQ4yWjicWSe64NQXs7xs8M4opBQq41VkSRfkg9U4qQnNHpwdWHaN8VY17laiUXm3a3ZxceC1jD+bFMukyoDB7IfmEhsZlMd/b4tZo/RP65x3wl+2YuTS6AVkZLwRne8b/hAQVJjgeFQFPam3jkgiQryYLIoWP9+rCdPZgeUljMDZgB2Z16zj1FSSG3wNrU/UAwkFg5UXkq3JCfBXk4VGcplh0QLBLSKeVLhiJZIrGVBGDyajpzk2EL0+Hf2AsuhdxF13SJCu3+fbBs+hjpmkbBnTyuWxWrAfdx+ouQrxokRxMSoDxlnCMUKqJKKBrNo+Kau4/DlfauL/qnThtrM6PTeAtE5samA+hRUJGdAj6TVsU/k4CjIZDsi4lU9Nm3g850H6VdWQJTiE4B+D9qXKNnkbNmrOKkBj/5DiNNIxKUtkiIsnVZYOTtqj9oc6LpVBn51y8m7uXUDxbY="
        );
        
        BLUE = of(
                NamedTextColor.BLUE,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTc1MjUxOSwKICAicHJvZmlsZUlkIiA6ICIzZWUxYWRlMzljZDI0ZjFkOWYwODliYjA2ZTkzNTY5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSdXNvR01SIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y4NWVhZjQ0MmI4NDg3YTJmMzVmZDE0ZmVlMzI2YWIwYjlhOGIzM2RkMjllODJkOTE4NTA2YWQzZGI2NWRlZDgiCiAgICB9CiAgfQp9",
                "xwHWfWP+MFnZMOVXsfvwZBhFE/IXKua03mcCUgYXxRUy1VyxQ/EXm02Z5VArKaDBXNGdmskwyaEMAFIaAQclT6CL4I69+FeJXjGaAW70YDOqrVaTrmkpcNTuGHLXAt3wuF6k+PssziJ0mC3XsCpXjE1xR6J8zDkwzs3Xisn2Aq+V4O+05NvDVL66oTDNFrVwOWCKx8IOl7XDom2i1FeRS5HeUeiYE+x4jQIFqVDDReUodk0y++yNyG7DJlk4Z1DlUY7O0A58SVP0ZgvRaCnZjtB11zOpFC1vG9wauuI2X/8ecTP0ir4MYUaLwAHjJOFup+BPfoFFqJZ/zdHwffb5Ap0w/yj07N4Rh5NIoftoP6XNs2Q1STVIO+F3qmv4AYY/0B/HnErZoJWS4g9K1SPx8UZnUKaptaHhRAyFmoM8vjy5HKVFYX8qGf08OoZXHSbUSKGwej8ylWuxLALZNy8WNmY4j7RkqN4uOIUmuSYB+nbFlRSBcILZkWR7x7LvJ/0eQfV/6BlHhJ0eZLmaVre/B8WQA1EZTsULMcMcDy4P6HeWsEqXrbu9jUvwDxYi82i6/aynhJVa3oUJOj0girRwlRbvVzQRgvMdWKTnRXqTWGcfi7tBNaPHYHhtdOXVzRC1a4VZR3OMrDYDHtUtPgp8q0sbITYGQjCfXNY059HxDoA="
        );
        
        GREEN = of(
                NamedTextColor.GREEN,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTk0NjI3OSwKICAicHJvZmlsZUlkIiA6ICJlY2Q0ZTI4NjdkMmE0MTE2OTljYzlkMjMzYmM1YmEyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSYXRlZEtub3QiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE0MDRjMWU0MTI2MTRjMWU5MWU0NWE2NzJjMjUyOTI5OWY5ZjFhNTJiNmIzMjk2NDg1MzdlMTIxMmViY2VkYiIKICAgIH0KICB9Cn0=",
                "rkCEdohkQ2KBx+Afou+2IbLHKiR1oEsxmyZJ60s9ev+U9q6jRWj0vIgf3MqcLsheniq8qJrg3CDL9hKwhTDadRx5GQh+B7oB3K42eALUMhGXENlHJdvHeRJGHW3QZ3iZwv//R8aMDO6titWMW3rkPYsU/80U+MSehFT74/P/rKbrOBn6aFe6B6D9HevLRtLaKUuy+szb56VQVNM19k9DoFIOLDKAwR7+wxmJ7X8aC04LQBzDdsb/ydAbVoX2KcP6qLK8BboydaHOD1pi7MFV/ONvWRqJ6Rbw8T9rhC4e5z66fudA1nIRhG/71UYSH3P29eqyDQWdiuDwwkVCZbk8MHDh4FfCwpJKfd6zfySQfqab9VGVlvjPGJgegbLe+pMvRQHaYBLqeQ5MdWNmjZ/SHH5TWdZQXngxXeR/qOUpGowv626EjJhKAfAc0JwiUtKmyE4rnpUNc2rIuP7N8jZtkqCJw4DY9dA1hJC1lby0Dj0JL2repGtpjVRSTwDYCsr+FGdVv1SLDi4jDKzQQa0HnND0fL/2OfNPJPcFVFIRQUJXF3TVrgiBLiLvGba1eFtZZctqphMIj3HXYwuDmpcctmhvKJZBu7aoTVZjyLDa4l8JDdIF4SMYh0lxbUiS/CAfrKyPy/pEm/fzdjYuhqG1pb75f9sMHKmH1cI5G5M9iS8="
        );
        
        AQUA = of(
                NamedTextColor.AQUA,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTQzMzAzMiwKICAicHJvZmlsZUlkIiA6ICI3ZDI4M2E4NzM3OGQ0Mzg0Yjc0ODM0ODBkYzdkMzgxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYW51bnVfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ViMTUxNmJkY2I2NGExMDMzOGJlMWY3ZTBiMWYwYjJjNGNhMGZiNWI1MzJkMGU2ZDEyNTFjNmI5ZjMyYzNkNmUiCiAgICB9CiAgfQp9",
                "DttDenT520xJrSJA9mMyRwc79msKs+gJ4npCFuefVhaEtDzdgKklOGxGrkdNiu0CYGfzdzY2dyGQ/pmXpqN5Ktn/kMGBtzGjRIa3+TqNWFUUvh5CcUqpJ8Rz8ERC+nb9feM1OOd/B83qrRSlsGOTL9TEbLTRwK1gTwiipPAJxf/xxKKgM0VxBs+bsUTWmn+3sU2D+D3UJPElKbr4QgXq1oGPuWOjwNUTTagLkgk2N9crzkV8aDkOBJ3gbfBnm7fdxuenDdPrSFIRtpQz2Y02PloQpwGa6zyqWCyLiZtrxDar1KMNvQ3Mfnw3W0Z2TKC+H5ZG26iflRCK9JgbKBtHAnyDcKjy0iKM6eHW/wNpXue+GvxBQcdsXfXE5pBT08MNYudx40dbOpCkWyVE3rWRy7VvALB7b/snICLivlHBx+Af3c+FP6i84HcWYJxA77NvFb9hyp2htXIUAo9b0uaoG54hcsXZSTm55ZovVaDaYF1hgonT2Fuy+ZlFdtBZREWBGMeQAGhGt8IskOJqOqmcE1v/AG0nmiY8zVoeXh/snXXmGugnanW2AsnAUXdGYzdVFPiaBE2UjxUbGYcJXTgUFNHxF1fJUZVIsaGmCgzDU6IYLHhxpKyha0J0RJzfyR56gsUTafrvud+sb0IQp7mMh5oYcqGdbUfISzpb6PYUcyA="
        );
        
        RED = of(
                NamedTextColor.RED,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTk4NDM2NywKICAicHJvZmlsZUlkIiA6ICIxZGI3ZjUxYWZmOWM0NTcyOGZmNmU5MjMxMmZhZGZkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsdXNtZW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWExYThlYzUxNzk5ZTBmOTg2MzQ3NDFiNDc4NDA4M2EwMGJlYmQ1YmQyNTNkNGExODc1NmJjYTdlOTAzYmJiOCIKICAgIH0KICB9Cn0=",
                "m0if4N+YxK5lrGovlReCbi6LJ0lS1f9nY3dQ0XZF3f2vNuotezKNqa2NCgSsjDR43A3DB5iD7GbDxHbVqK1ZSUKwnHzNbYKjo0CsDnWXjyIrEo5yxR96Nve1RMMuL7Q8iFvCb85xG8Ek1sktVnE06hAZbjqN8fWflBiUlwRohKEcKJeuc8RPqFwltRd20KrgMEYhYtTcWk7if7t8c3NFRuib3CKUocpOrbDBKTWjKdeB0RkAobUVwOyjI4poEj+yN/BWjzA6Y6Ul+R7JJnBBaNPa+nxNWVyV3PzP9h+j6392VbecOem8RDvPhhOBBvFCnYRzr1ZM5yzdUKR6zIlyDOxrJJQMNrJHWXH2KFMwRWZRpiVQYzjEbDBUKRcbWHvZ5B7mz8X8TbiajbOoGcjXrKfUEq+3xDmGbrJzknLwODCCD1B9Y7pnMF+SnuCn5do5osCCXvbg52Ep3Ix3J4unuhhr6sG23l6M9tfTP7VBGKRYSOyWMnVV6maoEeefZ+8JNuiAeL1uAaUuRpzt7cJYYuS90eaHw1iPS9/bihg+PdKZZUa8XirFH6BGSN2SiUW23w3my3j9rX8ri4iDM484f+wTprjFnm23z6Dzsiy6oUVLgqEG3PbQKqv49NFKQOQMHgvetoNT1ZM4+O0UfbcMK3Aa6YPVd+j+qaS7GLibNvE="
        );
        
        LIGHT_PURPLE = of(
                NamedTextColor.LIGHT_PURPLE,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTk2NjY5NiwKICAicHJvZmlsZUlkIiA6ICJiM2E3NjExNGVmMzI0ZjYyYWM4NDRiOWJmNTY1NGFiOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNcmd1eW1hbnBlcnNvbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jOTMzM2QyMjlmZDE4MTAwM2MwYWYzZGI4MDkyYzAxYzU4ZDYzZWJhZjE1NDBlZGI3YWUwZGZjYTIxODVhZWQ3IgogICAgfQogIH0KfQ==",
                "fcZuwh/xS91pyHYgM3tLBt4scCYtkTR3pcjD5cezQs/O/tCaknIsaujJDQ9wKzuakpnRTRc+QjIC1kSKnpjSFgoHXd2mWBGWIFasRFYPIiZqp0vEt6BiAu/+sNWAofh0cVO8m2HumjALe0tttrCKe/CGSf/5BirGDIR5NtsoOf7es0csEra10ic62ySmqEz+1ww1IKMoxGSonIPx7RT2esy+s9MsK9jBfxfxqjkX3f0KqAmoMYpAytZ6OHt6E1zh0qal/uzUtthyRpWvizD4BGvbXzPBwUjt8TYbw73AYzfbcVJivmMPFznC5SyQ7S51i0j/YEIVoHu31rZXED7z0C65KNv5VDg+QmxrBalx00luyTbBzYPXy+f1Z9l0sSY4ViVRNyvUiyAN2auV8Qk7XEC7wo0KMFSq8KF5jMIdREaa21av6Mgel4cKM1m+r7grzLuOke1bTa2Vr849AFtH07Esf9SGvQ8yt6wKZ4Xq8KCiONzDJVbNrBvZHIDi9aAoJxfE0fECfaPyIWLV2cSKmb4WEUmlvvTiOV70jJPR6uI3IXsWPjQUrLnjH0cHlrA80ehwCl61xp3+tAEgULDRLIMOlW2gNvCv1mG17R5fd49Xsp+YKrvHWKxcjltlClAU9hWZIFMhHeGP6hWmaRX5hQjp09BZZOcEgKbJ535U0N4="
        );
        
        YELLOW = of(
                NamedTextColor.YELLOW,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NjAyMTk0NCwKICAicHJvZmlsZUlkIiA6ICI0ZTE4ZDlmZmM1ODY0MjczODZlM2YzMDU3NDQyZDQ0OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJpMTk5OEx2dSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lNzIzMmNhZTc5NTU3YzA1OTk4MzkxNjIyYmZmYWU0NzBmYTk2NWNhM2NiMWViNDczYTZhZTgzZGU1ZmJmNjNlIgogICAgfQogIH0KfQ==",
                "XnR28WuwUfKRCemVn1gGYlP9KqmY+dcbLP+AL/St7/edQgFrZ5T8IQJIr7grbCaZ6ICwuLaQ1bqftsrIm+W2OwAtX5UwCJu24PzzNKrASQsjEiKu3S1IjEaEMOLuX8fMt5zcMpqOq/H9ja0cCsSTPl6z8pMHMAITHDMROqVbwyzzebrFpeTIDEvUUoj47Pv747YA/BmYxsdzowMA1npfNQXMjJg1cZ3AxxJZNK8OQ+Kt9xEDUEPGjL1XiXYxfGFE5V7a2vPDS+PKCowdnp2Y4ho15OnTsY1hMHQ6gpatoa/BGTy8JJGlUGwNeitAw3VY/+wa1UAlvEc7zR+vOzwYwixI36+3oU8eclC/rV5HsGtVsO/bn9lB9Hf4heaKZmkrGzYGaYSXArw7byPuXxwS9KlSnT73xWiDmlyfr79MVzbyvDT2hyRstnsnLaeFkLqby6NnfZlsWLb0/Vs+Oyk+pkwDEZmHOzoiUM1+tCCr357IXf9XQ9V38sDf6oJhQUPpL9aEcqm1qWKeS2I8NubEWIg37CbVppO/z4RpVdTC3L1+C3fBudsAYDA1B41Xj+5mJIp3HzI+Rrwp7SMOaVqTDO/DlyIK9rqgTQg4f4Z8xaT3P0OoGmytMncsp9lMyzmK5LQDkVfPMCn0T+jDlZU+7v7h5RXeaNJyYr+TNPL7FAI="
        );
        
        WHITE = of(
                NamedTextColor.WHITE,
                "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NjAwMzM4MCwKICAicHJvZmlsZUlkIiA6ICI5ZDE1OGM1YjNiN2U0ZGNlOWU0OTA5MTdjNmJlYmM5MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTbm9uX1NTIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ0NzFlMjI5ZTc0ZTg0ZTcxM2RmYmRiNDQxMzJlMzkyYzk3YzlhYzhmM2QzMDcyMGViNDQ2M2ZjMTI0YmE0NiIKICAgIH0KICB9Cn0=",
                "e5G7st+mfwPcxszR81ptlg/sXpuauUXXTZv6n8Z+mVGXr3mLqIlPuc0sTJFJFkAqUR6TJhac/movN7wUFtMInq0680GEFW5LcBh9PyzgRaGDipgocg/XxdpF/pt1XzT2JUj1HfCzkJ5G/C1HI4Kt2f74qLmCLSSvnSi/anTSmgPo9SNo8qe48eCIV9fJM+05CG/TsVSpFFg68UBKF9orS8Ky5vf1dwpt61QUX3oJZXRIhtFdhGm020sv8roFNZwwGUPl6K5TERAa0Wzx9wIKLDiLcFgFtBHMXjotcP6L7XlvR1TEj2JCa+VWm70M5SKSu3SX7hz9h34Q4kYLj0TeiMvjsK7mMUK9F7rLYPnT0iCEc1/HgDTeEEqmb1lP7XDcntYLXxEKIhqMqc4GjcTLDJ8tQ9ZOQiiQZotdd1fQKK5UdNQxAFFF+2rHd519m19YVSh2eP2JBGVCO0kYJ6YVxlaQQn5M/fKn4uhJiBn70n1xZCyE8laARrjllQGvQePFyop5nJmT279mKRfuuTxMFE2FNLdOX/t0FF7Q3ZzIaqN8YmA3dDmBImvX1KIRSdLDqp6K604+1LyGZq+J5+LVCe7Vpaa9dWXr6YpFYwV/CTy1a0fvPcEg3NTFZqMkkdtrNvCI6wdAh37ZUCwSRfl7uo9gNSTY6uQQWD9fUFTskOo="
        );
    }
    
    private final String[] value;
    
    public EntryTexture(@NotNull String value, @NotNull String signature) {
        this.value = new String[] { value, signature };
    }
    
    /**
     * Gets the <code>value</code> of this texture.
     *
     * @return the value of this texture.
     */
    @NotNull
    @Override
    public String texture() {
        return value[0];
    }
    
    /**
     * Gets the <code>signature</code> of this texture.
     *
     * @return the signature of this texture.
     */
    @NotNull
    @Override
    public String signature() {
        return value[1];
    }
    
    /**
     * Creates an {@link EntryTexture} from a <code>value</code> and <code>signature</code>.
     *
     * @param value     - Value.
     * @param signature - Signature.
     * @return an entry texture.
     */
    @NotNull
    public static EntryTexture of(@NotNull String value, @NotNull String signature) {
        return new EntryTexture(value, signature);
    }
    
    /**
     * Creates an {@link EntryTexture} from an online {@link Player}.
     *
     * @param player - Player.
     * @return an entry texture.
     */
    @NotNull
    public static EntryTexture of(@NotNull Player player) {
        EntryTexture texture = cachedPlayerTextures.get(player);
        
        if (texture == null) {
            final Skin skin = Skin.ofPlayer(player);
            
            cachedPlayerTextures.put(player, texture = new EntryTexture(skin.texture(), skin.signature()));
        }
        
        return texture;
    }
    
    @NotNull
    public static EntryTexture of(@NotNull NamedTextColor color) {
        final EntryTexture texture = chatColorMapped.get(color);
        
        if (texture != null) {
            return texture;
        }
        
        throw new IllegalArgumentException("Cannot get texture for %s!".formatted(color));
    }
    
    @NotNull
    public static EntryTexture random() {
        return CollectionUtils.randomElementOrFirst(chatColorMapped.values());
    }
    
    private static EntryTexture of(NamedTextColor color, String value, String signature) {
        final EntryTexture texture = new EntryTexture(value, signature);
        
        chatColorMapped.put(color, texture);
        return texture;
    }
    
}
