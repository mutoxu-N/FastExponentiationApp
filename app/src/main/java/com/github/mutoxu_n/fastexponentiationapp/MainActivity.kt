package com.github.mutoxu_n.fastexponentiationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
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
            try {
                val v = text.toString().toLong()
                binding.base.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setBase(v)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.base.background = drawable
                }
            }
        }

        binding.exp.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.base.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setExp(v)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.base.background = drawable
                }
            }
        }

        binding.num.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.base.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setNum(v)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.base.background = drawable
                }
            }
        }

        viewModel.result.observe(this) { it?.let {
            binding.result.text = it.toString()
        } }

        setContentView(binding.root)
    }
}