package me.hapyl.spigotutils.builtin.updater;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.util.Validate;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public final class Updater {

    private final String URL = "https://api.github.com/repos/hapyl/EternaAPI/releases/latest";
    private final Logger logger;

    private String pluginVersion;
    private String latestVersion;

    private int pluginVersionInt;
    private int latestVersionInt;

    private String downloadUrl;

    public Updater() {
        this.logger = EternaPlugin.getPlugin().getLogger();
    }

    public void checkForUpdatesAndGiveLink() {
        final UpdateResult result = checkForUpdates();

        if (result == UpdateResult.OUTDATED) {
            broadcastLink();
        }
    }

    public void broadcastLink() {
        EternaLogger.broadcastMessageOP("&aAn update available! Your version: %s, latest version: %s.", pluginVersion, latestVersion);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.isOp()) {
                Chat.sendClickableHoverableMessage(
                        online,
                        LazyEvent.openUrl(downloadUrl),
                        LazyEvent.showText("&7Click here to download!"),
                        EternaLogger.PREFIX + "&e&lCLICK HERE &ato download!"
                );
            }
        }

        EternaLogger.broadcastMessageConsole("&aDownload here: &e" + downloadUrl);
    }

    public UpdateResult checkForUpdates() {
        try {
            final InputStream stream = new URL(URL).openStream();

            final BufferedReader rd = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            final String jsonText = readAll(rd);

            this.pluginVersion = EternaPlugin.getPlugin().getDescription().getVersion();
            this.latestVersion = StringUtils.substringBetween(jsonText.trim(), "\"tag_name\":\"", "\"");

            this.pluginVersionInt = intVersion(pluginVersion);
            this.latestVersionInt = intVersion(latestVersion);

            stream.close();

            if (latestVersionInt > pluginVersionInt) {
                downloadUrl = StringUtils.substringBetween(jsonText.trim(), "\"browser_download_url\":\"", "\"");
                return UpdateResult.OUTDATED;
            }
            else if (pluginVersionInt > latestVersionInt) {
                return UpdateResult.DEVELOPMENT;
            }

            return UpdateResult.UP_TO_DATE;
        } catch (Exception ignored) {
            return UpdateResult.INVALID;
        }
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public int getPluginVersionInt() {
        return pluginVersionInt;
    }

    public int getLatestVersionInt() {
        return latestVersionInt;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    private int intVersion(String version) {
        final String[] split = version.split("\\.");
        final StringBuilder builder = new StringBuilder();

        for (String s : split) {
            builder.append(s);
        }

        return Validate.getInt(builder.toString().replace("-SNAPSHOT", ""));
    }

    private String readAll(Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }

}
