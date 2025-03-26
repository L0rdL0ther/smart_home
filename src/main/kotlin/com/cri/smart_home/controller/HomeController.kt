package com.cri.smart_home.controller


import com.cri.smart_home.dto.home.CHome
import com.cri.smart_home.dto.home.RHome
import com.cri.smart_home.dto.home.UHome
import com.cri.smart_home.error.Exceptions
import com.cri.smart_home.mapper.home.BaseMapper
import com.cri.smart_home.param.home.HomeParam
import com.cri.smart_home.service.TokenManager
import com.cri.smart_home.service.home.IHomeService
import com.cri.smart_home.spec.home.HomeSpec
import com.wanim_ms.wanimlibrary.model.SortOrder
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/homes")
class HomeController(
    private val service: IHomeService,
    private val mapper: BaseMapper,
    private val tokenManager: TokenManager
) {
    @PostMapping
    fun create(@RequestBody home: CHome): ResponseEntity<RHome> {
        return ResponseEntity.ok(service.create(home))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<RHome> {
        val home = service.findById(id)
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token")
        if (home.email != email) {
            throw Exceptions.BadRequestEx("Invalid email")
        }
        return ResponseEntity.ok(mapper.createResponse(home))
    }

    @PostMapping("/all")
    fun getAll(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?,
        @RequestParam(required = false) sortOrder: SortOrder?,
        @RequestParam(required = false) sortBy: String?,
        @RequestBody param: HomeParam
    ): ResponseEntity<Page<RHome>> {

        val updatedParam = param.apply {
            this.page = page ?: this.page
            this.size = pageSize ?: this.size
            this.sortOrder = sortOrder ?: this.sortOrder
            this.sortBy = sortBy ?: this.sortBy
        }
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token")
        return ResponseEntity.ok(service.findAll(HomeSpec(updatedParam).apply { this.email = email }).map { mapper.createResponse(it) })
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody update: UHome): ResponseEntity<RHome> {
        val home = service.findById(id)

        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token")

        if (home.email != email) {
            throw Exceptions.BadRequestEx("Invalid email")
        }

        return ResponseEntity.ok(service.update(home, update))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        val data = service.findById(id)
        val email = tokenManager.getToken()?.let { tokenManager.getMail(it) }
            ?: throw Exceptions.BadRequestEx("Invalid token for room")
        if (data.email != email)
            throw Exceptions.BadRequestEx("Invalid email")

        service.delete(id)
    }
}