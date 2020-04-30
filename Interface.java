import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Interface extends Remote{

    public void agrgarPelicula(String nombre, String genero, String duracion, String clasificacion, String horario, int asientos)
            throws  RemoteException;

    public String getNombrePeliculas()
            throws RemoteException;
       
    public String getBoletos()
            throws RemoteException;

    public int getDisponibles(int opc)
            throws RemoteException;

    public int getTotal(int opc)
            throws RemoteException;

    public String ticket(int opc, int nAsientos)
            throws RemoteException;

    public String reporteVentas()
            throws RemoteException;

    public void cerrar()
            throws RemoteException;
}
