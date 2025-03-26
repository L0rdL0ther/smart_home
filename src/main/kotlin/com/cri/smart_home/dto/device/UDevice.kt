package com.cri.smart_home.dto.device

import com.cri.smart_home.enums.ControlType
import com.cri.smart_home.enums.Label

class UDevice {
    var name: String? = null
    var type: Label? = null
    var esp32Id: Long? = null
    var currentValue: String? = null
    var controlType: ControlType? = null
}