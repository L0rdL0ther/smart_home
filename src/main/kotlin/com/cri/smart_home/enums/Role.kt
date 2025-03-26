package com.cri.smart_home.enums

enum class Role(val permission: Set<Permission>) {
    HOME_ADMIN(setOf(
        Permission.CREATE_HOME,
        Permission.CREATE_ROOM,
        Permission.INVITE_USER,
        Permission.REMOVE_USER,
        Permission.DELETE_HOME,
        Permission.DELETE_ROOM,
        Permission.ADD_NEW_PRODUCT,
        Permission.REMOVE_PRODUCT,
        Permission.UPDATE_PRODUCT
    )),

}