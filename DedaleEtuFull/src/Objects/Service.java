package Objects;

public enum Service {
// ENUM	
//	EXPLO ("explorer"),
//	
//	PICKUP ("tresorier"),
//	
//	FIGHT("combattant");	   
//	
//	private String name = "";
//	
//	private Service(String name){
//	
//	  this.name = name;
//	
//	}
//	
//	public String toString(){
//	
//	  return name;
//	
//	}
	
	explorer,
	tresorier,
	combattant;
	
	public static Service getService(String name){
		switch(name){
		case "explorer" : return explorer;
		case "tresorier" : return tresorier;
		case "combattant" : return combattant;
		default : return explorer; // par défaut
		}
	}
	
//	public static final String EXPLO = "explorer";
//	public static final String PICKUP = "tresorier";
//	public static final String FIGHT = "combattant";
}
