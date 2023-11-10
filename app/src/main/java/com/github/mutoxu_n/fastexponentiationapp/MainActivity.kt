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

        // upper side
        binding.baseUpper.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.baseUpper.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setBase(v, true)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.baseUpper.background = drawable
                }
            }
        }

        binding.expUpper.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.expUpper.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setExp(v, true)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.expUpper.background = drawable
                }
            }
        }

        binding.numUpper.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.numUpper.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setNum(v, true)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.numUpper.background = drawable
                }
            }
        }

        viewModel.resultUpper.observe(this) { it?.let {
            binding.resultUpper.text = it.toString()
        } }

        // bottom side
        binding.baseBottom.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.baseBottom.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setBase(v, false)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.baseBottom.background = drawable
                }
            }
        }

        binding.expBottom.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.expBottom.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setExp(v, false)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.expBottom.background = drawable
                }
            }
        }

        binding.numBottom.doOnTextChanged { text, _, _, _ ->
            try {
                val v = text.toString().toLong()
                binding.numBottom.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                viewModel.setNum(v, false)

            } catch (e: NumberFormatException) {
                if(text.toString().isNotEmpty()){
                    val drawable = AppCompatResources.getDrawable(
                        this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
                    drawable.setTint(getColor(R.color.red))
                    binding.numBottom.background = drawable
                }
            }
        }

        viewModel.resultBottom.observe(this) { it?.let {
            binding.resultBottom.text = it.toString()
        } }

        // sync button
        binding.sync.setOnClickListener {
            binding.baseBottom.setText(binding.resultUpper.text)
            binding.numBottom.text = binding.numUpper.text
        }

        setContentView(binding.root)
    }
}