package com.theburgerclub.burgercredit.presentation.shared

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val symbols = DecimalFormatSymbols(Locale.US)
    val formatter = DecimalFormat("'C$'#,##0.##", symbols)
    return formatter.format(amount)
}
