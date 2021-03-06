package hotciv.factories;

import hotciv.strategy.AgeStrategy;
import hotciv.strategy.AlphaAttack;
import hotciv.strategy.AlphaPopulationStrategy;
import hotciv.strategy.AlphaProduction;
import hotciv.strategy.AlphaUnitActionStrategy;
import hotciv.strategy.AttackStrategy;
import hotciv.strategy.BetaAgeStrategy;
import hotciv.strategy.BetaWinnerStrategy;
import hotciv.strategy.PopulationStrategy;
import hotciv.strategy.ProductionStrategy;
import hotciv.strategy.UnitActionStrategy;
import hotciv.strategy.WinnerStrategy;

public class BetaFactory implements GameFactory {

    @Override
    public AgeStrategy makeAgeStrategy() {
        return new BetaAgeStrategy();
    }

    @Override
    public WinnerStrategy makeWinnerStrategy() {
        return new BetaWinnerStrategy();
    }

    public UnitActionStrategy makeUnitActionStrategy() {
        return new AlphaUnitActionStrategy();
    }
    
    public AttackStrategy makeAttackStrategy() {
		return new AlphaAttack();
	}

	public PopulationStrategy makePopulationStrategy() {
		return new AlphaPopulationStrategy();
	}
	public ProductionStrategy makeProduction() {
		return new AlphaProduction();
	}
}
