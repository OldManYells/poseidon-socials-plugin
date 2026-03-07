package org.retromc.socials;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.util.config.Configuration;

/**
 * A custom configuration class for managing plugin configuration files in a
 * Bukkit environment. Extends the {@link Configuration} class to provide
 * additional utility methods for reading and writing configuration options with
 * defaults.
 */
public class SocialsConfig extends Configuration {
  private final int configVersion = 1;

  private SocialsPlugin plugin;

  /**
   * Constructs a new TemplateConfig instance.
   *
   * @param plugin     The plugin instance associated with this configuration.
   * @param configFile The configuration file to be managed.
   */
  public SocialsConfig(SocialsPlugin plugin, File configFile) {
    super(configFile);
    this.plugin = plugin;
    this.reload();
  }

  /**
   * Writes default configuration options to the file.
   * Ensures that default options are added when the file is loaded.
   */
  private void write() {
    // Convert old configuration keys to new keys if necessary
    if (this.getString("config-version") == null ||
        Integer.valueOf(this.getString("config-version")) < configVersion) {
      this.plugin.logger(java.util.logging.Level.INFO,
                         "Converting config to new version (" + configVersion +
                             ")...");
      convertToNewConfig();
      this.setProperty("config-version",
                       configVersion); // This should be handled by the
                                       // conversion method but just in case
    }

    // Main options
    generateConfigOption("config-version", configVersion);

    // Plugin options
    List<Map<String, String>> defaultLinks =
        new ArrayList<Map<String, String>>();
    Map<String, String> websiteLink = new LinkedHashMap<String, String>();
    websiteLink.put("name", "§2Website§r§f");
    websiteLink.put("url", "https://retromc.org");
    defaultLinks.add(websiteLink);


    Map<String, String> wikiLink = new LinkedHashMap<String, String>();
    wikiLink.put("name", "§6Wiki§r§f");
    wikiLink.put("url", "https://wiki.retromc.org/");
    defaultLinks.add(wikiLink);

    Map<String, String> discordLink = new LinkedHashMap<String, String>();
    discordLink.put("name", "§§9Discord§r§f§r§f");
    discordLink.put("url", "https://discord.retromc.org/");
    defaultLinks.add(discordLink);

    generateConfigOption("links", defaultLinks);

    generateConfigOption("settings.socialsplugin-command.enabled.value", true);
    generateConfigOption("settings.socialsplugin-command.response.value",
                         "Test command response");
  }

  private void convertToNewConfig() {
    // Convert old configuration keys to new keys

    // Convert from old config version 0 to new config version 1
    if (this.getString("config-version") == null ||
        Integer.valueOf(this.getString("config-version")) < 1) {
      convertToNewAddress("settings.socials-plugin.value",
                          "settings.socials-plugin.response.value", true);
      convertToNewAddress("settings.socials-plugin.enabled",
                          "settings.socials-plugin.enabled.value", true);
      convertToNewAddress("settings.socialsplugin-command.enabled.value",
                          "settings.test-command.enabled.value", true);
      convertToNewAddress("settings.socialsplugin-command.response.value",
                          "settings.test-command.response.value", true);
      convertToNewAddress("settings.socialsplugin-command.enabled.value",
                          "settings.plugins-command.enabled.value", true);
      convertToNewAddress("settings.socialsplugin-command.response.value",
                          "settings.plugins-command.response.value", true);
    }
  }

  /**
   * Reloads the configuration by loading the file, writing defaults, and saving
   * changes.
   */
  private void reload() {
    this.load();
    this.write();
    this.save();
  }

  /**
   * Converts an old configuration key to a new one.
   * If the old key exists and the new key does not, the old key's value is
   * copied to the new key, and the old key is removed.
   *
   * @param newKey The new configuration key.
   * @param oldKey The old configuration key.
   * @param log    Whether to log the conversion process.
   * @return True if the conversion was performed, false otherwise.
   */
  private boolean convertToNewAddress(String newKey, String oldKey,
                                      boolean log) {
    if (this.getString(newKey) != null) {
      return false;
    }
    if (this.getString(oldKey) == null) {
      return false;
    }
    if (log) {
      plugin.logger(java.util.logging.Level.INFO,
                    "Converting Config: " + oldKey + " to " + newKey);
    }
    Object value = this.getProperty(oldKey);
    this.setProperty(newKey, value);
    this.removeProperty(oldKey);
    return true;
  }

  /**
   * Adds a default value for a configuration key if it is not already set.
   *
   * @param key          The configuration key.
   * @param defaultValue The default value to set.
   */
  public void generateConfigOption(String key, Object defaultValue) {
    if (this.getProperty(key) == null) {
      this.setProperty(key, defaultValue);
    }
    final Object value = this.getProperty(key);
    this.removeProperty(key);
    this.setProperty(key, value);
  }

  // Getters Start
  public Object getConfigOption(String key) { return this.getProperty(key); }

  public String getConfigString(String key) {
    return String.valueOf(getConfigOption(key));
  }

  public Integer getConfigInteger(String key) {
    return Integer.valueOf(getConfigString(key));
  }

  public Long getConfigLong(String key) {
    return Long.valueOf(getConfigString(key));
  }

  public Double getConfigDouble(String key) {
    return Double.valueOf(getConfigString(key));
  }

  public Boolean getConfigBoolean(String key) {
    return Boolean.valueOf(getConfigString(key));
  }

  public List<SocialLink> getSocialLinks() {
    Object linksProperty = getConfigOption("links");
    if (!(linksProperty instanceof List)) {
      return Collections.emptyList();
    }

    List<?> rawLinks = (List<?>)linksProperty;
    List<SocialLink> links = new ArrayList<SocialLink>();
    for (Object rawLink : rawLinks) {
      if (!(rawLink instanceof Map)) {
        continue;
      }

            Map<?, ?> linkMap = (Map<?, ?>) rawLink;
            Object name = linkMap.get("name");
            Object url = linkMap.get("url");
            if (name == null || url == null) {
              continue;
            }

            String nameString = String.valueOf(name).trim();
            String urlString = String.valueOf(url).trim();
            if (nameString.length() == 0 || urlString.length() == 0) {
              continue;
            }

            links.add(new SocialLink(nameString, urlString));
    }

    return links;
  }

  public static class SocialLink {
    private final String name;
    private final String url;

    public SocialLink(String name, String url) {
      this.name = name;
      this.url = url;
    }

    public String getName() { return name; }

    public String getUrl() { return url; }
  }

  // Getters End
}
