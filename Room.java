/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
public class Room 
{
    private String description;
    private HashMap<String, Room> exits;
	private HashMap<String, Item> items;
	private String itemDescription;
	private int i;
	private String itemString;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
		items = new HashMap<>();
    }
    
    public void addItem(Item item){
        items.put(item.getName(), item);
    }
    
    public String getLongDescription(){
		return description + ".\n" + getItemString() + ".\n" + getExitString();
    }
	
	public String getItemString(){
	    String itemString = "Items: ";
	    Set<String> keys = items.keySet();
        for(String name : keys) {
          itemString += " " + name;
        }
        return itemString;
	}
    
    public String getExitString(){
        String result = "Exits: ";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
          result += " " + exit;
        }
        return result;
    }
    
    public Room getExit(String direction){
        return exits.get(direction);
    }
    
    public Item getItem(String itemName){
        return items.get(itemName);
    }
    
    public String getDescription(){
        return description;
    }

    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    public Item removeItem(String itemName){
        return items.remove(itemName);
    }
    
    public Item getRItem(String itemName){
	    return items.get(itemName);
	}
	
	public boolean hasRItem(String itemName){
	    return items.containsKey(itemName);
	}
}
