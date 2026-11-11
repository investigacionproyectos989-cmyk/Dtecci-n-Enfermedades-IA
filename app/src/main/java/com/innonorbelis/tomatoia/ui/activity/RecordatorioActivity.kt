package com.innonorbelis.tomatoia.ui.activity

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.databinding.ActivityRecordatorioBinding
import com.innonorbelis.tomatoia.ui.adapter.SimpleAdapter

class RecordatorioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordatorioBinding
    //private lateinit var listaVariedadDao: ReminderDao

    private var selectedPlant: String? = null
    private var selectedReminderType: String? = null
    private var selectedRepeatOption: String? = null
    private var selectedControlAnterior: String? = null
//Adapatadores
    private lateinit var adapterPlants: SimpleAdapter
    private lateinit var adapterReminders: SimpleAdapter
    private lateinit var adapterFrecuencia: SimpleAdapter
    private lateinit var  adapterControlAnterior: SimpleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordatorioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar DAO
      //  listaVariedadDao = AppDatabase.getDatabase(applicationContext).listaVariedadDao()

        // Transición suave al expandir/ocultar vistas
       // (binding.root as? View)?.layoutTransition = LayoutTransition()

        // Botón para cerrar y volver al inicio
        binding.ivclose.setOnClickListener {
            finish()
        }

        // --- Inicializar listas desplegables (RecyclerView) ---
        binding.recyclerPlants.layoutManager = LinearLayoutManager(this)
        binding.recyclerReminders.layoutManager = LinearLayoutManager(this)
        binding.recyclerFrecuencia.layoutManager= LinearLayoutManager(this)
        binding.recyclerPrevious.layoutManager= LinearLayoutManager(this)

        //Adaptadores para para ser visible el layout

        adapterPlants = SimpleAdapter(getPlantVarietiesFromDb()) { selected ->
            binding.plantSelection.text = selected
            selectedPlant = selected
            binding.recyclerPlants.visibility = View.GONE
        }

        adapterReminders = SimpleAdapter(getReminderTypesFromDb()) { selected ->
            binding.reminderTypeSelection.text = selected
            selectedReminderType = selected
            binding.recyclerReminders.visibility = View.GONE
        }
      //  adapter de la frecuencia de dias del control

        adapterFrecuencia = SimpleAdapter(getRepeatOptions()) { selected ->
            binding.repeatSelection.text = selected
            binding.repeatSelection.setTextColor(Color.parseColor("#2E7D32"))
            selectedRepeatOption = selected
            binding.recyclerFrecuencia.visibility = View.GONE
        }

        //adapter de la control anterior
        adapterControlAnterior= SimpleAdapter(getLastControlOptionsFromDb()) { selected ->
            binding.previousSelection.text = selected
            binding.previousSelection.setTextColor(Color.parseColor("#2E7D32"))
            selectedControlAnterior = selected
            binding.recyclerPrevious.visibility = View.GONE
        }

 // LOS RECYCLER VIEW
        binding.recyclerPlants.adapter = adapterPlants
        binding.recyclerReminders.adapter = adapterReminders
        binding.recyclerFrecuencia.adapter=adapterFrecuencia
        binding.recyclerPrevious.adapter=adapterControlAnterior

        // --- Listeners para mostrar/ocultar listas  variedad o tipo de tomate---
        binding.plantSelection.setOnClickListener {
            toggleRecyclerVisibility(binding.recyclerPlants)
            binding.recyclerReminders.visibility = View.GONE
        }

        binding.reminderTypeSelection.setOnClickListener {
            toggleRecyclerVisibility(binding.recyclerReminders)
            binding.recyclerPlants.visibility = View.GONE
        }

        // --- Listener para mostrar/ocultar lista de frecuencias ---
        binding.repeatSelection.setOnClickListener {
            toggleRecyclerVisibility(binding.recyclerFrecuencia)
            binding.recyclerReminders.visibility = View.GONE
            binding.recyclerPlants.visibility = View.GONE
        }
// --- Listener para mostrar/ocultar lista de control anterior ---
        binding.previousSelection.setOnClickListener {
            toggleRecyclerVisibility(binding.recyclerPrevious)
            binding.recyclerFrecuencia.visibility= View.GONE
            binding.recyclerReminders.visibility = View.GONE
            binding.recyclerPlants.visibility = View.GONE

           // showSelectionToast("Última control", getLastControlOptionsFromDb())
        }
