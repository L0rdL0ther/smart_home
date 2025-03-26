package com.cri.smart_home.mapper.home

import com.cri.smart_home.dto.device.RDevice
import com.cri.smart_home.dto.esp32.REsp
import com.cri.smart_home.dto.home.RHome
import com.cri.smart_home.dto.room.RRoom
import com.cri.smart_home.model.devicetoken.ESP32Model
import com.cri.smart_home.model.home.Device
import com.cri.smart_home.model.home.Home
import com.cri.smart_home.model.home.Room
import org.springframework.stereotype.Service

@Service
class BaseMapper {

    fun createResponse(home: Home):RHome{
        return RHome().apply {
            id = home.getId()
            name = home.name
            address = home.address
        }
    }

    fun createResponse(room: Room):RRoom{
        return RRoom().apply {
            id = room.getId()
            name = room.name
            description = room.description
            imageUrl = room.imageUrl
        }
    }

    fun createResponse(device: Device): RDevice {
        return RDevice().apply {
            id = device.getId()
            name = device.name
            type = device.type
            currentValue = device.currentValue
            controlType = device.controlType
        }
    }

    fun createResponse(espModel: ESP32Model): REsp {
        return REsp().apply {
            id = espModel.getId()
            title = espModel.title
            token = espModel.token
        }
    }

}