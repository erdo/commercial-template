package foo.bar.clean.domain.features.featureflags

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.FeatureFlag
import foo.bar.clean.domain.features.CanError
import foo.bar.clean.domain.features.CanLoad
import foo.bar.clean.domain.features.State
import foo.bar.clean.domain.utils.MapFeatureFlagBooleanDeserializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class FeatureFlagsState(
    @Serializable(with = MapFeatureFlagBooleanDeserializer::class)
    val features: Map<FeatureFlag, Boolean> = emptyMap(),
    @Transient
    override val error: DomainError = DomainError.NoError,
    @Transient
    override val loading: Boolean = false,
) : State, CanLoad, CanError {
    fun isEnabled(flag: FeatureFlag) = features[flag]
}