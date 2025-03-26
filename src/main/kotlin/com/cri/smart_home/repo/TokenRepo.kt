package com.cri.smart_home.repo

import com.cri.smart_home.model.devicetoken.ESP32Model
import com.wanim_ms.wanimlibrary.repo.BaseJpaRepo

interface TokenRepo : BaseJpaRepo<ESP32Model,Long>