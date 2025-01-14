package org.noxious.netty.utils;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.PlatformDependent;
import org.noxious.utils.Logger;

import java.util.concurrent.ThreadFactory;

public class PipelineUtils {

    private static boolean epoll;

    static {
        if (!PlatformDependent.isWindows()) {
            Logger.warn("Not on Windows, attempting to use enhanced EpollEventLoop");

            if (epoll == Epoll.isAvailable()) {
                Logger.info("Epoll is working, utilising it!");
            } else {
                Logger.warn("Epoll is not working, falling back to NIO");
            }
        }
    }

    public static String DECODER = "betty-decoder";
    public static String ENCODER = "betty-encoder";
    public static String HANDLER_BOSS = "betty-handlerboss";

    public static EventLoopGroup newEventLoopGroup(int threads, ThreadFactory factory) {
        return epoll ? new EpollEventLoopGroup( threads, factory ) : new NioEventLoopGroup( threads, factory );
    }

    public static Class<? extends Channel> getChannel() {
        return epoll ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    public static Class<? extends ServerChannel> getServerChannel(){
        return epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}

