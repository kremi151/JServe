package lu.mkremer.jserve.util;

import org.tinylog.Logger;

public class BuildVersion {

    public static final String VERSION = getVersion();

    private static String getVersion() {
        String version = BuildVersion.class.getPackage().getImplementationVersion();
        if (version == null || version.isEmpty()) {
            Logger.warn("Version cannot be determined");
            version = "unknown";
        }
        return version;
    }

}
