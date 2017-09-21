
/**
 * Write a description of class Item here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String description;
    private int weight;
    private String name;
    private boolean canBePickedUp;

    /**
     * Constructor for objects of class Item
     */
    public Item(String name, String description, int weight, boolean canBePickedUp)
    {
        this.description = description;
        this.weight = weight;
        this.name = name;
        this.canBePickedUp = canBePickedUp;
    }
	
	public String getDescription(){
		return description;
	}
	
	public String getName(){
	    return name;
	}
	
	public int getWeight(){
	    return weight;
	}
	
	public boolean canPickUp(){
	    return canBePickedUp;
	}
}
