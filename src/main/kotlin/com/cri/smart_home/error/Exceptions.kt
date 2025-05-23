package com.cri.smart_home.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

class Exceptions {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "USER ALREADY HAS A SAME EMAIL")
    class BadRequestEx (message: String) : RuntimeException(message)
}