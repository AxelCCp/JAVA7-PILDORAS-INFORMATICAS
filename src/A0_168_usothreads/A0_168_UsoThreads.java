package A0_168_usothreads;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class A0_168_UsoThreads {
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
	//RUN() PERMITE HACER TAREAS SIMULTÁNEAS.
	public void run() {
		
		for (int i=1; i<=3000; i++){
			//while(!Thread.currentThread().isInterrupted()) {
			pelota.mueve_pelota(componente.getBounds());
			componente.paint(componente.getGraphics());
			
			try {
				//HACEMOS UNA PAUSA DURANTE LA EJECUCIÓN DEL PROGRAMA
				Thread.sleep(9);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//System.out.println("Hilo Bloqueado. Imposible su interrupción"); // lo usamos con t.interrupt(); que está en la línea 245
				//System.exit(0);   //detenemos la ejecución.
				
				//PROGRAMAMOS UNA INTERRUPCIÓN DEL ACTUAL HILO
				Thread.currentThread().interrupt();
			}
			
		}
		System.out.println("Estado del hilo al terminar " + Thread.currentThread().isInterrupted());
	}
	
	private Pelota pelota;
	private Component componente;
}


//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


//CLASE QUE SE ENCARGA DEL MOVIMIENTO DE LA PELOTA
//TAMBN  SE ENCARGA DE QUE LA PELOTA REBOTE EN LOS BORDES
class Pelota{
	
	// MÉTODO MUEVE LA PELOTA INVIRTIENDO LA  POSICIÓN SI CHOCA CON LOS LÍMITES.
	// EL PARÁMETRO RECTANGLE2D, RECIBE LOS LÍMITES DE LA LÁMINA, PAR QUE LA PELOTA REBOTE EN LOS BORDES.
	public void mueve_pelota(Rectangle2D limites){
		
		//INCREMENTAMOS LAS COORDENADAS (X,Y), PARA QUE LA PELOTA SE MUEVA.
		x+=dx;
		y+=dy;
		
		//DETECTAMOS EL PUNTO MINIMO CON GETMAXX() E INVERTIMOS LA DIRECCIÓN DE LA PELOTA.
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
			
			//3.-CREAMOS UNA INSTANCIA DE LA CLASE QUE IMPLEMENTA LA INTERFAZ RUNNABLE, PELOTAHILOS, Y LA
			//..ALMACENAMOS EN UNA VARIABLE DE TIPO RUNNABLE.
			Runnable r=new PelotaHilos(pelota,lamina);
			
			//4.-CREAMOS UNA TAREA...ESTE CONSTRUCTOR DE THREAD, PERMITE PASAR UN OBJ DE TIPO RUNNABLE	
			t = new Thread(r);
			
			//5.-LE DECIMOS QUR COMIENCE LA TAREA
			t.start();
		
		
	}
	
	public void detener() {
		//t.stop();  //XXX está obsoleto
		t.interrupt();  //este no se adapta al caso. 
	}
	
	Thread t;
	private LaminaPelota lamina;
	
	
}


