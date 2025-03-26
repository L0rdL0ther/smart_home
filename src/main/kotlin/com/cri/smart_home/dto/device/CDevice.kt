package com.cri.smart_home.dto.device

import com.cri.smart_home.enums.ControlType
import com.cri.smart_home.enums.Label

class CDevice {
    var roomId: Long = 0
    var name: String = ""
    var type: Label = Label.LIGHT
    var currentValue: String? = null
    var controlType: ControlType = ControlType.SWITCH
    var esp32DeviceId: Long = 0
}