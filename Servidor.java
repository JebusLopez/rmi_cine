import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import rmi_cinema.Interface;

public class Servidor extends UnicastRemoteObject implements Interface{
    private final int PUERTO = 5000;
    private ArrayList<Pelicula> peliculas = new ArrayList<>();
    private ArrayList<String> ventas = new ArrayList<>();
    public Servidor() throws RemoteException{
    }
    
    public static void main(String[] args) throws RemoteException {
        (new Servidor()).iniciarServidor();
    }

    private void iniciarServidor() {
        try{
            String dirIP = (InetAddress.getLocalHost()).toString();
            System.out.println("Escuchando en.. "+dirIP+":"+PUERTO);
            Registry registry = LocateRegistry.createRegistry(PUERTO);
            registry.bind("operacionservidor", (Interface) this);
        }catch(Exception e){
            e.printStackTrace();
        }
    } 

    @Override
    public void agrgarPelicula(String nombre, String genero, String duracion, String clasificacion, String horario, int asientos) throws RemoteException {
        peliculas.add(new Pelicula(nombre, genero, duracion, clasificacion, horario, asientos));
    }

    @Override
    public String getNombrePeliculas() throws RemoteException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < peliculas.size(); i++) {
            sb.append(peliculas.get(i).getTitulo());
            sb.append(",");
        }
        return sb.toString();
    }

    @Override
    public String getBoletos() throws RemoteException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < peliculas.size(); i++) {
            sb.append("Titulo: ");
            sb.append(peliculas.get(i).getTitulo());
            sb.append(" -- Horario:");
            sb.append(peliculas.get(i).getHorario());
            sb.append(" -- Asientos disponibles: ");
            sb.append(getDisponibles(i));
            sb.append(" -- Asientos Totales: ");
            sb.append(getTotal(i));
            sb.append(";");
        }
        return sb.toString();
    }

    @Override
    public int getDisponibles(int opc) throws RemoteException {
        int disponibles =(peliculas.get(opc).getAsientos() - peliculas.get(opc).getOcupados());
        return disponibles;
    }

    @Override
    public int getTotal(int opc) throws RemoteException {
        return peliculas.get(opc).getAsientos();
    }

    @Override
    public String ticket(int opc, int nAsientos) throws RemoteException {
        String ticket="";
        
        ticket = "\n\n**************************************\n"
               + "//////////// MULTI CINEMA \\\\\\\\\\\\\\\\\\\\\\\\ \n"
               + "**************************************\n"
               + "Pelicula     : " + peliculas.get(opc).getTitulo()+"\n"
               + "Genero       : " + peliculas.get(opc).getGenero()+"\n"
               + "Clasificacion: " + peliculas.get(opc).getClasificacion()+"\n"
               + "Duracion     : " + peliculas.get(opc).getDuracion()+"\n"
               + "Horario      : " + peliculas.get(opc).getHorario()+"\n"
               + "No.Boletos   : " + nAsientos +"\n\n"
               + "$ por boleto : [$50.00]\n"
               + "**************************************\n"
               + "Total        : [$"+(50 * nAsientos)+".00]\n"
               + "**************************************\n\n";
        
        int Ocupados = peliculas.get(opc).getOcupados();
        peliculas.get(opc).setOcupados(Ocupados + nAsientos);
        String venta = peliculas.get(opc).getTitulo() + "," + nAsientos + "," + (50 * nAsientos);
        ventas.add(venta);
        
        return ticket;
    }

    @Override
    public String reporteVentas() throws RemoteException {
        String reporte = "";
        int totalBoletos = 0;
        int totalDinero = 0;
        reporte = "\n\n******************************************************\n"
                    + "//////////////////// MULTI CINEMA \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n"
                    + "******************************************************\n"
                    + "\nREPORTE DE VENTAS\n";
                
        for(String venta : ventas){
            String[] datos = venta.split(",");
            reporte += "Titulo: " + datos[0] + " Boletos vendidos: " + datos[1] + "Total de la venta: [$" + datos[2] + ".00]\n";
            totalBoletos += Integer.parseInt(datos[1]);
            totalDinero += Integer.parseInt(datos[2]);
        }
        reporte += "******************************************************\n";
        reporte += "\nTotal de boletos vendidos: " + totalBoletos + "\nTotal de Ingresos: [$" + totalDinero +".00]\n\n";
        reporte += "******************************************************\n";
        return reporte;
    }

    @Override
    public void cerrar() throws RemoteException {
        System.exit(0);
    }
}
