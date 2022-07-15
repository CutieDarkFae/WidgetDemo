package example.micronaut.micronautguide

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import io.micronaut.http.annotation.*
import jakarta.inject.Inject
import java.util.*
import javax.persistence.*

@Controller("/widgets")
class WidgetController() {
  @Inject
  lateinit var widgetRespository: WidgetRepository

   @Get("/all")
  fun getAllWidgets(): GetAllWidgetsResponse {
    val response = widgetRespository.findAllByEndDateIsNull()
    var widgets = ArrayList<Widget>()
    response.forEach { widget -> widgets.add(widget) }
    return GetAllWidgetsResponse(widgets)
  }

  @Get("/{key}")
  fun getWidgetByKey(@PathVariable key: String): AddWidgetResponse {
    val response = widgetRespository.findByKeyAndEndDateIsNull(UUID.fromString(key))
    if (response.isPresent) return AddWidgetResponse(response.get())
    return AddWidgetResponse(null)
  }

  @Post("/add")
  fun addWidget(@Body request: AddWidgetRequest) : AddWidgetResponse {
    val widget = Widget(request.name, request.data)
    val response = widgetRespository.save(widget)
    return AddWidgetResponse(response)
  }

  @Post("/update/{key}")
  fun updateWidget(@PathVariable key: String, @Body request: AddWidgetRequest): AddWidgetResponse? {
    val response = widgetRespository.findByKeyAndEndDateIsNull(UUID.fromString(key))
    if (response.isPresent) {
      var widget = response.get()
      widget.endDate = Date()
      widgetRespository.update(widget)
      val w2 = Widget(null, widget.key, request.name, request.data, widget.endDate!!, null)
      val w3 = widgetRespository.save(w2)
      return AddWidgetResponse(w3)
    }
    return null
  }
}

@Repository
interface WidgetRepository: CrudRepository<Widget, Long> {
  fun findByKeyAndEndDateIsNull(key: UUID): Optional<Widget>
  fun findAllByEndDateIsNull(): Iterable<Widget>
}

@Entity
@Introspected
data class Widget(
  @Id
  @GeneratedValue
  val id: Long? = null,
  val key: UUID? = null,
  val name: String,
  val data: String,
  val startDate: Date = Date(),
  var endDate: Date? = null
) {
  constructor() : this(null, UUID.randomUUID(),"", "", Date(), null)
  constructor(name: String, data: String): this(null, UUID.randomUUID(), name, data, Date(), null)
}

@Introspected
data class GetAllWidgetsResponse(val results: List<Widget>)

@Introspected
data class AddWidgetRequest(val name: String, val data: String)

@Introspected
data class AddWidgetResponse(val widget: Widget?)