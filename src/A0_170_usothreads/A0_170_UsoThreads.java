package A0_170_usothreads;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class A0_170_UsoThreads {
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
	
	//2.- M�TODO RUN Y METEMOS EL FOR, PARA QUE PUEDAN HABER VARIAS PELOTAS SIMULTANEAS
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
			
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//System.out.println("Hilo Bloqueado. Imposible su interrupci�n"); // lo usamos con t.interrupt(); que est� en la l�nea 245
				//System.exit(0);   //detenemos la ejecuci�n.
				//PROGRAMAMOS UNA INTERRUPCI�N DEL ACTUAL HILO
				Thread.currentThread().interrupt();
			}
			
		}
		System.out.println("Estado del hilo al terminar " + Thread.currentThread().isInterrupted());
	}
	
	private Pelota pelota;
	private Component componente;
}





//Movimiento de la pelota-----------------------------------------------------------------------------------------

class Pelota{
	
	// Mueve la pelota invirtiendo posici�n si choca con l�mites
	
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
	
	//Forma de la pelota en su posici�n inicial
	
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

//L�mina que dibuja las pelotas----------------------------------------------------------------------


class LaminaPelota extends JPanel{
	
	//A�adimos pelota a la l�mina
	
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


//Marco con l�mina y botones------------------------------------------------------------------------------

class MarcoRebote extends JFrame{
	
	public MarcoRebote(){
		
		setBounds(600,300,550,350);
		
		setTitle ("Rebotes");
		
		lamina=new LaminaPelota();
		
		add(lamina, BorderLayout.CENTER);
		
		JPanel laminaBotones=new JPanel();
		
		//--------------------------------------------------------------------------
		arranca1 = new JButton("Hilo1");
		arranca1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evento) {
				comienza_el_juego(evento);
			}
		});
		laminaBotones.add(arranca1);
		
		//--------------------------------------------------------------------------
		arranca2 = new JButton("Hilo2");
		arranca2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evento) {
				comienza_el_juego(evento);
			}
		});
		laminaBotones.add(arranca2);
		
		//--------------------------------------------------------------------------
		arranca3 = new JButton("Hilo3");
		arranca3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evento) {
				comienza_el_juego(evento);
			}
		});
		laminaBotones.add(arranca3);
		
		
		
		//--------------------------------------------------------------------------
		detener1 = new JButton("Detener1");
		detener1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evento) {
				detener(evento);
			}
		});
		laminaBotones.add(detener1);
				
		//--------------------------------------------------------------------------
		detener2 = new JButton("Detener2");
		detener2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evento) {
				detener(evento);
			}
		});
		laminaBotones.add(detener2);
		
		//--------------------------------------------------------------------------
		detener3 = new JButton("Detener3");
		detener3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evento) {
				detener(evento);
			}
		});
		laminaBotones.add(detener3);
				
		//--------------------------------------------------------------------------
		
		add(laminaBotones, BorderLayout.SOUTH);
	}
	
	
	
	
	
	
	//A�ade pelota y la bota 1000 veces
	
	public void comienza_el_juego (ActionEvent e){
		
					
			Pelota pelota=new Pelota();
			
			lamina.add(pelota);
			
			//CREAMOS UNA INSTANCIA DE LA CLASE QUE IMPLEMENTA LA INTERFAZ RUNNABLE, PELOTAHILOS, Y LA
			//..ALMACENAMOS EN UNA VARIABLE DE TIPO RUNNABLE.
			Runnable r=new PelotaHilos(pelota,lamina);
			
			//VEMOS QU� BOT�N HA SIDO PULDADO
			if(e.getSource().equals(arranca1)) {
				
				//ESTE CONSTRUCTOR DE THREAD, PERMITE PASAR UN OBJ DE TIPO RUNNABLE		
				t1 = new Thread(r);
			
				//LE DECIMOS QUR COMIENCE LA TAREA
				t1.start();
			}else if(e.getSource().equals(arranca2)) {
				t2 = new Thread(r);
				t2.start();
			
			}else if(e.getSource().equals(arranca3)) {
				t3 = new Thread(r);
				t3.start();
			}
		

	}
	
	//DETIENE LA EJECUCI�N DEL HILO
	public void detener(ActionEvent e) {
		
		if(e.getSource().equals(detener1)) {
			//GENERAMOS UNA SOLICITUD DE DETENCI�N DEL HILO
			t1.interrupt(); 
		}else if(e.getSource().equals(detener2)) {
			t2.interrupt(); 
			
		}else if(e.getSource().equals(detener3)) {
			t3.interrupt(); 
		}
		  
	}
	
	Thread t1,t2,t3;
	JButton arranca1,arranca2,arranca3;
	JButton detener1,detener2,detener3;
	private LaminaPelota lamina;
	
}

