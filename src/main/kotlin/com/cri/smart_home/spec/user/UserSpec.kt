package com.cri.smart_home.spec.user
import com.cri.smart_home.model.user.UserModel
import com.cri.smart_home.param.user.UserParam
import com.wanim_ms.wanimlibrary.spec.BaseModelJpaSpec
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

class UserSpec(var params: UserParam, projection: Collection<String>? = null) :
    BaseModelJpaSpec<UserModel, Long>(params, projection) {
    override var archived: Boolean? = null
    override var deleted: Boolean? = null
    override var id: Long? = null
    var email: String? = null
    var username: String? = null
    var pk: Long? = null
    constructor(projection: Collection<String>? = null) : this(UserParam(), projection)
    override fun ofSearch(): Specification<UserModel> {
        return Specification { root, query, builder ->
            var predicates: Predicate? = builder.conjunction()
            username?.let {
                predicates = builder.and(predicates, builder.equal(root.get<String>("userName"), it))
            }

            params.email?.let {
                predicates = builder.and(predicates, builder.like(builder.lower(root.get("email")), it.lowercase()))
            }
            params.userName?.let {
                predicates = builder.and(predicates, builder.like(builder.lower(root.get("userName")), it.lowercase()))
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