// Función de la hora
        setupHourCard()
        setupDateCard()
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

    // --- Ejemplo: Simular obtención de datos ---
    private fun getPlantVarietiesFromDb(): List<String> =
        listOf("Tomate Común", "Tomate Centauro", "Tomate Cherry", "Tomate Roma")

    private fun getReminderTypesFromDb(): List<String> =
        listOf("Pulverización", "Riego", "Rotación", "Fertilización")
//Cada cuanto se realiza el control
    private fun getRepeatOptions(): List<String> =
        (1..31).map { "$it días" }


//funcion o metodo sobre el control anterior
    private fun getLastControlOptionsFromDb(): List<String> =
        listOf("Hoy", "Ayer", "Hace 1 semana", "Hace 2 semanas", "Más de 2 semanas", "No recuerdo")
//n calendario para agendar la proximo control
    private fun showDatePicker() {
        Toast.makeText(this, "Abrir selector de fecha", Toast.LENGTH_SHORT).show()
    }

    private fun showTimePicker() {
        Toast.makeText(this, "Abrir selector de hora", Toast.LENGTH_SHORT).show()
    }

    private fun showSelectionToast(title: String, list: List<String>) {
        Toast.makeText(this, "$title: ${list.joinToString(", ")}", Toast.LENGTH_SHORT).show()
    }

    //funciones del layout hora
    private fun setupHourCard() {
        val hourPicker = findViewById<NumberPicker>(R.id.numberPickerHour)
        val minutePicker = findViewById<NumberPicker>(R.id.numberPickerMinute)
        val textAm = findViewById<TextView>(R.id.textAm)
        val textPm = findViewById<TextView>(R.id.textPm)
        val reminderTime = findViewById<TextView>(R.id.reminder_time)
        val hourContainer = findViewById<LinearLayout>(R.id.hour_container)

        // 🔹 Mostrar/ocultar contenedor al hacer clic en "Seleccionar hora"
        reminderTime.setOnClickListener {
            if (hourContainer.visibility == View.GONE) {
                hourContainer.visibility = View.VISIBLE
            } else {
                hourContainer.visibility = View.GONE
            }
        }

        //  Configurar NumberPickers
        hourPicker.minValue = 1
        hourPicker.maxValue = 12
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.setFormatter { i -> String.format("%02d", i) }

        var selectedHour = 2
        var selectedMinute = 0
        var selectedPeriod = "AM"

        hourPicker.value = selectedHour
        minutePicker.value = selectedMinute

        //  Función para cambiar estilo AM/PM
        fun updateSelection(selected: TextView, unselected: TextView) {
            selected.setBackgroundResource(R.drawable.bg_ampm_selector)
            selected.setTextColor(Color.WHITE)
            unselected.setBackgroundResource(R.drawable.bg_ampm_unselected)
            unselected.setTextColor(Color.parseColor("#2E7D32"))
        }

        //  Detectar cambios, pero no cerrar aún
        hourPicker.setOnValueChangedListener { _, _, newVal ->
            selectedHour = newVal
        }

        minutePicker.setOnValueChangedListener { _, _, newVal ->
            selectedMinute = newVal
        }

        // Función final: actualizar texto y ocultar contenedor
        fun confirmTimeSelection() {
            reminderTime.text = String.format("%02d:%02d %s", selectedHour, selectedMinute, selectedPeriod)
            reminderTime.setTextColor(Color.parseColor("#2E7D32"))

            // Efecto de desvanecimiento suave
            hourContainer.animate()
                .alpha(0f)
                .setDuration(150)
                .withEndAction {
                    hourContainer.visibility = View.GONE
                    hourContainer.alpha = 1f
                }
                .start()
        }

        // Selección AM / PM — aquí se confirma y se cierra
        textAm.setOnClickListener {
            selectedPeriod = "AM"
            updateSelection(textAm, textPm)
            confirmTimeSelection()
        }

        textPm.setOnClickListener {
            selectedPeriod = "PM"
            updateSelection(textPm, textAm)
            confirmTimeSelection()
        }
    }

//Función del calendario
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