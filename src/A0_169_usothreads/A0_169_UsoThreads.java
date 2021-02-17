package A0_169_usothreads;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


public class A0_169_UsoThreads {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame marco=new MarcoRebote();
		
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		marco.setVisible(true);
	}
}


//1.- CLASE QUE IMPLEMENTA LA INTERFAZ RUNABLE-------------------------------------------------------
class PelotaHilos implements Runnable{
	
	public PelotaHilos(Pelota unaPelota, Component unComponente) {
		pelota=unaPelota;
		componente=unComponente;
	}
	
	//2.- MÉTODO RUN Y METEMOS EL FOR, PARA QUE PUEDAN HABER VARIAS PELOTAS SIMULTANEAS
	public void run() {
			
			System.out.println("Estado del hilo al comenzar: " + Thread.currentThread().isInterrupted());
		
			//QUIERE DECIR: MIENTRAS NO HAYA SIDO INTERRUMPIDO EL HILO, HACE LO DEL BLOQUE INDEFINIDAMENTE.
			//while(!Thread.interrupted()) {
			
			//interrupted() y isInterrupted(), 
			//son parecidos, solo que interrupted es static, mientras que isInterrupted(),no. pertenece a una instancia en concreto.
			//con currentThread() le decimos la referencia del hilo que hay que detener.
		while(!Thread.currentThread().isInterrupted()) {
				
				pelota.mueve_pelota(componente.getBounds());	
				componente.paint(componente.getGraphics());
			
			/*try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//System.out.println("Hilo Bloqueado. Imposible su interrupción"); // lo usamos con t.interrupt(); que está en la línea 245
				//System.exit(0);   //detenemos la ejecución.
				//PROGRAMAMOS UNA INTERRUPCIÓN DEL ACTUAL HILO
				Thread.currentThread().interrupt();
			}*/
			
		}
		System.out.println("Estado del hilo al terminar " + Thread.currentThread().isInterrupted());
	}
	
	private Pelota pelota;
	private Component componente;
}





//Movimiento de la pelota-----------------------------------------------------------------------------------------

class Pelota{
	
	// Mueve la pelota invirtiendo posición si choca con límites
	
	public void mueve_pelota(Rectangle2D limites){
		
		x+=dx;
		
		y+=dy;
		
		if(x<limites.getMinX()){
			
			x=limites.getMinX();
			
			dx=-dx;
		}
		
		if(x + TAMX>=limites.getMaxX()){
			
			x=limites.getMaxX() - TAMX;
			
			dx=-dx;
		}
		
		if(y<limites.getMinY()){
			
			y=limites.getMinY();
			
			dy=-dy;
		}
		
		if(y + TAMY>=limites.getMaxY()){
			
			y=limites.getMaxY()-TAMY;
			
			dy=-dy;
			
		}
		
	}
	
	//Forma de la pelota en su posición inicial
	
	public Ellipse2D getShape(){
		
		return new Ellipse2D.Double(x,y,TAMX,TAMY);
		
	}	
	
	private static final int TAMX=15;
	
	private static final int TAMY=15;
	
	private double x=0;
	
	private double y=0;
	
	private double dx=1;
	
	private double dy=1;
	
	
}

//Lámina que dibuja las pelotas----------------------------------------------------------------------


class LaminaPelota extends JPanel{
	
	//Añadimos pelota a la lámina
	
	public void add(Pelota b){
		
		pelotas.add(b);
	}
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D)g;
		
		for(Pelota b: pelotas){
			
			g2.fill(b.getShape());
		}
		
	}
	
	private ArrayList<Pelota> pelotas=new ArrayList<Pelota>();
}


//Marco con lámina y botones------------------------------------------------------------------------------

class MarcoRebote extends JFrame{
	
	public MarcoRebote(){
		
		setBounds(600,300,400,350);
		
		setTitle ("Rebotes");
		
		lamina=new LaminaPelota();
		
		add(lamina, BorderLayout.CENTER);
		
		JPanel laminaBotones=new JPanel();
		
		ponerBoton(laminaBotones, "Dale!", new ActionListener(){
			
			public void actionPerformed(ActionEvent evento){
				
				comienza_el_juego();
			}
			
		});
		
		
		ponerBoton(laminaBotones, "Salir", new ActionListener(){
			
			public void actionPerformed(ActionEvent evento){
				
				System.exit(0);
				
			}
			
		});
		
		//DIBUJA EL BOTÓN DE DETENER
		ponerBoton(laminaBotones, "Detener", new ActionListener(){
			
			public void actionPerformed(ActionEvent evento){
				//LLAMAMOS A UN METODO DETENER, CREADO AL FINAL
				detener();
				
			}
			
		});
		
		add(laminaBotones, BorderLayout.SOUTH);
	}
	
	
	//Ponemos botones
	
	public void ponerBoton(Container c, String titulo, ActionListener oyente){
		
		JButton boton=new JButton(titulo);
		
		c.add(boton);
		
		boton.addActionListener(oyente);
		
	}
	
	//Añade pelota y la bota 1000 veces
	
	public void comienza_el_juego (){
		
					
			Pelota pelota=new Pelota();
			
			lamina.add(pelota);
			
			//CREAMOS UNA INSTANCIA DE LA CLASE QUE IMPLEMENTA LA INTERFAZ RUNNABLE, PELOTAHILOS, Y LA
			//..ALMACENAMOS EN UNA VARIABLE DE TIPO RUNNABLE.
			Runnable r=new PelotaHilos(pelota,lamina);
			
			//ESTE CONSTRUCTOR DE THREAD, PERMITE PASAR UN OBJ DE TIPO RUNNABLE
			//Thread t = new Thread(r);
			
			t = new Thread(r);
			
			//LE DECIMOS QUR COMIENCE LA TAREA
			t.start();
		
		
	}
	
	//DETIENE LA EJECUCIÓN DEL HILO
	public void detener() {
		//t.stop();  //XXX está obsoleto
		//GENERAMOS UNA SOLICITUD DE DETENCIÓN DEL HILO
		t.interrupt();  //este no se adapta al caso. 
	}
	
	Thread t;
	private LaminaPelota lamina;
	
}
