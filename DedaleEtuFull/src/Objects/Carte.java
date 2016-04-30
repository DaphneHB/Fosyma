package Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import env.Attribute;
import env.Couple;


/**
 * Représente la carte que chacun des agents construit petit a petit
 * 
 * Represente une liste des lignes de la carte (grille)
 * Chaque ligne represente une liste d'ensemble/de liste de tresor
 * Indexes selon la ou ils ont été trouvé, null sinon
 * 
 * @author 3200234
 *
 */
public class Carte {
	private HashMap<String,NoeudCarte> noeuds;
	private GrapheCarte graphe ;
	private String currentLocation = "";
	private String previousLocation = "";
	private long derniere_vue = 0;
	
	public Carte(){
		this.noeuds = new HashMap<String, NoeudCarte>();
		// on genere un graph dorogovtsev
		this.graphe = new GrapheCarte(30, true);
	}
	
	/**
	 * Mixe la carte actuelle avec celle en parametre
	 * pour le partage de connaissances
	 * 
	 * @return 0 si ca c'est bien passé, 1 sinon
	 * 
	 */
	public int mixerCarte(Carte autreCarte){
		HashMap<String, NoeudCarte> autresNoeuds = autreCarte.mesNoeuds();
		NoeudCarte value,maVal;
		this.derniere_vue = Math.max(this.derniere_vue, autreCarte.derniere_vue);
		// pour chaque noeud que l'autre agent connait
		//this.noeuds.forEach((k,v) -> autreCarte.merge(k,v,List<String>));
		for(String key : autresNoeuds.keySet()){
			value = autresNoeuds.get(key);
			maVal = this.noeuds.get(key);
			// si ma valeur est null, je mets a jour
			if(maVal==null){
				this.addNoeud(key, value);
			} else {
				//this.graphe.updatePoint(key,value);
				maVal.mixerData(value);
			}
		}
		return 0;
	}
	
//	// Utilisation de la fonction merge des hashmap
//	public List<String> merge(String key,List<String> value, BiFunction<? super List<String>,? super List<String>,? extends List<String>> remappingFunction){
//		return this.noeuds.merge(key, value, remappingFunction);
//	}
	
	public HashMap<String, NoeudCarte> mesNoeuds(){
		return this.noeuds;
	}
	
	/**
	 * Methode utilisee pour le partage de connaissances
	 * @param id
	 * @param voisins
	 */
	public void addNoeud(String id,NoeudCarte voisin){
		this.noeuds.put(id, voisin);
		this.graphe.handlePoint(id, voisin);
	}
	
	/**
	 * Methode utilisee pour l'actualisation des connaissances selon les observations
	 * Ajoute un nouveau noeud visite a la carte
	 * 
	 * @param position
	 * @param tresor
	 * @param voisins
	 */
	public void addPoint(String position,List<Attribute> attr,List<Couple<String,List<Attribute>>> voisins){
		// TODO Traitement des differents attributs possibles (monstre,tresor,trou,...)
		int tres = 0;//(int)tresor.getValue();
		// on se retire de la liste des voisins
		voisins.remove(0);
		boolean isTresor = false;
		for (int a = 0; a<attr.size(); a++){
			//System.out.println("-*********- tres class = "+attr.get(a+1).getClass().getName());
			if (attr.get(a).getName().equals("Treasure")){
				isTresor = true;
			}
			if (isTresor){
				try{tres = (int) attr.get(a).getValue();} catch (Exception e){}
				isTresor = false;
			}
		}
		// on itère sur tous les couples "case"
		Iterator<Couple<String,List<Attribute>>> it = voisins.iterator();
		Couple<String, List<Attribute>> couple;
		List<String> vois = new ArrayList<String>();
		while(it.hasNext()){
			couple = it.next();
			vois.add(couple.getLeft());
			this.addVoisin(couple.getLeft(),couple.getRight(), position);
		}
		
		// on teste si lepoint n'existe pas deja
		if(this.noeuds.containsKey(position)){
			this.updatePoint(position,vois,tres,true);
		} else {
			// sinon on cree le point visite et l'ajoute a la carte
			NoeudCarte nd = new NoeudCarte(position,vois,tres,true, this.derniere_vue);
			this.noeuds.put(position, nd);
			// on l'ajoute au graphe a afficher
			this.graphe.handlePoint(position, nd);
		}
		previousLocation = currentLocation;
		currentLocation = position;
		this.graphe.setCurrentLocation(currentLocation, previousLocation);
	}

	

	/**
	 * Methode utilisee pour l'actualisation des connaissances selon les observations
	 * Ajoute un nouveau noeud voisin d'un en cours d'exploration a la carte
	 * 
	 * @param position
	 * @param tresor
	 * @param voisins
	 */
	private void addVoisin(String left, List<Attribute> right, String position) {
		// TODO Traitement des differents attributs possibles (monstre,tresor,trou,...)
		Integer tres = 0;//(int)tresor.getValue();
		// on gere les voisins de ce point
		List<String> vois = new ArrayList<String>();
		vois.add(position);
		// si le point d'ID =left a deja ete ajoute a la carte
		if (this.noeuds.containsKey(left)){
			this.updatePoint(left, vois,tres, false);
		} else {
			// sinon on cree le point voisin et l'ajoute a la carte
			NoeudCarte nd = new NoeudCarte(left, vois,0,false,this.derniere_vue);
			this.noeuds.put(left, nd);
			this.graphe.handlePoint(left, nd);
		}
	}
	
