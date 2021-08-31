package com.secoo.library.hybrid.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

public class JavaUtil {
    @Nullable
    public static <V> V getValueFromLikelyMap(@Nullable Object likelyMapObject, @NonNull String key) {
        return getValueFromLikelyMap(likelyMapObject, key, null);
    }

    @Nullable
    public static <V> V getValueFromLikelyMap(@Nullable Object likelyMapObject, @NonNull String key, V fallback) {
        if (likelyMapObject == null) {
            return fallback;
        } else if (likelyMapObject instanceof Map) {
            Map map = (Map) likelyMapObject;
            V value = (V) map.get(key);
            if (value == null) {
                return fallback;
            } else {
                return value;
            }
        } else {
            return fallback;
        }
    }
}
