package Objects;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class GrapheCarte {
	private Graph graphe;
	@SuppressWarnings("unused")
	private Viewer viewer = null;
	
	public GrapheCarte(int taille,boolean type){
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		// graphe propre a l'agent:
		// creation petit a petit de la mise en place de la carte
		this.graphe = new SingleGraph("Ma Carte");//TestGraphS.generateGraph(type, taille);
		this.viewer = this.graphe.display();
		
		this.initStyleSheet();
	}
	
	private void initStyleSheet(){
		String defaultNodeStyle= "node {"+"fill-color: black;"+" size-mode:fit;text-alignment:under; text-size:14;text-color:white;text-background-mode:rounded-box;text-background-color:black;}";
		String nodeStyle_wumpus= "node.wumpus {"+"fill-color: red;"+"}";
		String nodeStyle_agent= "node.agent {"+"fill-color: blue;"+"}";
		String nodeStyle_visite= "node.visite {"+"fill-color: yellow;"+"}";
		//String nodeStyle_treasure="node.treasure {"+"fill-color: yellow;"+"}";
		//String nodeStyle_EntryExit="node.exit {"+"fill-color: green;"+"}";
		
		String nodeStyle=defaultNodeStyle+nodeStyle_wumpus+nodeStyle_agent+nodeStyle_visite;
		this.graphe.setAttribute("ui.stylesheet",nodeStyle);
		
	}
	
	public void setCurrentLocation(String position, String previous){
		Node curr = this.graphe.getNode(position),
				prev = this.graphe.getNode(previous);
		
		// un agent est actuellement sur cette position
		if (curr!=null){
			curr.setAttribute("ui.class", "agent");
			curr.addAttribute("ui.label", position+": agent");
		} if (prev!=null){
			prev.setAttribute("ui.label",previous);
			prev.setAttribute("ui.class", "visite");
		}
	}
	
	public void updatePoint(String key, NoeudCarte value){
		// on recupere le noeud correspondant deja existant dans le graphe
		Node nd = this.graphe.getNode(key);
		// on modifie les donnees a ce point
		// on le marque si visite
		if (value.isVisited()){
			nd.setAttribute("ui.class", "visite");
		}
		// Gestion du tresor
	}
	
	public void handlePoint(String key, NoeudCarte value){
		Node nd=this.graphe.getNode(key);
		// si le point a deja été créé on ne le recréé pas
		if(nd==null){
			nd = this.graphe.addNode(key);
		}
		nd.setAttribute("ui.label", key);
		for (String keyVois : value.getNeighboors()){
			// si le voisin n'existe pas encore on le crée
			Node vois = this.graphe.getNode(keyVois);
			if (vois==null){
				vois = this.graphe.addNode(keyVois);
				vois.setAttribute("ui.label", keyVois);
			}
			// si l'arc key_keyVois ou keyVois_key n'existe pas deja
			Object edg = this.graphe.getEdge(key+"_"+keyVois);
			Object revEdg = this.graphe.getEdge(keyVois+"_"+key);
			if (edg==null && revEdg==null){
				// on ajoute l'arc
				this.graphe.addEdge(key+"_"+keyVois, key, keyVois);
			}
		}
		// on actualise le point
		this.updatePoint(key, value);
	}
	
	public void affiche(){
		// on se donne le temps de regarder les changements
		try{
			Thread.sleep(1000);
		} catch(InterruptedException e){}
	}

}
