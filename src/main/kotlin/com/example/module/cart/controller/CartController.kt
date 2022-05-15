package com.example.module.cart.controller

import com.example.common.OperateFailCauses
import com.example.common.ensure
import com.example.common.infoOrIdentityless
import com.example.common.response.ErrorResults
import com.example.common.tryReceive
import com.example.common.vo.basic.OperateResult
import com.example.common.vo.basic.Page
import com.example.common.vo.basic.pageResponse
import com.example.module.authentication.controller.LoginController
import com.example.module.cart.service.CartService
import com.example.springify.ApplicationAware
import com.example.springify.Controller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.controller.AbstractDIController

@Controller
class CartController(override val application: Application) : AbstractDIController(application), ApplicationAware {
    private val cartService: CartService by application.closestDI().instance()

    @kotlinx.serialization.Serializable
    data class AddToCartVO(val id: Int, val quantity: Int)

    override fun Route.getRoutes() {
        authenticate(LoginController.AUTHENTICATED_SESSION) {
            post<CartApi> {
                val current = call.infoOrIdentityless() ?: return@post
                val userId = current.id.ensure()
                val page: Page = call.tryReceive() ?: return@post
                val cartItems = cartService.getCartItemsByPage(userId, page)
                return@post call.respond(
                    pageResponse(
                        Page(
                            page.pageNumber,
                            page.pageSize,
                            page.order,
                            cartItems.data.size,
                            cartItems.total
                        ), cartItems.data
                    )
                )
            }
            post<AddToCart> {
                val current = call.infoOrIdentityless() ?: return@post
                val userId = current.id.ensure()
                val addToCartVO: AddToCartVO = call.tryReceive() ?: return@post
                val result = cartService.addToCart(userId, addToCartVO.id, addToCartVO.quantity)
                if (result.success) call.respond(OperateResult(msg = "添加成功")) else when (result.cause) {
                    OperateFailCauses.RECORD_NOT_EXIST -> call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResults.RecordNotExist.apply { this.error!!.title = "${result.message} 不存在" }
                    )
                    OperateFailCauses.UNKNOWN, null ->
                        call.respond(HttpStatusCode.InternalServerError, ErrorResults.SystemError)
                }
            }
        }
    }
}