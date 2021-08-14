package foo.bar.clean.domain.utils.version

/**
 * Consider using the DefaultArtifactVersion implementation from org.apache.maven:maven-artifact
 *
 * Version can have an arbitrary number of tokens, but it needs to be in the form x.x.x etc
 * where x is a positive integer _less_ than Int.MAX_VALUE
 *
 * Valid examples:
 *
 * 1
 * 1.0
 * 0.1
 * 1.0.0.0.0
 * 0.0
 * 999999.1.1.1
 * {Int.MAX_VALUE - 1}.1.1.1
 *
 * Invalid examples:
 *
 * 1.
 * 1..0
 * 1.a
 * 1.1-beta
 * 1,1,1
 * 1.-1.1
 * 1-1-1
 * 1.0.{Int.MAX_VALUE}
 *
 */
class Version(
    v: String
) : Comparable<Version> {

    val v = v
    private val tokens = tokenizeAndVerify(v)

    override fun compareTo(other: Version): Int {

        val listSwap = tokens.size < other.tokens.size
        val mostTokens = if (listSwap) { other } else this
        val leastTokens = if (listSwap) { this } else other

        mostTokens.tokens.forEachIndexed { index, i ->
            when {
                // this <--> other (without list swap)
                i < leastTokens.tokens.getOrElse(index) { 0 } -> return otherIsBigger(listSwap)
                i > leastTokens.tokens.getOrElse(index) { 0 } -> return otherIsSmaller(listSwap)
            }
        }
        return 0
    }

    private fun otherIsBigger(listSwap: Boolean) = if (listSwap) { 1 } else { -1 }
    private fun otherIsSmaller(listSwap: Boolean) = if (listSwap) { -1 } else { 1 }

    private fun tokenizeAndVerify(version: String): List<Int> {
        val tokens = version.split(".")
        val ints = tokens.map {
            val versionComponent = it.toIntOrNull()
            require(versionComponent != null && versionComponent >= 0 && versionComponent != Int.MAX_VALUE) {
                "Version needs to be in the form x.x.x etc where x is a positive integer _less_ than Int.MAX_VALUE. $it is invalid"
            }
            versionComponent
        }
        return ints
    }
}
