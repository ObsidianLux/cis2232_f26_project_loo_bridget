package ca.hccis.softball.util;

import ca.hccis.softball.entity.Inning;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Converts a JSON string to an ArrayList and vice versa.
 * @author BML
 * @since 20261025
 */
@Converter
public class JsonListConverter implements AttributeConverter<ArrayList<Inning>, String> {
    /** Reusable Gson instance */
    private static final Gson gson = new Gson();

    /** Explicit type for the ArrayList<Inning> so Gson preserves it during deserialization */
    private static final Type LIST_TYPE = new TypeToken<ArrayList<Inning>>() {}.getType();

    /**
     * Convert an ArrayList to JSON String. This will be done right before saving to the database.
     * @param attribute  the entity attribute value to be converted
     * @return JSON string
     * @author BML
     * @since 20261025
     */
    @Override
    public String convertToDatabaseColumn(ArrayList<Inning> attribute) {
        if (attribute == null) {
            return null;
        }
        return gson.toJson(attribute);
    }

    /**
     * Convert JSON String to ArrayList. This will be done right after fetching from the database.
     * @param dbData  the data from the database column to be converted
     * @return ArrayList of Innings
     * @author BML
     * @since 20261025
     */
    @Override
    public ArrayList<Inning> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            // Return an empty list instead of null.
            return new ArrayList<>();
        }
        return gson.fromJson(dbData, LIST_TYPE);
    }
}
