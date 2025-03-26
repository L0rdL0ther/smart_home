package com.cri.smart_home.service.esp32

import com.cri.smart_home.dto.esp32.CEsp
import com.cri.smart_home.dto.esp32.REsp
import com.cri.smart_home.dto.esp32.UEsp
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.model.devicetoken.ESP32Model
import com.cri.smart_home.param.ESP32Param
import com.cri.smart_home.repo.TokenRepo
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.spec.ESP32Spec
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class IESPService(
    private val repo: TokenRepo,
    private val mapper: BaseMapper,
    private val tokenManager: TokenManager
) : ESPService{
    override fun create(crate: CEsp): REsp {
        if (exists(ESP32Spec(ESP32Param().apply {
            title = crate.title
            })))
            throw Exceptions.BadRequestEx("This title already has been created")

        val token = tokenManager.getToken() ?: throw Exceptions.BadRequestEx("Token is null")
        val email = tokenManager.getMail(token)
        val newModel = ESP32Model().apply {
            this.title = crate.title
            this.email = email
            this.token = generateRandomString(32)
        }
        return mapper.createResponse(save(newModel))
    }

    override fun exists(spec: ESP32Spec): Boolean {
        return repo.exists(spec,ESP32Model::class.java)
    }
    override fun delete(id: Long) {
        repo.delete(findById(id))
    }
    override fun find(spec: ESP32Spec): ESP32Model {
        return repo.findOne(spec,ESP32Model::class.java).orElseThrow{ Exceptions.BadRequestEx("ESP specification not found") }
    }

    override fun findAll(spec: ESP32Spec): Page<ESP32Model> {
        return repo.findAll(spec,ESP32Model::class.java)
    }

    override fun findById(id: Long): ESP32Model {
        return find(ESP32Spec(ESP32Param()).apply {
            this.id = id
            deleted = false
            archived = false
        })
    }

    override fun save(main: ESP32Model): ESP32Model {
        return repo.save(main)
    }

    override fun update(main: ESP32Model, update: UEsp): REsp {
        update.title?.let {
            main.title = it
        }
        return mapper.createResponse(save(main))
    }


    fun generateRandomString(minLength: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val length = minLength + Random.nextInt(0, minLength + 1) // Rastgele uzunluk belirlenir
        return (1..length)
            .map { chars.random() }  // Her karakteri rastgele seç
            .joinToString("")
    }

}