package foo.bar.clean.data.api.graphql

import foo.bar.clean.domain.features.auth.AuthModel
import foo.bar.clean.domain.features.meta.MetaModel
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * This will be specific to your own app.
 */
class InterceptorCommonGql(
    private val metaModel: MetaModel,
    private val authModel: () -> AuthModel,
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val requestBuilder = original.newBuilder()

        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("User-Agent", "user-agent-" + metaModel.state.meta.appName)
        requestBuilder.addHeader("Authorization", authModel().state.token)

        requestBuilder.method(original.method, original.body)

        return chain.proceed(requestBuilder.build())
    }
}
