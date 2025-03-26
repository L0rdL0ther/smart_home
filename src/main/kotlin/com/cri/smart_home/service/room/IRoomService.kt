package com.cri.smart_home.service.room

import com.cri.smart_home.dto.room.CRoom
import com.cri.smart_home.dto.room.RRoom
import com.cri.smart_home.dto.room.URoom
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.model.home.Room
import com.cri.smart_home.param.home.HomeParam
import com.cri.smart_home.param.room.RoomParam
import com.cri.smart_home.repo.home.RoomRepo
import com.cri.smart_home.service.home.IHomeService
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.spec.home.HomeSpec
import com.cri.smart_home.spec.room.RoomSpec
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IRoomService(
    private val repo: RoomRepo,
    private val mapper: BaseMapper,
    private val homeService: IHomeService,
    private val tokenManager: TokenManager,
) : RoomService {
    override fun create(crate: CRoom): RRoom {
        if (exists(RoomSpec(RoomParam().apply {
            name = crate.name
            })))
            throw Exceptions.BadRequestEx("Already exists Room")

        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")

        val home = homeService.find(HomeSpec(HomeParam()).apply {
            id = crate.homeId
            deleted = false
            archived = false
        })

        if (email != home.email)
            throw Exceptions.BadRequestEx("You are not authorized to access this house")

        val newRoom = Room().apply {
            name = crate.name
            description = crate.description
            imageUrl = crate.imageUrl
            this.home = home
        }
        return mapper.createResponse(save(newRoom))

    }

    override fun exists(spec: RoomSpec): Boolean {
        return repo.exists(spec,Room::class.java)
    }

    override fun find(spec: RoomSpec): Room {
        return repo.findOne(spec,Room::class.java).orElseThrow{ Exceptions.BadRequestEx("Room not found") }
    }

    override fun findAll(spec: RoomSpec): Page<Room> {
        return repo.findAll(spec,Room::class.java)
    }

    override fun findById(id: Long): Room {
        return find(RoomSpec(RoomParam()).apply {
            this.id = id
            deleted = false
            archived = false
        })
    }

    override fun save(main: Room): Room {
        return repo.save(main)
    }override fun delete(id: Long) {
        repo.delete(findById(id))
    }

    override fun update(main: Room, update: URoom): RRoom {
        update.name?.let { main.name = it }
        update.description?.let { main.description = it }
        update.imageUrl?.let { main.imageUrl = it }
        return mapper.createResponse(save(main))
    }
}