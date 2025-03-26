package com.cri.smart_home.service.device

import com.cri.smart_home.controller.Esp32WebSocketHandler
import com.cri.smart_home.dto.device.CDevice
import com.cri.smart_home.dto.device.RDevice
import com.cri.smart_home.dto.device.UDevice
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.model.home.Device
import com.cri.smart_home.param.device.DeviceParam
import com.cri.smart_home.param.room.RoomParam
import com.cri.smart_home.repo.home.DeviceRepo
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.service.esp32.IESPService
import com.cri.smart_home.service.room.IRoomService
import com.cri.smart_home.spec.device.DeviceSpec
import com.cri.smart_home.spec.room.RoomSpec
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IDeviceService(
    private val repo: DeviceRepo,
    private val mapper: BaseMapper,
    private val roomService: IRoomService,
    private val tokenManager: TokenManager,
    private val esp32WebSocketHandler: Esp32WebSocketHandler,
    private var esp32Service: IESPService
) : DeviceService {
    override fun create(crate: CDevice): RDevice {

        var email: String?
        tokenManager.getToken().let {
            email = it?.let { it1 -> tokenManager.getMail(it1) }
        }
        if (email == null)
            throw Exceptions.BadRequestEx("Invalid token")

        val room = roomService.find(RoomSpec(RoomParam()).apply {
            this.id = crate.roomId
            deleted = false
            archived = false
        })

        if (room.home.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        val esp32 = esp32Service.findById(crate.esp32DeviceId)
        if (esp32.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        val newDevice = Device().apply {
            name = crate.name
            type = crate.type
            currentValue = crate.currentValue
            controlType = crate.controlType
            this.esp32DeviceId = esp32.getId()
            this.room = room
        }
        return mapper.createResponse(save(newDevice))
    }


    override fun exists(spec: DeviceSpec): Boolean {
        return repo.exists(spec, Device::class.java)
    }

    override fun find(spec: DeviceSpec): Device {
        return repo.findOne(spec, Device::class.java).orElseThrow { Exceptions.BadRequestEx("Device not found") }
    }

    override fun findAll(spec: DeviceSpec): Page<Device> {
        return repo.findAll(spec, Device::class.java)
    }

    override fun writeData(id: Long, data: String) {
        val espId = getEspId(id)
        val device = findById(id)
        val responseString = "datasend:"+id+":"+device.controlType.name+":"+data
        println(responseString)

        esp32WebSocketHandler.sendBinaryMessageToEsp32(espId, responseString.toByteArray())
    }

    override fun setData(id: Long, data: String): Device {
        val device = findById(id)
        device.currentValue = data
        return save(device)
    }

    override fun findById(id: Long): Device {
        return find(DeviceSpec(DeviceParam()).apply {
            this.id = id
            deleted = false
            archived = false
        })
    }

    override fun getEspId(id: Long): Long {
        return findById(id).esp32DeviceId
    }

    override fun save(main: Device): Device {
        return repo.save(main)
    }

    override fun update(main: Device, update: UDevice): RDevice {
        update.name?.let { main.name = it }
        update.type?.let { main.type = it }
        update.currentValue?.let { main.currentValue = it }
        update.controlType?.let { main.controlType = it }
        return mapper.createResponse(save(main))
    }

    override fun delete(id: Long) {
        repo.delete(findById(id))
    }
}