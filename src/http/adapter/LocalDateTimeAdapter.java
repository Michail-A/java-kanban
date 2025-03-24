package http.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import exception.LocalDateTimeParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.value("null");
        } else {
            jsonWriter.value(localDateTime.format(formatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) {
        try {
            return LocalDateTime.parse(jsonReader.nextString(), formatter);
        } catch (Exception e) {
            throw new LocalDateTimeParseException("Ошибка формата при парсинге строки");
        } finally {
            return null;
        }

    }
}
