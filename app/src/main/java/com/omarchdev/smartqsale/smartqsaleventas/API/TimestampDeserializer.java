package com.omarchdev.smartqsale.smartqsaleventas.API;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.sql.Timestamp;

public class TimestampDeserializer implements JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String s = json.getAsString();
        try {
            s = s.replace("T", " ").replace("Z", "");
            if (s.contains("+")) {
                s = s.substring(0, s.indexOf("+"));
            }
            if (s.contains(".")) {
                int dot = s.lastIndexOf(".");
                String ms = s.substring(dot + 1);
                if (ms.length() > 3) {
                    s = s.substring(0, dot + 4);
                }
                StringBuilder sb = new StringBuilder(s);
                while (sb.substring(dot + 1).length() < 3) {
                    sb.append("0");
                }
                s = sb.toString();
            } else {
                s += ".000";
            }
            return Timestamp.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
}
