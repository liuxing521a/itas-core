package org.itas.core.net.nio.server;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.itas.core.net.nio.Message;
import org.itas.core.net.nio.SessionListener;

import net.itas.core.Dispatch;
import net.itas.core.Rule;
import net.itas.core.User;
import net.itas.util.Logger;

@Sharable
public class NioServerHandler extends SimpleChannelInboundHandler<Message> implements Rule {

	/** 事件分配器*/
	private Dispatch dispatch;
	
	/** 行为监听器*/
	private final List<SessionListener> listeners;
	
	public NioServerHandler() throws Exception {
		this.listeners = new ArrayList<>(4);
	}

	public void addListener(SessionListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(SessionListener listener) {
		this.listeners.remove(listener);
	}
	
	public void setDispatch(Dispatch dispatch) {
		this.dispatch = dispatch;
	}
	
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    	for (SessionListener listener : listeners) {
    		listener.onSessionConnected(ctx.channel());
		}
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	for (SessionListener listener : listeners) {
    		listener.onSessionDestroyed(ctx.channel());
		}
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, Message message) throws Exception {
    	User user = ctx.channel().attr(key).get();
    	try {
    		if (Objects.isNull(user)) {
    			for (SessionListener listener : listeners) {
    				listener.onTokenReceived(message);
    			}
    		} else {
    			dispatch.dispatch(user, message);
    		}
    	} finally {
    		message.release();
    	}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	if (cause instanceof IOException) {
    		ctx.close();
    		return;
		} 

    	Logger.error("netHandler exceptionCaught:", cause);
    }
}