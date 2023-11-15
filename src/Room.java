package zork;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Room {
    private String name;
    private String id;
    private List<String> items = new LinkedList<>();
    private String description;
    private Map<String, String> directions = new HashMap<>();

    public Room(String id) {
        this.id = id;
    }

    public Room(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getItems() {
        return items;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getDirections() {
        return directions;
    }

    public void setDirection(String direction, String roomId){
        directions.put(direction, roomId);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //put items in the room
    public void putItem(String item){
        items.add(item);
    }

    public String getRoom(String id){
        return directions.get(id);
    }
    public List<String> takeItems(List<String> items){
        return Collections.unmodifiableList(items); //don't let user modify the items 
    }

    public Optional <String> takeItem(String item){
        //to take if there is more than 1 item
        List<String> newList = items.stream()
            .filter(items -> !items.equals(item)) //filter items that are NOT the item the user wants to take
            .collect(Collectors.toList()); //newList contains all the items left that the user did not take
        if (newList.size() != items.size()){
            items = newList; //assign items to new list
            return Optional.of(item); //.of() get an instance of optional with the specified type (String here)
        }
        return Optional.empty();

    }
    public void printItems(){
        System.out.println("Items in the room:");
        for (String item:items){
            System.out.println(item);
        }
    }
    public void printDirections() {
        System.out.println("Directions:");
        for (Map.Entry<String, String> direction : directions.entrySet()) {
            System.out.println(direction.getKey() + "-> " + direction.getValue());
        }
    }

    public Optional<String> getRoomInDirection(String direction) { //check if there is a 'north/south' room
        return Optional.ofNullable(directions.get(direction)); // returns Optional obj with the specified value. If null, an empty Optional object is returned
    }


}
