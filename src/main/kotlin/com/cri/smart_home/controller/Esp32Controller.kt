package com.cri.smart_home.controller

import com.cri.smart_home.dto.esp32.CEsp
import com.cri.smart_home.dto.esp32.REsp
import com.cri.smart_home.dto.esp32.UEsp
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.param.ESP32Param
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.service.esp32.IESPService
import com.cri.smart_home.spec.ESP32Spec
import com.wanim_ms.wanimlibrary.model.SortOrder
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/esp32")
class Esp32Controller(
    private val service: IESPService,
    private val mapper: BaseMapper,
    private val tokenManager: TokenManager
) {

    @PostMapping
    fun create(@RequestBody createRequest: CEsp): REsp {
        return service.create(createRequest)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): REsp {
        return mapper.createResponse(service.find(ESP32Spec(ESP32Param()).apply {
            this.id = id
            email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
                ?: throw Exceptions.BadRequestEx("Invalid token for room")
            deleted = false
            archived = false
        }))
    }

    @PostMapping("/all")
    fun getAll(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?,
        @RequestParam(required = false) sortOrder: SortOrder?,
        @RequestParam(required = false) sortBy: String?,
        @RequestBody param: ESP32Param
    ): Page<REsp> {

        val updatedParam = param.apply {
            this.page = page ?: this.page
            this.size = pageSize ?: this.size
            this.sortOrder = sortOrder ?: this.sortOrder
            this.sortBy = sortBy ?: this.sortBy
        }

        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")

        return service.findAll(ESP32Spec(updatedParam).apply {
            this.email = email
            deleted = false
            archived = false
        }).map { mapper.createResponse(it) }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updateRequest: UEsp): REsp {
        val esp32 = service.findById(id)

        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")

        if (esp32.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        return service.update(esp32, updateRequest)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        val data = service.findById(id)
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        if (data.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        service.delete(id)
    }
}