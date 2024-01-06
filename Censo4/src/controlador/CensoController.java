/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.interfazDAO.DataAccessObject;
import controlador.listas.LinkedList;
import controlador.listas.exception.VacioException;
import java.io.IOException;
import modelo.Censo;

/**
 *
 * @author santiago
 */
public class CensoController extends DataAccessObject<Censo> {

    private Censo censo = new Censo();

    public CensoController() {
        super(Censo.class);
    }

    public Censo getCenso() {
        if (censo == null) {
            censo = new Censo();
        }
        return censo;
    }

    public void setCenso(Censo censo) {
        this.censo = censo;
    }

    public Boolean save() {
        censo.setId(generated_id());
        return save(censo);
    }

    public void update(Integer pos) throws IOException {
        this.update(censo, pos);
    }

    public String generatedCode() {
        StringBuilder code = new StringBuilder();
        Integer length = listAll().getSize() + 1;
        Integer pos = length.toString().length();
        for (int i = 0; i < (10 - pos); i++) {
            code.append("0");
        }
        code.append(length.toString());
        return code.toString();
    }

    public int contarPorMotivo(String Estado) throws VacioException {
        int contador = 0;
        LinkedList<Censo> censos = listAll();

        for (int i = 0; i < censos.getSize(); i++) {
            Censo censo = censos.get(i);
            if (Estado.equals(censo.getEstadoCivil())) {
                contador++;
            }
        }

        return contador;
    }

    public int contarDivorciados() throws VacioException {
        return contarPorMotivo("Divorciado");
    }

    public int contarSeparados() throws VacioException {
        return contarPorMotivo("Separado");
    }

    public void eliminar(Integer pos) {
        delete(pos);
    }

    public LinkedList<Censo> buscar(String argumento, String criterio, String metodo) {

        LinkedList<Censo> resultado = new LinkedList<>();
        Censo[] listaCenso = listAll().toArray();

        if (metodo.equalsIgnoreCase("binario")) {
            mergeSort(listaCenso, true, criterio);
            Integer posResultado = binario(listaCenso, argumento, 0, listaCenso.length - 1, criterio);
            if (posResultado >= 0) {
                resultado.add(listaCenso[posResultado]);
            }
            return resultado;
        } else {
            return lineal(listaCenso, argumento, criterio);
        }

    }

    private LinkedList<Censo> lineal(Censo[] censo, String argumento, String criterio) {

        LinkedList<Censo> resultado = new LinkedList<>();

        for (int i = 0; i < censo.length; i++) {
            if(critBusqueda(censo[i], argumento, criterio) == 0){
                resultado.add(censo[i]);
            }
        }
        
        return resultado;
    }

    private int binario(Censo[] censo, String argumento, Integer menor, Integer mayor, String criterio) {

        if (mayor >= menor) {

            Integer mid = menor + (mayor - menor) / 2;

            if (critBusquedaBinaria(censo[mid], argumento, criterio) == 0) {
                return mid;
            }

            if (critBusquedaBinaria(censo[mid], argumento, criterio) > 0) {
                return binario(censo, argumento, menor, mid - 1, criterio);
            }

            return binario(censo, argumento, mid + 1, mayor, criterio);

        }

        return -1;
    }

    private void mergeSort(Censo[] censos, boolean tipo, String criterio) {
        if (censos == null || censos.length <= 1) {
            return;
        }

        int centro = censos.length / 2;
        Censo[] izq = new Censo[centro];
        Censo[] der = new Censo[censos.length - centro];

        System.arraycopy(censos, 0, izq, 0, centro);

        if (censos.length - centro >= 0) {
            System.arraycopy(censos, centro, der, 0, censos.length - centro);
        }

        mergeSort(izq, tipo, criterio);
        mergeSort(der, tipo, criterio);
        merge(izq, der, censos, tipo, criterio);
    }

    private void merge(Censo[] izq, Censo[] der, Censo[] censos, boolean tipo, String criterio) {
        int i = 0, j = 0, k = 0;

        // Effectively sorts left and right array
        while (i < izq.length && j < der.length) {

            if (tipo ? critOrdenamiento(izq[i], der[j], criterio) <= 0 : critOrdenamiento(izq[i], der[j], criterio) >= 0) {
                censos[k++] = izq[i++];
            } else {
                censos[k++] = der[j++];
            }

        }

        while (i < izq.length) {
            censos[k++] = izq[i++];
        }
        while (j < der.length) {
            censos[k++] = der[j++];
        }
    }

    private Integer critBusqueda(Censo obj1, String obj2, String criterio) {

        if (criterio.equalsIgnoreCase("nombre")) {
            return obj1.getNombrePersona().toLowerCase().startsWith(obj2) ? 0 : 1;
        } else if (criterio.equalsIgnoreCase("estadocivil")) {
            return obj1.getEstadoCivil().toLowerCase().startsWith(obj2) ? 0 : 1;
        } else if (criterio.equalsIgnoreCase("Fecha")) {
            return obj1.getFecha().toLowerCase().startsWith(obj2) ? 0 : 1;
        } else if (criterio.equalsIgnoreCase("motivo")) {
            return obj1.getMotivo().toLowerCase().startsWith(obj2) ? 0 : 1;
        } else {
            return 1;
        }
    }

    private Integer critOrdenamiento(Censo obj1, Censo obj2, String criterio) {

        if (criterio.equalsIgnoreCase("nombre")) {
            return Integer.compare(obj1.getNombrePersona().compareToIgnoreCase(obj2.getNombrePersona()), 0);
        } else if (criterio.equalsIgnoreCase("estadocivil")) {
            return Integer.compare(obj1.getEstadoCivil().compareToIgnoreCase(obj2.getEstadoCivil()), 0);
        } else if (criterio.equalsIgnoreCase("Fecha")) {
            return Integer.compare(obj1.getFecha().compareToIgnoreCase(obj2.getFecha()), 0);
        } else if (criterio.equalsIgnoreCase("motivo")) {
            return Integer.compare(obj1.getMotivo().compareToIgnoreCase(obj2.getMotivo()), 0);
        } else {
            return 0;
        }
    }
    
        private Integer critBusquedaBinaria(Censo obj1, String obj2, String criterio) {

        if (criterio.equalsIgnoreCase("nombre")) {
            return Integer.compare(obj1.getNombrePersona().compareToIgnoreCase(obj2), 0);
        } else if (criterio.equalsIgnoreCase("estadocivil")) {
            return Integer.compare(obj1.getEstadoCivil().compareToIgnoreCase(obj2), 0);
        } else if (criterio.equalsIgnoreCase("Fecha")) {
            return Integer.compare(obj1.getFecha().compareToIgnoreCase(obj2), 0);
        } else if (criterio.equalsIgnoreCase("motivo")) {
            return Integer.compare(obj1.getMotivo().compareToIgnoreCase(obj2), 0);
        } else {
            return 2;
        }
    }

}
