# NetLog

Simple utility to log all packets (and values) between client and server.

We do this by injecting a pipeline handler for both inbound and outbound packets, using [NMSProxy] to handle the NMS 
reflection into nice interfaces.

After the player disconnects, we serialize all logged packets into a json file inside the the plugin folder grouped by
player uuid.

#### Limitations
- We are adding the pipeline handlers when `PlayerJoinEvent` is fired from the server. This means we miss 
both handshake and login packets, as well some early play packets. Willing to take suggestions on early events to use 
to achieve the same result
- When serializing we we don't deep dive into packet fields where there are objects. This means we are reliant on what 
`.toString()` gives us from the object itself. This is hopefully be rectified at a later point.
- There is also an issue where arrays that aren't object arrays aren't being serialized.

[NMSProxy]: https://github.com/theminecoder/NMSProxy