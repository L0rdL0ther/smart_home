package com.cri.smart_home.spec.home

import com.cri.smart_home.model.home.Home
import com.cri.smart_home.param.home.HomeParam
import com.wanim_ms.wanimlibrary.spec.BaseModelJpaSpec
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

class HomeSpec(var params: HomeParam, projection: Collection<String>? = null) : BaseModelJpaSpec<Home,Long>(params, projection) {
    override var archived: Boolean? = null
    override var deleted: Boolean? = null
    override var id: Long? = null
    var email: String? = null
    var pk: Long? = null

    constructor(projection: Collection<String>? = null) : this(HomeParam(), projection)

    override fun ofSearch(): Specification<Home> {
        return Specification { root, query, builder ->

            var predicates: Predicate? = builder.conjunction()

            email?.let {
                predicates = builder.and(predicates, builder.equal(root.get<String>("email"), it))
            }
            pk?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Long>("pk"), it))
            }

            params.homeName?.let {
                predicates = builder.and(predicates, builder.equal(root.get<String>("name"), it))
            }

            predicates
        }
    }
}