	/**
	 * Mise à jour d'un point deja dans la carte selon l'observation la plus recente
	 * 
	 * @param position
	 * @param attr
	 * @param voisins
	 */
	private void updatePoint(String position, List<String> vois, Integer tres,
			boolean visit) {
		NoeudCarte node = this.noeuds.get(position);
		node.mixerData(position, vois, tres, visit,this.derniere_vue);
		this.graphe.handlePoint(position, node);
		this.graphe.setCurrentLocation(currentLocation, previousLocation);
	}
	
	/**
	 * Getting the neighboorhood of a position in the current map
	 * @param position
	 * @return
	 */
	public List<String> getNeighboors(String position){
		return this.noeuds.get(position).getNeighboors();
	}
	
	/**
	 * Getting in a string list the information about a specific position in the map
	 * @param position
	 * @return
	 */
	public List<String> getNodeInfo(String position){
		return this.noeuds.get(position).getInfos();
	}
	
	/**
	 * String of the neighboors of the currentPosition and their infos
	 */
	public String nodeToString(String position,String agentName){
		StringBuilder myStr = new StringBuilder("\n"+agentName);
		myStr.append("   Location : "+position+"  ");
		List<String> infos = this.getNodeInfo(position);
		List<String> neighb = this.getNeighboors(position);
		String visit = (infos.get(2).equals("0"))?"Visité":"Non visité";
		myStr.append(visit);
		if (infos.get(2).equals("1")){
			myStr.append("   Trésor inconnu...");
		} else {
			myStr.append((infos.get(1).equals("0")?"   Pas de trésor":"   "+infos.get(1)+" de trésor!!!"));
		}
		myStr.append("\n\tMy neighboors location: ");
		for (String s : neighb){
			myStr.append(s+" ");
		}
		return myStr.toString();
		
	}

	/**
	 * Return the first non visited neighboor of the node at position position
	 * @param position
	 * @return
	 */
	public String getNonVisitedNeighboor(String position) {
		List<String> neighb = this.getNeighboors(position);
		String theOne = "";
		for (String s:neighb){
			if (this.noeuds.get(s).isVisited()==false) {
				theOne = s;
				break;
			}
		}
		return theOne;
	}

	/**
	 * Return the list of non visited neighboors of the node at position position
	 * @param position
	 * @return
	 */
	public List<String> getNonVisitedNeighboors(String position) {
		List<String> neighb = this.getNeighboors(position);
		List<String> list = new ArrayList<String>();
		for (String s:neighb){
			if (this.noeuds.get(s).isVisited()==true) {
				list.add(s);
			}
		}
		return list;
	}
	
	/**
	 * Return the current knowledge of the map
	 * @return
	 */
	public String toStringKnowledge(){
		//this.graphe.affiche();
		StringBuilder myStr = new StringBuilder("\nMa Carte :");
		// boucling on each known node to get his string
		//this.noeuds.keySet().forEach( (nodeID) -> myStr.append(this.nodeToString(nodeID, "\n")));
		return myStr.toString();
	}
	
	/**
	 * Getting the nodes which are unvisited in all the known map
	 * @return
	 */
	public List<String> unvisitedNodesLeft(){
		List<String> list = new ArrayList<String>();
		for (Entry<String, NoeudCarte> s : this.noeuds.entrySet()){
			if (s.getValue().isVisited()){
				list.add(s.getKey());
			}
		}		
		return list;
	}
	
	/**
	 * Getting the number of the univisited neighboors from this position
	 * @param position
	 * @return
	 */
	public int getNeighboorsAvg(String position){
		int avg = this.getNonVisitedNeighboors(position).size();
		return avg;
	}
	
	/**
	 * Return the best position to go from the agent's position position
	 * Stopping at a non visited node
	 * Depthly for visited ones
	 *
	 * @param position
	 * @param previous_positions : list of the previous tested (nodeId,nbneighboors)
	 * @param depth
	 * @return [bestPosition,nbOnTheWay] (string)
	 */
	public String[] getBestNeighboor(String position, List<String> previous_positions, int depth){
		String[] res = new String[2];
		// if depth==0 dont calculating anything
		// or the node is unvisited
		if (!this.noeuds.get(position).isVisited() || depth==0){
			// returning the current position as the best
			res[0] = position;
			res[1] = "1";
			return res;
		}
		int maxNbNeigh = 0, nbNeigh = 0;
		String maxNeigh = position;
		// for each direct neighboor of position
		// calculating the indirect number of neighboors in a specific depth
		// if one not visited, do not calculated -> return 1
		// otherwhise iterating on the neighboorhood
		List<String> neighboorhood = this.getNeighboors(position);
		// if the node is all alone (no neighboor)
		// it can be unvisited
		if (neighboorhood.size()==0){
			res[0] = position;
			res[1] = "1";
			return res;
		}
		// else
		String[] calc ;
		for (String s:neighboorhood){
			// if the point is in the list of the previous ones
			if (previous_positions.contains(s)){
				continue;
			}
			// calculating the number of indirect accessibles unvisited neighboors
			previous_positions.add(s);
			calc = getBestNeighboor(s, previous_positions, depth-1);
			nbNeigh = Integer.parseInt(calc[1]);
			// if this one is unvisited too
			// if the score is better
			if (maxNbNeigh<nbNeigh){
				// changing the current best choice
				maxNbNeigh = nbNeigh;
				maxNeigh = s;
			}
		}
		res[0] = maxNeigh;
		res[1] = maxNbNeigh+"";
		return res;
	}
	
