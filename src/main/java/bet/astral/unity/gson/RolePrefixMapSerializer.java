package bet.astral.unity.gson;

import bet.astral.unity.model.FPrefix;
import bet.astral.unity.model.FRole;
import bet.astral.unity.utils.collections.PrefixMap;
import bet.astral.unity.utils.UniqueId;
import com.google.gson.*;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class RolePrefixMapSerializer implements JsonSerializer<PrefixMap>, JsonDeserializer<PrefixMap> {
	@Override
	public JsonElement serialize(PrefixMap fRoleFPrefixMap, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject object = new JsonObject();
		for (UniqueId uniqueIdHolder : fRoleFPrefixMap.keySet()){
			object.addProperty(uniqueIdHolder.getUniqueIdString(), fRoleFPrefixMap.get(uniqueIdHolder).asJson());
		}
		return object;
	}

	@Override
	public PrefixMap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		PrefixMap prefixMap = new PrefixMap();
		if (jsonElement.isJsonObject()){
			JsonObject obj = jsonElement.getAsJsonObject();
			for (String key : obj.keySet()){
				FPrefix prefix = new FPrefix(null, GsonComponentSerializer.gson().deserialize(obj.get(key).getAsString()));

				UUID uniqueId = UUID.fromString(key);
				FRole role = FRole.valueOf(uniqueId);

				prefixMap.put(role, prefix);
			}
		}
		return prefixMap;
	}
}
