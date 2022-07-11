package example.micronaut.micronautguide

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.transaction.annotation.ReadOnly
import jakarta.inject.Singleton
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Controller("/widgets")
class WidgetController() {
  var widgets = ArrayList<Widget>()

  @Get("/all")
  fun getAllWidgets(): GetAllWidgetsResponse {
    return GetAllWidgetsResponse(widgets)
  }

  @Post("/add")
  fun addWidget(@Body request: AddWidgetRequest) : AddWidgetResponse {
    val widget = Widget(widgets.size + 1L, request.name, request.data, Date(), Date())
    widgets.add(widget)
    return AddWidgetResponse(widget)
  }
}

@Singleton
class WidgetRepository(private val entityManager: EntityManager) {
  @ReadOnly
  fun getAllWidgets(): List<Widget> {
    val sql = "Select w from Widget w order by w.lastActionDate"
    val query: TypedQuery<Widget> = entityManager.createQuery(sql)
  }
}

@Entity
@Table(name = "Widget")
public open class Widget(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long,
  @NotNull
  @Column(name="name", nullable=false)
  val name: String,
  @NotNull
  @Column(name="data", nullable=false)
  val data: String,
  @NotNull
  @Column(name="start_date", nullable=false)
  val startDate: Date,
  @NotNull
  @Column(name="last_action_date", nullable=false)
  val lastActionDate: Date)
) {

}

@Introspected
data class GetAllWidgetsResponse(val results: List<Widget>)

@Introspected
data class AddWidgetRequest(val name: String, val data: String)

@Introspected
data class AddWidgetResponse(val widget: Widget)