package com.theburgerclub.burgercredit.presentation.debt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.domain.model.Debt
import com.theburgerclub.burgercredit.domain.usecase.DebtUseCase
import com.theburgerclub.burgercredit.presentation.debt.model.DebtUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtViewModel @Inject constructor(
    private val debtUseCase: DebtUseCase
) : ViewModel() {

    private val _debtUiState = MutableStateFlow(DebtUiState())
    val debtUiState: StateFlow<DebtUiState> = _debtUiState.asStateFlow()

    init {
        loadDebts()
    }

    fun loadDebts() {
        viewModelScope.launch {
            debtUseCase.getAllDebts().collect { list ->
                _debtUiState.update { it.copy(debts = list) }
            }
        }
    }

    fun addDebt(debt: Debt) {
        viewModelScope.launch {
            debtUseCase.addDebt(debt)
            loadDebts()
        }
    }

    fun updateDebt(debt: Debt) {
        viewModelScope.launch {
            debtUseCase.updateDebt(debt)
            loadDebts()
        }
    }

    fun deleteDebt(debt: Debt) {
        viewModelScope.launch {
            debtUseCase.deleteDebt(debt)
            loadDebts()
        }
    }

    fun selectDebt(debt: Debt?) {
        _debtUiState.update { it.copy(selectedDebt = debt) }
    }

    fun searchDebtsByDescription(desc: String) {
        viewModelScope.launch {
            debtUseCase.searchDebtsByDescription(desc).collect { list ->
                _debtUiState.update { it.copy(debts = list) }
            }
        }
    }
} 