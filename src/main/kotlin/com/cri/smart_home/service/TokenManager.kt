package com.cri.smart_home.service

import com.cri.smart_home.service.user.IUserService
import com.wanim_ms.wanimlibrary.JwtService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class TokenManager(private val request: HttpServletRequest, private val service: IUserService,private val jwtService: JwtService) {
    fun getToken(): String? {
        val authHeader: String? = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null
        }
        val jwtToken: String = authHeader.substring(7)
        if (jwtToken.isEmpty()) {
            return null
        }
        if (!service.isTokenValid(jwtToken)) {
            println("JWT Token is invalid")
            return null
        }
        return jwtToken
    }

    fun getMail(token:String): String {
        val email = jwtService.extractSubject(token)
        return email
    }

}