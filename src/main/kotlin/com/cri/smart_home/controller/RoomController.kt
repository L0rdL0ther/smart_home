package com.cri.smart_home.controller

import com.cri.smart_home.dto.room.CRoom
import com.cri.smart_home.dto.room.RRoom
import com.cri.smart_home.dto.room.URoom
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.param.room.RoomParam
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.service.room.IRoomService
import com.cri.smart_home.spec.room.RoomSpec
import com.wanim_ms.wanimlibrary.model.SortOrder
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rooms")
class RoomController(
    private val service: IRoomService,
    private val mapper: BaseMapper,
    private val tokenManager: TokenManager
) {
    @PostMapping
    fun create(@RequestBody room: CRoom): ResponseEntity<RRoom> {
        return ResponseEntity.ok(service.create(room))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<RRoom> {
        val room = service.findById(id)
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        if (room.home.email != email)
            throw Exceptions.BadRequestEx("Invalid email for room")
        return ResponseEntity.ok(mapper.createResponse(room))
    }

    @PostMapping("/all/{homeId}")
    fun getRoomsByHomeId(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?,
        @RequestParam(required = false) sortOrder: SortOrder?,
        @RequestParam(required = false) sortBy: String?,
        @PathVariable("homeId") homeId: Long,
        @RequestBody param: RoomParam
    ): ResponseEntity<Page<RRoom>> {
        val updatedParam = param.apply {
            this.page = page ?: this.page
            this.size = pageSize ?: this.size
            this.sortOrder = sortOrder ?: this.sortOrder
            this.sortBy = sortBy ?: this.sortBy
        }
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        val rooms = service.findAll(RoomSpec(updatedParam).apply {
            this.homeId = homeId
            deleted = false
            archived = false
        })
        if (rooms.first().home.email != email) {
            throw Exceptions.BadRequestEx("Invalid email for room")
        }

        return ResponseEntity.ok(rooms.map { mapper.createResponse(it) })
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody update: URoom): ResponseEntity<RRoom> {
        val room = service.findById(id)

        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        if (room.home.email != email)
            throw Exceptions.BadRequestEx("Invalid email for room")

        return ResponseEntity.ok(service.update(room, update))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        val data = service.findById(id)
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        if (data.home.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        service.delete(id)
    }

}