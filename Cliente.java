import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.util.Scanner;


public class Cliente {
    private static Interface servidorOBJ;
    private static String serverAddress;
    private static int serverPort,opc;

    public Cliente(){
        serverAddress  = "localhost";
        serverPort = 5000;
    } 
    
    private static void menuInicio() {
        System.out.println("\nMULTI CINEMA:");
        System.out.println("0)Cerrar Cine");
        System.out.println("1)Agregar pelicula");
        System.out.println("2)Asientos");
        System.out.println("3)Venta de voletos");   
    }
    private static void menuGeneros(){
        System.out.println("\nGenero:");
        System.out.println("1)Accion");
        System.out.println("2)Terror");
        System.out.println("3)Suspenso");
        System.out.println("4)Animadas");
        System.out.println("5)Fantasia");
        System.out.println("6)CIFI");
        System.out.println("7)Otro...");
    }
    private static void menuClasif(){
        System.out.println("\nClasificacion:");
        System.out.println("1)AA : Infantiles");
        System.out.println("2)A  : Publico en general");
        System.out.println("3)B  : Mayores de 12 años");
        System.out.println("4)B15: Mayores de 15 años");
        System.out.println("5)C  : Adultos de 18 años en adelante");
        System.out.println("6)D  : +18 escenas sexuales o violencia explicitas");
    }
    
    public static void main(String[] args) throws RemoteException, InterruptedException {
        Cliente cliente = new Cliente();
        
        String nombre;
        String genero="";
        String duracion;
        String clasificacion="";
        String horario;
        int asientos;
        
        try{
            Registry registry = LocateRegistry.getRegistry(serverAddress,serverPort);
            servidorOBJ = (rmi_cinema.Interface) (registry.lookup("operacionservidor"));

            do{
                menuInicio();
                opc = new Scanner(System.in).nextInt();
                switch(opc){
                    case 0:
                        System.out.println("Cerrando, Multi Cinema...");
                        System.out.println("Generando reporte de ventas...");
                        System.out.println(servidorOBJ.reporteVentas());
                        servidorOBJ.cerrar();
                        opc = -1;
                        break;
                    case 1:
                        System.out.print("Nombre:");
                        nombre = new Scanner(System.in).nextLine();
                        menuGeneros();
                        opc = new Scanner(System.in).nextInt();
                        switch(opc){
                            case 1:
                                genero = "Accion";
                                break;
                            case 2:
                                genero = "Terror";
                                break;
                            case 3:
                                genero = "Suspenso";
                                break;
                            case 4:
                                genero = "Animadas";
                                break;
                            case 5:
                                genero = "Fantasia";
                            case 6:
                                genero = "CIFI";
                                break;
                            case 7:
                                genero = "Otro...";
                                break;
                        }
                        System.out.print("Duracion (hh:mm):");
                        duracion = new Scanner(System.in).nextLine();
                        menuClasif();
                        opc = new Scanner(System.in).nextInt();
                        switch(opc){
                            case 1:
                                clasificacion = "AA";
                                break;
                            case 2:
                                clasificacion = "A";
                                break;
                            case 3:
                                clasificacion = "B";
                                break;
                            case 4:
                                clasificacion = "B15";
                                break;
                            case 5:
                                clasificacion = "C";
                                break;
                            case 6:
                                clasificacion = "D";
                                break;
                        }
                        System.out.print("Horario (dd/MM/aa):");
                        horario = new Scanner(System.in).nextLine();
                        System.out.print("Asientos (max 30):");
                        asientos = new Scanner(System.in).nextInt();

                        servidorOBJ.agrgarPelicula(nombre,genero,duracion,clasificacion,horario,asientos);

                        System.out.println("Agregada");  
                        break;
                    case 2:
                        String[] request = servidorOBJ.getBoletos().split(";");
                        for (int i = 0; i < request.length; i++) {
                            System.out.println(i+")"+ request[i]);
                        }
                        break;
                    case 3:
                        System.out.println("\nElija la pelicula");
                        String[] request2 = servidorOBJ.getNombrePeliculas().split(",");
                        for (int i = 0; i < request2.length; i++) {
                            System.out.println(i + ") "+request2[i]);
                        }
                        int opc2 = new Scanner(System.in).nextInt();
                        System.out.println("Asientos libres: " + servidorOBJ.getDisponibles(opc2) + " de " + servidorOBJ.getTotal(opc2));
                        System.out.print("Comprar # asientos: ");
                        int nAsientos = new Scanner(System.in).nextInt();
                        if(nAsientos <= servidorOBJ.getDisponibles(opc2)){
                            System.out.println(servidorOBJ.ticket(opc2,nAsientos));
                        }
                        else{
                            System.out.println("La cantidad de boletos excede los asientos disponibles\n");
                        }
                        break;
                }
            }while(opc != -1);

        }catch(NotBoundException e ){
            e.printStackTrace();
        }
    }
    
}
