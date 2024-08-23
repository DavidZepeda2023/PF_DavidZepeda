package david.fabiola.hospbloom.ui.notifications

import Modelo.Conexion
import Modelo.Pacientes
import ViewHolders.Adaptador
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import david.fabiola.hospbloom.R
import david.fabiola.hospbloom.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val btnIngresarPaciente = root.findViewById<Button>(R.id.btnguardar)
        val rcvPaciente = root.findViewById<RecyclerView>(R.id.rcvpacientes)


        fun obtenerDatos(): List<Pacientes>{

            val objConexion = Conexion().cadenaConexion()


            val statement = objConexion?.createStatement()
            val resulSet = statement?.executeQuery("select * from Paciente")!!


            val Pacientes = mutableListOf<Pacientes>()

            while (resulSet.next()){
                val uuid = resulSet.getString("UUID_Paciente")
                val nombre = resulSet.getString("Nombre")
                val apellido = resulSet.getString("Apellido")
                val edad = resulSet.getInt("Edad")
                val numHabitacion = resulSet.getInt("NumeroHabitacion")
                val NumeroCama = resulSet.getInt("NumeroCama")
                val MedicamentoAsignado = resulSet.getString("MedicamentoAsignado")
                val FechaIngreso = resulSet.getString("FechaIngreso")
                val HoraDeAplicacionMedicamento = resulSet.getString("HoraDeAplicacionMedicamento")


                val Paciente = Pacientes(uuid, nombre, apellido, edad, numHabitacion, NumeroCama, MedicamentoAsignado, FechaIngreso, HoraDeAplicacionMedicamento )
                Pacientes.add(Paciente)
            }
            return Pacientes
        }

        btnIngresarPaciente.setOnClickListener{

            val txtnombrePaciente = root.findViewById<EditText>(R.id.txtagnombre)
            val txtapellidoPaciente = root.findViewById<EditText>(R.id.txtagapellido)
            val txtedadPaciente = root.findViewById<EditText>(R.id.txtagedad)
            val txtnumeroHabitacion = root.findViewById<EditText>(R.id.txtagnumhab)
            val txtnumeroCama = root.findViewById<EditText>(R.id.txtagnumcam)
            val txtmedicamentoAsignado = root.findViewById<EditText>(R.id.txtagmedasig)
            val txtfechaIngreso = root.findViewById<EditText>(R.id.txtagfechaing)
            val txthoraAplicacionMed = root.findViewById<EditText>(R.id.txthorapl)


            CoroutineScope(Dispatchers.IO).launch {

                val objConexion = Conexion().cadenaConexion()

                try {
                    val IngresarPaciente = objConexion?.prepareStatement("INSERT INTO Paciente(UUID_Paciente, Nombre, Apellido, Edad, NumeroHabitacion,NumeroCama, MedicamentoAsignado, FechaIngreso, HoraDeAplicacionMedicamento) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")!!
                    IngresarPaciente.setString(1, UUID.randomUUID().toString())
                    IngresarPaciente.setString(2, txtnombrePaciente.text.toString())
                    IngresarPaciente.setString(3, txtapellidoPaciente.text.toString())
                    IngresarPaciente.setInt(4, txtedadPaciente.text.toString().toInt())
                    IngresarPaciente.setInt(5, txtnumeroHabitacion.text.toString().toInt())
                    IngresarPaciente.setInt(6, txtnumeroCama.text.toString().toInt())
                    IngresarPaciente.setString(7, txtmedicamentoAsignado.text.toString())
                    IngresarPaciente.setString(8, txtfechaIngreso.text.toString())
                    IngresarPaciente.setString(9, txthoraAplicacionMed.text.toString())
                    IngresarPaciente.executeUpdate()
                } catch (e: Exception) {
                    println(e)
                }
            }


        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}