package strategies.exploring;

import java.util.ArrayList;
import java.util.List;

import strategies.Strategy;
import Exceptions.ExplorationClosedException;
import Objects.Carte;
import env.Attribute;
import env.Couple;

/**
 * Strategy leading the agent on the first non visited Node
 * if there is none: random
 * @author 3200234
 *
 */
public class ShorterWayStrategy implements Strategy{

	private String previousPosition = "";
	private List<String> way = new ArrayList<String>();
	
	@Override
	public String indexNextMove(String currentPosition, Carte knowledge,
			List<Couple<String, List<Attribute>>> observations) throws ExplorationClosedException {
		if(!way.isEmpty()){
			return way.remove(0);
		}
		// Getting the simplest way a possible next position
		String nextPosition = knowledge.getNonVisitedNeighboor(currentPosition);
		// si toutes les positions ont déja été visitées
		if (nextPosition.equals("") || nextPosition.equals(previousPosition) || nextPosition.equals(currentPosition)){
			// on genere le plus court chemin jusquau point "tovisit"
			this.way = knowledge.getSimpleWay(currentPosition);
			// si tous les noeuds de la carte ont été visités
			if (this.way==null){
				// on en informe le behaviour by exception
				throw new ExplorationClosedException("");
			}
			// on recupere la prochaine position
			nextPosition = way.remove(0);
		}
		return nextPosition;
	}

	@Override
	public String getCurrentPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
