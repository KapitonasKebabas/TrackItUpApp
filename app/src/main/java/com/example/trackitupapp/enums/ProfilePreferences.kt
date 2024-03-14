package com.example.trackitupapp.enums

enum class ProfilePreferences {
    Username {
        override fun toString(): String {
            return "username"
        }
    },
    Password {
        override fun toString(): String {
            return "password"
        }
    },
    Email {
        override fun toString(): String {
            return "email"
        }
    },
    UserId {
        override fun toString(): String {
            return "userId"
        }
    },
    FirstName {
        override fun toString(): String {
            return "firstName"
        }
    },
    LastName {
        override fun toString(): String {
            return "lastName"
        }
    },
    IsStaff {
        override fun toString(): String {
            return "isStaff"
        }
    }
}