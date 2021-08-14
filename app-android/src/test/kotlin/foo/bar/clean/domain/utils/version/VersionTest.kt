package foo.bar.clean.domain.utils.version

import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.delegate.TestDelegateDefault
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class VersionTest {

    @Before
    fun setup() {
        Fore.setDelegate(TestDelegateDefault())
    }

    @Test
    fun `invalid version _ throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version(".")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }


    @Test
    fun `invalid version _1 throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version(".1")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `invalid version 1_a throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version("1.a")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `invalid version 1_2_3-beta throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version("1.2.3-beta")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `invalid version 1_ throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version("1.")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `invalid version 1__0 throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version("1..0")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `invalid version 1,1,1 throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version("1,1,1")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `invalid version 1_-1_1 throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version("1.-1.1")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `invalid version 1-1-Int_MAX_VALUE throws exception`() {

        // arrange
        var exception: Exception? = null

        // act
        try {
            Version("1.1.${Int.MAX_VALUE}")
        } catch (e: Exception) {
            exception = e
        }

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `1 is bigger than 0`() {
        Assert.assertTrue(Version("1") > Version("0"))
        Assert.assertFalse(Version("0") > Version("1"))
    }

    @Test
    fun `200 is bigger than 3`() {
        Assert.assertTrue(Version("200") > Version("3"))
        Assert.assertFalse(Version("3") > Version("200"))
    }

    @Test
    fun `1_1 is bigger than 1`() {
        Assert.assertTrue(Version("1.1") > Version("1"))
        Assert.assertFalse(Version("1") > Version("1.1"))
    }

    @Test
    fun `1_1 is bigger than 1_0`() {
        Assert.assertTrue(Version("1.1") > Version("1.0"))
        Assert.assertFalse(Version("1.0") > Version("1.1"))
    }

    @Test
    fun `1_7 is bigger than 1_2`() {
        Assert.assertTrue(Version("1.7") > Version("1.2"))
        Assert.assertFalse(Version("1.2") > Version("1.7"))
    }

    @Test
    fun `1_17 is bigger than 1_2`() {
        Assert.assertTrue(Version("1.17") > Version("1.2"))
        Assert.assertFalse(Version("1.2") > Version("1.17"))
    }

    @Test
    fun `2_0_0_0_1 is bigger than 2`() {
        Assert.assertTrue(Version("2.0.0.0.1") > Version("2"))
        Assert.assertFalse(Version("2") > Version("2.0.0.0.1"))
    }

    @Test
    fun `0 is smaller than 1`() {
        Assert.assertTrue(Version("0") < Version("1"))
        Assert.assertFalse(Version("1") < Version("0"))
    }

    @Test
    fun `3 is smaller than 200`() {
        Assert.assertTrue(Version("3") < Version("200"))
        Assert.assertFalse(Version("200") < Version("3"))
    }

    @Test
    fun `1 is smaller than 1_1`() {
        Assert.assertTrue(Version("1") < Version("1.1"))
        Assert.assertFalse(Version("1.1") < Version("1"))
    }

    @Test
    fun `1_0 is smaller than 1_1`() {
        Assert.assertTrue(Version("1.0") < Version("1.1"))
        Assert.assertFalse(Version("1.1") < Version("1.0"))
    }

    @Test
    fun `1_2 is smaller than 1_7`() {
        Assert.assertTrue(Version("1.2") < Version("1.7"))
        Assert.assertFalse(Version("1.7") < Version("1.2"))
    }

    @Test
    fun `1_2 is smaller than 1_17`() {
        Assert.assertTrue(Version("1.2") < Version("1.17"))
        Assert.assertFalse(Version("1.17") < Version("1.2"))
    }

    @Test
    fun `2 is smaller than 2_0_0_0_1`() {
        Assert.assertTrue(Version("2") < Version("2.0.0.0.1"))
        Assert.assertFalse(Version("2.0.0.0.1") < Version("2"))
    }

    @Test
    fun `2 is equal to 2`() {
        Assert.assertTrue(Version("2").compareTo(Version("2")) == 0)
    }

    @Test
    fun `2_0_0_0_0 is equal to 2`() {
      //  a.compareTo(b) == 0
        Assert.assertTrue(Version("2.0.0.0.0").compareTo(Version("2")) == 0)
        Assert.assertTrue(Version("2").compareTo(Version("2.0.0.0.0")) == 0)
    }
}
