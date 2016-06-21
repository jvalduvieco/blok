package com.blok.Post.Functional

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.util.*

fun HttpResponse.hasFailed(): Boolean {
    return when (this.statusLine.statusCode) {
        HttpStatus.SC_OK -> false
        HttpStatus.SC_ACCEPTED -> false
        HttpStatus.SC_CREATED -> false
        else -> true
    }
}

class FailedRequest(val statusCode: Int, val responseBody: String, val url:String) : Throwable()

inline fun <reified T: Any> get(url: String): T {
    val client: CloseableHttpClient = HttpClients.createDefault()
    val request: HttpGet = HttpGet(url)
    val response: CloseableHttpResponse = client.execute(request)
    assertResponseWentOk(response, url)
    val payload: T = Gson().fromJson<T>(EntityUtils.toString(response.entity))
    client.close()
    return payload
}

 fun assertResponseWentOk(response: CloseableHttpResponse, url: String) {
    if (response.hasFailed()) {
        println("URL: ${url} Status: ${response.statusLine.statusCode} response: ${response.entity.toString()}")
        throw FailedRequest(response.statusLine.statusCode, response.entity.toString(), url)
    }
}

fun post(url: String, payload: String): String {
    val client: CloseableHttpClient = HttpClients.createDefault()
    val request: HttpPost = HttpPost(url)
    val postingString: StringEntity = StringEntity(payload)
    request.entity = postingString
    val response: CloseableHttpResponse = client.execute(request)
    assertResponseWentOk(response, url)
    val result: String = EntityUtils.toString(response.entity)
    client.close()
    return result
}
fun put(url: String, payload: String): Int {
    val client: CloseableHttpClient = HttpClients.createDefault()
    val request: HttpPut = HttpPut(url)
    val postingString: StringEntity = StringEntity(payload)
    request.entity = postingString
    val response: CloseableHttpResponse = client.execute(request)
    assertResponseWentOk(response, url)
    val result: Int = response.statusLine.statusCode
    client.close()
    return result
}
fun delete(url: String) {
    val client: CloseableHttpClient = HttpClients.createDefault()
    val request: HttpDelete = HttpDelete(url);
    val response: CloseableHttpResponse = client.execute(request)
    assertResponseWentOk(response, url)
    client.close()
}

fun parseObjectCreationResponse(response: String): UUID {
    val parser: JsonParser = JsonParser()
    val rootObj: JsonObject = parser.parse(response).getAsJsonObject();
    return UUID.fromString(rootObj.get("id").asString)
}
