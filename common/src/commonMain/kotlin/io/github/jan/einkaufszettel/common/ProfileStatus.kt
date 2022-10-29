package io.github.jan.einkaufszettel.common

import io.github.jan.einkaufszettel.common.data.remote.RemoteUser

sealed interface ProfileStatus {

    object NotExisting: ProfileStatus
    object NotTried: ProfileStatus
    object Loading: ProfileStatus
    @JvmInline
    value class Available(val profile: RemoteUser): ProfileStatus

}