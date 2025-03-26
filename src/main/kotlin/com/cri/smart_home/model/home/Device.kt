package com.cri.smart_home.model.home

import com.cri.smart_home.enums.ControlType
import com.cri.smart_home.enums.Label
import com.wanim_ms.wanimlibrary.model.BaseModel
import jakarta.persistence.*

@Entity
@Table(name = "devices")
class Device : BaseModel<Long>() {
    @Column(nullable = false)
    var name: String = ""

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: Label = Label.LIGHT // Default value

    @Column(name = "current_value")
    var currentValue: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var controlType: ControlType = ControlType.SWITCH

    var esp32DeviceId: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    lateinit var room: Room
}

