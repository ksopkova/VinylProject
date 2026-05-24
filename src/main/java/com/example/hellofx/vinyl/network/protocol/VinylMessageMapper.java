package com.example.hellofx.vinyl.network.protocol;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;

import java.util.LinkedHashMap;
import java.util.Map;

public final class VinylMessageMapper {
    private VinylMessageMapper() {
    }

    public static Map<String, Object> toMap(Vinyl vinyl) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", vinyl.getId());
        map.put("title", vinyl.getTitle());
        map.put("artist", vinyl.getArtist());
        map.put("year", vinyl.getYear());
        map.put("state", vinyl.stateNameProperty().get());
        map.put("reservedBy", userToMap(vinyl.getReservedBy()));
        map.put("borrowedBy", userToMap(vinyl.getBorrowedBy()));
        map.put("removalRequested", vinyl.isRemovalRequested());
        map.put("reservationBlocked", vinyl.isReservationBlocked());
        return map;
    }

    public static Vinyl fromMap(Map<String, Object> map) {
        Vinyl vinyl = new Vinyl(
                stringValue(map.get("id")),
                stringValue(map.get("title")),
                stringValue(map.get("artist")),
                intValue(map.get("year"))
        );
        copyInto(vinyl, map);
        return vinyl;
    }

    public static void copyInto(Vinyl vinyl, Map<String, Object> map) {
        vinyl.setTitle(stringValue(map.get("title")));
        vinyl.setArtist(stringValue(map.get("artist")));
        vinyl.setYear(intValue(map.get("year")));
        vinyl.applyRemoteState(
                stringValue(map.get("state")),
                userFromMap(map.get("reservedBy")),
                userFromMap(map.get("borrowedBy")),
                booleanValue(map.get("removalRequested")),
                booleanValue(map.get("reservationBlocked"))
        );
    }

    public static User userFromRequest(Map<String, Object> request) {
        String userId = stringValue(request.get("userId"));
        String userName = stringValue(request.get("userName"));
        if (userId == null || userId.isBlank()) {
            userId = "unknown";
        }
        if (userName == null || userName.isBlank()) {
            userName = userId;
        }
        return new User(userId, userName);
    }

    public static String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    public static int intValue(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private static boolean booleanValue(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    private static Map<String, Object> userToMap(User user) {
        if (user == null) {
            return null;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getUserID());
        map.put("name", user.getUserName());
        return map;
    }

    @SuppressWarnings("unchecked")
    private static User userFromMap(Object value) {
        if (!(value instanceof Map<?, ?> rawMap)) {
            return null;
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        return new User(stringValue(map.get("id")), stringValue(map.get("name")));
    }
}
