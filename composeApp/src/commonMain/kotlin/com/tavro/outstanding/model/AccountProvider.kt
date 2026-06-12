package com.tavro.outstanding.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Used to distinguish "not yet loaded" from null (logged out) and a real Account.
private val INITIAL_ACCOUNT = Account.EMPTY

interface AccountProvider {
    val accountFlow: Flow<Account?>

    /**
     * Emits `null` while the account is loading, `false` when logged out, `true` when an account
     * exists. Distinct until changed so observers do not react to repeated identical states.
     */
    val hasAccountFlow: Flow<Boolean?>

    class Impl(mainScope: CoroutineScope, accountRepository: AccountRepository) : AccountProvider {
        private val _accountFlow: Flow<Account?> = accountRepository.getAccountFlow()
            .stateIn(mainScope, SharingStarted.Lazily, initialValue = INITIAL_ACCOUNT)

        override val accountFlow = _accountFlow
            .filter { it != INITIAL_ACCOUNT }
        override val hasAccountFlow = _accountFlow
            .map { if (it == INITIAL_ACCOUNT) null else it != null }
            .distinctUntilChanged()
    }
}
