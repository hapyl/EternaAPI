package me.hapyl.eterna.builtin.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.Runnables;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public final class Updater extends EternaKeyed {
    
    public static final URL URL = BukkitUtils.url("https://api.github.com/repos/hapyl/EternaAPI/releases/latest");
    public static final String VERSION_REGEX = "-?SNAPSHOT.*";
    
    public Updater(@Nullable EternaKey key) {
        super(key);
    }
    
    @NotNull
    public CompletableFuture<UpdateResponse> checkForUpdates() {
        final CompletableFuture<UpdateResponse> future = new CompletableFuture<>();
        
        Runnables.async(() -> {
            try (final HttpClient client = HttpClient.newHttpClient()) {
                final HttpRequest request = HttpRequest.newBuilder()
                                                       .uri(URL.toURI())
                                                       .header("Accept", "application/vnd.github+json")
                                                       .build();
                
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                final JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                
                
                final String pluginVersion = Eterna.getPlugin().getPluginMeta().getVersion();
                final String latestVersion = json.get("tag_name").getAsString();
                
                final String downloadUrl = json.get("assets").getAsJsonArray()
                                               .get(0).getAsJsonObject()
                                               .get("browser_download_url").getAsString();
                
                // Compare versions
                final UpdateResult result = compareVersions(pluginVersion, latestVersion);
                
                future.complete(new UpdateResponse(
                        result, Component.text()
                                         .append(Component.text("Download from ", NamedTextColor.GREEN))
                                         .append(
                                                 Component.text("GITHUB", NamedTextColor.AQUA, TextDecoration.BOLD, TextDecoration.UNDERLINED)
                                                          .hoverEvent(
                                                                  HoverEvent.showText(Component.text("Click to open the link in your browser!", NamedTextColor.YELLOW, TextDecoration.UNDERLINED))
                                                          )
                                                          .clickEvent(
                                                                  ClickEvent.clickEvent(
                                                                          ClickEvent.Action.OPEN_URL,
                                                                          ClickEvent.Payload.string(downloadUrl)
                                                                  )
                                                          )
                                         ).build()
                ));
            }
            catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });
        
        return future;
    }
    
    @NotNull
    private UpdateResult compareVersions(@NotNull String pluginVersion, @NotNull String latestVersion) {
        // Remove -SNAPSHOT
        pluginVersion = pluginVersion.replaceFirst(VERSION_REGEX, "");
        latestVersion = latestVersion.replaceFirst(VERSION_REGEX, "");
        
        try {
            final String[] pluginVersionParts = pluginVersion.split("\\.");
            final String[] latestVersionParts = latestVersion.split("\\.");
            
            final int length = Math.max(pluginVersionParts.length, latestVersionParts.length);
            
            for (int i = 0; i < length; i++) {
                final int pluginVersionPart = i < pluginVersionParts.length ? Numbers.toInt(pluginVersionParts[i]) : 0;
                final int latestVersionPart = i < latestVersionParts.length ? Numbers.toInt(latestVersionParts[i]) : 0;
                
                // If any plugin part is lower than the latest part, we're using an outdated version
                if (pluginVersionPart < latestVersionPart) {
                    return UpdateResult.OUTDATED;
                }
                // Otherwise it's a development version
                else if (pluginVersionPart > latestVersionPart) {
                    return UpdateResult.DEVELOPMENT;
                }
            }
            
            // If no early return, we're up to date
            return UpdateResult.UP_TO_DATE;
        }
        catch (Exception ex) {
            // Errors may happen when we got rate-limited, so just skip it
            return UpdateResult.ERROR;
        }
    }
    
    
}
