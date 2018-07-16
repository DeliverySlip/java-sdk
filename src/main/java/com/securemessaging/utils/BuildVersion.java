package com.securemessaging.utils;

public class BuildVersion {

    public static String getBuildVersion(){
        return BuildVersion.class.getPackage().getImplementationVersion();
    }
}
