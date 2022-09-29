package com.mintrocket.navigation.screens

import androidx.fragment.app.Fragment

interface DialogFragmentScreen {
    val tag: String

    fun getFragment(): Fragment
}