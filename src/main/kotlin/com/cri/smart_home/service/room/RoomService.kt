package com.cri.smart_home.service.room

import com.cri.smart_home.dto.room.CRoom
import com.cri.smart_home.dto.room.RRoom
import com.cri.smart_home.dto.room.URoom
import com.cri.smart_home.model.home.Room
import com.cri.smart_home.spec.room.RoomSpec
import com.wanim_ms.wanimlibrary.service.BaseServiceHandler

interface RoomService : BaseServiceHandler<Room,CRoom,URoom,RRoom,RoomSpec,Long>