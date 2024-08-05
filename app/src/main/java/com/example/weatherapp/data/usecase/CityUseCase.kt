package com.example.weatherapp.data.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.repository.CityRepository
import com.example.weatherapp.view.city.uiViewModel.CityUIViewModel
import kotlinx.coroutines.flow.channelFlow

class CityUseCase(
    private val cityRepository: CityRepository
) {
    suspend fun getCityUIViewModel() = channelFlow<Either<ErrorData, List<CityUIViewModel?>>> {
        try {
            val weather = cityRepository.loadCity()
            weather.collect {
                it.onRight {
                    it?.let {
                        send(it.data.asSequence().map {
                            CityUIViewModel.from(it)
                        }.toList().right())
                    }
                }
                it.onLeft {
                    send(it.left())
                }
            }
        } catch (ex: Exception) {
            send(ErrorData(400, "").left())
        }
    }

    suspend fun getCityUIViewModel2() = channelFlow<Either<ErrorData, List<CityUIViewModel?>>> {
        try {
            val weather = cityRepository.loadCity2()
            weather.collect {
                it.onRight {
                    it?.let {
                        send(it.asSequence().map {
                            CityUIViewModel.from(it)
                        }.toList().right())
                    }
                }
                it.onLeft {
                    send(it.left())
                }
            }
        } catch (ex: Exception) {
            send(ErrorData(400, "").left())
        }
    }
}