package org.noxious.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.noxious.netty.utils.PipelineUtils;
import org.noxious.utils.Logger;


public class NoxiousServerThread extends Thread {

    private static volatile NoxiousServerThread instance;

    private int port;
    private final int bossGroup;
    private final int workerGroup;
    private final int maxBacklog;

    // Netty server bootstrap
    private ServerBootstrap bootstrap;
    private ChannelFuture serverChannelFuture;
    private boolean running = false;

    public NoxiousServerThread(int port, int bossGroup, int workerGroup, int maxBacklog) {
        this.port = port;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.maxBacklog = maxBacklog;

    }

    public NoxiousServerThread(int port) {
        this(port, 1, 8, 1000);
        instance = this;
    }

    @Override
    public void run() {
        try {
            setName("server-t-" + port);
            start_server(this.port, this.bossGroup, this.workerGroup, this.maxBacklog); // Default parameters
        } catch (Exception e) {
            Logger.error("Server thread encountered an error: " + e.getMessage());
        }
    }


    @Override
    public synchronized void start() {
        if (this.isRunning()) {
            throw new IllegalStateException("Server is already running.");
        }
        super.start();
    }


    // Default 6060, 1, 8, 1000
    public void start_server(int port, int bossGroupThreads, int workerGroupThreads, int maxBacklogs) throws Exception {
        this.port = port;
        Logger.info("Starting server on port " + port);
        Logger.info("Boss group threads: " + bossGroupThreads);
        Logger.info("Worker group threads: " + workerGroupThreads);
        Logger.info("Max backlog: " + maxBacklogs);
        // Boss group: handles incoming connections
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupThreads); // Single thread for accepting connections
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupThreads); // Adjust based on your CPU cores

        try {
            this.bootstrap = new ServerBootstrap();
            this.bootstrap.group(bossGroup, workerGroup)
                    .channel(PipelineUtils.getServerChannel()) // Use NIO-based server channel
                    .childHandler(new NoxiousInitializer())
                    .option(ChannelOption.SO_BACKLOG, maxBacklogs) // Max pending connections
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // Keep connections alive
                    .childOption(ChannelOption.TCP_NODELAY, true); // Reduce latency for real-time use
            // Bind server to the port
            this.serverChannelFuture = this.bootstrap.bind(port).sync();
            Logger.info("Server started on port " + port);
            this.running = true;
            // Wait for the server channel to close
            this.serverChannelFuture.channel().closeFuture().sync();
        }
        finally {
            // Shut down gracefully
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            this.running = false;
        }
    }

    public void shutdown_server() throws Exception {
        // TODO close all clients
        if (this.serverChannelFuture != null) {
            this.serverChannelFuture.channel().close().sync();
            this.running = false;
        }
        Logger.info("Server stopped.");
    }

    public synchronized boolean isRunning() {
        return this.running;
    }

    public ServerBootstrap getBootstrap() {
        return this.bootstrap;
    }

    public static NoxiousServerThread getInstance() {
        return instance;
    }
}
