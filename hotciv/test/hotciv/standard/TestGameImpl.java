package hotciv.standard;

import hotciv.common.CityImpl;
import hotciv.common.GameImpl;
import hotciv.factories.AlphaFactory;
import hotciv.framework.City;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Unit;
import hotciv.framework.WorldGeneration;
import hotciv.standard.utilities.StringWorldGeneration;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class TestGameImpl {
	private GameImpl g;
	private List<Position> positions;
	
	@Before
	public void setUp() {
		g = new GameImpl(new AlphaFactory(), new AlphaWorldGeneration());
		positions = null;
	}
	/* This is a major list of test designed to test the getPositions function of GameImpl class.
	 */
	@Test
	public void ShouldGetAllPositionsInDist1From8_9ClockwiseStartingAt7_9() {
		Position p = new Position(8,9);
		positions = g.getPositions(p,1);
		assertEquals("There are 8 positions in the list, " +
				"since there are 8 positions with distance 1",
				8, positions.size());
		assertEquals("(7,9) has same x as index 0 in the list",
				new Position(7,9), positions.get(0));
		assertEquals("(7,10) is at index 1 on the list",
				new Position(7,10), positions.get(1));
		assertEquals("(9,10) is at index 3",
				new Position(9,10), positions.get(3));
		
	}
	@Test
	public void ShouldGet6_7WhenAskingForAllPositionsWithDistance0From6_7() {
		Position p = new Position(6,7);
		positions = g.getPositions(p,0);
		assertEquals("Only 1 position with dist 0 to (6,7)", 1, positions.size());
		assertEquals("(6,7) is in the list", p, positions.get(0));
	}
	
	/* === ARGUMENT FOR THE FOLLOWING TEST CASES ===
	 * It can be proven mathematically that there are 8*n points (x',y') around (x,y)
	 * with x,y integers, where max(|x-x'|,|y-y'|) = n, n>=1. To see this, realize
	 * this is the case for n = 1 and assume for an n. Now, consider the points of n+1. 
	 * Ignonring the cases where |x-x'|=|y-y'| = n+1, these points are either rigt above, below
	 * or to the sides of the prevoius points. The corners have 2 close to them
	 * (one above or below, and one to the side), the rest 1. This means we have 8n + 4, where 
	 * the 4 is the extra from the corners. Add 4 for the corners we ignonred and we get that
	 * 8n + 8 = 8(n+1), as claimed.
	 */
	@Test
	public void ShouldGetAllPositionsInDistance3From7_7InOrder() {
		positions = g.getPositions(new Position(7,7),3);
		
		assertEquals("There are 24 positions with dist 7 to (7,7)", 24, positions.size());
		
		// these are all positions in distance 3 from (7,7). 
		assertEquals("(4,7) is at index 0",
				new Position(4,7), positions.get(0));
		assertEquals("(4,9) is at index 2",
				new Position(4,9), positions.get(2));
		assertEquals("(4,10) is at index 3",
				new Position(4,10), positions.get(3));
		assertEquals("(7,10) is at index 6",
				new Position(7,10), positions.get(6));
		assertEquals("(10,10) is at index 9",
				new Position(10,10), positions.get(9));
		assertEquals("(10,8) is at index 11",
				new Position(10,8), positions.get(11));
		assertEquals("(10,4) is at index 15",
				new Position(10,4), positions.get(15));
		assertEquals("(6,4) is at index 19",
				new Position(6,4), positions.get(19));
		assertEquals("(4,4) is at index 21",
				new Position(4,4), positions.get(21));
		assertEquals("(4,6) is at index 23",
				new Position(4,6), positions.get(23));
	}
	
	@Test
	public void ShouldHandleRectangularWorldsPositions() {
		String[] tiles = { "..ffMOh", "hh..Off" };
		String[] cities = { "B.....R", "......R" };
		String[] units = { "..AASL.", "llaass." };
		WorldGeneration wg = new StringWorldGeneration( tiles,
				cities, units);
		g = new GameImpl( new AlphaFactory(), wg);
		
		positions = g.getPositions(new Position(0,6), 1);
		assertEquals( "3 tiles", 3, positions.size());
		assertEquals( "(1,6) is at index 0", 
				new Position( 1, 6 ), positions.get(0) );
		assertEquals( "(0,5) is at index 2",
				new Position( 0, 5 ), positions.get(2));
		
		positions = g.getPositions( new Position( 1, 0 ), 1);
		assertEquals( "3 tiles", 3, positions.size());
		assertEquals( "(0,0) is at index 0", 
				new Position( 0,0 ), positions.get(0) );
		assertEquals( "(1,1) is at index 2",
				new Position( 1, 1 ), positions.get(2));
	}
	
	@Test
	public void ShouldSpawnCorrectlyInRectangularWorld() {
		String[] t = { ".", "." };
		String[] c = { "R", "." };
		String[] u = { "A", "." };
		WorldGeneration wg = new StringWorldGeneration( t, c, u );
		g = new GameImpl( new AlphaFactory(), wg);
		g.changeProductionInCityAt(new Position( 0, 0 ), GameConstants.ARCHER);
		g.endOfTurn();
		g.endOfTurn();
		Position p = new Position( 1, 0 );
		assertNull( "No unit at (1,0)", g.getUnitAt( p ));
		g.endOfTurn();
		g.endOfTurn();
		assertNotNull( "unit at (1,0)", g.getUnitAt( p ));
	}
	
	@Test
	public void ShouldOnlyGetValidWorldPositions() {

		/* We check it also cuts y values in opposite side
		 * and where x < 0 and x > 16 and the same with y
		 */
		positions = g.getPositions(new Position(0,0),1);
		assertEquals("There are 3 points with dist 1 to (0,0)", 3, positions.size());
		assertEquals("(0,1) is at index 0", 
				new Position(0,1),positions.get(0));
		
		positions = g.getPositions(new Position(15,15),2);
		assertEquals("There are 5 points with dist 2 to (15,15)",
				5, positions.size());
		assertEquals("(13,15) is at index 0", 
				new Position(13,15), positions.get(0));
	}
	
	@Test
	public void ShouldNotGetAnyPositionsWhenDistanceIsNegative() {
		positions = g.getPositions(new Position(4,5), -5);
		assertEquals("No positions in negative distance", 0, positions.size());
	}
	
	@Test
	public void ShouldRemoveUnitCorrectly() {
		Position p1 = new Position( 4, 3 );
		Position p2 = new Position ( 3, 2);
		Unit u1 = g.getUnitAt( p1 );
		Unit u2 = g.getUnitAt( p2 );
		assertNotNull( "There is a unit at (4,3)", u1 );
		assertNotNull( "There is a unit at (3,2)", u2 );
		g.removeUnitAt( p1 );
		g.removeUnitAt( p2 );
		u1 = g.getUnitAt( p1 );
		u2 = g.getUnitAt ( p2 );
		assertNull( "No unit at (4,3)", u1 );
		assertNull( "No unit at (3,2)", u2 );
	}
	
	@Test
	public void ShouldAddCityCorrectly() {
		Position p = new Position( 8, 8 );
		CityImpl ci = new CityImpl( Player.RED);
		City c = g.getCityAt(p);
		assertNull("There is no city at (8,8)", c);
		g.addCity(ci, p);
		c = g.getCityAt(p);
		assertNotNull("There is a city at (8,8)", c);
		assertEquals("Red owns city at (8,8)", 
				Player.RED, c.getOwner());
	}

	@Test
	public void ShouldGiveCitiesInTheWorldCorrectly() {
		String[] t = { "....", "....", "...." };
		String[] cities = { "R..B", ".BBR", "BRB." };
		String[] u = t;
		WorldGeneration wg = new StringWorldGeneration( t, cities, u );
		g = new GameImpl(new AlphaFactory(), wg);
		List<City> list = g.getCities();
		
		assertEquals( "There should be 8 in list", 8, list.size() );
		assertEquals( "Red city at index 0", 
				Player.RED, list.get(0).getOwner() );
		assertEquals( "Blue city at index 1",
				Player.BLUE, list.get(1).getOwner() );
		assertEquals( "Red city at index 2", 
				Player.BLUE, list.get(2).getOwner() );
		assertEquals( "Blue city at index 3",
				Player.BLUE, list.get(3).getOwner() );
		assertEquals( "Red city at index 4", 
				Player.RED, list.get(4).getOwner() );
		assertEquals( "Blue city at index 5",
				Player.BLUE, list.get(5).getOwner() );
		assertEquals( "Red city at index 6", 
				Player.RED, list.get(6).getOwner() );
		assertEquals( "Blue city at index 7",
				Player.BLUE, list.get(7).getOwner() );
	}
	
	@Test
	public void ShouldNotBeAbleToEnterMountainsOrOcean() {
		String[] t = { 	"MOMO",
					 	"M..O", 
					 	"MOMO"};
		String[] c = {	"....",
						"....",
						"...." };
		String[] u = {	"....",
						".Al.",
						"...." };
		
		WorldGeneration wg = 
				new StringWorldGeneration( t, c, u );
		g = new GameImpl( new AlphaFactory(), wg );
		
		Position redStart = new Position( 1, 1 );
		
		Position m0 = new Position( 1, 0 );
		assertFalse( "Can't move red archer to mountain",
				g.moveUnit( redStart, m0 ));
		g.endOfTurn();
		
		Position blueStart = new Position( 1, 2 );
		Position m1 = new Position( 0, 2 );
		assertFalse( "Can't move blue legion to mountain",
				g.moveUnit( blueStart, m1 ));
		
		g.endOfTurn();
		
		Position oc0 = new Position( 0, 1 );
		assertFalse( "Can't move red archer to ocean", 
				g.moveUnit( redStart, oc0 ) );
		g.endOfTurn();
		
		Position oc1 = new Position( 1, 3 );
		assertFalse( "Can't move blue legion to ocean",
				g.moveUnit( blueStart, oc1 ));
	}
}
