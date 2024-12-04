package wrappersEstructurasDeDatos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.proyecto.base.dto.OperacionReporteDTO;

public class LinkedHashMapDeOperaciones {

	private LinkedHashMap<LocalDate, ListaEnlazadaDeOperaciones> operaciones;
	private int cantidadDeElementos;

	public LinkedHashMapDeOperaciones() {
		this.operaciones = new LinkedHashMap<LocalDate, ListaEnlazadaDeOperaciones>();
		cantidadDeElementos = 0;
	}

	public void mostrarHashMap() {
		System.out.println("MOSTRANDO EL MAP");
		System.out.println("MOSTRANDO EL MAP");
		System.out.println("MOSTRANDO EL MAP");
		int i=0;

		for (LocalDate key : operaciones.keySet()) {
			System.out.println("Clave: " + key + ", Valor: " + operaciones.get(key));
            i++;
			System.out.println("nro de lista: "+i);
			operaciones.get(key).mostrarOperacionesInternas();

		}

	}

	public void comparar(LinkedHashMapDeOperaciones reporteDeOperacionesAjeno) {

		System.out.println(" COMPARANDO LOS REPORTES en el metodo de LinkedHashMapDeOperaciones 'comparar'");

		for (LocalDate fecha : operaciones.keySet()) {

			System.out.println("LA FECHA en (comparar) de (LinkedHashMapDeOperaciones) es : " + fecha);
			System.out.println("LA FECHA en (comparar) de (LinkedHashMapDeOperaciones) es : " + fecha);

			System.out.println();

			ListaEnlazadaDeOperaciones listaDeOperacionesActual = this.operaciones.get(fecha);
			ListaEnlazadaDeOperaciones listaAjenaDeOperaciones = reporteDeOperacionesAjeno.getLista(fecha);

			if (listaAjenaDeOperaciones != null) {
				compararListas(listaDeOperacionesActual, listaAjenaDeOperaciones);
				System.out.println("la Lista ajena SI TIENE la fecha: " + fecha);
			} else {
				System.out.println("la Lista ajena no tiene la fecha: " + fecha);
			}

		}
	}

	public void comparar(LinkedHashMapDeOperaciones reporteDeOperacionesAjeno,ArrayList<OperacionReporteDTO> contextoOperacionesOK,
			ArrayList<OperacionReporteDTO> alycOperacionesOK) {

		System.out.println(" COMPARANDO LOS REPORTES en el metodo de LinkedHashMapDeOperaciones 'comparar'");

		for (LocalDate fecha : operaciones.keySet()) {

			System.out.println("LA FECHA en (comparar) de (LinkedHashMapDeOperaciones) es : " + fecha);
			System.out.println("LA FECHA en (comparar) de (LinkedHashMapDeOperaciones) es : " + fecha);

			System.out.println();

			ListaEnlazadaDeOperaciones listaDeOperacionesActual = this.operaciones.get(fecha);
			ListaEnlazadaDeOperaciones listaAjenaDeOperaciones = reporteDeOperacionesAjeno.getLista(fecha);

			if (listaAjenaDeOperaciones != null) {
				compararListas(listaDeOperacionesActual, listaAjenaDeOperaciones,contextoOperacionesOK,alycOperacionesOK);
				System.out.println("la Lista ajena SI TIENE la fecha: " + fecha);
			} else {
				System.out.println("la Lista ajena no tiene la fecha: " + fecha);
			}

		}
	}
	
	public void compararOperacionesRojas(LinkedHashMapDeOperaciones reporteDeOperacionesAjeno,ArrayList<OperacionReporteDTO> contextoAzul,ArrayList<OperacionReporteDTO> alycAzul) {

		System.out.println(" COMPARANDO LOS REPORTES en el metodo de LinkedHashMapDeOperaciones 'comparar'");

		for (LocalDate fecha : operaciones.keySet()) {

			/*System.out.println("LA FECHA en (comparar) de (LinkedHashMapDeOperaciones) es : " + fecha);
			System.out.println("LA FECHA en (comparar) de (LinkedHashMapDeOperaciones) es : " + fecha);

			System.out.println();*/

			ListaEnlazadaDeOperaciones listaDeOperacionesActual = this.operaciones.get(fecha);
			ListaEnlazadaDeOperaciones listaAjenaDeOperaciones = reporteDeOperacionesAjeno.getLista(fecha);

			if (listaAjenaDeOperaciones != null) {
				compararListasRojas(listaDeOperacionesActual, listaAjenaDeOperaciones,contextoAzul,alycAzul);
				System.out.println("la Lista ajena SI TIENE la fecha: " + fecha);
			} else {
				System.out.println("la Lista ajena no tiene la fecha: " + fecha);
			}

		}
	}

