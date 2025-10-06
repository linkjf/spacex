package com.linkjf.spacex.launch.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Type converters for Room database to handle complex objects
 */
class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toStringList(value: String?): List<String>? =
        value?.let {
            try {
                json.decodeFromString<List<String>>(it)
            } catch (e: Exception) {
                emptyList()
            }
        }

    @TypeConverter
    fun fromBoolean(value: Boolean?): String? = value?.toString()

    @TypeConverter
    fun toBoolean(value: String?): Boolean? = value?.toBooleanStrictOrNull()

    @TypeConverter
    fun fromInt(value: Int?): String? = value?.toString()

    @TypeConverter
    fun toInt(value: String?): Int? = value?.toIntOrNull()

    @TypeConverter
    fun fromLong(value: Long?): String? = value?.toString()

    @TypeConverter
    fun toLong(value: String?): Long? = value?.toLongOrNull()

    @TypeConverter
    fun fromDouble(value: Double?): String? = value?.toString()

    @TypeConverter
    fun toDouble(value: String?): Double? = value?.toDoubleOrNull()
}
