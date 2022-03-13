package reprogrammed.bedbed;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;

public class PacketReader {

	Channel channel;
	public static Map<UUID, Channel> channels = new HashMap<UUID, Channel>();
	
	public void inject(final Player player)
	{
		CraftPlayer craftPlayer = (CraftPlayer) player;
		channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
		channels.put(player.getUniqueId(), channel);
		
		if(channel.pipeline().get("PacketInjector") != null)
		{
			return;
		}
		
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>()
		{

			@Override
			protected void decode(ChannelHandlerContext channel, Packet<?> packet, List<Object> arg) throws Exception {
				arg.add(packet);
				readPacket(player, packet);
			}
			
		});
	}
	
	public void uninject(Player player)
	{
		channel = channels.get(player.getUniqueId());
		if(channel.pipeline().get("PacketInjector") != null)
		{
			channel.pipeline().remove("PacketInjector");
		}
	}
	
	public void readPacket(final Player player, Packet<?> packet)
	{
		
	}
	
	private Object getValue(Object instance, String name)
	{
		Object result = null;
		
		try {
			
			Field field = instance.getClass().getDeclaredField(name);
			
			field.setAccessible(true);
			
			result = field.get(instance);
			
			field.setAccessible(false);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
}
