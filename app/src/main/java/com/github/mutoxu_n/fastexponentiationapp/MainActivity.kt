package com.github.mutoxu_n.fastexponentiationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            viewModel.setBase(v)
        }

        binding.exp.doOnTextChanged { text, _, _, _ ->
            val v = Integer.parseInt(text.toString())
            viewModel.setExp(v)
        }

        binding.num.doOnTextChanged { text, _, _, _ ->
            val v = Integer.parseInt(text.toString())
            viewModel.setNum(v)
        }

        viewModel.result.observe(this) { it?.let {
            binding.result.text = it.toString()
        } }

        setContentView(binding.root)
    }
}