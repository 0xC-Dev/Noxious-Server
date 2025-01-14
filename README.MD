# NoxiousServer

Noxious Server is the base I use for my C2 server. Using Netty it allows for better performance then the default java sockets and has a more coherent structure making it easy to manage clients.

There will be a full NoxiousC2 released eventually when I finish that

## Features

- **High Performance**: Powered by Netty, offering efficient NIO-based networking.
- **Modular Packet System**: Simplifies the creation, encoding, and decoding of custom protocol packets.
- **Thread Management**: Configurable thread pools for boss and worker groups to optimize resource usage.

---

## How It Works

### Architecture

NoxiousServerThread:

This class is the Thread of the SocketListener / Bootstrap in Netty, at the moment the idea is the server only listens to 1 port at a time but this can be changed to many ports at one time.

1. **NioEventLoopGroups**:

    - **Boss Group**: Handles incoming connection requests.

    - **Worker Group**: Manages read/write operations for established connections.


2. **ServerBootstrap**:

    - Sets up the Netty server with channel options, child handlers, and thread groups.


3. **Packet System**:

    - Packets are defined as objects with the data being written and read in sequence .

    Note: The packet design can be copied over to the Client if it is written in java


4. **Server Management**:

    - The server can be started, stopped, and monitored programmatically.
    - Currently, this is just the base of the server so GUI isnt included yet


### Packet System

I tried to make the packet system as coherent and flexible as possible.

[Visual Representation of Packet System](https://raw.githubusercontent.com/0xC-Dev/Noxious-Server/refs/heads/master/ssph.png)


#### Example Workflow:

1. **Client** sends a packet with a specific id and payload.

2. **Server** receives the packet, decodes it using the pipeline, and routes it to the corresponding handler.

3. **Handler** processes the packet and optionally sends a response.

---


### Configuration

#### Server Initialization

Create and configure a server instance:

```
int port = 6060;
int bossThreads = 1;
int workerThreads = 8;
int maxBacklog = 1000;

NoxiousServerThread server = new NoxiousServerThread(port, bossThreads, workerThreads, maxBacklog);
server.start();

// OR for defualt config

NoxiousServerThread server = new NoxiousServerThread(6060);
server.start();
```

#### Packet Handling

Define and register custom packets:

```
public class ExamplePacket extends DefinedPacket {  

    private String string;

    @Override  
    public void write(ByteBuf buf) throws Exception {  
	    ByteBufWrapper.writeString("Hello Worl", buf);
    }  
  
    @Override  
    public void read(ByteBuf buf) throws Exception {  
		this.string = ByteBufWrapper.readString(buf);
    }
    
    @Override  
    public int getPacketId() {  
        return 0x01; // PacketId 1  
    }  
}

// Register the packet PacketRegistry.init()
registerPacket(HandshakePacket::new);
```

---

## Development

Note: if attempting to implement own client in a native language take a look at [ByteBufWrapper.java](https://github.com/0xC-Dev/Noxious-Server/blob/af2717b15c47cdc412ec924851135d12d3f8810a/src/main/java/org/noxious/netty/bytebuf/ByteBufWrapper.java#L11) for reading and sending data over socket, this was referenced from [How fast can you write a VarInt](https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/)


- **Java 8 or higher**

- **Netty**: The core networking library.

#### Terms of Use
Anyone can use this just give me credit please and thanks :)

---