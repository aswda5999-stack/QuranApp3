package com.example.qurannative.azkar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qurannative.data.TasbihDao
import com.example.qurannative.data.TasbihEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TasbihViewModel @Inject constructor(
    private val tasbihDao: TasbihDao
) : ViewModel() {

    val counter = tasbihDao.getCounter()
        .map { entity ->
            entity ?: TasbihEntity()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TasbihEntity()
        )

    fun increment() {
        viewModelScope.launch {
            tasbihDao.increment(System.currentTimeMillis())
        }
    }

    fun reset() {
        viewModelScope.launch {
            tasbihDao.reset(System.currentTimeMillis())
        }
    }

    fun setTarget(target: Long) {
        if (target < 1L) return

        viewModelScope.launch {
            val current = counter.value

            tasbihDao.save(
                current.copy(
                    target = target,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
