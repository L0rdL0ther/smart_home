package com.cri.smart_home.model.home

import com.wanim_ms.wanimlibrary.model.BaseModel
import jakarta.persistence.*


@Entity
@Table(name = "homes")
class Home : BaseModel<Long>() {
    @Column(nullable = false)
    var name: String = ""
    @Column(nullable = false)
    var email: String = ""

    @Column(nullable = false)
    var address: String = ""

    @OneToMany(mappedBy = "home", cascade = [CascadeType.ALL], orphanRemoval = true)
    var rooms: MutableList<Room> = mutableListOf()
}