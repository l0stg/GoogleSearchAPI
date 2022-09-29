package com.mintrocket.datacore.errorhandling

import com.mintrocket.datacore.utils.TextContainer
import java.io.IOException

open class ExceptionWithMessage(
    val messageContainer: TextContainer,
    val titleContainer: TextContainer
) : IOException()