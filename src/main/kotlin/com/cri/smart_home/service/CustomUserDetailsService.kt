package com.cri.smart_home.service



import com.cri.smart_home.model.user.UserPrincipal
import com.cri.smart_home.param.user.UserParam
import com.cri.smart_home.service.user.IUserService
import com.cri.smart_home.spec.user.UserSpec
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val service: IUserService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = service.find(UserSpec(UserParam()).apply {
            deleted = false
            archived = false
            email = username
        })
        return UserPrincipal(user)
    }
}