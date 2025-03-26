package com.cri.smart_home.enums

enum class Permission(s: String) {
    CREATE_HOME("create:home"),
    CREATE_ROOM("create:room"),
    INVITE_USER("invite:user"),
    REMOVE_USER("remove:user"),
    DELETE_HOME("delete:home"),
    DELETE_ROOM("delete:room"),
    ADD_NEW_PRODUCT("add:product"),
    REMOVE_PRODUCT("remove:product"),
    UPDATE_PRODUCT("update:product"),
}