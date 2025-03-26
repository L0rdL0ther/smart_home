package com.cri.smart_home.model.user

import com.cri.smart_home.enums.Permission
import com.cri.smart_home.enums.Role
import com.wanim_ms.wanimlibrary.model.BaseModel
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Entity
@Table(name = "users")
class UserModel : BaseModel<Long>(), UserDetails {


    var email: String? = null
    private var password: String? = null
    var userName: String? = null

    @Enumerated(EnumType.STRING)
    var role: Role = Role.HOME_ADMIN


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")]
    )
    @Column(name = "permissions", nullable = false)
    var permissions: MutableList<Permission> = mutableListOf()

    fun setPassword(rawPassword: String) {
        this.password = PasswordUtils.bcryptEncoder.encode(rawPassword)
    }

    fun isPasswordMatch(rawPassword: String): Boolean {
        return PasswordUtils.bcryptEncoder.matches(rawPassword, this.password)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return getAuth().toMutableList()
    }

    override fun getPassword(): String {
        return "hell nah"
    }

    override fun getUsername(): String {
        return email!!
    }

    fun getAuth(): List<SimpleGrantedAuthority> {
        val grantedRoles: MutableList<SimpleGrantedAuthority> = mutableListOf()
        role.getAuth().forEach { grantedRoles.add(SimpleGrantedAuthority(it)) }
        permissions.forEach { perms ->
            val permissionAuthority = SimpleGrantedAuthority("ROLE_${perms.name}")
            if (grantedRoles.none { it.authority == permissionAuthority.authority }) {
                grantedRoles.add(permissionAuthority)
            }
        }

        return grantedRoles
    }
}

object PasswordUtils {
    val bcryptEncoder = BCryptPasswordEncoder()
}


private fun Role.getAuth(): MutableList<String> {
    val authorities = permission.map { ("ROLE_" + it.name) }.toMutableList()
    authorities.add(("ROLE_${this.name}"))
    return authorities
}
