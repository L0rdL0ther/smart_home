package com.cri.smart_home.repo.user

import com.cri.smart_home.model.user.UserModel
import com.wanim_ms.wanimlibrary.repo.BaseJpaRepo

interface UserRepo : BaseJpaRepo<UserModel,Long>