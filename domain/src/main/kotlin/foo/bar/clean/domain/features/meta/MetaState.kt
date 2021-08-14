package foo.bar.clean.domain.features.meta

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.device.Meta
import foo.bar.clean.domain.features.CanError
import foo.bar.clean.domain.features.CanLoad
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Transient

/**
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
data class MetaState(
    val meta: Meta = Meta(
        appName = "",
        version = "",
        packageName = ""
    ),
    @Transient
    override val error: DomainError = DomainError.NoError,
    @Transient
    override val loading: Boolean = false,
) : State, CanLoad, CanError

