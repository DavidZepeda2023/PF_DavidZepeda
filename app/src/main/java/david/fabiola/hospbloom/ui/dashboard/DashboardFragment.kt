package david.fabiola.hospbloom.ui.dashboard

import Modelo.Conexion
import Modelo.Pacientes
import ViewHolders.Adaptador
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import david.fabiola.hospbloom.R
import david.fabiola.hospbloom.databinding.FragmentDashboardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rcvPaciente = root.findViewById<RecyclerView>(R.id.rcvpacientes)
        rcvPaciente.layoutManager = LinearLayoutManager(context) // Asegúrate de configurar el LayoutManager

        CoroutineScope(Dispatchers.IO).launch {
            val pacientesDB = obtenerDatos()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(pacientesDB)
                rcvPaciente.adapter = adapter
            }
        }

        return root
    }

    private fun obtenerDatos(): List<Pacientes> {
        val objConexion = Conexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Paciente")

        val pacientesList = mutableListOf<Pacientes>()

        while (resultSet?.next() == true) {
            val uuid = resultSet.getString("UUID_Paciente")
            val nombre = resultSet.getString("Nombre")
            val apellido = resultSet.getString("Apellido")
            val edad = resultSet.getInt("Edad")
            val numHabitacion = resultSet.getInt("NumeroHabitacion")
            val numeroCama = resultSet.getInt("NumeroCama")
            val medicamentoAsignado = resultSet.getString("MedicamentoAsignado")
            val fechaIngreso = resultSet.getString("FechaIngreso")
            val horaDeAplicacionMedicamento = resultSet.getString("HoraDeAplicacionMedicamento")

            val paciente = Pacientes(
                UUID = uuid,
                NombrePaciente = nombre,
                ApellidoPaciente = apellido,
                Edad = edad,
                NumeroHabitacion = numHabitacion,
                NumeroCama = numeroCama,
                MedicamentoAsignado = medicamentoAsignado,
                FechaIngreso = fechaIngreso,
                HoraAplicacionMed = horaDeAplicacionMedicamento
            )
            pacientesList.add(paciente)
        }

        return pacientesList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
