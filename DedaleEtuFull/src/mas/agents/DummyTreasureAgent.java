package mas.agents;

import Objects.Service;


public class DummyTreasureAgent extends MyAgent{

	private static final long serialVersionUID = 1L;
	
	public DummyTreasureAgent(){
		super();
		this.monService = Service.tresorier;
	}
}
