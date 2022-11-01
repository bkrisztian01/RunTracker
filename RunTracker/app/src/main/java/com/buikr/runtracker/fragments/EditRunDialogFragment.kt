package com.buikr.runtracker.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.buikr.runtracker.R
import com.buikr.runtracker.databinding.DialogEditRunBinding

class EditRunDialogFragment : DialogFragment() {
    private lateinit var listener: EditRunDialogListener
    private lateinit var binding: DialogEditRunBinding

    var startingName: String = ""
    var startingDescription: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? EditRunDialogListener
            ?: throw RuntimeException("Activity must implement the EditRunDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditRunBinding.inflate(LayoutInflater.from(context))
        binding.etName.setText(startingName)
        binding.etDescription.setText(startingDescription)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.edit_run))
            .setView(binding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onRunEdited(
                        binding.etName.text.toString(),
                        binding.etDescription.text.toString()
                    )
                } else {
                    binding.etName.error = "Title is required"
                }
            }
            .create()
        return dialog
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    interface EditRunDialogListener {
        fun onRunEdited(name: String, description: String)
    }
}