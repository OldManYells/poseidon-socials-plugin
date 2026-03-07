# Poseidon-Socials-Plugin

This plugin allows defining a set of social links that players can request by
running the `/socials` command.

## Settings

The plugin creates `plugins/Poseidon-Socials-Plugin/config.yml` and supports
the following keys:

- `config-version` (integer)
- `links` (list of objects with `name` and `url`), as shown below
- `settings.socialsplugin-command.enabled.value` (boolean)
- `settings.socialsplugin-command.response.value` (string)

Example:

```yml
config-version: 1
links:
  - name: "§2Website§r§f"
    url: "https://retromc.org"
  - name: "§6Wiki§r§f"
    url: "https://wiki.retromc.org/"
  - name: "§9Discord§r§f"
    url: "https://discord.retromc.org/"
settings:
  socialsplugin-command:
    enabled:
      value: true
    response:
      value: "Test command response"
```

Notes:

- `name` supports Minecraft formatting codes (such as `§2`, `§6`, `§9`).
- Entries missing `name`/`url`, or with empty values, are ignored.
- If no valid entries exist, `/socials` will return `No social links are configured.`
- `settings.socialsplugin-command.*` is read by `SocialsLinksCommand` for its enabled
  flag and response text.

## Command

- `/socials` (alias: `/social`) shows all configured links.

Permission:

- `poseidon.socials` (ops are also allowed)

## GitHub Actions

This repository includes a pre-configured GitHub Action:

1. **`build-and-test.yml`**:
    - Runs tests on every push to ensure code quality.
    - Uploads an artifact for each commit, allowing others to download the plugin for testing.

2. **`release.yml`**:
    - Automatically creates a GitHub release if the `-SNAPSHOT` suffix is removed from the version in `pom.xml`.

With this template, you can kickstart your plugin development for Project Poseidon quickly and efficiently.
