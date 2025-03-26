package com.cri.smart_home.service.esp32

import com.cri.smart_home.dto.esp32.CEsp
import com.cri.smart_home.dto.esp32.REsp
import com.cri.smart_home.dto.esp32.UEsp
import com.cri.smart_home.model.devicetoken.ESP32Model
import com.cri.smart_home.spec.ESP32Spec
import com.wanim_ms.wanimlibrary.service.BaseServiceHandler

interface ESPService : BaseServiceHandler<ESP32Model,CEsp,UEsp,REsp,ESP32Spec,Long>