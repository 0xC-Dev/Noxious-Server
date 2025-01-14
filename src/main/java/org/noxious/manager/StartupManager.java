package org.noxious.manager;


import org.noxious.netty.NoxiousServerThread;
import org.noxious.packet.PacketRegistry;

public class StartupManager {

    private static NoxiousServerThread serverThread;

    static {
        /*
            Note: Theres nothing right now but in future will use JNI for optimization and for crypto handling

        String projectDir = System.getProperty("user.dir");
        String rustLibPath = projectDir + File.separator + "NoxiousRust" + File.separator + "target" + File.separator + "release" + File.separator + "noxiousLib.dll";
        System.load(rustLibPath);
         */
    }

    //private static native void init_dll();


    public static void startNoxious() {
        PacketRegistry.init();
        serverThread = new NoxiousServerThread(6969);
        serverThread.start();
        // First start native library's
        //init_dll();
    }
}
