package foo.bar.clean.data.api.rest.service.phonehome

import foo.bar.clean.domain.services.api.PhoneHomeResult

/**
 * For mapping our data pojos / DTOs to domain classes
 */

fun PhoneHomeResultPojo.toDomain(): PhoneHomeResult {
    return PhoneHomeResult(
        endpointsUrl = this.endpoints,
        minSupportedVersion = this.minSupportedVersion,
        minNoNagVersion = this.minNoNagVersion,
    )
}