	public int llenarSheet(Sheet sheet, int posRow) {

		for (LocalDate fecha : operaciones.keySet()) {
			posRow = this.operaciones.get(fecha).llenarSheet(sheet, posRow);
		}

		return posRow;
	}

	private void compararListas(ListaEnlazadaDeOperaciones listaDeOperacionesActual,
			ListaEnlazadaDeOperaciones listaAjenaDeOperaciones) {

		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");

		OperacionReporteDTO operacionBuscada = null;
		List<OperacionReporteDTO> operacionesAEliminarActual = new LinkedList<>();

		for (OperacionReporteDTO operacion : listaDeOperacionesActual) {

			System.out.println("ITERANDO LA OPERACION: " + operacion.getEspecie());
			System.out.println("ITERANDO LA OPERACION: " + operacion.getEspecie());

			System.out.println("la operacionActual tiene fecha: " + operacion.getFecha());
			System.out.println("la operacionActual tiene fecha: " + operacion.getFecha());

			operacionBuscada = listaAjenaDeOperaciones.tenesEstaOperacion(operacion);

			if (operacionBuscada == null) {
				System.out.println("operacionBuscada NULLA");
				System.out.println("operacionBuscada NULLA");
				System.out.println("");
				System.out.println("");
			}

			if (operacionBuscada != null) {
				operacionesAEliminarActual.add(operacion);
				boolean b = listaAjenaDeOperaciones.remove(operacionBuscada);

				System.out.println("compararListas, perfecto tu operacion ES IGUAL, ahora deberian eliminarse del map");
				System.out.println("compararListas, perfecto tu operacion ES IGUAL, ahora deberian eliminarse del map");

				System.out.println("Booleano para saber si se pudo borrar de la ListaAjena: " + b);

			}
		}
		eliminarOperaciones(operacionesAEliminarActual, listaDeOperacionesActual);
	}

	private void compararListas(ListaEnlazadaDeOperaciones listaDeOperacionesActual,
			ListaEnlazadaDeOperaciones listaAjenaDeOperaciones, ArrayList<OperacionReporteDTO> contextoOperacionesOK,
			ArrayList<OperacionReporteDTO> alycOperacionesOK) {

		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");
		System.out.println("ITERANDO 'compararListas' de LinkedHashMap");

		OperacionReporteDTO operacionBuscada = null;
		List<OperacionReporteDTO> operacionesAEliminarActual = new LinkedList<>();

		for (OperacionReporteDTO operacion : listaDeOperacionesActual) {

			System.out.println("ITERANDO LA OPERACION: " + operacion.getEspecie());
			System.out.println("ITERANDO LA OPERACION: " + operacion.getEspecie());

			System.out.println("la operacionActual tiene fecha: " + operacion.getFecha());
			System.out.println("la operacionActual tiene fecha: " + operacion.getFecha());

			operacionBuscada = listaAjenaDeOperaciones.tenesEstaOperacion(operacion);

			if (operacionBuscada == null) {
				System.out.println("operacionBuscada NULLA");
				System.out.println("operacionBuscada NULLA");
				System.out.println("");
				System.out.println("");
			}

			if (operacionBuscada != null) {
				operacionesAEliminarActual.add(operacion);
				contextoOperacionesOK.add(operacion);
				boolean b = listaAjenaDeOperaciones.remove(operacionBuscada);
				alycOperacionesOK.add(operacionBuscada);

				System.out.println("compararListas, perfecto tu operacion ES IGUAL, ahora deberian eliminarse del map");
				System.out.println("compararListas, perfecto tu operacion ES IGUAL, ahora deberian eliminarse del map");

				System.out.println("Booleano para saber si se pudo borrar de la ListaAjena: " + b);

			}
		}
		eliminarOperaciones(operacionesAEliminarActual, listaDeOperacionesActual);
	}
	
	
	private void compararListasRojas(ListaEnlazadaDeOperaciones listaDeOperacionesActual,
			ListaEnlazadaDeOperaciones listaAjenaDeOperaciones,ArrayList<OperacionReporteDTO> contextoAzul,ArrayList<OperacionReporteDTO> alycAzul) {

		
		OperacionReporteDTO operacionBuscada = null;
		List<OperacionReporteDTO> operacionesAEliminarActual = new LinkedList<>();

		for (OperacionReporteDTO operacion : listaDeOperacionesActual) {
			
			operacionBuscada = listaAjenaDeOperaciones.tenesEstaOperacionRoja(operacion, contextoAzul, alycAzul);

			if (operacionBuscada == null) {
				System.out.println("");
				System.out.println("");
				System.out.println("operacionBuscada NULLA");
				System.out.println("operacionBuscada NULLA");
				System.out.println("");
				System.out.println("");
			}

			if (operacionBuscada != null) {
				operacionesAEliminarActual.add(operacion);
				listaAjenaDeOperaciones.remove(operacionBuscada);
			}
		}
		eliminarOperaciones(operacionesAEliminarActual, listaDeOperacionesActual);
	}

