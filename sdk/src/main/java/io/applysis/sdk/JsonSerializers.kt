package io.applysis.sdk

import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * No need to use fancy JSON serializers.
 * Since they increase APK size a lot.
 * Having lightweight implementation for this library is enough.
 */
internal fun Feedback.toJson(): JSONObject {
    return JSONObject().apply {
        put("text", text)
        put("title", title)
        put("date", date?.toIsoString())
        put("rating", rating)
        put("author", author)
        put("region", region)
        put("version", version)
    }
}

internal fun List<Feedback>.toJson(): String {
    return JSONArray().apply {
        forEach {
            put(it.toJson())
        }
    }.toString()
}

internal fun Date.toIsoString(): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US).format(this)
}
