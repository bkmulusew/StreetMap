/*
 * Name: Beakal Lemeneh
 * Net ID: 31390484
 * Project Number: 03
 * I did not collaborate with anyone on this assignment.
*/

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.JFrame;

//Data structure to read intersections from a text file.
class Vertex{
	String vertexID;
	double latitude;
	double longitude;
}

//Data structure to store edges from a text file.
class EdgeGraph{
	int source;
	int destination;
	double weight;
}

//Data structure to read edges from a text file.
class EdgeMap{
	String roadID;
	String source;
	String destination;
}

//Data structure to store graph edges
class Edge{
	int source, dest;
	double weight;

	public Edge(int source, int dest, double weight) {
		this.source = source;
		this.dest = dest;
		this.weight = weight;
	}
};

// Data structure to store heap nodes
class Node{
	int vertex;
	double weight;

	public Node(int vertex, double weight) {
		this.vertex = vertex;
		this.weight = weight;
	}
};

// class to represent a graph object
class Graph{
	// A List of Lists to represent an adjacency list
	List<List<Edge>> adjList = null;

	// Constructor
	Graph(List<Edge> edges, int N)
	{
		adjList = new ArrayList<>(N);

		for (int i = 0; i < N; i++) {
			adjList.add(i, new ArrayList<>());
		}

		// add edges to the undirected graph
		for (Edge edge: edges) {
			adjList.get(edge.source).add(edge);
		}
	}
}

// Draws the map and outlines the route with the shortest distance.
@SuppressWarnings("serial")
class DrawingLines extends JComponent{
	
	// Stores the x and y components to draw the map.
	private static ArrayList<Double> mapX1 = new ArrayList<Double>();
	private static ArrayList<Double> mapY1 = new ArrayList<Double>();
	private static ArrayList<Double> mapX2 = new ArrayList<Double>();
	private static ArrayList<Double> mapY2 = new ArrayList<Double>();
	
	// Stores the x and y components to show the direction of the shortest route.
	private static ArrayList<Double> routeX = new ArrayList<Double>();
	private static ArrayList<Double> routeY = new ArrayList<Double>();
	
	// Takes and stores the coordinates to draw the original map.
	public DrawingLines(ArrayList<Double> X1, ArrayList<Double> Y1, ArrayList<Double> X2, ArrayList<Double> Y2){
		
		for(int i = 0; i < X1.size(); i++) {
			mapX1.add(X1.get(i));
			mapY1.add(Y1.get(i));
			mapX2.add(X2.get(i));
			mapY2.add(Y2.get(i));
		}
	}
	
	// Takes and stores the coordinates to draw the original map along with the coordinates used to draw the route of the shortest distance.
	public DrawingLines(ArrayList<Double> X1, ArrayList<Double> Y1, ArrayList<Double> X2, ArrayList<Double> Y2, ArrayList<Double> directionX, ArrayList<Double> directionY){
		
		for(int i = 0; i < X1.size(); i++) {
			mapX1.add(X1.get(i));
			mapY1.add(Y1.get(i));
			mapX2.add(X2.get(i));
			mapY2.add(Y2.get(i));
		}
		
		for(int i = 0; i < directionX.size(); i++) {
			routeX.add(directionX.get(i));
			routeY.add(directionY.get(i));
		}
	}
	
	// Draws the map using Java Graphics.
	public void paint(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		for(int i = 0; i < mapX1.size(); i++) {
			g2.draw(new Line2D.Double(mapX1.get(i) * (getWidth() - 10), getHeight() - (mapY1.get(i) * (getHeight() - 10)), mapX2.get(i) * (getWidth() - 10), getHeight() - (mapY2.get(i) * (getHeight() - 10))));
		}
		
		// Red thick line used to show the shortest path between two points.
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(4f));
		for(int i = 0; i < routeX.size() - 2; i++) {
			g2.draw(new Line2D.Double(routeX.get(i) * (getWidth() - 10), getHeight() - (routeY.get(i) * (getHeight() - 10)), routeX.get(i + 1) * (getWidth() - 10), getHeight() - (routeY.get(i + 1) * (getHeight() - 10))));
		}
	}
}

