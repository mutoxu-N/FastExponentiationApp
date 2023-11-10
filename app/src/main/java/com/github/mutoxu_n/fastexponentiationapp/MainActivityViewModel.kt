package com.github.mutoxu_n.fastexponentiationapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    private var _base: MutableLiveData<Int> = MutableLiveData(null)
    val base: LiveData<Int> get() = _base

    private var _exp: MutableLiveData<Int> = MutableLiveData(null)
    val exp: LiveData<Int> get() = _exp

    private var _num: MutableLiveData<Int> = MutableLiveData(null)
    val num: LiveData<Int> get() = _num

    private var _result: MutableLiveData<Int> = MutableLiveData(null)
    val result: LiveData<Int> get() = _result

    private fun calc() {
        var b = base.value
        var e = exp.value
        val n = num.value
        if(b == null || e == null || n == null) return

        // 高速指数演算
        var r = 1
        while(e!! > 0) {
            if(e and 1 == 1) {
                r *= b
                r %= n
            }
            e = e shr 1
            b *= b
            b %= n
        }

        Log.e(this.javaClass.name, "$r")

        // output
        _result.value = r
    }

    fun setBase(base: Int) {
        _base.value = base
        calc()
    }

    fun setExp(exp: Int) {
        _exp.value = exp
        calc()
    }

    fun setNum(num: Int) {
        _num.value = num
        calc()
    }

}