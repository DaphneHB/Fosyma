package Objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import env.Attribute;
import env.Couple;

public class NoeudCarte {
	// identifiant de la case
	private String ID;
	// voisins direct de la case
	private List<String> voisins;
	// tresor sur la case (0 si rien ou pas de données)
	private int tresor = 0;
	// case deja visite ou non
	private boolean visite = false;
	// dernier passage sur cette case
	private long derniere_vue = 0;
	
	
	public NoeudCarte(String id, List<String> vois, int tres, boolean visit, long derniere_vue){
		this.ID = id;
		this.voisins = vois;
		this.tresor = tres;
		this.visite = visit;
		this.derniere_vue = derniere_vue;
	}
	
	public NoeudCarte(String position, Attribute tresor,
			List<Couple<String, List<Attribute>>> voisins) {
		this.ID = position;
		this.tresor = (int)tresor.getValue();
		// on itère sur tous les couples "case"
		Iterator<Couple<String,List<Attribute>>> it = voisins.iterator();
		Couple<String, List<Attribute>> couple;
		List<String> vois = new ArrayList<String>();
		while(it.hasNext()){
			couple = (Couple<String, List<Attribute>>) it.next();
			vois.add(couple.getLeft());
		}
		this.visite = true;
	}

	public void mixerData(NoeudCarte node){
		if(this.ID!=node.ID){
			return;
		}
		// on met a jour les voisins
		for (String v: node.voisins){
			if (!this.voisins.contains(v)){
				this.voisins.add(v);
			}
		}
		// cas ou les deux agents ont visité ce noeud
		if(this.visite && node.visite){
			// on actualise alors le tresor avec la valeur la plus récente associée à cette case si les dates de derniere vue sont les mêmes : on prend la valeur min de tresor (cas ou une partie du tresor a ete ramasse)
			this.tresor = (this.derniere_vue>node.derniere_vue)?this.tresor:((this.derniere_vue<node.derniere_vue)?node.tresor:Math.min(this.tresor, node.tresor));
		} // sinon
		else {
			// le tresor est le maximum des deux
			// car l'un des deux ne l'a pas encore vu ou aucun (0)
			this.tresor = Math.max(this.tresor, node.tresor);
		}
		// si moi ou un autre agent a deja visite cette case
		this.visite = this.visite || node.visite;		
	}
	
	/**
	 * Mélange les connaissances deja acquise et celles recemment recues
	 */
	public void mixerData(String pos,List<String> vois, Integer tres,boolean visite, long vue){
		if(this.ID!=pos){
			return;
		}
		// on met a jour les voisins
		/* Other version without any update possible
		 * List<Foo> twoCopy = new ArrayList<>(two);
		 * twoCopy.removeAll(one);
		 * one.addAll(twoCopy);
		 * */
		for (String v: vois){
			if (!this.voisins.contains(v)){
				this.voisins.add(v);
			}
		}
		if(this.visite && visite){
			// on actualise alors le tresor avec la valeur la plus récente associée à cette case si les dates de derniere vue sont les mêmes : on prend la valeur min de tresor (cas ou une partie du tresor a ete ramasse)
			this.tresor = (this.derniere_vue>vue)?this.tresor:((this.derniere_vue<vue)?tres:Math.min(this.tresor, tres));
		} // sinon
		else {
			// le tresor est le maximum des deux
			// car l'un des deux ne l'a pas encore vu ou aucun (0)
			this.tresor = Math.max(this.tresor, tres);
		}
		// si moi ou un autre agent a deja visite cette case
		this.visite = this.visite || visite;		
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof NoeudCarte)){
			return false;
		}
		NoeudCarte oNode = (NoeudCarte)o; 
		return (this.ID==oNode.ID
				&& this.tresor==oNode.tresor
				&& this.visite==oNode.visite
				&& this.voisins.equals(oNode.voisins))
				&& this.derniere_vue==oNode.derniere_vue;
	}
	
	/**
	 * Returning this node directs neighboors
	 * @return
	 */
	public List<String> getNeighboors(){
		return this.voisins;
	}
	
	/**
	 * Returning the list of information
	 * Every information as a string
	 * the information about if the node has been visited in int:
	 * 0 -> visited; 1 -> not visited
	 * @return
	 */
	public List<String> getInfos(){
		List<String> mesInfos = new ArrayList<String>();
		mesInfos.add(this.ID);
		mesInfos.add(this.tresor+"");
		int visite = (this.visite)?0:1;
		mesInfos.add(visite+"");
		return mesInfos;
	}

	public boolean isVisited() {
		return this.visite;
	}

	/**
	 * Returning the number of its neighboors
	 * @return
	 */
	public int getNbNeighboors() {
		return this.voisins.size();
	}
	
	public String serialize(){
		return "";
	}
	
	public boolean deserialize(){
		return false;
	}
}