	/**
	 * Returning the first univisited node found in the whole map
	 * @return
	 */
	public String getNonVisitedPosition(){
		// pour chaque noeud connu
		for(Entry<String,NoeudCarte> node: this.noeuds.entrySet()){
			// si 'il n'a pas encore été visité
			if (!node.getValue().isVisited()){
				return node.getKey();
			}
		}
		return "";
	}

	/**
	 * Returning the list of the univisited nodes left in the whole map
	 * @return
	 */
	public List<String> getNonVisitedPositionsList(){
		List<String> liste = new ArrayList<String>();
		// pour chaque noeud connu
		for(Entry<String,NoeudCarte> node: this.noeuds.entrySet()){
			// si 'il n'a pas encore été visité
			if (!node.getValue().isVisited()){
				// on l'ajoute a la liste
				liste.add(node.getKey());
			}
		}
		return liste;
	}

	/**
	 * Return a random way to go from the fromLocation point to the toLocation point
	 * Taking node in the current map
	 * elt 0 : next node on the way
	 * elt size-1 : arrival
	 * @param fromLocation
	 * @param toLocation
	 * @return
	 */
	public List<String> getSpecificWay(String fromLocation, String toLocation){
		List<String> chemin = new ArrayList<String>();
		String onTheWay = fromLocation;
		// tant qu'on est pas arrivé
		while(!onTheWay.equals(toLocation)){
			// TODO
		}
		return chemin;
		
	}

	/**
	 * Return a random way to go from the fromLocation point to the first unvisited point
	 * Getting the nearest unvisited point accessing by neighboorhood
	 * "Parcours en largeur"
	 * Taking nodes in the current map
	 * elt 0 : next node on the way
	 * elt size-1 : arrival
	 * @param fromLocation
	 * @param toLocation
	 * @return
	 */
	public List<String> getSimpleWay(String fromLocation){
		// le chemin final qui sera retourné
		List<String> chemin = new ArrayList<String>();
		// le noeud testé
		String onTheWay = fromLocation;
		// les noeuds a verifier
		List<String> toCheck = new ArrayList<String>();
		// les noeuds passés associés à leur predessesseurs
		HashMap<String,String> node_prev = new HashMap<String, String>();
		toCheck.add(onTheWay);
		boolean continuer = true;
		// tant qu'on est pas arrivé
		while(continuer){
			onTheWay = toCheck.remove(0);
			// on recupere le noeud correspondant pour les manipulations
			NoeudCarte nd = this.noeuds.get(onTheWay);
			// ce noeud a deja ete vu
			List<String> neighboors = nd.getNeighboors();
			//on regarde ses voisins
			for (int i=0; i<nd.getNbNeighboors() && continuer; i++){
				String neighb = neighboors.get(i);
				// si le noeud voisin en qst n'a pas encore été visité
				if(!this.noeuds.get(neighb).isVisited()){
					// on l'associe a son pere
					node_prev.put(neighb, onTheWay);
					// on l'ajoute comme arrivee du chemin
					onTheWay = neighb;
					// et on quitte la boucle
					continuer = false;
				} // sinon: le noeud est deja visite
				else {
					// s'il n'a pas déja été vu
					if (!node_prev.containsKey(neighb)){ // s'il a deja ete vu -> il y a un chemin plus court pour y acceder
						// on l'ajoute avec son parent pour garder la trace
						//  on verra ses voisins plus tard
						toCheck.add(neighb);
						node_prev.put(neighb, onTheWay);
					}
				}
			}
			// s'il n'y a plus aucun noeud a visiter
			if(toCheck.isEmpty()){
				return null;
			}
		}
		// on remonte le chemin a parcourir jusquau noeud choisi comme destination
		while(!onTheWay.equals(fromLocation)){
			// ajout en tete
			chemin.add(0, onTheWay);
			// on recupere le precedent sur la route
			onTheWay = node_prev.get(onTheWay);
		}
		return chemin;
	}
	
	/**
	 * Method to serialize a map
	 * Changing it into a simple string
	 * @return
	 */
	public String serialize(){
		String str = "";
		
		return str;
	}
	
	/**
	 * Mixing the current Map with serMap,
	 * a serialized map 
	 * @param serMap
	 */
	public void mixWithSerializedMap(String serMap){
		
	}
	
	public void vueSuivante(){
		this.derniere_vue ++;
	}
}
