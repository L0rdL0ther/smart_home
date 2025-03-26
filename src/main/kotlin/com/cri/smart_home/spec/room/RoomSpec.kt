package com.cri.smart_home.spec.room

import com.cri.smart_home.model.home.Room
import com.cri.smart_home.param.room.RoomParam
import com.wanim_ms.wanimlibrary.spec.BaseModelJpaSpec
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification


class RoomSpec(var params: RoomParam, projection: Collection<String>? = null) : BaseModelJpaSpec<Room, Long>(params, projection) {
    override var archived: Boolean? = null
    override var deleted: Boolean? = null
    override var id: Long? = null
    var homeId: Long? = null
    var email: String? = null
    constructor(projection: Collection<String>? = null) : this(RoomParam(), projection)


    override fun ofSearch(): Specification<Room> {
        return Specification { root, query, builder ->
            var predicates: Predicate? = builder.conjunction()

            params.name?.let {
                predicates = builder.and(predicates, builder.equal(root.get<String>("name"), it))            }
            homeId?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Any>("home").get<Long>("id"), it))
            }

            predicates
        }
    }
}