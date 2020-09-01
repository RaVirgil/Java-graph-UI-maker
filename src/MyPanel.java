import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.*;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private int nodeNr = 1;
	private int node_diam = 30;
	private int[][] adjacencyMatrix=new int[10][10];
	private Vector<Node> listaNoduri;
	private Vector<Arc> listaArce;
	private Point pointStart = null;
	private Point pointEnd = null;
	private boolean isDragging = false;

	public MyPanel()
	{
		listaNoduri = new Vector<Node>();
		listaArce = new Vector<Arc>();
		// borderul panel-ului
		setBorder(BorderFactory.createLineBorder(Color.black));
		addMouseListener(new MouseAdapter() {
			//evenimentul care se produce la apasarea mousse-ului
			public void mousePressed(MouseEvent e) {
				pointStart = e.getPoint();

			}
			//evenimentul care se produce la eliberarea mousse-ului
			public void mouseReleased(MouseEvent e) {
				if (!isDragging) {
					int mouseX = pointStart.x-14;
					int mouseY = pointStart.y-14;
					if(listaNoduri.size() == 0)
					{
						addNode(mouseX, mouseY);

					}else
					{
						boolean ok=false;
						for (Node node : listaNoduri) {

							if (Math.sqrt(Math.pow(node.getCoordX() - mouseX, 2) + Math.pow(node.getCoordY() - mouseY, 2)) < 2 * node_diam) {
								ok = true;
								break;
							}
						}
					if(!ok)
						addNode(mouseX,mouseY);
				}
					}
				else
				{if(listaNoduri.size()!=0)
				{
					boolean ok=false;
					int goodNode=0;
					for (Node node : listaNoduri) {
                            if(Math.sqrt(Math.pow(node.getCoordX() - pointStart.x, 2) + Math.pow(node.getCoordY() -pointStart.y, 2)) < node_diam)
							{goodNode=node.getNumber();
							ok =true;
                        break;
					}}if(ok)
				{for(Node node:listaNoduri)
					{if(Math.sqrt(Math.pow(node.getCoordX() - pointEnd.x, 2) + Math.pow(node.getCoordY() - pointEnd.y, 2)) <node_diam)
					{   adjacencyMatrix[goodNode-1][node.getNumber()-1]=1;
						Arc arc=new Arc(pointStart,pointEnd);
						listaArce.add(arc);
						break;

					}


				}}}}
                     pointStart=null;
				isDragging=false;
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
								   //evenimentul care se produce la drag&drop pe mousse
								   public void mouseDragged(MouseEvent e) {
									   pointEnd = e.getPoint();
									   isDragging = true;
									    repaint();
								   }
							   });
	}


	private void addNode(int x, int y) {
		Node node = new Node(x, y, nodeNr);
		listaNoduri.add(node);
		nodeNr++;
		repaint();
	} //metoda care se apeleaza la eliberarea mouse-ului

	protected void paintComponent(Graphics g) //se executa atunci cand apelam repaint()
	{
		super.paintComponent(g);//apelez metoda paintComponent din clasa de baza


		for (Arc a : listaArce) //deseneaza arcele existente in lista
		{
			a.drawArc(g);
		}

		if (pointStart != null)//deseneaza arcul curent; cel care e in curs de desenare
		{
			g.setColor(Color.RED);
			g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);


		}

		for (int i = 0; i < listaNoduri.size(); i++) //deseneaza lista de noduri & matricea de adiacenta
		{
			listaNoduri.elementAt(i).drawNode(g, node_diam);
		}
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter("output.txt"));
			output.write((nodeNr-1)+" ");
			for (int i = 0; i < adjacencyMatrix.length; i++) {
				output.newLine();
			for (int j = 0; j < adjacencyMatrix.length; j++)
				output.write(adjacencyMatrix[i][j] + " ");
		}

			output.flush();
		} catch (IOException e) {
		}
	}}
