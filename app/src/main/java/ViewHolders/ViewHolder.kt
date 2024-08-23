package ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import david.fabiola.hospbloom.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val NombrePaciente: TextView = view.findViewById(R.id.txtnombrepaciente)
    val ApellidoPaciente: TextView = view.findViewById(R.id.txtapellido)
    val EdadPaciente: TextView = view.findViewById(R.id.txtedad)
    val EnfermedadPaciente: TextView = view.findViewById(R.id.txtenfermedad)
    val NumerodeHabitacion: TextView = view.findViewById(R.id.txtnumhab)
    val NumeroDecama: TextView = view.findViewById(R.id.txtnumcam)
    val MedicamentosAsignados: TextView = view.findViewById(R.id.txtmedasig)
    val FechadeIngreso: TextView = view.findViewById(R.id.txtfechaing)
    val HoraAplicacion: TextView = view.findViewById(R.id.txthoraapl)

    val imgEditar: ImageView = view.findViewById(R.id.btnactualizar)
    val imgBorrar: ImageView = view.findViewById(R.id.btnborrar)
}
