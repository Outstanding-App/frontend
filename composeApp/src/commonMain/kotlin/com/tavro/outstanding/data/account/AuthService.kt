package com.tavro.outstanding.data.account

import com.tavro.outstanding.feature.login.UserSession
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

// TODO: Move
@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val password_confirmation: String,
    val email: String,
)

@Serializable
data class AuthResponse(
    val user_id: String,
    val token: String,
)

interface AuthService {
    suspend fun login(username: String, password: String): Result<UserSession>
    suspend fun register(username: String, password: String, passwordConfirmation: String, email: String): Result<UserSession>
}

class HttpAuthService(private val client: HttpClient) : AuthService {
    override suspend fun login(username: String, password: String): Result<UserSession> =
        runCatching {
            client.post("http://localhost:8000/users/login/") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }.body<AuthResponse>().toSession(username)
        }

    override suspend fun register(
        username: String,
        password: String,
        passwordConfirmation: String,
        email: String
    ): Result<UserSession> = runCatching {
        client.post("http://localhost:8000/users/register/") {
            contentType(ContentType.Application.Json)
            println(RegisterRequest(username, password, passwordConfirmation, email))
            setBody(RegisterRequest(username, password, passwordConfirmation, email))
        }.body<AuthResponse>().toSession(username)
    }

    private fun AuthResponse.toSession(username: String) = UserSession(
        user_id = user_id,
        username = username,
    ).withToken(token)
}
