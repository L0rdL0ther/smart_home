package com.cri.smart_home.service.device

import com.cri.smart_home.dto.device.CDevice
import com.cri.smart_home.dto.device.RDevice
import com.cri.smart_home.dto.device.UDevice
import com.cri.smart_home.model.home.Device
import com.cri.smart_home.spec.device.DeviceSpec
import com.wanim_ms.wanimlibrary.service.BaseServiceHandler

interface DeviceService : BaseServiceHandler<Device, CDevice, UDevice, RDevice, DeviceSpec, Long> {
    fun getEspId(id:Long):Long
    fun writeData(id:Long, data:String)
    fun setData(id:Long, data:String) : Device
}