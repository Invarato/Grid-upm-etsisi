package es.upm.etsisi.grid_upm_etsisi.vista;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import es.upm.etsisi.grid_upm_etsisi.Controlador.Capa;
import es.upm.etsisi.grid_upm_etsisi.Controlador.CapaFiltros;
import es.upm.etsisi.grid_upm_etsisi.Controlador.ventana_cantidadimagenes;
import es.upm.etsisi.grid_upm_etsisi.R;


/** Interfaz de PFG_Grid
 * @author Ramón Invarato Menéndez
 * @author Roberto Pérez
 */
public class interfaz extends Activity {
	private Capa imagen_total;
	
	private LinearLayout LL_Tabla;
	
	private static final int IMAGEN_SELECCIONADA = 1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_interfaz);
        
        inicializar_views();
    }
    
    
    
    private void inicializar_views(){
    	LL_Tabla = (LinearLayout) findViewById(R.id.Tabla);
    	
    	cargar_imagen ();
    }
    
    
    
    private void cargar_imagen (){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);	//Permite al usuarios seleccionar un tipo de datos y devolverlo
		intent.setType("image/*"); //Tipos de archivos
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {	//Prueba que exista un selector de ficheros
			this.startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), IMAGEN_SELECCIONADA);
		} catch (android.content.ActivityNotFoundException ex) {	//En caso de que no exista ningún selector de ficheros
			Toast.makeText(this, "No existe ningún administrador de imágenes. Instale uno", Toast.LENGTH_SHORT).show();
		}
    }
    
    
	//Cuando se seleccione una imagen para cargar entrará aquí
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        if (requestCode == IMAGEN_SELECCIONADA) {
	        	imagen_total = new Capa (this);
	        	Uri uriSeleccionado = data.getData();
	        	imagen_total.cargar_imagen(uriSeleccionado);

				new ventana_cantidadimagenes(this){
					@Override
					public void ventana_aceptada(int filas, int columnas) {
						Log.v("test", "filas:"+filas+"columnas:"+columnas);
						iniciar_grid (filas, columnas);
					}
				};
	        }
	    }
	}
    
	
	private void iniciar_grid (int filas, int columnas){
		Display display = getWindowManager().getDefaultDisplay();
		imagen_total.ajustar_imagen_a(display.getWidth()-(filas+1), display.getHeight()-(columnas+1)); //Ajustar a pantalla
//		imagen_total.mover_imagen_a(0, 0, false);
		imagen_total.mover_a(0, 0);

		//Tomar trozos
		int tamanio_cacho_horizontal = imagen_total.get_tamImg_anchura()/columnas;
		int tamanio_cacho_vertial = imagen_total.get_tamImg_altura()/filas;
		Log.v("test", "tamanio_cacho_horizontal:"+tamanio_cacho_horizontal+"tamanio_cacho_vertial:"+tamanio_cacho_vertial);
		
		
		for (int fila_voy=0; fila_voy<filas; fila_voy++){
			LinearLayout fila = new LinearLayout(this);
			LL_Tabla.addView(fila);
			
			for (int columna_voy=0; columna_voy<columnas; columna_voy++){
				fila.setOrientation(LinearLayout.HORIZONTAL);
				
				FrameLayout Trozo = new FrameLayout(this);
				fila.addView(Trozo);
				
				Trozo.addView(imagen_total.convertir_a_subimagen(tamanio_cacho_vertial*columna_voy, tamanio_cacho_horizontal*fila_voy, tamanio_cacho_vertial*(columna_voy+1), tamanio_cacho_horizontal*(fila_voy+1)));
				
				LayoutParams params = Trozo.getLayoutParams();
				params.height = tamanio_cacho_horizontal;
				params.width = tamanio_cacho_vertial;
				Trozo.setLayoutParams(params);
				Trozo.setPadding(1, 1, 1, 1);
				
				Trozo.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {
						FrameLayout Trozo = (FrameLayout) view;
						Capa capa = (Capa) Trozo.getChildAt(0);
						capa.cambiar_color(CapaFiltros.ORDEN.SEPIA);
					}
				});
				
			}
		}
		
		
	}

    
    
}
