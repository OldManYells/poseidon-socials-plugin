package org.retromc.socials.commands;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.retromc.socials.SocialsPlugin;
import org.retromc.socials.SocialsConfig;

public class SocialsCommand implements CommandExecutor {
  private final SocialsConfig config;

  public SocialsCommand(SocialsPlugin plugin) {
    this.config = plugin.getConfig();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
                           String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("This command can only be executed by players.");
      return true;
    }

    if (!sender.hasPermission("myplugin.socials") && !sender.isOp()) {
      sender.sendMessage("You do not have permission to execute this command.");
      return true;
    }

    List<SocialsConfig.SocialLink> links = config.getSocialLinks();
    if (links.isEmpty()) {
      sender.sendMessage("No social links are configured.");
      return true;
    }

    sender.sendMessage("§2Social links:");
    for (SocialsConfig.SocialLink link : links) {
      sender.sendMessage("- " + link.getName() + ": " + link.getUrl());
    }
    return true;
  }
}
