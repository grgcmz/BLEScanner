package com.grgcmz.blescanner.model

import com.grgcmz.blescanner.R

enum class ScanFilterOption{
    NAME
}

data class ScanFilter(
    val scanFilterOption: ScanFilterOption,
    val icon: Int,
    val value: String
)

val SCAN_FILTERS = listOf(
    ScanFilter(ScanFilterOption.NAME, R.drawable.baseline_manage_search_24, "Name")
)