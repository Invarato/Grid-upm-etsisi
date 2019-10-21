package es.upm.etsisi.grid_upm_etsisi.Controlador;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import es.upm.etsisi.grid_upm_etsisi.R;


/** Clase encargada de gestionar la ventana emergente
 * @author Ram�n Invarato Men�ndez
 * @author Roberto P�rez
 */
public abstract class ventana_cantidadimagenes extends Dialog {
	private Context contexto;
	
	private EditText edittext_filas, edittext_columnas;
	
	public ventana_cantidadimagenes (Context contexto){
		super (contexto);
		this.contexto = contexto;
		mensaje ();
	}

   private void mensaje (){
   	OnClickListener dialogoListener = new OnClickListener() {
   		public void onClick(DialogInterface dialog, int which) {
   			try {
   				ventana_aceptada (Integer.parseInt(edittext_filas.getText()+""), Integer.parseInt(edittext_columnas.getText()+""));
   			} catch (Exception e) {
   				e.printStackTrace();
   			};
   		}
   	};

   	
   	//Frame hinchado.............................
   	LayoutInflater inflater = this.getLayoutInflater();
   	FrameLayout frameHinchado = new FrameLayout(contexto);
   	frameHinchado.addView(inflater.inflate(R.layout.ventana_cantidadimagenes, frameHinchado, false));

   	View view = frameHinchado.getChildAt(0);
   	//Frame hinchado.............................
   	
   	edittext_filas = (EditText) view.findViewById(R.id.ET_filas);
   	edittext_columnas = (EditText) view.findViewById(R.id.ET_columnas);
   	
   	//Di�logo....................................
   	new AlertDialog.Builder(contexto)
   	.setPositiveButton("Procesar", dialogoListener)
   	.setNegativeButton("Cancelar", null)
   	.setView(frameHinchado)
   	.setTitle("Dividir")
   	.show();
   	//Di�logo....................................
   }
   
   public abstract void ventana_aceptada (int filas, int columnas);
	
}
