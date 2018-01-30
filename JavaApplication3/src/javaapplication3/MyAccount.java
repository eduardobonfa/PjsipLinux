/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

/**
 *
 * @author eduardo
 */
import java.lang.reflect.Field;
import java.util.Vector;
import org.pjsip.pjsua2.*;
// Subclass to extend the Account and get notifications etc.

class MyAccount extends Account {

    @Override
    public void onRegState(OnRegStateParam prm) {
        System.out.println("*** On registration state: " + prm.getCode() + prm.getReason());
    }
}

class test {

    static {
        //System.setProperty("java.library.path", "/home/eduardo/Downloads/PJSIP/pjproject/pjsip-apps/src/swig/java/output");
        System.out.println(System.getProperty("java.library.path"));

        
        
        System.loadLibrary("pjsua2");
        
        
        System.out.println("Library loaded");
        final String[] libraries = ClassScope.getLoadedLibraries(ClassLoader.getSystemClassLoader()); //MyClassName.class.getClassLoader()
        
    }

    public static void main(String argv[]) {
        try {
            // Create endpoint
            Endpoint ep = new Endpoint();
            ep.libCreate();
            // Initialize endpoint
            EpConfig epConfig = new EpConfig();
            ep.libInit(epConfig);
            // Create SIP transport. Error handling sample is shown
            TransportConfig sipTpConfig = new TransportConfig();
            sipTpConfig.setPort(5060);
            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
            // Start the library
            ep.libStart();
            AccountConfig acfg = new AccountConfig();
            acfg.setIdUri("sip:test@pjsip.org");
            acfg.getRegConfig().setRegistrarUri("sip:pjsip.org");
            AuthCredInfo cred = new AuthCredInfo("digest", "*", "test", 0, "secret");
            acfg.getSipConfig().getAuthCreds().add(cred);
            // Create the account
            MyAccount acc = new MyAccount();
            acc.create(acfg);
            // Here we don't have anything else to do..
            Thread.sleep(10000);
            /* Explicitly delete the account.
           * This is to avoid GC to delete the endpoint first before deleting
           * the account.
             */
            acc.delete();
            // Explicitly destroy and delete endpoint
            ep.libDestroy();
            ep.delete();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }
}

class ClassScope {
 private static Field LIBRARIES = null;
    static {
        try {
        LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
        LIBRARIES.setAccessible(true);
        } catch (Exception e) {
        }
       
    }
    public static String[] getLoadedLibraries(final ClassLoader loader) {
        try {
            Vector<String> libraries = (Vector<String>) LIBRARIES.get(loader);
            return libraries.toArray(new String[] {});
        } catch (Exception e) {
        }
        return null;
    }

}