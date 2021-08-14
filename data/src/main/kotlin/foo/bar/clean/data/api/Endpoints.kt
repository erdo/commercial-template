package foo.bar.clean.data.api

import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey
import foo.bar.clean.data.api.rest.service.endpoints.EndpointUrl

data class EndpointException(val key: EndpointKey) : Exception()

class Endpoints {

    private var endpoints: Map<EndpointKey, EndpointUrl> = mapOf()

    fun setEndpoints(newEndpoints: Map<EndpointKey, EndpointUrl>) {
        endpoints = newEndpoints
    }

    fun url(key: EndpointKey): EndpointUrl {
        return endpoints[key] ?: throw EndpointException(key)
    }
}
