package com.example.module.product.controller

import com.example.common.PageDataDTO
import com.example.common.tryReceive
import com.example.common.vo.*
import com.example.module.product.service.ProductService
import com.example.springify.ApplicationAware
import com.example.springify.Controller
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.controller.AbstractDIController

@Controller
class ProductController(override val application: Application) : AbstractDIController(application), ApplicationAware {
    private val productService: ProductService by application.closestDI().instance()
    override fun Route.getRoutes() {
        get<ProductApi.Tags> {
            val tags: List<Tag> = productService.getTags()
            call.respond(ordinaryResponse(mapOf("count" to tags.size), tags))
        }
        post<ProductApi> {
            val page: Page = call.tryReceive() ?: return@post
            val tags =
                it.tags?.map { tag -> if (tag.toIntOrNull() != null) Tag(id = tag.toIntOrNull()) else Tag(id = Int.MIN_VALUE) }
            val products: PageDataDTO<Product> = if (tags == null) {
                productService.getProductsByPage(page)
            } else {
                productService.getTaggedProductsByPage(page, tags)
            }
            call.respond(
                pageResponse(
                    Page(
                        page.pageNumber,
                        page.pageSize,
                        page.order,
                        products.data.size,
                        products.total
                    ), products.data
                )
            )
        }
    }
}