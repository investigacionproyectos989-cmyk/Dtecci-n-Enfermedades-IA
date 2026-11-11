package com.innonorbelis.tomatoia.ui.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.databinding.ActivityRegistroSiembraBinding
import com.innonorbelis.tomatoia.ui.adapter.SimpleAdapter

class RegistroSiembraActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityRegistroSiembraBinding

    private var selectedPlant: String? = null

    // Adapatadores
    private lateinit var adapterPlants: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityRegistroSiembraBinding.inflate(layoutInflater)
        setContentView(binding.root)

    // FUNCIÓN DEL CALENDARIO
        setupDateCard()

        // FUNCION DE SELECCION DE PLANTAS

        // --- Listeners para mostrar Y/O ocultar listas  variedad o tipo de tomate---
        binding.plantSelection.setOnClickListener {
            toggleRecyclerVisibility(binding.recyclerPlants)
            binding.recyclerReminders.visibility = View.GONE
        }
      // CERRAR LA ACTIVIDAD
        binding.ivClose.setOnClickListener {
            finish()
        }
        // --- Inicializar listas desplegables (RecyclerView) ---
        binding.recyclerPlants.layoutManager = LinearLayoutManager(this)
        binding.recyclerDate.layoutManager= LinearLayoutManager(this)

        //Adaptadores para para ser visible el layout

        adapterPlants = SimpleAdapter(getPlantVarietiesFromDb()) { selected ->
            binding.plantSelection.text = selected
            selectedPlant = selected
            binding.recyclerPlants.visibility = View.GONE
        }
        // LOS RECYCLER VIEW
        binding.recyclerPlants.adapter = adapterPlants
    }

    private fun getPlantVarietiesFromDb(): List<String> =
        listOf("Tomate Común", "Tomate Centauro", "Tomate Cherry", "Tomate Roma")

    // FUNCION DEL CALENDARIO

    private fun setupDateCard() {
        val dateSelection = findViewById<TextView>(R.id.date_selection)

        dateSelection.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val fecha =
                        String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    dateSelection.text = fecha
                    dateSelection.setTextColor(Color.parseColor("#2E7D32"))
                },
                year,
                month,
                day
            )

            // Quita el modo de entrada por teclado
            datePickerDialog.datePicker.descendantFocusability =
                DatePicker.FOCUS_BLOCK_DESCENDANTS

            datePickerDialog.show()
        }
    }

}

// --- Alterna la visibilidad con animación suave de los botones selecction---
private fun toggleRecyclerVisibility(view: View) {
    if (view.visibility == View.VISIBLE) {
        view.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction { view.visibility = View.GONE }
            .start()
    } else {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(150)
            .start()
    }
}

