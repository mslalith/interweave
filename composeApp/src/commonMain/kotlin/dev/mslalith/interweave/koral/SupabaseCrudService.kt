package dev.mslalith.interweave.koral

import io.github.jan.supabase.SupabaseClient
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class SupabaseCrudService<T : SyncableEntity>(
    private val httpClient: HttpClient,
    private val supabaseClient: SupabaseClient,
    private val tableName: String,
    private val primaryKeyColumnName: String = "id",
    private val serializer: KSerializer<T>
) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun getAll(): List<T> {
        return httpClient.get(urlString = supabaseUrl) {
            addSupabaseHeaders()
            parameter("select", "*")
        }.toEntityList()
    }

    suspend fun getById(id: String): T? {
        return httpClient.get(urlString = supabaseUrl) {
            addSupabaseHeaders()
            parameter("select", "*")
            parameter(primaryKeyColumnName, "eq.$id")
        }
            .toEntityList()
            .firstOrNull()
    }

    suspend fun upsert(item: T): T {
        return httpClient.post(urlString = supabaseUrl) {
            addSupabaseHeaders()

            header("Prefer", "return=representation,resolution=merge-duplicates")
            parameter("select", "*")

            contentType(type = ContentType.Application.Json)
            setBody(item.toJson())
        }
            .toEntityList()
            .first()
    }

    suspend fun delete(id: String) {
        httpClient.delete(urlString = supabaseUrl) {
            addSupabaseHeaders()
            parameter(primaryKeyColumnName, "eq.$id")
        }
    }

    private suspend fun HttpResponse.toEntity(): T = json.decodeFromString(serializer, bodyAsText())
    private suspend fun HttpResponse.toEntityList(): List<T> = json.decodeFromString(ListSerializer(serializer), bodyAsText())

    private fun T.toJson(): String = json.encodeToString(serializer, this)

    private val supabaseUrl: String
        get() = "${supabaseClient.supabaseHttpUrl}/rest/v1/$tableName"

    private fun HttpMessageBuilder.addSupabaseHeaders() {
        headers {
            append("apikey", supabaseClient.supabaseKey)
            append("Authorization", "Bearer ${supabaseClient.supabaseKey}")
        }
    }
}
