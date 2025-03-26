package com.cri.smart_home.controller

import com.cri.smart_home.dto.user.CUser
import com.cri.smart_home.dto.user.LUser
import com.cri.smart_home.dto.user.RUser
import com.cri.smart_home.service.user.IUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val service: IUserService
) {
    @PostMapping("/register")
    fun register(@RequestBody user: CUser): ResponseEntity<RUser> {
        return ResponseEntity.ok(service.create(user))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginUser: LUser): ResponseEntity<RUser> {
        return ResponseEntity.ok(service.login(loginUser))
    }
}