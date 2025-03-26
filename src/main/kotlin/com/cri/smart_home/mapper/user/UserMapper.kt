package com.cri.smart_home.mapper.user

import com.cri.smart_home.dto.user.RUser
import com.cri.smart_home.model.user.UserModel
import org.springframework.stereotype.Service

@Service
class UserMapper {

    fun crateResponse(userModel: UserModel): RUser {
        return RUser().apply {
            email = userModel.email
            username = userModel.userName
        }
    }


}