// Main method
public class StreetMap {
	
	static final double radEarth = 6371; // radius of the earth in km.
	static ArrayList<Integer> finalPath = new ArrayList<Integer>(); // stores the path with the shortest distance. 
	
	// figures out the path with the shortest distance and stores it in the finalPath array list once Dijkstra's algorithm is run.
	private static void finalRoute(int prev[], int i){
		if (i < 0)
			return;

		finalRoute(prev, prev[i]);
		finalPath.add(i);
	}

	// Run Dijkstra's algorithm on given graph
	public static void shortestPath(Graph graph, int source, int destination, int N)
	{
		// create min heap and push source node having distance 0
		PriorityQueue<Node> minHeap = new PriorityQueue<>(
								(lhs, rhs) -> (int)(lhs.weight - rhs.weight));

		minHeap.add(new Node(source, 0));

		// set infinite distance from source to v initially
		List<Double> dist = new ArrayList<>(
					Collections.nCopies(N, Double.MAX_VALUE));

		// distance from source to itself is zero
		dist.set(source, 0.0);

		// boolean array to track vertices for which minimum
		// cost is already found
		boolean[] done = new boolean[N];
		done[0] = true;

		// stores predecessor of a vertex (to print path)
		int prev[] = new int[N];
		prev[0] = -1;

		// run till minHeap is not empty
		while (!minHeap.isEmpty())
		{
			// Remove and return best vertex
			Node node = minHeap.poll();

			// get vertex number
			int u = node.vertex;

			// do for each neighbor v of u
			for (Edge edge: graph.adjList.get(u))
			{
				int v = edge.dest;
				double weight = edge.weight;

				// Relaxation step
				if (!done[v] && (dist.get(u) + weight) < dist.get(v))
				{
					dist.set(v, dist.get(u) + weight);
					prev[v] = u;
					minHeap.add(new Node(v, dist.get(v)));
				}
			}

			// marked vertex u as done so it will not get picked up again
			done[u] = true;
		}
		finalRoute(prev, destination);
		
		// Some adjustments made to the code.
		// Explained in the README.txt file.
		if(finalPath.contains(source) && finalPath.contains(destination)) {
			System.out.println("The distance of the path is: " + dist.get(destination) / 54 + " km."); // Prints the shortest distance in kms.
		}
		
		if(source == 0 && destination == 0) {
			return;
		}
		else if(source != 0 && destination != 0) {
			finalPath.remove(0);
			return;
		}
		else if(destination == 0) {
			shortestPath(graph, destination, source, N);
			Collections.reverse(finalPath);
			finalPath.remove(finalPath.size() - 1);
			return;
		}
	}
	
