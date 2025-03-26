package com.cri.smart_home.spec

import com.cri.smart_home.model.devicetoken.ESP32Model
import com.cri.smart_home.param.ESP32Param
import com.wanim_ms.wanimlibrary.spec.BaseModelJpaSpec
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

class ESP32Spec(var params: ESP32Param, projection: Collection<String>? = null) :
    BaseModelJpaSpec<ESP32Model, Long>(params, projection) {
    override var archived: Boolean? = null
    override var deleted: Boolean? = null
    override var id: Long? = null
    var email: String? = null
    var pk: Long? = null
    var token: String? = null

    constructor(projection: Collection<String>? = null) : this(ESP32Param(), projection)

    override fun ofSearch(): Specification<ESP32Model> {
        return Specification { root, query, builder ->
            var predicates: Predicate? = builder.conjunction()

            token?.let {
                predicates = builder.and(predicates, builder.equal(root.get<String>("token"), it))
            }

            params.title?.let {
                predicates = builder.and(predicates, builder.equal(root.get<String>("title"), it))
            }

            email?.let {
                predicates = builder.and(predicates, builder.equal(root.get<String>("email"), it))
            }
            pk?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Long>("pk"), it))
            }
            predicates
        }
    }
}