package com.blok.Functional
import cucumber.api.java.en.Given
import cucumber.api.java.en.When
import org.apache.http.HttpStatus
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.junit.Assert

class WebServerInfrastructureStepDefinitions {
    @Given("^I can connect to my test webserver$")
    fun iCanConnectToTheWebserver() {
        var exception: Throwable? = null
        try {
            val httpclient: CloseableHttpClient = HttpClients.createDefault()
            val request: HttpGet = HttpGet("http://localhost:4567/")  // FIXME Use parametrized URL
            httpclient.execute(request)
            httpclient.close()
        } catch (e: Throwable) {
            exception = e
        }
        Assert.assertNull(exception)
    }
    @When("^I run alive check I get a 200$")
    fun iRunALiveCheckIGet200() {
        val httpclient: CloseableHttpClient = HttpClients.createDefault()
        val request: HttpGet = HttpGet("http://localhost:4567/alive")
        val response: CloseableHttpResponse = httpclient.execute(request)
        Assert.assertEquals(HttpStatus.SC_OK, response.statusLine.statusCode)
        httpclient.close()
    }
}