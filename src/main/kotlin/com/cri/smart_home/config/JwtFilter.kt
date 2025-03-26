package com.cri.smart_home.config

import com.cri.smart_home.service.user.IUserService
import com.wanim_ms.wanimlibrary.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val service: IUserService,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService

) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Retrieve the Authorization header
        val authHeader: String? = request.getHeader("Authorization")
        println("Authorization Header: $authHeader")

        // If no Authorization header or if it's not a Bearer token, just continue the filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            println("Authorization header is missing or invalid")
            filterChain.doFilter(request, response)
            return
        }

        // Extract the JWT token
        val jwtToken: String = authHeader.substring(7) // Remove "Bearer " from the token
        println("Extracted JWT Token: $jwtToken")

        // If there's no JWT token, continue the filter chain
        if (jwtToken.isEmpty()) {
            println("JWT Token is empty")
            filterChain.doFilter(request, response)
            return
        }

        // Validate the token
        if (!service.isTokenValid(jwtToken)) {
            println("JWT Token is invalid")
            filterChain.doFilter(request, response)
            return
        }

        val email = jwtService.extractSubject(jwtToken)
        println("Extracted Email from JWT Token: $email")

        // Load the user details using UserDetailsService
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)
        println("Loaded UserDetails: $userDetails")

        // Create a new authentication token using the user details
        val authToken = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )
        userDetails.authorities.forEach { principal ->
            println("Authority: $principal")
        }

        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
        println("Authentication token set in SecurityContext")

        filterChain.doFilter(request, response)
        println("Filter chain continued")
    }

}