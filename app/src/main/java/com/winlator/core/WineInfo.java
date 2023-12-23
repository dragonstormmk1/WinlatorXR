package com.winlator.core;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.winlator.xenvironment.ImageFs;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WineInfo implements Parcelable {
    public static final WineInfo MAIN_WINE_VERSION = new WineInfo("8.0.1", "x86_64");
    private static final Pattern pattern = Pattern.compile("^wine\\-([0-9\\.]+)\\-?([0-9\\.]+)?\\-(x86|x86_64)$");
    public final String version;
    public final String subversion;
    public final String path;
    private String arch;

    public WineInfo(String version, String arch) {
        this.version = version;
        this.subversion = null;
        this.arch = arch;
        this.path = null;
    }

    public WineInfo(String version, String subversion, String arch, String path) {
        this.version = version;
        this.subversion = subversion != null && !subversion.isEmpty() ? subversion : null;
        this.arch = arch;
        this.path = path;
    }

    private WineInfo(Parcel in) {
        version = in.readString();
        subversion = in.readString();
        arch = in.readString();
        path = in.readString();
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public boolean isWin64() {
        return arch.equals("x86_64");
    }

    public String identifier() {
        return "wine-"+fullVersion()+"-"+arch;
    }

    public String fullVersion() {
        return version+(subversion != null ? "-"+subversion : "");
    }

    @NonNull
    @Override
    public String toString() {
        return "Wine "+fullVersion()+" ("+arch+")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<WineInfo> CREATOR = new Parcelable.Creator<WineInfo>() {
        public WineInfo createFromParcel(Parcel in) {
            return new WineInfo(in);
        }

        public WineInfo[] newArray(int size) {
            return new WineInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(version);
        dest.writeString(subversion);
        dest.writeString(arch);
        dest.writeString(path);
    }

    @NonNull
    public static WineInfo fromIdentifier(Context context, String identifier) {
        if (identifier.equals(MAIN_WINE_VERSION.identifier())) return MAIN_WINE_VERSION;
        Matcher matcher = pattern.matcher(identifier);
        if (matcher.find()) {
            File installedWineDir = ImageFs.find(context).getInstalledWineDir();
            String path = (new File(installedWineDir, identifier)).getPath();
            return new WineInfo(matcher.group(1), matcher.group(2), matcher.group(3), path);
        }
        else return MAIN_WINE_VERSION;
    }
}
