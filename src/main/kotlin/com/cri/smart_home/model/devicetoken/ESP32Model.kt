package com.cri.smart_home.model.devicetoken

import com.wanim_ms.wanimlibrary.model.BaseModel
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "esp_32_")
class ESP32Model : BaseModel<Long>() {
    var title: String = ""
    var email = ""
    var token = ""
}