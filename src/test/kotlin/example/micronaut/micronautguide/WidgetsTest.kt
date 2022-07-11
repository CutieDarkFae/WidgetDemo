package example.micronaut.micronautguide

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.microsoft.azure.functions.HttpStatus
import io.micronaut.http.HttpMethod
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WidgetsTest {
  @Test
  fun testGetAllWidgets() {
    Function().use { function ->
      val response = function.request(HttpMethod.GET, "/widgets/all").invoke()
      Assertions.assertEquals(HttpStatus.OK, response.status)
    }
  }

  @Test
  fun testAddWidget() {
    Function().use { function ->
      val name = "Test Widget"
      val data = "A Widget for testing"
      val input = AddWidgetRequest(name, data)
      val mapper = jacksonObjectMapper()
      val response = function.request(HttpMethod.POST, "/widgets/add").body(mapper.writeValueAsString(input)).invoke()
      Assertions.assertEquals(HttpStatus.OK, response.status)
      val responseWidget: AddWidgetResponse = mapper.readValue(response.bodyAsString)
      Assertions.assertEquals(responseWidget.widget.name, name)
      Assertions.assertEquals(responseWidget.widget.data, data)
      Assertions.assertNotEquals(responseWidget.widget.id, null)
      Assertions.assertNotEquals(responseWidget.widget.lastActionDate, null)
      Assertions.assertNotEquals(responseWidget.widget.startDate, null)
    }
  }

}