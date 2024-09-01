package dev.nordix.services

import kotlinx.coroutines.flow.Flow

interface ServicesProvider {

    val services: Flow<List<NordixService>>
    val supportedServices: Flow<List<NordixService>>

}
