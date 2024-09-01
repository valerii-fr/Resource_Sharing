package dev.nordix.homescreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nordix.service_manager.holder.ServicesStateProvider
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val servicesStateProvider: ServicesStateProvider,
) : ViewModel() {

    val serviceStates = servicesStateProvider.holder.asStateFlow()

}