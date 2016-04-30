package strategies.exploring;

import java.util.List;
import java.util.Random;

import strategies.Strategy;
import env.Attribute;
import env.Couple;
import Objects.Carte;

public class RandomStrategy implements Strategy {


	@Override
	public String indexNextMove(String currentPosition, Carte knowledge,
			List<Couple<String, List<Attribute>>> observations) {
		//Random move from the current position
		Random r= new Random();
		// getting randomly a solution among his neighboors detected
		int moveId=r.nextInt(observations.size());
		return observations.get(moveId).getLeft();
	}

	@Override
	public String getCurrentPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
