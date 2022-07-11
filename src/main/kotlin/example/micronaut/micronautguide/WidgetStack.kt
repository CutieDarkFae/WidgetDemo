package example.micronaut.micronautguide

import io.micronaut.context.annotation.Executable
import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.transaction.annotation.ReadOnly
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Controller("/widgets")
class WidgetController() {
  @Inject
  lateinit var widgetRespository: WidgetRepository

   @Get("/all")
  fun getAllWidgets(): GetAllWidgetsResponse {
    val response = widgetRespository.findAll()
    var widgets = ArrayList<Widget>()
    response.forEach { widget -> widgets.add(widget) }
    return GetAllWidgetsResponse(widgets)
  }

  @Post("/add")
  fun addWidget(@Body request: AddWidgetRequest) : AddWidgetResponse {
    val widget = widgetRespository.addWidget(request.name, request.data)
    return AddWidgetResponse(widget)
  }
}

@Repository
interface WidgetRepository: CrudRepository<Widget, Long> {
  @Executable
  fun addWidget(name: String, data: String): Widget;
}

@Entity
data class Widget(
  @Id
  @GeneratedValue
  val id: Long,
  val name: String,
  val data: String,
  val startDate: Date = Date(),
  val lastActionDate: Date = Date()
) {
  constructor() : this(-1, "", "", Date(), Date())
}

@Introspected
data class GetAllWidgetsResponse(val results: List<Widget>)

@Introspected
data class AddWidgetRequest(val name: String, val data: String)

@Introspected
data class AddWidgetResponse(val widget: Widget)