package com.example.remotelist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.remotelist.model.Model
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(model: Model): ViewModel()