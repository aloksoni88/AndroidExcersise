package com.alok.androidexcersise

import org.mockito.ArgumentCaptor

/**
 * Created by Alok Soni on 13/11/20.
 */
fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()