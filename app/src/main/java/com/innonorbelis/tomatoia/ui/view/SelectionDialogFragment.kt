package com.innonorbelis.tomatoia.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.R

class SelectionDialogFragment : DialogFragment() {

    interface OnOptionSelectedListener {
        fun onOptionSelected(selectedOption: String, tag: String)
    }

    private var listener: OnOptionSelectedListener? = null
    private var options: ArrayList<String> = arrayListOf()
    private var dialogTitle: String = ""
    private var fieldTag: String = "" // Para identificar qué campo se está seleccionando

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dialogTitle = it.getString("title", "")
            options = it.getStringArrayList("options") ?: arrayListOf()
            fieldTag = it.getString("tag", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.item_selection_dialog, container, false)

        val titleTv = view.findViewById<TextView>(R.id.dialog_title)
        titleTv.text = dialogTitle

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_options)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = SelectionAdapter(options) { selectedOption ->
            listener?.onOptionSelected(selectedOption, fieldTag)
            dismiss() // Cierra el diálogo tras seleccionar una opción
        }
        recyclerView.adapter = adapter

        return view
    }

    fun setOnOptionSelectedListener(listener: OnOptionSelectedListener) {
        this.listener = listener
    }

    // Adaptador del RecyclerView
    private class SelectionAdapter(
        private val localOptions: List<String>,
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<SelectionAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_option, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val option = localOptions[position]
            holder.optionText.text = option
            holder.itemView.setOnClickListener { onClick(option) }
        }

        override fun getItemCount(): Int = localOptions.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val optionText: TextView = itemView.findViewById(R.id.option_text)
        }
    }

    companion object {
        fun newInstance(title: String,
                        optionsList: ArrayList<String>,
                        tag: String): SelectionDialogFragment {
            val fragment = SelectionDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putStringArrayList("options", optionsList)
            args.putString("tag", tag)
            fragment.arguments = args
            return fragment
        }
    }
}
