package ViewHolders

import Modelo.Conexion
import Modelo.Pacientes
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import david.fabiola.hospbloom.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Adaptador(private var datos: List<Pacientes>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista: List<Pacientes>) {
        datos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paciente = datos[position]
        holder.NombrePaciente.text = paciente.NombrePaciente
        holder.ApellidoPaciente.text = paciente.ApellidoPaciente
        holder.EdadPaciente.text = paciente.Edad.toString()
        holder.EnfermedadPaciente.text = paciente.MedicamentoAsignado
        holder.NumerodeHabitacion.text = paciente.NumeroHabitacion.toString()
        holder.NumeroDecama.text = paciente.NumeroCama.toString()
        holder.MedicamentosAsignados.text = paciente.MedicamentoAsignado
        holder.FechadeIngreso.text = paciente.FechaIngreso
        holder.HoraAplicacion.text = paciente.HoraAplicacionMed

        holder.imgBorrar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el paciente?")

            builder.setPositiveButton("Sí") { _, _ ->
                EliminarPaciente(paciente.UUID, position)
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        holder.imgEditar.setOnClickListener {
            holder.imgEditar.setOnClickListener {
                val context = holder.itemView.context

                // Crea un LinearLayout para contener los EditText
                val linearLayout = LinearLayout(context)
                linearLayout.orientation = LinearLayout.VERTICAL

                // Crea EditTexts para cada campo
                val nombreEditText = EditText(context)
                nombreEditText.hint = "Nombre"
                nombreEditText.setText(paciente.NombrePaciente)
                linearLayout.addView(nombreEditText)

                val apellidoEditText = EditText(context)
                apellidoEditText.hint = "Apellido"
                apellidoEditText.setText(paciente.ApellidoPaciente)
                linearLayout.addView(apellidoEditText)

                val edadEditText = EditText(context)
                edadEditText.hint = "Edad"
                edadEditText.setText(paciente.Edad.toString())
                edadEditText.inputType = InputType.TYPE_CLASS_NUMBER
                linearLayout.addView(edadEditText)

                val habitacionEditText = EditText(context)
                habitacionEditText.hint = "Número de habitación"
                habitacionEditText.setText(paciente.NumeroHabitacion.toString())
                habitacionEditText.inputType = InputType.TYPE_CLASS_NUMBER
                linearLayout.addView(habitacionEditText)

                val camaEditText = EditText(context)
                camaEditText.hint = "Número de cama"
                camaEditText.setText(paciente.NumeroCama.toString())
                camaEditText.inputType = InputType.TYPE_CLASS_NUMBER
                linearLayout.addView(camaEditText)

                val medicamentoEditText = EditText(context)
                medicamentoEditText.hint = "Medicamento asignado"
                medicamentoEditText.setText(paciente.MedicamentoAsignado)
                linearLayout.addView(medicamentoEditText)

                val fechaEditText = EditText(context)
                fechaEditText.hint = "Fecha de ingreso"
                fechaEditText.setText(paciente.FechaIngreso)
                linearLayout.addView(fechaEditText)

                val horaEditText = EditText(context)
                horaEditText.hint = "Hora de aplicación"
                horaEditText.setText(paciente.HoraAplicacionMed)
                linearLayout.addView(horaEditText)

                // Crea el AlertDialog
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Actualizar Paciente")
                builder.setView(linearLayout)

                builder.setPositiveButton("Actualizar") { _, _ ->
                    // Recupera los valores de los EditText
                    val nuevoNombre = nombreEditText.text.toString()
                    val nuevoApellido = apellidoEditText.text.toString()
                    val nuevaEdad = edadEditText.text.toString().toIntOrNull() ?: paciente.Edad
                    val nuevoHabitacion = habitacionEditText.text.toString().toIntOrNull() ?: paciente.NumeroHabitacion
                    val nuevaCama = camaEditText.text.toString().toIntOrNull() ?: paciente.NumeroCama
                    val nuevoMedicamento = medicamentoEditText.text.toString()
                    val nuevaFecha = fechaEditText.text.toString()
                    val nuevaHora = horaEditText.text.toString()

                    // Llama a la función para actualizar el paciente
                    ActualizarPaciente(
                        paciente.UUID, nuevoNombre, nuevoApellido, nuevaEdad, nuevoHabitacion, nuevaCama,
                        nuevoMedicamento, nuevaFecha, nuevaHora, position
                    )
                }

                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }

        }
    }

    fun ActualizarPaciente(
        UUID: String, nuevoNombrePaciente: String, nuevoApellidoPaciente: String, nuevoEdadPaciente: Int,
        nuevoNumeroHabitacion: Int, nuevoNumeroCama: Int, nuevoMedicamentoAsignado: String,
        nuevoFechaIngreso: String, nuevoHoraAplicacionMed: String, position: Int
    ) {
        // Actualiza la lista local
        val datalist = datos.toMutableList()
        val pacienteActualizado = datalist[position].copy(
            NombrePaciente = nuevoNombrePaciente,
            ApellidoPaciente = nuevoApellidoPaciente,
            Edad = nuevoEdadPaciente,
            NumeroHabitacion = nuevoNumeroHabitacion,
            NumeroCama = nuevoNumeroCama,
            MedicamentoAsignado = nuevoMedicamentoAsignado,
            FechaIngreso = nuevoFechaIngreso,
            HoraAplicacionMed = nuevoHoraAplicacionMed
        )
        datalist[position] = pacienteActualizado
        datos = datalist.toList()
        notifyItemChanged(position)

        // Actualiza la base de datos en un hilo de fondo
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = Conexion().cadenaConexion()
            val updatePaciente = objConexion?.prepareStatement(
                "UPDATE Paciente SET Nombre = ?, Apellido = ?, Edad = ?, NumeroHabitacion = ?, NumeroCama = ?, MedicamentoAsignado = ?, FechaIngreso = ?, HoraDeAplicacionMedicamento = ? WHERE UUID_Paciente = ?"
            )
            updatePaciente?.apply {
                setString(1, nuevoNombrePaciente)
                setString(2, nuevoApellidoPaciente)
                setInt(3, nuevoEdadPaciente)
                setInt(4, nuevoNumeroHabitacion)
                setInt(5, nuevoNumeroCama)
                setString(6, nuevoMedicamentoAsignado)
                setString(7, nuevoFechaIngreso)
                setString(8, nuevoHoraAplicacionMed)
                setString(9, UUID)
                executeUpdate()
            }
        }
    }

    fun EliminarPaciente(UUID: String, position: Int) {
        val datalist = datos.toMutableList()
        datalist.removeAt(position)
        datos = datalist.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = Conexion().cadenaConexion()
            val deletePaciente = objConexion?.prepareStatement("DELETE FROM Paciente WHERE UUID_Paciente = ?")
            deletePaciente?.setString(1, UUID)
            deletePaciente?.executeUpdate()
        }
    }
}
