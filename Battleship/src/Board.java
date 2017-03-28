import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Board {

    private final int maxX;

    private final int maxY;

    private final List<Ship> ships;

    private final List<Coordinate> shots;
    
    private int numberOfShips;
    
    private ArrayList<Integer> shipSizes;

    public Board() {
        this.maxX = 10;
        this.maxY = 10;
        this.ships = new ArrayList<>();
        this.shots = new ArrayList<>();
        this.numberOfShips = this.ships.size();
        this.shipSizes = new ArrayList<Integer>() ;

    }
    
    public int getNShips(){
    	return this.numberOfShips;
    }
    
    public List<Coordinate> getShots(){
    	return this.shots;
    }

    public void putShip(ShipType shipType, Coordinate coordinate, boolean vertical) {
        if (isPlacedOutOfBoard(shipType, coordinate, vertical)) {
            throw new RuntimeException("Navio não pode ser posto aqui: limitações de mapa");
        }
        if (isConflictingWithOtherShip(shipType, coordinate)) {
            throw new RuntimeException("Navio não pode ser posto aqui: espaço já ocupado.");
        }
        Ship ship = new Ship(shipType, coordinate, vertical);
        this.ships.add(ship);
        this.shipSizes.add(ship.getShipType().getSize());
        this.numberOfShips++;
    }

    private boolean isPlacedOutOfBoard(ShipType shipType, Coordinate coordinate, boolean vertical) {
        //TODO implement
        return false;
    }

    private boolean isConflictingWithOtherShip(ShipType shipType, Coordinate coordinate) {
        //TODO implement
        return false;
    }

    public ShootResult shoot(Coordinate coordinate) {
        //System.out.println("Shot at " + coordinate);
        this.shots.add(coordinate);
        Optional<Ship> optional = this.ships.stream()
                .filter(s -> s.matchesCoordinate(coordinate))
                .findAny();

        if (optional.isPresent()) {
            System.err.println("Navio encontrado em " + coordinate);
            Ship ship = optional.get();
            ship.shot(coordinate);
            if (ship.isAlive()) {
                return ShootResult.HIT;
            }
            this.numberOfShips--;
            return ShootResult.SUNK;
        }
        return ShootResult.MISS;
    }

    public Field[][] print() {
        Field[][] map = new Field[maxX][maxY];
        for (Field[] row : map) {
            for (int i = 0; i < row.length; i++) {
                row[i] = Field.EMPTY;
            }
        }
        for (Coordinate shot : shots) {
            int x = shot.getDigit() - 1;
            int y = shot.getLetter().getIndex();
            if (y < 10 && y > 0 && x < 10 && x > 0){
            	map[y][x] = Field.MISS;
            }
        }
        for (Ship ship : ships) {
            boolean sunk = !ship.isAlive();
            for (Map.Entry<Coordinate, Boolean> entry : ship.getBody().entrySet()) {
                int x = entry.getKey().getDigit() - 1;
                int y = entry.getKey().getLetter().getIndex();
                if (sunk) {
                    map[y][x] = Field.SUNK;
                } else if (entry.getValue()) {
                    map[y][x] = Field.HIT;
                } else {
                    map[y][x] = Field.SHIP;
                }
            }
        }
        return map;
    }
    
    public ArrayList<Integer> getBoardShipSizes(){
    	return this.shipSizes;
    }
} 
