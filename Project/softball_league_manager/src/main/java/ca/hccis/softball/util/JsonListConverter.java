package ca.hccis.softball.util;

import ca.hccis.softball.model.domain.Inning;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Converts a JSON string to an ArrayList and vice versa.
 * @author BML
 * @since 20261025
 */
@Converter
public class JsonListConverter implements AttributeConverter<List<Inning>, String> {
    /** Reusable Gson instance */
    private static final Gson gson = new Gson();

    /** Explicit type for the ArrayList<Inning> so Gson preserves it during deserialization */
    private static final Type LIST_TYPE = new TypeToken<List<Inning>>() {}.getType();

    /**
     * Convert an ArrayList to JSON String. This will be done right before saving to the database.
     * @param attribute  the entity attribute value to be converted
     * @return JSON string
     * @author BML
     * @since 20261025
     */
    @Override
    public String convertToDatabaseColumn(List<Inning> attribute) {
        if (attribute == null || attribute.isEmpty()) {
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
    public List<Inning> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            // Return null.
            return null;
        }
        return gson.fromJson(dbData, LIST_TYPE);
    }
}
