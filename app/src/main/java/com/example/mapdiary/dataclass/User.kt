package com.example.mapdiary.dataclass

data class User(
    var uid: String? = null,
    var id: String? = null,
    var userEmail: String? = null,
    var userPassword: String? = null,
    var userNickname: String? = null,
    var token: String? = null,
    var followingCount: Int? = 0,
    var followerCount: Int? = 0,
    var photoUrl: String? = null, // photoUrl 필드 추가
) : java.io.Serializable