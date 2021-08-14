package foo.bar.clean.domain.features.navigation

import co.early.fore.core.observer.Observer
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.delegate.TestDelegateDefault
import co.early.persista.PerSista
import foo.bar.clean.domain.services.api.Fruit
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.rules.TemporaryFolder

class NavModelTest {

    private lateinit var perSista: PerSista

    @MockK
    private lateinit var mockObserver: Observer

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Fore.setDelegate(TestDelegateDefault())

        val dataFolder = TemporaryFolder()
        dataFolder.create()
        perSista = PerSista(dataFolder.newFolder())
    }


    @Test
    fun `when created, back stack has initial location only`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(1, navigationModel.state.backStack.size)
        assertEquals(HOME_LOCATION, navigationModel.state.currentPage())
    }

    @Test
    fun `when navigating forward, back stack is added to`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(4, navigationModel.state.backStack.size)
        assertEquals(Location.TicketLocation(), navigationModel.state.currentPage())
    }

    @Test
    fun `when navigating forward with addToHistory set to false, location is not added to history`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation, false)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation(), false)
        navigationModel.navigateTo(Location.CounterLocation, false)
        navigationModel.navigateTo(Location.WeatherLocation())
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(3, navigationModel.state.backStack.size)
        assertEquals(Location.WeatherLocation(), navigationModel.state.currentPage())
        assertEquals(Location.NetworkLocation, navigationModel.state.backStack[1])
    }

    @Test
    fun `given previous location was added with addToHistory = false, when popping backstack, addToHistory is set back to true`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.TicketLocation(), false)
        navigationModel.popBackStack()
        navigationModel.navigateTo(Location.WeatherLocation())
        navigationModel.navigateTo(Location.NetworkLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(4, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.backStack[1])
    }

    @Test
    fun `given previous location was added with addToHistory = false, when navigating back, addToHistory is set back to true`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.TicketLocation(), false)
        navigationModel.navigateBackTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.WeatherLocation())
        navigationModel.navigateTo(Location.NetworkLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(4, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.backStack[1])
    }

    @Test
    fun `given previous location was added with addToHistory = false, when navigating back to the same location, addToHistory is set back to true`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.TicketLocation(), false)
        navigationModel.navigateBackTo(Location.TicketLocation(99))
        navigationModel.navigateTo(Location.WeatherLocation())
        navigationModel.navigateTo(Location.NetworkLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(5, navigationModel.state.backStack.size)
        assertEquals(Location.TicketLocation(99), navigationModel.state.backStack[2])
    }

    @Test
    fun `given previous location was added with addToHistory = false, when rewriting backstack, addToHistory is set back to true`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.TicketLocation(), false)
        navigationModel.updateBackStack(
            listOf(
                Location.WeatherLocation(),
                Location.NetworkLocation,
            )
        )
        navigationModel.navigateTo(Location.CounterLocation)
        navigationModel.navigateTo(Location.FavouritesLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(4, navigationModel.state.backStack.size)
        assertEquals(Location.NetworkLocation, navigationModel.state.backStack[1])
    }

        @Test
    fun `when popBackStack is called, back stack is cleared`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.popBackStack()
        navigationModel.popBackStack()
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(2, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.currentPage())
    }

    @Test
    fun `when popBackStack is called with times=3, back stack is cleared three times`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.CounterLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.popBackStack(times = 3)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(2, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.currentPage())
    }

    @Test
    fun `given the backStack is only 3 items long, when popBackStack is called with times=5, back stack is cleared to home item`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.popBackStack(times = 5)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(1, navigationModel.state.backStack.size)
        assertEquals(HOME_LOCATION, navigationModel.state.currentPage())
    }

    @Test
    fun `given location is visited twice, both entries are added to backstack`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.navigateTo(Location.FavouritesLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(5, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.currentPage())
        assertEquals(Location.FavouritesLocation, navigationModel.state.backStack[1])
    }

    @Test
    fun `given location has been previously visited, when navigating back to it, back stack is cleared forward from that point`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.navigateTo(Location.WeatherLocation())
        navigationModel.navigateBackTo(Location.FavouritesLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(2, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.currentPage())
    }

    @Test
    fun `given location has been visited twice before, when navigating back to it, the most recent entry becomes the current page`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.WeatherLocation())
        navigationModel.navigateBackTo(Location.FavouritesLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(4, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.currentPage())
    }

    @Test
    fun `given location is the current page, when navigating back to it, page is replaced, but history status remains the same`() {

        // arrange
        val navigationModel = NavigationModel(perSista)
        val aFruit = Fruit.FruitSome("Mango")
        val noFruit = Fruit.FruitNone

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.navigateTo(Location.FruitLocation(noFruit))
        navigationModel.navigateBackTo(Location.FruitLocation(aFruit), addToHistory = false)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(5, navigationModel.state.backStack.size)
        assertEquals(Location.FruitLocation(aFruit), navigationModel.state.currentPage())
        assertNotEquals(Location.FruitLocation(noFruit), navigationModel.state.currentPage())
        assertEquals(false, navigationModel.state.currentLocationWillBeAddedToHistoryOnNextNavigation)
    }

    @Test
    fun `given location has NOT been previously visited, when navigating BACK to it, navigate to the location as normal`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.navigateTo(Location.WeatherLocation())
        navigationModel.navigateBackTo(Location.FavouritesLocation)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(5, navigationModel.state.backStack.size)
        assertEquals(Location.FavouritesLocation, navigationModel.state.currentPage())
    }

    @Test
    fun `given location is the current page, when navigating back to it, new location is swapped for current location`() {

        // arrange
        val navigationModel = NavigationModel(perSista)
        val aFruit = Fruit.FruitSome("Mango")
        val noFruit = Fruit.FruitNone

        // act
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.navigateTo(Location.FruitLocation(noFruit))
        navigationModel.navigateBackTo(Location.FruitLocation(aFruit))
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(4, navigationModel.state.backStack.size)
        assertEquals(Location.FruitLocation(aFruit), navigationModel.state.currentPage())
        assertNotEquals(Location.FruitLocation(noFruit), navigationModel.state.currentPage())
    }

    @Test
    fun `when navigateBackTo is called with the same location as homepage, all other locations are cleared and the homepage is swapped`() {

        // arrange
        val origHomeLocationRef = Location.FruitLocation(Fruit.FruitNone)
        val newHomeLocationRef = Location.FruitLocation(Fruit.FruitSome("Mango"))
        val navigationModel = NavigationModel(
            perSista = perSista,
            homeLocation = origHomeLocationRef
        )

        // act
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.navigateBackTo(newHomeLocationRef)
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(1, navigationModel.state.backStack.size)
        assertNotEquals(origHomeLocationRef, navigationModel.state.currentPage())
        assertEquals(newHomeLocationRef, navigationModel.state.currentPage())
    }

    @Test
    fun `when reWriteBackStack is called, with a non empty back stack, back stack is replaced`() {

        // arrange
        val navigationModel = NavigationModel(
            perSista = perSista,
        )

        // act
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.updateBackStack(
            listOf(
                Location.WeatherLocation(),
                Location.CounterLocation,
            )
        )
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, navigationModel.state.loading)
        assertEquals(2, navigationModel.state.backStack.size)
        assertEquals(Location.CounterLocation, navigationModel.state.currentPage())
    }

    @Test
    fun `when reWriteBackStack is called, with an empty back stack, exception is thrown`() {

        // arrange
        val navigationModel = NavigationModel(
            perSista = perSista,
        )
        var exception: Exception? = null

        // act
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.navigateTo(Location.TicketLocation())
        try {
            navigationModel.updateBackStack(emptyList())
        } catch (e: Exception) {
            exception = e
        }
        Fore.i(navigationModel.toString())

        // assert
        Assert.assertEquals(IllegalArgumentException::class.java, exception?.javaClass)
    }

    @Test
    fun `when popBackStack is called with setData, final location receives data`() {

        // arrange
        val navigationModel = NavigationModel(perSista)
        val colourToPassBack = 200

        // act
        navigationModel.navigateTo(Location.TicketLocation())
        navigationModel.navigateTo(Location.NetworkLocation)
        navigationModel.popBackStack(
            setData = {
                when (it){
                    is Location.TicketLocation -> Location.TicketLocation(
                        maxWaitTimeMin = colourToPassBack
                    )
                    is Location.WeatherLocation -> Location.WeatherLocation(
                        colourInt = colourToPassBack
                    )
                    else -> it
                }
            }
        )
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(Location.TicketLocation(maxWaitTimeMin = colourToPassBack), navigationModel.state.currentPage())
    }

    @Test
    fun `given we are already on the home location, when popBackStack is called, false is returned`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        val result = navigationModel.popBackStack()
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(false, result)
    }

    @Test
    fun `given we are not on the home location, when popBackStack is called, true is returned`() {

        // arrange
        val navigationModel = NavigationModel(perSista)

        // act
        navigationModel.navigateTo(Location.TicketLocation())
        val result = navigationModel.popBackStack()
        Fore.i(navigationModel.toString())

        // assert
        assertEquals(true, result)
    }

    /**
     * NB all we are checking here is that observers are called AT LEAST once
     *
     * We don't really want tie our tests (OR any observers in production code)
     * to an expected number of times they are notified. (This would be
     * testing an implementation detail and make the tests unnecessarily brittle)
     *
     * The contract says nothing about how many times the observers will get called,
     * only that they will be called if something changes ("something" is not defined
     * and can change between implementations).
     *
     * (This is similar to how Composables are written - they should not be written with the
     * expectation that they will be composed a certain number of times)
     */
    @Test
    fun `when navigating forward, observers are notified`() {

        // arrange
        val navigationModel = NavigationModel(
            perSista = perSista,
        )
        navigationModel.addObserver(mockObserver)

        // act
        navigationModel.navigateTo(Location.NetworkLocation)
        Fore.i(navigationModel.toString())

        // assert
        verify(atLeast = 1) {
            mockObserver.somethingChanged()
        }
    }

    @Test
    fun `when navigating back to a previous location, observers are notified`() {

        // arrange
        val navigationModel = NavigationModel(
            perSista = perSista,
        )
        navigationModel.addObserver(mockObserver)

        // act
        navigationModel.navigateBackTo(HOME_LOCATION)
        Fore.i(navigationModel.toString())

        // assert
        verify(atLeast = 1) {
            mockObserver.somethingChanged()
        }
    }

    @Test
    fun `when popping back stack, observers are notified`() {

        // arrange
        val navigationModel = NavigationModel(
            perSista = perSista,
        )
        navigationModel.addObserver(mockObserver)

        // act
        navigationModel.navigateTo(Location.FavouritesLocation)
        navigationModel.popBackStack()
        Fore.i(navigationModel.toString())

        // assert
        verify(atLeast = 1) {
            mockObserver.somethingChanged()
        }
    }

    @Test
    fun `when rewriting back stack, observers are notified`() {

        // arrange
        val navigationModel = NavigationModel(
            perSista = perSista,
        )
        navigationModel.addObserver(mockObserver)

        // act
        navigationModel.updateBackStack(listOf(Location.NetworkLocation))
        Fore.i(navigationModel.toString())

        // assert
        verify(atLeast = 1) {
            mockObserver.somethingChanged()
        }
    }

    @Test
    fun `export navigation state to deeplink`() {
        assertTrue(false)
    }

    @Test
    fun `import navigation state from deeplink`() {
        assertTrue(false)
    }
}
