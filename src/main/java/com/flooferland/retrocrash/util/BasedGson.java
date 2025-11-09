package com.flooferland.retrocrash.util;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

/**
 * In what world do we live in where a JSON library does not support setting custom indentation?
 */
public class BasedGson<T> {
	protected Class<T> type;
	protected Gson internal = new GsonBuilder()
		.serializeNulls()
		.setLenient()
		.disableJdkUnsafe()
		.setPrettyPrinting()
		.excludeFieldsWithoutExposeAnnotation()
		.create();
	protected @Nullable String comment;

	public BasedGson(Class<T> classType, @Nullable String comment) {
		this.type = classType;
		this.comment = comment;
	}

	public T deserialize(Reader reader) throws JsonIOException, JsonSyntaxException {
		return internal.fromJson(reader, type);
	}

	public String serialize(T data) throws IOException {
		var writer = new StringWriter();
		var jsonWriter = new JsonWriter(writer);
		jsonWriter.setIndent("\t");
		jsonWriter.setLenient(true);
		jsonWriter.setHtmlSafe(false);
		jsonWriter.setSerializeNulls(true);

		var built = internal.toJsonTree(data).getAsJsonObject();
		var object = new JsonObject();
		if (comment != null) {
			object.addProperty("comment", comment);
		}
		for (String key : built.keySet()) {
			object.add(key, built.get(key));
		}

		internal.toJson(object, jsonWriter);
		return writer.toString();
	}
}
