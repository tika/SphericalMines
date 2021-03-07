package one.tika.sphericalmines.listeners;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.SphericalMinesAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public class BreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        // % reset
        Optional<Mine> _mine = SphericalMinesAPI.getMineAt(event.getBlock().getLocation());

        // Not in a mine
        if (!_mine.isPresent()) return;

        Mine mine = _mine.get();

        // Does not have % reset enabled
        if (mine.getAutoReset() == null || mine.getAutoReset().charAt(mine.getName().length() - 1) != '%') return;

        // Increase blocks mined
        mine.incrementBlocksMined();

        int percentage = Integer.parseInt(StringUtils.chop(mine.getAutoReset()));

        // If the blocks mined
        if (mine.getBlocksMined() >= mine.getRegion().getVolume() * (percentage / 100))
            mine.reset();
    }

}
