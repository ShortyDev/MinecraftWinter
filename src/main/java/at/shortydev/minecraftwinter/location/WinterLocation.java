package at.shortydev.minecraftwinter.location;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
@Builder
public class WinterLocation {
    
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    
    public Location getLegacyLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
    
    public static WinterLocation toWinterLocation(Location location) {
        return new WinterLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
