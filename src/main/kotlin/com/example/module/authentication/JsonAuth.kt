/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package com.example.module.authentication

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*

/**
 * A json-based authentication provider.
 *
 * @see [json]
 */
class JsonAuthenticationProvider internal constructor(config: Config) : AuthenticationProvider(config) {

    private val challengeFunction: JsonAuthChallengeFunction = config.challengeFunction

    private val authenticationFunction: AuthenticationFunction<UserPasswordCredential> =
        config.authenticationFunction

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        // TODO: 使 LoginRequestBody 可配置 （反射）

        @kotlinx.serialization.Serializable
        data class LoginRequestBody(
            val username: String,
            val password: String
        )

        val call = context.call
        val requestBody = call.receiveOrNull<LoginRequestBody>()
        val username = requestBody?.username
        val password = requestBody?.password

        val credentials = if (username != null && password != null) UserPasswordCredential(username, password) else null
        val principal = credentials?.let { (authenticationFunction)(call, it) }

        if (principal != null) {
            context.principal(principal)
            return
        }
        val cause = when (credentials) {
            null -> AuthenticationFailedCause.NoCredentials
            else -> AuthenticationFailedCause.InvalidCredentials
        }

        @Suppress("NAME_SHADOWING")
        context.challenge(jsonAuthenticationChallengeKey, cause) { challenge, call ->
            challengeFunction(JsonAuthChallengeContext(call), credentials, cause)
            if (!challenge.completed && call.response.status() != null) {
                challenge.complete()
            }
        }
    }

    /**
     * A configuration for the [json]-based authentication provider.
     */
    class Config internal constructor(name: String?) : AuthenticationProvider.Config(name) {
        internal var authenticationFunction: AuthenticationFunction<UserPasswordCredential> = { null }

        internal var challengeFunction: JsonAuthChallengeFunction =
            { _: UserPasswordCredential?, _: AuthenticationFailedCause? ->
                call.respond(UnauthorizedResponse())
            }

        /**
         * Specifies a response sent to the client if authentication fails.
         */
        fun challenge(function: JsonAuthChallengeFunction) {
            challengeFunction = function
        }

        /**
         * Sets a validation function that checks a specified [UserPasswordCredential] instance and
         * returns [UserIdPrincipal] in a case of successful authentication or null if authentication fails.
         */
        fun validate(body: suspend ApplicationCall.(UserPasswordCredential) -> Principal?) {
            authenticationFunction = body
        }

        internal fun build() = JsonAuthenticationProvider(this)
    }
}

/**
 * Installs the json-based [Authentication] provider.
 * Json-based authentication uses a web json to collect credential injsonation and authenticate a user.
 * To learn how to configure it, see [Json-based authentication](https://ktor.io/docs/json.html).
 */
fun AuthenticationConfig.json(
    name: String? = null,
    configure: JsonAuthenticationProvider.Config.() -> Unit
) {
    val provider = JsonAuthenticationProvider.Config(name).apply(configure).build()
    register(provider)
}

/**
 * A context for [JsonAuthChallengeFunction].
 */
class JsonAuthChallengeContext(
    val call: ApplicationCall
)

/**
 * Specifies what to send back if json-based authentication fails.
 */
typealias JsonAuthChallengeFunction =
        suspend JsonAuthChallengeContext.(UserPasswordCredential?, AuthenticationFailedCause?) -> Unit

private val jsonAuthenticationChallengeKey: Any = "JsonAuth"
