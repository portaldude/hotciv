package hotciv.standard;

import hotciv.common.GameImpl;
import hotciv.factories.AlphaFactory;
import hotciv.factories.BetaFactory;
import hotciv.factories.EpsilonFactory;
import hotciv.factories.GammaFactory;
import hotciv.framework.*;
import hotciv.standard.utilities.StringWorldGeneration;

import org.junit.*;
import static org.junit.Assert.*;
public class TestIntegration {
	@Test
	public void BetaCivIntegration() {
		GameImpl g = new GameImpl(new BetaFactory(), new AlphaWorldGeneration());
		assertEquals("should start at age -4000", -4000, g.getAge());
		// perform 2*39 end of turn, to advance the age
		// which advanced with 39*100 years
		for (int i = 0; i < 2*39; i++) {
			g.endOfTurn();
		}
		assertEquals("age is -100", -100, g.getAge());
		g.endOfTurn();
		g.endOfTurn();
		assertEquals("age is -1", -1, g.getAge());
		assertNull("no winners", g.getWinner());
	}
    @Test
    public void GammaCivIntegration() {
        GameImpl g = new GameImpl(new GammaFactory(), new AlphaWorldGeneration());
        Unit u = g.getUnitAt(new Position(4, 3));
        assertEquals("settler at position 4, 3", GameConstants.SETTLER, u.getTypeString());
        assertNull("no city at position 4, 3", g.getCityAt(new Position(4, 3)));
        g.performUnitActionAt(new Position(4, 3));
        assertNotNull("city at position 4, 3", g.getCityAt(new Position(4, 3)));
        Unit arch = g.getUnitAt(new Position(2, 0));
        assertEquals("archer has 3 defense", 3, arch.getDefensiveStrength() );
        g.performUnitActionAt(new Position(2, 0));
        assertEquals("archer now has 6 defense", 6, arch.getDefensiveStrength());
        g.endOfTurn();
        g.endOfTurn();
        g.endOfTurn();
        g.endOfTurn();
        assertEquals("archer should have 0 move at start of turn",0 , arch.getMoveCount());
        g.performUnitActionAt(new Position(2, 0));
        g.endOfTurn();
        g.endOfTurn();
        assertEquals("archer should now have 1 movecount", 1, arch.getMoveCount());

    }
    
    @Test
    public void DeltaCivIntegration() {
    	GameImpl g = new GameImpl(new AlphaFactory(), new DeltaWorldGeneration());
    	Tile t = g.getTileAt(new Position(5,2));
    	assertNotNull("Tile aint null", t);
    	assertEquals("Tile is forest", GameConstants.FOREST, t.getTypeString());
    	t = g.getTileAt(new Position(0,0));
    	assertNotNull("Tile aint null", t);
    	assertEquals("Tile is ocean", GameConstants.OCEANS, t.getTypeString());
    	
    	City c = g.getCityAt(new Position(8,12));
    	assertNotNull("City is not null", c);
    	assertEquals("City belongs to red", Player.RED, c.getOwner());
    }
    
    @Test
    public void EpsilonCivIntegration() {
    	String[] t = { 	".....",
    					".....",
    					".....",
    					"....."};
    	String[] c = { 	".....",
    					".B...",
    					"RRRRR",
    					"....."};
    	String[] u = { 	".A...",
    					"lll..",
    					"LLLLL",
    					"s.s.s"};
    	WorldGeneration wg = new StringWorldGeneration( t, c, u );
    	GameImpl g = new GameImpl(new EpsilonFactory(), wg);
    	Position p0 = new Position(0,1);
    	Position p1 = new Position(1,1);
    	g.moveUnit(p0, p1);
    	assertNull(g.getUnitAt(p0));
    	assertEquals("Blue legion", Player.BLUE, g.getUnitAt(p1).getOwner());
    	Position r0 = new Position(2, 0);
    	Position b0 = new Position(3,0);
    	g.moveUnit(r0, b0);
    	Position r1 = new Position(2, 2);
    	Position b1 = new Position(3, 2);
    	g.moveUnit(r1, b1);
    	assertNull(g.getWinner());
    	Position r2 = new Position(2,4);
    	Position b2 = new Position(3,4);
    	g.moveUnit(r2, b2);
    	assertEquals(Player.RED, g.getWinner());
    }
}