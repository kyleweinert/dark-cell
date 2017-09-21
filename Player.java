
/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Set;
import java.util.HashMap;
public class Player
{
    private Room currentRoom;
    private String name;
    private int maxWeight;
    private HashMap<String, Item> pItems;
    private int health;
    private int i;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom, String name, int maxWeight)
    {
        pItems = new HashMap<>();
        this.name = name;
        this.currentRoom = currentRoom;
        this.maxWeight = maxWeight;
        this.health = 3;
    }
    
    public Room getCurrentRoom(){
        return currentRoom;
    }
    
    public String getName(){
        return name;
    }
    
    public int getMaxWeight(){
        return maxWeight;
    }
    
    public String getItemString(){
	    String itemString = "Items: ";
	    Set<String> keys = pItems.keySet();
        for(String name : keys) {
          itemString += " " + name;
        }
        return itemString;
	}
	
	public boolean hasItem(String itemName){
	    return pItems.containsKey(itemName);
	}
	
	public void setCurrentRoom(Room newRoom){
	    currentRoom = newRoom;
	}
	
	public void setMaxWeight(int newWeight){
	    this.maxWeight = newWeight;
	}
	
	public void addItem(Item item){
	    pItems.put(item.getName(), item);
	}
	
	public Item pickUpItem(String itemName){
	    if(canPickUpItem(itemName)){
	        Item item = currentRoom.removeItem(itemName);
	        pItems.put(itemName, item);
	        return item;
	    }else{
	        return null;
	    }
	}
	
	public boolean canPickUpItem(String itemName){
	    boolean canPick = true;
	    Item item = currentRoom.getItem(itemName);
	    if(item == null && (getCurrentWeight()+ getCurrentRoom().getRItem(itemName).getWeight()) > maxWeight){
	        canPick = false;
	    } else{
	        canPick = item.canPickUp();
	    }
	    return canPick;
	}
	
	public int getCurrentWeight(){
        int currentWeight = 0;
	    for(String key : pItems.keySet()){
            Item item = pItems.get(key);
            currentWeight += item.getWeight();
        }
        return currentWeight;
	}
	
	public void dropItem(Item item){
	    pItems.remove(item.getName());
	    currentRoom.addItem(item);
	}
	
	public Item getItem(String itemName){
	    return pItems.get(itemName);
	}
	
	public void removeItem(String itemName){
	    pItems.remove(itemName);
	}
	
	public void restoreHealth(){
	    this.health = 3;
	}
	
	public void removeHealth(int damage){
	    this.health -= damage;
	}
}
