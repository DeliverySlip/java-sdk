package com.securemessaging.utils;

/**
 * BuildVersion is a helper class to isolate versioning information about the client SDK. This class ensures version
 * information can be accessed from anywhere, and changes to the version only need to be updated in one place
 * (the build.gradle file)
 */
public class BuildVersion {

    public static String getBuildVersion(){
        return BuildVersion.class.getPackage().getImplementationVersion();
    }
}
