package mas.agents;

import Objects.Service;


public class DummyFightAgent extends MyAgent{

	private static final long serialVersionUID = 1L;
	
	public DummyFightAgent(){
		super();
		this.monService = Service.combattant;
	}
}
