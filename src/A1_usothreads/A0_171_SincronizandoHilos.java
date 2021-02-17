package A1_usothreads;

public class A0_171_SincronizandoHilos {
	public static void main(String[]args) {
		
		HilosVarios hilo1 = new HilosVarios();
		hilo1.start();
		
		HilosVarios2 hilo2 = new HilosVarios2(hilo1);
		hilo2.start();
		
		
		//que aparezca esto al proncipio en la consola. quiere decir que se liberó el main()
		System.out.println("Terminadas las tareas");
		
		//------------------------------------------------
		/*try {
			hilo1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		//------------------------------------------------
		
		//------------------------------------------------
		/*try {
			hilo2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		//------------------------------------------------	
	}
}


class HilosVarios extends Thread{
	//SOBREESCRIBIMOS EL MÉTODO DE LA CLASE THREAD
	//ESTE MÉTODO CONTENDRÁ LA TAREA (HILO), QUE QUEREMOS QUE HAGA. 
	//GETNAME(): NOS DA EL NOMBRE DEL HILO QUE SE ESTÁ EJECUTANDO.
	//THREAD.SLEEP():PARA QUE SE EJECUTE MÁS LENTO.
	//JOIN():SINCRONIZAMOS LOS HILOS
	public void run() {
		
		for(int i=0; i<15;i++) {
			System.out.println("Ejecutando hilo " + getName());
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {	
				e.printStackTrace();
			}
		}	
	}
}


class HilosVarios2 extends Thread{
	
	//CONSTRUCTOR 
	public HilosVarios2(Thread hilo) {
		this.hilo=hilo;
	}
	
	public void run() {
		
		//LE DECIMOS QUE LA TAREA NO COMIENCE HASTA QUE EL HILO QUE LE HEMOS PASADO POR PARÁMETRO AL..
		//CONSTRUCTOR, NO HAYA TERMINADO SU TAREA.
		try {
			hilo.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for(int i=0; i<15;i++) {
			System.out.println("Ejecutando hilo " + getName());
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {	
				e.printStackTrace();
			}
		}	
	}
	private Thread hilo;
}