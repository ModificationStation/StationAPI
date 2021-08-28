package net.modificationstation.stationapi.impl.client.model;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonFacesData {

    public final JsonFaceData north;
    @SerializedName("west")
    public final JsonFaceData east;
    public final JsonFaceData south;
    @SerializedName("east")
    public final JsonFaceData west;
    public final JsonFaceData up;
    public final JsonFaceData down;
}
