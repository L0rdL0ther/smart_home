package com.cri.smart_home.service.user

import com.cri.smart_home.dto.user.CUser
import com.cri.smart_home.dto.user.LUser
import com.cri.smart_home.dto.user.RUser
import com.cri.smart_home.dto.user.UUser
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.user.UserMapper
import com.cri.smart_home.model.user.UserModel
import com.cri.smart_home.param.user.UserParam
import com.cri.smart_home.repo.user.UserRepo
import com.cri.smart_home.spec.user.UserSpec
import com.wanim_ms.wanimlibrary.JwtService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IUserService(
    private val repo: UserRepo,
    private val mapper: UserMapper,
    private val jwtService: JwtService,
) : UserService {

    val PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}\$"
    val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"

    override fun create(crate: CUser): RUser {
        if (!EMAIL_REGEX.toRegex().matches(crate.email.toString()))
            throw Exceptions.BadRequestEx("Invalid email format")
        if (!PASSWORD_REGEX.toRegex().matches(crate.password.toString()))
            throw Exceptions.BadRequestEx("Invalid password format")
        if (exists(UserSpec(UserParam()).apply {
                email = crate.email
                deleted = false
                archived = false
            }))
            throw Exceptions.BadRequestEx("User already exists")

        val newUser = UserModel().apply {
            email = crate.email
            password = crate.password.toString()
            userName = crate.username
        }

        val savedUser = save(newUser)
        val claims = HashMap<String, Any>()
        claims["pk"] = savedUser.pk

        return mapper.crateResponse(savedUser).apply {
            token = jwtService.generateToken(claims, savedUser.email!!)
        }
    }

    override fun findByEmail(email: String): UserModel {
        return find(UserSpec(UserParam()).apply {
            deleted = false
            archived = false
            this.email = email
        })
    }

    override fun login(lUser: LUser): RUser {
        if (!EMAIL_REGEX.toRegex().matches(lUser.email))
            throw Exceptions.BadRequestEx("Invalid email format")

        val spec = UserSpec(UserParam()).apply {
            email = lUser.email
            deleted = false
            archived = false
        }

        val user = find(spec)
        if (!user.isPasswordMatch(lUser.password)) throw Exceptions.BadRequestEx("Wrong password")
        return mapper.crateResponse(user).apply {
            val claims = HashMap<String, Any>()
            claims["pk"] = user.pk
            this.token = jwtService.generateToken(claims, user.email!!)
        }
    }

    override fun isTokenValid(token: String): Boolean {
        if (!jwtService.isJwtCorrectFormat(token)) return false

        val claims = jwtService.extractClaims(token)
        val email = jwtService.extractSubject(token) as? String
            ?: throw Exceptions.BadRequestEx("Token is missing 'email' claim")
        val pk = claims["pk"] as? Long
            ?: throw Exceptions.BadRequestEx("Token is missing 'pk' claim")
        val user = findByEmail(email)
        if (user.pk != pk || jwtService.isTokenExpired(token)) {
            throw Exceptions.BadRequestEx("Invalid or expired token")
        }
        return true
    }

    override fun exists(spec: UserSpec): Boolean {
        return repo.exists(spec, UserModel::class.java)
    }

    override fun find(spec: UserSpec): UserModel {
        return repo.findOne(spec, UserModel::class.java).orElseThrow { throw Exceptions.BadRequestEx("User not found") }
    }

    override fun findAll(spec: UserSpec): Page<UserModel> {
        return repo.findAll(spec, UserModel::class.java)
    }

    override fun findById(id: Long): UserModel {
        return find(UserSpec(UserParam()).apply {
            this.id = id
            deleted = false
            archived = false
        })
    }

    override fun save(main: UserModel): UserModel {
        return repo.save(main)
    }
    override fun delete(id: Long) {
        repo.delete(findById(id))
    }
    override fun update(main: UserModel, update: UUser): RUser {
        TODO("Not yet implemented")
    }
}