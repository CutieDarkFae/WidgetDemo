package example.micronaut.micronautguide;

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.microsoft.azure.functions.HttpStatus
import io.micronaut.http.HttpMethod
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AzureFunctionTest {

  @Test
  fun testFunction() {
    Function().use { function ->
      val response = function.request(HttpMethod.GET, "/azure")
        .invoke()
      Assertions.assertEquals(HttpStatus.OK, response.status)
    }
  }

  @ParameterizedTest
  @ValueSource(strings = ["racecar", "radar", "able was I ere I saw elba", "¯\\_(ツ)_/¯", "\uD83D\uDE3F" ])
  fun testPost(value: String) {
    Function().use { function ->
      val inputMessage = SampleInputMessage(value)
      val mapper = jacksonObjectMapper()
      val response = function.request(HttpMethod.POST, "/azure").body(mapper.writeValueAsString(inputMessage)).invoke()
      Assertions.assertEquals(HttpStatus.OK, response.status)
      val responseMessage: SampleReturnMessage = mapper.readValue(response.bodyAsString)
      Assertions.assertEquals("Hello $value, thank you for sending the message", responseMessage.returnMessage)
    }
  }
}