package com.cri.smart_home

import com.wanim_ms.wanimlibrary.EnableWanimService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableWanimService
class SmartHomeApplication

fun main(args: Array<String>) {
    runApplication<SmartHomeApplication>(*args)
}
