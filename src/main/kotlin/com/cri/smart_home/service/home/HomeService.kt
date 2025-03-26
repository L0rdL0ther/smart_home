package com.cri.smart_home.service.home

import com.cri.smart_home.dto.home.CHome
import com.cri.smart_home.dto.home.RHome
import com.cri.smart_home.dto.home.UHome
import com.cri.smart_home.model.home.Home
import com.cri.smart_home.spec.home.HomeSpec
import com.wanim_ms.wanimlibrary.service.BaseServiceHandler

interface HomeService : BaseServiceHandler<Home,CHome,UHome,RHome,HomeSpec,Long>