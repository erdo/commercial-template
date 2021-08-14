package foo.bar.clean.di.domain

import foo.bar.clean.App
import foo.bar.clean.R
import foo.bar.clean.data.api.graphql.service.auth.AuthServiceImp
import foo.bar.clean.data.api.graphql.service.launch.LaunchServiceImp
import foo.bar.clean.data.api.rest.service.config.ConfigServiceImp
import foo.bar.clean.data.api.rest.service.endpoints.EndpointsServiceImp
import foo.bar.clean.data.api.rest.service.featureflags.FeatureFlagsServiceImp
import foo.bar.clean.data.api.rest.service.fruit.FruitServiceImp
import foo.bar.clean.data.api.rest.service.phonehome.PhoneHomeServiceImp
import foo.bar.clean.data.api.rest.service.tickets.TicketServiceImp
import foo.bar.clean.data.api.rest.service.weather.WeatherServiceImp
import foo.bar.clean.data.db.service.todo.TodoItemServiceImp
import foo.bar.clean.data.device.meta.MetaServiceImp
import foo.bar.clean.domain.features.auth.AuthModel
import foo.bar.clean.domain.services.api.AuthService
import foo.bar.clean.domain.services.api.ConfigService
import foo.bar.clean.domain.services.api.EndpointsService
import foo.bar.clean.domain.services.api.FeatureFlagsService
import foo.bar.clean.domain.services.api.FruitService
import foo.bar.clean.domain.services.api.LaunchService
import foo.bar.clean.domain.services.api.PhoneHomeService
import foo.bar.clean.domain.services.api.TicketService
import foo.bar.clean.domain.services.api.WeatherService
import foo.bar.clean.domain.services.db.TodoItemService
import foo.bar.clean.domain.services.device.MetaService
import org.koin.dsl.module

/**
 * Domain Services
 */
val domainServices = module {

    single<MetaService> {
        MetaServiceImp(
            application = get() as App,
            appName = (get() as App).resources.getString(R.string.app_name),
        )
    }

    single<ConfigService> {
        ConfigServiceImp(
            api = get(),
            wrapper = get(),
        )
    }

    single<TodoItemService> {
        TodoItemServiceImp(
            todoItemDb = get(),
            systemTimeWrapper = get(),
        )
    }

    single<EndpointsService> {
        EndpointsServiceImp(
            api = get(),
            wrapper = get(),
            endpoints = get(),
        )
    }

    single<FeatureFlagsService> {
        FeatureFlagsServiceImp(
            api = get(),
            wrapper = get(),
        )
    }

    single<FruitService> {
        FruitServiceImp(
            api = get(),
            wrapper = get(),
        )
    }

    single<PhoneHomeService> {
        PhoneHomeServiceImp(
            api = get(),
            wrapper = get(),
        )
    }

    single<TicketService> {
        TicketServiceImp(
            api = get(),
            wrapper = get(),
        )
    }

    single<WeatherService> {
        WeatherServiceImp(
            api = get(),
            wrapper = get(),
        )
    }

    single<AuthService> {
        AuthServiceImp(
            api = get(),
            wrapper = get(),
        )
    }

    single<LaunchService> {
        LaunchServiceImp(
            api = get(),
            wrapper = get(),
            authModel = { get() as AuthModel }
        )
    }
}
