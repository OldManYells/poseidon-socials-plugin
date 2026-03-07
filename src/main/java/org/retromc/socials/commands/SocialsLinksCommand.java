package org.retromc.socials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.retromc.socials.SocialsPlugin;
import org.retromc.socials.SocialsConfig;

public class SocialsLinksCommand implements CommandExecutor {

  private final SocialsPlugin plugin;

  private final SocialsConfig config;

  public SocialsLinksCommand(SocialsPlugin plugin) {
    this.plugin = plugin;
    this.config = plugin.getConfig();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
                           String[] args) {
    // Check if the sender is a player
    if (!(sender instanceof Player)) {
      sender.sendMessage("This command can only be executed by players.");
      return true;
    }


    // Check if the command is enabled
    Boolean isEnabled =
        config.getConfigBoolean("settings.socialsplugin-command.enabled.value");
    if (!isEnabled) {
      sender.sendMessage("This command is currently disabled.");
      return true;
    }

    // Get the response message from the config
    String response =
        config.getConfigString("settings.socialsplugin-command.response.value");

    // Send the response message to the player
    sender.sendMessage(response);
    return true;
  }
}
