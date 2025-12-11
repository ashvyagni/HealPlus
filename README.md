# HealPlus

HealPlus is a Paper/Spigot 1.12.2 plugin that adds a /heal command with a simple GUI for self-healing and healing other players. It works alongside ViaVersion and ViaBackwards.

## Requirements
- Java 8
- Maven 3.6+
- Paper/Spigot 1.12.2 (compatible with ViaVersion/ViaBackwards)

## Build
```bash
mvn clean package
```
The built jar will be at `target/healplus-1.0.0.jar`.

## Install
1. Place `target/healplus-1.0.0.jar` into your server's `plugins` folder.
2. Start or restart the server.

## Commands
- `/heal` — Heal yourself.
- `/heal <player>` — Heal another player.
- `/heal gui` — Open the heal GUI (player only).

## Permissions
- `healplus.self` — Use `/heal` on yourself and heal via GUI. (default: op)
- `healplus.others` — Heal other players via command or GUI. (default: op)

## Download 

you can also directly download the healplus 1.0.0 .jar 
from the above repo

## thank you for downloading

message ashxx.ily on discord for support!

