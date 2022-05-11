package com.example.module.authentication.controller

import com.example.common.response.ErrorResults
import com.example.common.vo.OperateError
import com.example.common.vo.OperateResult
import com.example.module.authentication.json
import com.example.module.authentication.service.AuthService
import com.example.plugin.AuthSession
import com.example.springify.ApplicationAware
import com.example.springify.Controller
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.controller.AbstractDIController

@Controller
class LoginController(override val application: Application) : AbstractDIController(application), ApplicationAware {
    private val authService: AuthService by application.closestDI().instance()

    companion object {
        const val AUTHENTICATED_SESSION = "auth-session"

        fun Application.authenticationInit() {
            val authService: AuthService by this.closestDI().instance()
            install(Authentication) {
                json("auth-api") {
                    validate { credentials ->
                        if (authService.isCredentialValid(credentials.name, credentials.password)) {
                            UserIdPrincipal(credentials.name)
                        } else null
                    }
                    challenge { _: UserPasswordCredential?, authenticationFailedCause: AuthenticationFailedCause? ->
                        when (authenticationFailedCause) {
                            AuthenticationFailedCause.NoCredentials, null -> call.respond(
                                HttpStatusCode.Unauthorized,
                                ErrorResults.NoCredentials
                            )
                            AuthenticationFailedCause.InvalidCredentials -> call.respond(
                                HttpStatusCode.Unauthorized,
                                ErrorResults.InvalidCredentials
                            )
                            is AuthenticationFailedCause.Error -> call.respond(
                                HttpStatusCode.InternalServerError,
                                ErrorResults.SystemError
                            )
                        }
                    }
                }
                session<AuthSession>(AUTHENTICATED_SESSION) {
                    validate { session ->
                        session
                    }
                    challenge {
                        call.respond(status = HttpStatusCode.Unauthorized, ErrorResults.Identityless)
                    }
                }
            }
        }
    }

    // @TODO: Typed request doesn't work
    class Routes {
        @Serializable
        @Resource("/login")
        class LoginRoute
    }

    override fun Route.getRoutes() {
        authenticate("auth-api") {
            post("/login") {
                val principal = call.principal<UserIdPrincipal>()
                if (principal == null) call.respond(HttpStatusCode.BadRequest, ErrorResults.NoCredentials) else {
                    val info = authService.getUserInfo(principal.name)
                    if (info == null) {
                        call.respond(OperateResult(error = OperateError(title = "info is null", "DevError")))
                        return@post
                    }
                    call.sessions.set(AuthSession(info))
                    call.respond(OperateResult(msg = "well done, ${principal.name}"))
                }
            }
        }
        val logoutBuild: Route.() -> Unit = {
            handle {
                if (call.sessions.get<AuthSession>() != null) {
                    call.sessions.clear<AuthSession>()
                    call.respond(OperateResult(msg = "退出成功"))
                } else {
                    call.respond(status = HttpStatusCode.Unauthorized, ErrorResults.Identityless)
                }
            }
        }
        route("/logout", HttpMethod.Get, logoutBuild)
        route("/logout", HttpMethod.Post, logoutBuild)
        authenticate("auth-session") {
            if (application.developmentMode) get("/debug/user_info") {
                call.sessions.get<AuthSession>()?.info?.let {
                    call.respond(it)
                    return@get
                }
                call.respond(OperateResult(error = OperateError(title = "info is null", "DevError")))
            }
        }
    }
}