	private void eliminarOperaciones(List<OperacionReporteDTO> operacionesAEliminarActual,
			ListaEnlazadaDeOperaciones listita) {
		listita.eliminarOperaciones(operacionesAEliminarActual);
	}

	public ListaEnlazadaDeOperaciones getLista(LocalDate date) {
		return this.operaciones.get(date);
	}

	// Agregar una operación
	public void agregar(OperacionReporteDTO operacion) {
		LocalDate fecha = operacion.getFecha();

		// Si no existe una lista para esta fecha, crearla
		operaciones.putIfAbsent(fecha, new ListaEnlazadaDeOperaciones());

		// Agregar la operación a la lista correspondiente
		operaciones.get(fecha).add(operacion);

		sumarElemento();
	}

	private void sumarElemento() {
		cantidadDeElementos++;
	}

	private void restarElemento() {
		cantidadDeElementos--;
	}

	public int getCantidadDeElementos() {
		return this.cantidadDeElementos;
	}

	// Método para eliminar una operación
	public boolean eliminar(OperacionReporteDTO operacion) {
		LocalDate fecha = operacion.getFecha();
		boolean eliminado = false;

		LinkedList<OperacionReporteDTO> listaOperaciones = operaciones.get(fecha);
		if (listaOperaciones != null) {
			eliminado = listaOperaciones.remove(operacion);

			// borrarListaSiEstaVacia(listaOperaciones, fecha);

		}

		if (eliminado) {
			restarElemento();
		}

		return eliminado;
	}

	// Método para obtener operaciones de una fecha específica
	public LinkedList<OperacionReporteDTO> obtenerOperacionesPorFecha(LocalDate fecha) {
		return operaciones.getOrDefault(fecha, new ListaEnlazadaDeOperaciones());
	}

	// Método para obtener todas las operaciones
	public LinkedHashMap<LocalDate, ListaEnlazadaDeOperaciones> obtenerTodasOperaciones() {
		return operaciones;
	}

	private void borrarListaSiEstaVacia(LinkedList<OperacionReporteDTO> lista, LocalDate fecha) {
		if (lista.isEmpty()) {
			operaciones.remove(fecha);
		}
	}

	// Método para obtener el año de la primera fecha
	// OJO CON EL FORMATO ya que toma el formato yyyy/mm/dd y no el formato
	// dd/mm/yyyy. fijarase como los guardo el ReporteDTO TODO
	public int obtenerAñoDePrimeraFecha() {
		if (!operaciones.isEmpty()) {
			return operaciones.keySet().iterator().next().getYear();
		} else {
			throw new IllegalStateException("No hay operaciones disponibles para obtener el año.");
		}
	}

	public ArrayList<OperacionReporteDTO> generarArrayList() {
		ArrayList<OperacionReporteDTO> list = new ArrayList<OperacionReporteDTO>();

		for (LocalDate fecha : operaciones.keySet()) {
			operaciones.get(fecha).llenarListConOperaciones(list);
		}

		return list;
	}

}
