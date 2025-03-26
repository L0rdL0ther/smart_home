package com.cri.smart_home.service.home

import com.cri.smart_home.dto.home.CHome
import com.cri.smart_home.dto.home.RHome
import com.cri.smart_home.dto.home.UHome
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.model.home.Home
import com.cri.smart_home.param.home.HomeParam
import com.cri.smart_home.repo.home.HomeRepo
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.spec.home.HomeSpec
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IHomeService(
    private val repo: HomeRepo,
    private val tokenService: TokenManager,
    private val mapper: BaseMapper,
) : HomeService {

    override fun create(crate: CHome): RHome {
        val token = tokenService.getToken() ?: throw Exceptions.BadRequestEx("Invalid Token")

        val email = tokenService.getMail(token)

        if (exists(HomeSpec(HomeParam().apply {
                homeName = crate.name
            }).apply {
                this.email = email
                deleted = false
                archived = false
            }))
            throw Exceptions.BadRequestEx("This Home name already exists")

        val newModel : Home = Home().apply {
            name = crate.name
            this.email = email.toString()
            address = crate.address
        }
        return mapper.createResponse(save(newModel))
    }

    override fun exists(spec: HomeSpec): Boolean {
        return repo.exists(spec, Home::class.java)
    }override fun delete(id: Long) {
        repo.delete(findById(id))
    }

    override fun find(spec: HomeSpec): Home {
        return repo.findOne(spec, Home::class.java).orElseThrow { throw Exceptions.BadRequestEx("Home not found") }
    }

    override fun findAll(spec: HomeSpec): Page<Home> {
        return repo.findAll(spec, Home::class.java)
    }

    override fun findById(id: Long): Home {
        return find(HomeSpec(HomeParam()).apply {
            this.id = id
            deleted = false
            archived = false
        })
    }

    override fun save(main: Home): Home {
        return repo.save(main)
    }

    override fun update(main: Home, update: UHome): RHome {
        update.name?.let { main.name = it }
        update.address?.let { main.address = it }
        return mapper.createResponse(save(main))
    }
}