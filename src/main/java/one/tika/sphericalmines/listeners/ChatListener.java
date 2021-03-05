package one.tika.sphericalmines.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.function.Consumer;

public class ChatListener implements Listener {
    public static final Map<UUID, Consumer<String>> awaitingInput = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // Player is NOT awaiting input
        if (!awaitingInput.containsKey(player.getUniqueId())) return;

        String input = event.getMessage();
        Consumer<String> func = awaitingInput.get(player.getUniqueId());
        awaitingInput.remove(player.getUniqueId());
        func.accept(input);
        event.setCancelled(true);
    }
}
