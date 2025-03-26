package com.cri.smart_home.spec.device

import com.cri.smart_home.model.home.Device
import com.cri.smart_home.param.device.DeviceParam
import com.wanim_ms.wanimlibrary.spec.BaseModelJpaSpec
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

class DeviceSpec(var params: DeviceParam, projection: Collection<String>? = null) :
    BaseModelJpaSpec<Device, Long>(params, projection) {
    override var archived: Boolean? = null
    override var deleted: Boolean? = null
    override var id: Long? = null
    var roomId: Long? = null

    constructor(projection: Collection<String>? = null) : this(DeviceParam(), projection)

    override fun ofSearch(): Specification<Device> {
        return Specification { root, query, builder ->

            var predicates: Predicate? = builder.conjunction()

            roomId?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Any>("room").get<Long>("id"), it))
            }

            predicates
        }
    }
}