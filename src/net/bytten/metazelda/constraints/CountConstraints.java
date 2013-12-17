package net.bytten.metazelda.constraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.bytten.metazelda.IDungeon;
import net.bytten.metazelda.util.Coords;
import net.bytten.metazelda.util.Direction;
import net.bytten.metazelda.util.IntMap;

/**
 * Limits the {@link net.bytten.metazelda.generators.IDungeonGenerator} in
 * the <i>number</i> of keys, switches and rooms it is allowed to place.
 * 
 * Also restrict to a grid of 1x1 rooms.
 * 
 * @see IDungeonConstraints
 */
public class CountConstraints implements IDungeonConstraints {

    protected int maxSpaces, maxKeys, maxSwitches;
    
    protected IntMap<Coords> gridCoords;
    protected TreeMap<Coords,Integer> roomIds;
    protected int firstRoomId;
    
    public CountConstraints(int maxSpaces, int maxKeys, int maxSwitches) {
        this.maxSpaces = maxSpaces;
        this.maxKeys = maxKeys;
        this.maxSwitches = maxSwitches;

        gridCoords = new IntMap<Coords>();
        roomIds = new TreeMap<Coords,Integer>();
        Coords first = new Coords(0,0);
        firstRoomId = getRoomId(first);
    }
    
    protected int getRoomId(Coords xy) {
        if (roomIds.containsKey(xy)) {
            assert gridCoords.get(roomIds.get(xy)).equals(xy);
            return roomIds.get(xy);
        } else {
            int id = gridCoords.newInt();
            gridCoords.put(id, xy);
            roomIds.put(xy, id);
            return id;
        }
    }
    
    protected Coords getRoomCoords(int id) {
        assert gridCoords.containsKey(id);
        return gridCoords.get(id);
    }
    
    @Override
    public int getMaxSpaces() {
        return maxSpaces;
    }
    
    public void setMaxSpaces(int maxSpaces) {
        this.maxSpaces = maxSpaces;
    }
    
    @Override
    public Collection<Integer> initialRooms() {
        return Arrays.asList(firstRoomId);
    }

    @Override
    public int getMaxKeys() {
        return maxKeys;
    }
    
    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }
    
    @Override
    public boolean isAcceptable(IDungeon dungeon) {
        return true;
    }

    @Override
    public int getMaxSwitches() {
        return maxSwitches;
    }

    public void setMaxSwitches(int maxSwitches) {
        this.maxSwitches = maxSwitches;
    }

    protected boolean validRoomCoords(Coords c) {
        return c.y >= 0;
    }
    
    @Override
    public Collection<Integer> getAdjacentRooms(int id) {
        Coords xy = gridCoords.get(id);
        List<Integer> ids = new ArrayList<Integer>();
        for (Direction d: Direction.values()) {
            Coords neighbor = xy.add(d.x, d.y);
            if (validRoomCoords(neighbor)) ids.add(getRoomId(neighbor));
        }
        return ids;
    }

    @Override
    public Set<Coords> getCoords(int id) {
        return new TreeSet<Coords>(Arrays.asList(getRoomCoords(id)));
    }

}
