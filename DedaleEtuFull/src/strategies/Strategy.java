package strategies;

import java.util.List;

import Exceptions.ExplorationClosedException;
import Exceptions.MeetingExpectedException;
import Objects.Carte;
import env.Attribute;
import env.Couple;

public interface Strategy {

	public String indexNextMove(String currentPosition,Carte knowledge, List<Couple<String, List<Attribute>>> observations) throws ExplorationClosedException,MeetingExpectedException;

	public String getCurrentPosition();
}