	// Driver Code
	public static void main(String[] args) throws IOException {
		
		List<Edge> edges = new ArrayList<Edge>(); // Adjacency list.
		
		// Stores all the information related to the intersections.
		ArrayList<Vertex> vertex = new ArrayList<Vertex>();
		ArrayList<EdgeMap> edge = new ArrayList<EdgeMap>();
		ArrayList<String> vertexId = new ArrayList<String>();
		
		// Stores the longitude and latitude of the intersections.
		ArrayList<Double> vertexLat = new ArrayList<Double>();
		ArrayList<Double> vertexLong = new ArrayList<Double>();
		
		// Stores the x and y components of all edges.
		// Used to draw the map.
		ArrayList<Double> sourceX = new ArrayList<Double>();
		ArrayList<Double> sourceY = new ArrayList<Double>();
		ArrayList<Double> destX = new ArrayList<Double>();
		ArrayList<Double> destY = new ArrayList<Double>();
		
		// Stores the x and y components of the shortest path.
		// Used to draw the line representing the route.
		ArrayList<Double> shortPathX = new ArrayList<Double>();
		ArrayList<Double> shortPathY = new ArrayList<Double>();
		
		// File imported.
		File text = new File(args[0]);
		
		try{
			@SuppressWarnings("resource")
			
			Scanner scanner = new Scanner(text);
			
			// Starts reading the text in the file.
			while (scanner.hasNextLine()){
				String textLine = scanner.nextLine();
				String[] commandArr = textLine.split("\t"); // Changes the text on every line to an array form.
				
				// reads and stores all the intersections as vertices.
				if(commandArr[0].equals("i")) {
					Vertex ver = new Vertex();
					ver.vertexID = commandArr[1];
					ver.latitude = Double.valueOf(commandArr[2]);
					ver.longitude = Double.valueOf(commandArr[3]);
					vertex.add(ver);
					vertexId.add(commandArr[1]);
					
					vertexLat.add(Double.valueOf(commandArr[2]));
					vertexLong.add(Double.valueOf(commandArr[3]));
				}
				
				// reads and stores all the roads as edges.
				if(commandArr[0].equals("r")) {
					EdgeMap e = new EdgeMap();
					e.roadID = commandArr[1];
					e.source = commandArr[2];
					e.destination = commandArr[3];
					edge.add(e);
				}
			}
		}
		
		// Throws an error message if the file imported has no text in it.
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		// Coordinate manipulation used to fit the map into the screen of the computer.
		// Explained in the README.txt file.
		Double minLat = Collections.min(vertexLat);
		Double minLong = Collections.min(vertexLong);
		
		for(int i = 0; i < vertex.size(); i++) {
			vertexLat.set(i, vertexLat.get(i) - minLat);
			vertexLong.set(i, vertexLong.get(i) - minLong);
		}
		
		Double maxLat = Collections.max(vertexLat);
		Double maxLong = Collections.max(vertexLong);
		
		for(int i = 0; i < vertex.size(); i++) {
			vertexLat.set(i, vertexLat.get(i) / maxLat);
			vertexLong.set(i, vertexLong.get(i) / maxLong);
		}
		
		for(int i = 0; i < edge.size(); i++) {
			int j = vertexId.indexOf(edge.get(i).source);
			int k = vertexId.indexOf(edge.get(i).destination);
			sourceX.add(vertexLong.get(j));
			sourceY.add(vertexLat.get(j));
			destX.add(vertexLong.get(k));
			destY.add(vertexLat.get(k));
		}
		
		for(int i = 0; i < edge.size(); i++) {
			int index1 = vertexId.indexOf(edge.get(i).source);
			int index2 = vertexId.indexOf(edge.get(i).destination);
			double lat1 = vertex.get(index1).latitude;
			double long1 = vertex.get(index1).longitude;
			double lat2 = vertex.get(index2).latitude;
			double long2 = vertex.get(index2).longitude;
			
			// Haversine formula.
			// Calculates the distance between two points.
			// Used as a weight for edges.
			double distance = 2 * radEarth * Math.asin(Math.sqrt((Math.sin((lat2 - lat1) / 2) * Math.sin((lat2 - lat1) / 2)) + Math.cos(lat1) * Math.cos(lat2) * (Math.sin((long2 - long1) / 2) * Math.sin((long2 - long1) / 2))));
			
			// Stored as an adjacency list.
			// Undirected
			edges.add(new Edge(index1, index2, distance));
			edges.add(new Edge(index2, index1, distance));
			
		}
		
		final int N = vertex.size(); // Total number of intersections.

		if(args[1].equals("--show")) {
			
			// Displays the map.
			if(args.length == 2) {
				JFrame draw1 = new JFrame("Street Map");
				draw1.setSize(500, 500);
				DrawingLines drawPic = new DrawingLines(sourceX, sourceY, destX, destY);
				
				draw1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				draw1.add(drawPic);
				draw1.pack();
				draw1.setVisible(true);
			}
			
			// Displays the map along with the path that needs to be taken to travel the shortest distance.
			else if(args[2].equals("--directions")) {
				
				// construct graph
				Graph graph = new Graph(edges, N);
				
				int startIntersection = vertexId.indexOf(args[3]); // Source
				int endIntersection = vertexId.indexOf(args[4]); // Destination

				shortestPath(graph, startIntersection, endIntersection, N); // Runs the Dijkstra's algorithm to find the shortest path.
				
				for(int i = 0; i < finalPath.size(); i++) {
					shortPathX.add(vertexLong.get(finalPath.get(i)));
					shortPathY.add(vertexLat.get(finalPath.get(i)));
				}
				
				// Prints the route on the console if the source and destination are connected.
				if(finalPath.contains(startIntersection) && finalPath.contains(endIntersection)) {
					
					System.out.print("Route: ");
					for(int i = 0; i < finalPath.size() - 1; i++) {
						System.out.print(vertexId.get(finalPath.get(i)) + "->");
					}
					System.out.println(vertexId.get(finalPath.get(finalPath.size() - 1)));
				}
				
				// Notifies the user that the source and the destination are not connected if they are not connected.
				else
					System.out.print(vertexId.get(startIntersection) + " and " + vertexId.get(endIntersection) + " are not connected.");
				
				
				JFrame draw1 = new JFrame("Street Map");
				draw1.setSize(500, 500);
				DrawingLines drawPic = new DrawingLines(sourceX, sourceY, destX, destY, shortPathX, shortPathY);
				draw1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				draw1.add(drawPic);
				draw1.pack();
				draw1.setVisible(true);
			}
		}
		
		else if(args[1].equals("--directions")) {
			
			if(args.length == 4) {
				
				// construct graph
				Graph graph = new Graph(edges, N);
				
				int startIntersection = vertexId.indexOf(args[2]); // Source
				int endIntersection = vertexId.indexOf(args[3]); // Destination

				shortestPath(graph, startIntersection, endIntersection, N); // Runs the Dijkstra's algorithm to find the shortest path.
				
				for(int i = 0; i < finalPath.size(); i++) {
					shortPathX.add(vertexLong.get(finalPath.get(i)));
					shortPathY.add(vertexLat.get(finalPath.get(i)));
				}
				
				// Prints the route on the console if the source and destination are connected.
				if(finalPath.contains(startIntersection) && finalPath.contains(endIntersection)) {
					System.out.print("Route: ");
					for(int i = 0; i < finalPath.size() - 1; i++) {
						System.out.print(vertexId.get(finalPath.get(i)) + "->");
					}
					System.out.println(vertexId.get(finalPath.get(finalPath.size() - 1)));
				}
				
				// Notifies the user that the source and the destination are not connected if they are not connected.
				else
					System.out.println(vertexId.get(startIntersection) + " and " + vertexId.get(endIntersection) + " are not connected.");
			}
			
			// Displays the map along with the path that needs to be taken to travel the shortest distance.
			else if(args[2].equals("--show")) {
				// construct graph
				Graph graph = new Graph(edges, N);
				
				int startIntersection = vertexId.indexOf(args[3]); // Source 
				int endIntersection = vertexId.indexOf(args[4]); // Destination

				shortestPath(graph, startIntersection, endIntersection, N); // Runs the Dijkstra's algorithm to find the shortest path.
				
				for(int i = 0; i < finalPath.size(); i++) {
					shortPathX.add(vertexLong.get(finalPath.get(i)));
					shortPathY.add(vertexLat.get(finalPath.get(i)));
				}
				
				// Prints the route on the console if the source and destination are connected.
				if(finalPath.contains(startIntersection) && finalPath.contains(endIntersection)) {
					System.out.print("Route: ");
					for(int i = 0; i < finalPath.size() - 1; i++) {
						System.out.print(vertexId.get(finalPath.get(i)) + "->");
					}
					System.out.println(vertexId.get(finalPath.get(finalPath.size() - 1)));
				}
				
				// Notifies the user that the source and the destination are not connected if they are not connected.
				else
					System.out.println(vertexId.get(startIntersection) + " and " + vertexId.get(endIntersection) + " are not connected.");
				
				
				JFrame draw1 = new JFrame("Street Map");
				draw1.setSize(500, 500);
				DrawingLines drawPic = new DrawingLines(sourceX, sourceY, destX, destY, shortPathX, shortPathY);
				
				draw1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				draw1.add(drawPic);
				draw1.pack();
				draw1.setVisible(true);
			}
		}
	}
}
