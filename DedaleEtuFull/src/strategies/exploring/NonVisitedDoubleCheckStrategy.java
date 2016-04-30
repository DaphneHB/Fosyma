package strategies.exploring;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import strategies.Strategy;
import Objects.Carte;
import env.Attribute;
import env.Couple;

public class NonVisitedDoubleCheckStrategy implements Strategy {

	private String previousPosition=null;
	private int depth;
	
	public NonVisitedDoubleCheckStrategy() {
		// useless : let for more modularity
		// depth is 1 by default 
		this.depth = 1;
	}
	
	@Override
	public String indexNextMove(String currentPosition, Carte knowledge,
			List<Couple<String, List<Attribute>>> observations) {
		// adding the current position to the previous for the first iteration
		List<String> previous = new ArrayList<String>();
		previous.add(currentPosition);
		// calculating the best choice among currentPosition's neighboor
		String[] nextPositionInfo= knowledge.getBestNeighboor(currentPosition, previous, this.depth);
		// getting the id of the best choice
		String nextPosition = nextPositionInfo[0];
		// TODO remove? if it went back
		if ((previousPosition!=null && nextPosition.equals(previousPosition))){
			int moveId=(new Random()).nextInt(observations.size());
			nextPosition = observations.get(moveId).getLeft();
		}
		
		// Anticipating the move after
		// to optimize the choice
		// choising by the second turn
		// adding the previous proposed position to the previous for the first iteration
		previous.add(nextPosition);
		// calculating the best choice among currentPosition's neighboor
		String[] nextPositionInfo2= knowledge.getBestNeighboor(nextPosition, previous, this.depth);
		// getting the id of the best choice
		String nextPosition2 = nextPositionInfo2[0];
		// si toutes les positions ont déja été visitées, on en choisit une au hasard
		if(nextPosition2.equals(nextPosition) || nextPosition.equals(currentPosition)){
			// retrieving the dangerous node from the current possibility 
			observations.remove(nextPosition2);
			observations.remove(previousPosition);
			int moveId=(new Random()).nextInt(observations.size());
			nextPosition = observations.get(moveId).getLeft();
		}
		
		// executing the mvt
		previousPosition = currentPosition;
		return nextPosition;
	}

	@Override
	public String getCurrentPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
