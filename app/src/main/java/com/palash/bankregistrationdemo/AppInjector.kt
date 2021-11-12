package com.palash.bankregistrationdemo

import com.palash.bankregistrationdemo.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val viewModelModule = module {

    viewModel {
        MainViewModel(get())
    }


}

