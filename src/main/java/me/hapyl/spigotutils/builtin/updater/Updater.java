package me.hapyl.spigotutils.builtin.updater;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class Updater {

    private static final String URL = "https://api.github.com/repos/hapyl/EternaAPI/releases/latest";

    private String pluginVersion;
    private String latestVersion;

    private String downloadUrl;

    public Updater() {
    }

    public void checkForUpdatesAndGiveLink() {
        final UpdateResult result = checkForUpdates();

        if (result == UpdateResult.OUTDATED) {
            broadcastLink();
        }
    }

    public void broadcastLink() {
        final String updateMessage = String.format(
                "&aUpdate is available! Your version: %s, latest version: %s.",
                pluginVersion,
                latestVersion
        );

        EternaLogger.broadcastMessageOP(updateMessage);
        EternaLogger.broadcastMessageConsole(updateMessage);

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

            stream.close();

            final UpdateResult updateResult = compareVersions(pluginVersion, latestVersion);

            if (updateResult == UpdateResult.OUTDATED) {
                this.downloadUrl = StringUtils.substringBetween(jsonText.trim(), "\"browser_download_url\":\"", "\"");
            }

            return updateResult;
        } catch (Exception ignored) {
            return UpdateResult.INVALID;
        }
    }

    private UpdateResult compareVersions(String current, String remote) {
        try {
            final String[] currentParts = current.split("\\.");
            final String[] remoteParts = remote.split("\\.");

            int length = Math.max(currentParts.length, remoteParts.length);
            for (int i = 0; i < length; i++) {
                final int thisPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
                final int thatPart = i < remoteParts.length ? Integer.parseInt(remoteParts[i]) : 0;

                if (thisPart < thatPart) {
                    return UpdateResult.OUTDATED;
                }
                if (thisPart > thatPart) {
                    return UpdateResult.DEVELOPMENT;
                }
            }
            return UpdateResult.UP_TO_DATE;
        } catch (Exception e) {
            e.printStackTrace();
            return UpdateResult.INVALID;
        }
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getDownloadUrl() {
        return downloadUrl;
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
