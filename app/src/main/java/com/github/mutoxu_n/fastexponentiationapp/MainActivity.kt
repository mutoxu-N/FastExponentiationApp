package com.github.mutoxu_n.fastexponentiationapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.github.mutoxu_n.fastexponentiationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.base.doOnTextChanged { text, _, _, _ ->
            val v = Integer.parseInt(text.toString())
            Log.e(this.localClassName, "base: $v")
            viewModel.setBase(v)
        }

        binding.exp.doOnTextChanged { text, _, _, _ ->
            val v = Integer.parseInt(text.toString())
            Log.e(this.localClassName, "exp: $v")
            viewModel.setExp(v)
        }

        binding.num.doOnTextChanged { text, _, _, _ ->
            val v = Integer.parseInt(text.toString())
            Log.e(this.localClassName, "num: $v")
            viewModel.setNum(v)
        }

        viewModel.result.observe(this) { it?.let {
            binding.result.text = it.toString()
        } }

        setContentView(binding.root)
    }
}