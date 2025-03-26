package com.cri.smart_home.service.user

import com.cri.smart_home.dto.user.CUser
import com.cri.smart_home.dto.user.LUser
import com.cri.smart_home.dto.user.RUser
import com.cri.smart_home.dto.user.UUser
import com.cri.smart_home.model.user.UserModel
import com.cri.smart_home.spec.user.UserSpec
import com.wanim_ms.wanimlibrary.service.BaseServiceHandler

interface UserService : BaseServiceHandler<UserModel,CUser,UUser,RUser,UserSpec,Long> {
    fun findByEmail(email: String): UserModel
    fun isTokenValid(token: String): Boolean
    fun login(lUser: LUser): RUser
}