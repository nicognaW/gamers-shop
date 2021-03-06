package com.example.module.user.controller

import com.example.common.ensure
import com.example.common.infoOrIdentityless
import com.example.common.respondOperateResult
import com.example.common.tryReceive
import com.example.common.vo.PhysicalAddress
import com.example.common.vo.UserInfo
import com.example.common.vo.basic.OperateResult
import com.example.common.vo.basic.Page
import com.example.common.vo.basic.pageResponse
import com.example.module.authentication.controller.LoginController.Companion.AUTHENTICATED_SESSION
import com.example.module.user.service.UserInfoService
import com.example.plugin.AuthSession
import com.example.springify.ApplicationAware
import com.example.springify.Controller
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.controller.AbstractDIController


@Suppress("unused")
@Controller
class UserInfoController(override val application: Application) : AbstractDIController(application), ApplicationAware {

    private val userInfoService: UserInfoService by application.closestDI().instance()


    override fun Route.getRoutes() {
        authenticate(AUTHENTICATED_SESSION) {
            get<UserInfoApi> {
                call.respond(call.infoOrIdentityless() ?: return@get)
            }
            post<UserInfoApi.Addresses> {
                val current = call.infoOrIdentityless() ?: return@post
                val userId = current.id.ensure()
                val page: Page = call.tryReceive() ?: return@post
                val addresses = userInfoService.getAddressesByPage(
                    userId, page.pageNumber, page.pageSize
                )
                call.respond(
                    pageResponse(
                        meta = Page(
                            pageNumber = page.pageNumber,
                            pageSize = page.pageSize,
                            pageTotal = addresses.data.size,
                            total = addresses.total
                        ), data = addresses.data
                    )
                )
            }
            post<UserInfoApi.SaveAddress> {
                val pa: PhysicalAddress = call.tryReceive() ?: return@post
                val current = call.infoOrIdentityless() ?: return@post
                val userId = current.id.ensure()
                userInfoService.saveAddress(
                    userId,
                    pa.realName,
                    pa.phoneNum,
                    pa.area,
                    pa.city,
                    pa.county,
                    pa.detail,
                    pa.id
                )
                call.respond(OperateResult(msg = "????????????"))
            }
            post<UserInfoApi.RemoveAddresses> {
                val ids: List<Int> = call.tryReceive() ?: return@post
                val current = call.infoOrIdentityless() ?: return@post
                val userId = current.id.ensure()
                val result = userInfoService.removeAddresses(userId, ids)
                call.respondOperateResult(result)
            }
            post<UserInfoApi.Update> {
                val tobeUpdated: UserInfo = call.tryReceive() ?: return@post
                val current = call.infoOrIdentityless() ?: return@post
                val userId = current.id.ensure()
                userInfoService.updateUserInfo(
                    userId,
                    tobeUpdated.username,
                    tobeUpdated.headPicUrl,
                    tobeUpdated.phoneNum,
                    tobeUpdated.emailAddress
                )
                val updated = userInfoService.getUserInfo(userId)
                call.sessions.get<AuthSession>()!!.info = updated
                call.respond(OperateResult(msg = "?????????????????????"))
            }
        }
    }
}
