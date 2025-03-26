package com.cri.smart_home.controller


import com.cri.smart_home.dto.device.CDevice
import com.cri.smart_home.dto.device.RDevice
import com.cri.smart_home.dto.device.UDevice
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.model.home.Device
import com.cri.smart_home.param.device.DeviceParam
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.service.device.IDeviceService
import com.cri.smart_home.spec.device.DeviceSpec
import com.wanim_ms.wanimlibrary.model.SortOrder
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/devices")
class DeviceController(
    private val service: IDeviceService,
    private val mapper: BaseMapper,
    private val tokenManager: TokenManager,
) {
    @PostMapping
    fun create(@RequestBody device: CDevice): ResponseEntity<RDevice> {
        return ResponseEntity.ok(service.create(device))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<RDevice> {
        return ResponseEntity.ok(mapper.createResponse(service.findById(id)))
    }

    @PutMapping("/{id}/write")
    fun writeData(@PathVariable id: Long,@RequestParam data: String) {
        val device = service.find(DeviceSpec(DeviceParam()).apply { this.id = id })

        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        if (device.room.home.email != email)
            throw Exceptions.BadRequestEx("The email address doesn't match")
        service.writeData(id,data)
    }

    @PostMapping("/all/{roomId}")
    fun getAll(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?,
        @RequestParam(required = false) sortOrder: SortOrder?,
        @RequestParam(required = false) sortBy: String?,
        @PathVariable("roomId") roomId:Long?,
        @RequestBody param: DeviceParam
    ): ResponseEntity<Page<RDevice>> {

        val updatedParam = param.apply {
            this.page = page ?: this.page
            this.size = pageSize ?: this.size
            this.sortOrder = sortOrder ?: this.sortOrder
            this.sortBy = sortBy ?: this.sortBy
        }

        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        val device = service.findAll(DeviceSpec(updatedParam).apply {
            this.roomId = roomId
            deleted = false
            archived = false
        })
        if (device.first().room.home.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        return ResponseEntity.ok(device.map { mapper.createResponse(it) })
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody update: UDevice): ResponseEntity<RDevice> {
        val device = service.findById(id)
        return ResponseEntity.ok(service.update(device, update))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        val data = service.findById(id)
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        if (data.room.home.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        service.delete(id)
    }
}