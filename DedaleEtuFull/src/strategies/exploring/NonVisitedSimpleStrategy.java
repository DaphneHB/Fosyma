package strategies.exploring;

import java.util.List;
import java.util.Random;

import strategies.Strategy;
import Objects.Carte;
import env.Attribute;
import env.Couple;

/**
 * Strategy leading the agent on the first non visited Node
 * if there is none: random
 * @author 3200234
 *
 */
public class NonVisitedSimpleStrategy implements Strategy{

	private String previousPosition = "";
	
	@Override
	public String indexNextMove(String currentPosition, Carte knowledge,
			List<Couple<String, List<Attribute>>> observations) {
		
		// Getting the simplest way a possible next possition
		String nextPosition = knowledge.getNonVisitedNeighboor(currentPosition);
		// si toutes les positions ont déja été visitées, on en choisit une au hasard
		if (nextPosition.equals("") || nextPosition.equals(previousPosition) || nextPosition.equals(currentPosition)){
			int moveId=(new Random()).nextInt(observations.size());
			nextPosition = observations.get(moveId).getLeft();
		}
		return nextPosition;
	}

	@Override
	public String getCurrentPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
