package A5_usothreads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class A0_178_BancoHilosCnSynchronized {

	public static void main(String[]args) {	
		
		Banco b = new Banco();
		//CREAMOS BUCLE FOR PARA QUE HAGA TRANFERENCIAS DESDE LA CUENTA N° 0 A LA 99
		for(int i=0;i<100;i++) {
			//INSTANCIAMOS LA CLASE QUE TIENE LA INTERFAZ RUNNABLE Y ARRANCAMOS EL HILO.
			//(BANCO=b,,,i=N°CTAS QUE RECORRE EL FOR,,,2000=CANTIDAD MAX A TRANSFERIR)
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
	
	
	//MÉTODO QUE SE ENCARGA DE HACER LAS TRANSFERENCIAS
	//CUÁL ES LA CUENTA DE ORIGEN.
	//CUÁL ES LA CUENTA DE DESTINO.
	//CUÁL ES A CANTIDAD DE DINERO A TRANFERIR.
	//APLICAMOS SYNCHRONIZED
	public synchronized void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) throws InterruptedException {
		
		
		
		//PARA QUE EL DINERO DE LA CUENTADEORIGEN NO SUPERE A LA CANTIDAD A TRANSFERIR,
		//CAMBIAMOS EL IF POR UN WHILE, PARA QUE EL BUCLE SEA INDEFINIDO, MIENTRAS LA CONDICIÓN SEA TRUE.
			while(cuentas[cuentaOrigen]<cantidad) {
				
				//PONEMOS LOS HILOS A LA ESPERA CON ESTE MET DE LA CLASE OBJECT.
				wait();	
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
		
		
		//PONEMOS LOS HILOS A LA ESCUCHA CON ESTE MET DE LA CLASE OBJECT..
		//LOS HILOS EN ESPERA, VERIFICAN SI YA NO SE CUMPLE LA CONDICIÓN DEL WHILE..
		//..(cuentas[cuentaOrigen]<cantidad)
		notifyAll();
		
	}
	
	
	//MÉTODO QUE DÉ EL SALDO TOTAL DE TODAS LAS CUENTAS
	public double getSaldoTotal() {
		double sumaCuentas=0;
		for(double a : cuentas) {
			sumaCuentas+=a;
		}
		return sumaCuentas;
	}
	
	
	
	//DECLARAMOS UN CAMPO DE CLASE ARRAY PARA ALMACENAR CUENTAS
	private final double[]cuentas;
		
}



class EjecucionTranferencias implements Runnable{

	//CONTRUCTOR...ESTE DEBE PERMITIR ALMACENAR 3 DATOS(OBJ BANCO,CUENTAORIGEN,CANTIDAD A TRANSFERIR)
	public EjecucionTranferencias(Banco b, int de, double max) {
		
		//BANCO SERÁ IGUAL A LO QUE LE PASEMOS POR PARÁMETRO AL COSNTRUCTOR.
		banco=b;
		deLaCuenta=de;
		cantidadMax=max;
	}
	
	public void run() {
		try {
			//CREAMOS UN BUCLE INFINITO
			while(true) {
				//CREAMOS VARIABLE DESTINO ----> PARALACUENTA
				//HACEMOS QUE LA CUENTA DE DESTINO SEA ALEATORIA.
				int paraLaCuenta=(int)(100*Math.random());
			
				//LAS CANTIDADES TAMBN ERÁN DIFERENTES, POR LO TANTO USAMOS RANDOM();
				double cantidad=cantidadMax*Math.random(); 
			
				//LLAMAMOS AL MÉTODO TRANSFERENCIA CON EL OBJ banco DE LA CLASE Banco.
				banco.transferencia(deLaCuenta, paraLaCuenta, cantidad);
			
				//DORMIMOS LOS HILOS PARA ALCANZAR A VER LA INFORMACIÓN  A TRAVÉS DE LA  CONSSOLA
				//EL TIEMPO EN EL MÉTODO SLEEP() ES ALEATORIO
				Thread.sleep((int)Math.random()*10);	
			} 
			
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	private Banco banco;
	private int deLaCuenta;
	private double cantidadMax;
}


