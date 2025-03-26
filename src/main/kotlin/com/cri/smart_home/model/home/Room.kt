package com.cri.smart_home.model.home

import com.wanim_ms.wanimlibrary.model.BaseModel
import jakarta.persistence.*

@Entity
@Table(name = "rooms")
class Room : BaseModel<Long>() {
    @Column(nullable = false)
    var name: String = ""

    @Column(length = 1000)
    var description: String? = null

    @Column(name = "image_url")
    var imageUrl: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    lateinit var home: Home

    @OneToMany(mappedBy = "room", cascade = [CascadeType.ALL], orphanRemoval = true)
    var devices: MutableList<Device> = mutableListOf()
}