package A2_usothreads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class A0_175_BancoConSincronizar {

	public static void main(String[]args) {
		
		
		Banco b = new Banco();
		//CREAMOS BUCLE FOR PARA QUE HAGA TRANFERENCIAS DESDE LA CUENTA N� 0 A LA 99
		for(int i=0;i<100;i++) {
			//INSTANCIAMOS LA CLASE QUE TIENE LA INTERFAZ RUNNABLE Y ARRANCAMOS EL HILO.
			//(BANCO=b,,,i=N�CTAS QUE RECORRE EL FOR,,,2000=CANTIDAD MAX A TRANSFERIR)
			EjecucionTranferencias r = new EjecucionTranferencias(b, i, 2000);
			
			//INSTANCIAMOS THREAD Y EN EL CONSTRUCTOR DE ESTA CLASE LE PASAMOS "r"
			Thread t = new Thread(r);
			
			//ARRANCAMOS EL HILO
			t.start();
		}
		
	}
	
}


//ESTA CLASE DEBE ALMACENAR 100 CUENTAS BANCARIAS Y CADA CUENTA DEBE TENER 2000 EUROS
class Banco{
	
	//CONSTRUCTOR
	public Banco() {	
		//INICIALIZAMOS EL ARRAY Y LE DAMOS 100.
		cuentas = new double [100];
		
		//CARGAMOS LAS 100 CUENTAS CON 2000EUROS
		for(int i=0;i<cuentas.length;i++) {
			cuentas[i]=2000;
		}
	}
	
	
	//M�TODO QUE SE ENCARGA DE HACER LAS TRANSFERENCIAS
	//CU�L ES LA CUENTA DE ORIGEN.
	//CU�L ES LA CUENTA DE DESTINO.
	//CU�L ES A CANTIDAD DE DINERO A TRANFERIR.
	public void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) {
		
		//SINCRONIZACI�N:
		//APLICAMOS SEM�FORO + try/finally{}
		//CON ESTO SOLO PUEDE PASAR UN HILO A LA VEZ A TRAV�S DEL C�DIGO, HASTA EL UNLOCK.
		cierreBanco.lock();
		try {
		
		//PARA QUE EL DINERO DE LA CUENTADEORIGEN NO SUPERE A LA CANTIDAD A TRANSFERIR,
		//..ESCRIBIMOS ESTE IF. SI LA CONDICI�N SE CUMPLE, NO HACE ABSOLUTAMENTE NADA.
		if(cuentas[cuentaOrigen]<cantidad) {
			return; 
		}
		//IMPRIMIMOS EL HILO DE LA CUENTA QUE VA A HACER LA TRANSFERENCIA.
		System.out.println(Thread.currentThread());
		
		//DESCONTAMOS EL DINERO TRANSFERIDO A LA CUENTA QUE HIZO LA TRANSFERENCIA
		cuentas[cuentaOrigen]-=cantidad;
		
		//QUE NOS IMPRIMA UNA CANTIDAD CON 2 DECIMALES
		//%d NOS INFORMA LA CANTIDAD, Y LA CUENTA ORIGEN Y DESTINO 
		System.out.printf("%10.2f de %d para %d ", cantidad, cuentaOrigen, 	cuentaDestino);
	
		//INCREMENTAMOS LA CANTIDAD QUE SALIO DE LA CUENTA DE ORIGEN, HACIA LA CUENTA DE DESTINO.
		cuentas[cuentaDestino]+=cantidad;
		
		System.out.printf("Saldo total: %10.2f%n ", getSaldoTotal());
		
		}finally {
			//QUITAMOS EL SEM�FORO
			cierreBanco.unlock();
		}
	}
	
	
	//M�TODO QUE D� EL SALDO TOTAL DE TODAS LAS CUENTAS
	public double getSaldoTotal() {
		double sumaCuentas=0;
		for(double a : cuentas) {
			sumaCuentas+=a;
		}
		return sumaCuentas;
	}
	
	
	
	//DECLARAMOS UN CAMPO DE CLASE ARRAY PARA ALMACENAR CUENTAS
	private final double[]cuentas;
	
	
	//SINCRONIZACI�N DE HILOS 
	//CREAMOS UNA INSTANCIA PERTENECIENTE A LA CLASE RENTRANTLOCK..
	//..ESTE SER�A UN CAMPO ENCAPSULADO DE LA INTERFAZ LOCK.
	//CON ESTE NOMBRE DE INSTANCIA, PODEMOS LLAMAR AL M�TODO LOCK() Y UNLOCK(),.. 
	//..PARA PONER SEM�FORO A LOS HILOS EN EL CODIGO.
	private Lock cierreBanco = new ReentrantLock();
}



class EjecucionTranferencias implements Runnable{

	//CONTRUCTOR...ESTE DEBE PERMITIR ALMACENAR 3 DATOS(OBJ BANCO,CUENTAORIGEN,CANTIDAD A TRANSFERIR)
	public EjecucionTranferencias(Banco b, int de, double max) {
		
		//BANCO SER� IGUAL A LO QUE LE PASEMOS POR PAR�METRO AL COSNTRUCTOR.
		banco=b;
		deLaCuenta=de;
		cantidadMax=max;
	}
	
	public void run() {
	
		//CREAMOS UN BUCLE INFINITO
		while(true) {
			//CREAMOS VARIABLE DESTINO ----> PARALACUENTA
			//HACEMOS QUE LA CUENTA DE DESTINO SEA ALEATORIA.
			int paraLaCuenta=(int)(100*Math.random());
			
			//LAS CANTIDADES TAMBN ER�N DIFERENTES, POR LO TANTO USAMOS RANDOM();
			double cantidad=cantidadMax*Math.random(); 
			
			//LLAMAMOS AL M�TODO TRANSFERENCIA CON EL OBJ banco DE LA CLASE Banco.
			banco.transferencia(deLaCuenta, paraLaCuenta, cantidad);
			
			//DORMIMOS LOS HILOS PARA ALCANZAR A VER LA INFORMACI�N  A TRAV�S DE LA  CONSSOLA
			//EL TIEMPO EN EL M�TODO SLEEP() ES ALEATORIO
			try {
				Thread.sleep((int)Math.random()*10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
	
	private Banco banco;
	private int deLaCuenta;
	private double cantidadMax;
}