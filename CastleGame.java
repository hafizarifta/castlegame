package ujian.castlegame;
import java.util.*;


enum ArmyType {
	Infantry,
	Cavalry,
	Archer,
	Catapult
}

/** Troop consists of the number of troops and its power against other army,
 * it will be extended/inherited by Infantry, Cavalry, Archer and Catapult. */
abstract class Troop {
	private final int numberOfTroops;

	private double attackPowerLeft; // remaining attack power that this army has
	private int troopsLeft; // number of troops left after battle

	public Troop(int numberOfTroops) {
		this.numberOfTroops = numberOfTroops;
		this.troopsLeft = numberOfTroops;
	}

	public int getNumberOfTroops() {
		return this.numberOfTroops;
	}

	/** Returns the power against certain troops, this is base power
	 * before multiplied by heroes & castle */
	public abstract double getPowerAgainst(Troop otherTroop);

	public abstract ArmyType getType();

	@Override
	public String toString() {
		return (int)((double)numberOfTroops/1000.0) + "K " + getType().name() + "";
	}

	public int getTroopsLeft() {
		return troopsLeft;
	}

	public void setTroopsLeft(int amount) {
		this.troopsLeft = amount;
	}

	public double getAttackPowerLeft() {
		return attackPowerLeft;
	}

	public void setAttackPower(double attackPower) {
		this.attackPowerLeft = attackPower;
	}

}

class Infantry extends Troop {

	public Infantry(int numberOfTroops) {
		super(numberOfTroops);
	}

	@Override
	public double getPowerAgainst(Troop otherTroop) {
		switch (otherTroop.getType()) {
			case Archer: return 0.4;
			case Cavalry: return 0.1;
			case Infantry: return 0.3;
			default: return 0.2;
		}
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Infantry;
	}
}

class Cavalry extends Troop {

	public Cavalry(int numberOfTroops) {
		super(numberOfTroops);
	}

	@Override
	public double getPowerAgainst(Troop otherTroop) {
		switch (otherTroop.getType()) {
			case Archer: return 0.1;
			case Cavalry: return 0.2;
			case Infantry: return 0.4;
			default: return 0.3;
		}
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Cavalry;
	}

}

class Archer extends Troop {

	public Archer(int numberOfTroops) {
		super(numberOfTroops);
	}

	@Override
	public double getPowerAgainst(Troop otherTroop) {
		switch (otherTroop.getType()) {
			case Archer: return 0.3;
			case Cavalry: return 0.4;
			case Infantry: return 0.1;
			default: return 0.2;
		}
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Archer;
	}


}

class Catapult extends Troop {

	public Catapult(int numberOfTroops) {
		super(numberOfTroops);
	}

	@Override
	public double getPowerAgainst(Troop otherTroop) {
		switch (otherTroop.getType()) {
			case Archer: return 0.3;
			case Cavalry: return 0.1;
			case Infantry: return 0.4;
			default: return 0.2;
		}
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Catapult;
	}

}



/** An abstract class that will be extended by 4 army types by the use of inheritance */
abstract class Hero {
	private int level;

	public Hero(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public abstract ArmyType getType();

	/** Sets the level of hero, the level is clamped with range 1 and 50 */
	public void setLevel(int level) {
		if (level < 1) level = 1;
		if (level > 50) level = 50;
		this.level = level;
	}

	/** Gets the percentage up for specific own troop */
	public abstract double getBoostAttack(Troop troop);

	@Override
	public String toString() {
		return "" + getType().name() + " Hero Level " + this.level;
	}
}


class InfantryHero extends Hero {

	public InfantryHero(int level) {
		super(level);
	}

	@Override
	public double getBoostAttack(Troop troop) {
		if (troop.getType() == ArmyType.Infantry) {
			return 0.4; // boost attack of Infantry troops by 40%
		}
		return 0.0;
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Infantry;
	}

}

class CavalryHero extends Hero {

	public CavalryHero(int level) {
		super(level);
	}

	@Override
	public double getBoostAttack(Troop troop) {
		if (troop.getType() == ArmyType.Cavalry) {
			return 0.4; // boost attack of Cavalry troops by 40%
		}
		return 0.0;
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Cavalry;
	}

}

class ArcherHero extends Hero {

	public ArcherHero(int level) {
		super(level);
	}

	@Override
	public double getBoostAttack(Troop troop) {
		if (troop.getType() == ArmyType.Archer) {
			return 0.4; // boost attack of Archer troops by 40%
		}
		return 0.0;
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Archer;
	}

}

class CatapultHero extends Hero {

	public CatapultHero(int level) {
		super(level);
	}

	@Override
	public double getBoostAttack(Troop troop) {
		if (troop.getType() == ArmyType.Catapult) {
			return 0.4; // boost attack of Catapult troops by 40%
		}
		return 0.0;
	}

	@Override
	public ArmyType getType() {
		return ArmyType.Catapult;
	}

}

/** Each Castle has its skin that determines the skill boost of specific army type.
 * Each castle can also have some troops and heroes with maximum 100K troops and 5 heroes. */
abstract class Castle {
	public static final int MAX_NUMBER_OF_TROOPS = 100000;
	public static final int MAX_NUMBER_OF_HEROES = 5;

	private final ArrayList<Troop> troopList = new ArrayList<>();
	private final ArrayList<Hero> heroList = new ArrayList<>();

	/** Adds the troops if it does not exceed 100k */
	public boolean addTroops(Troop troops) {
		// check if exceeding 100,000
		int quotaRemaining = MAX_NUMBER_OF_TROOPS - this.getTotalInitialTroops();
		if (troops.getNumberOfTroops() > quotaRemaining) {
			// get cancelled because the troops that is going to add exceed maximum troops quota per castle
			return false;
		}
		this.troopList.add(troops);
		return true;
	}

	/** Adds the troops if it does not exceed 5 heroes */
	public void addHero(Hero hero) {
		if (heroList.size() < MAX_NUMBER_OF_HEROES) {
			this.heroList.add(hero);
		}
	}

	public void clear() {
		troopList.clear();
		heroList.clear();
	}

	/** Copies the heroes and troops from other castle */
	public void copy(Castle other) {
		for (Troop troop : other.troopList) {
			this.addTroops(troop);
		}
		for (Hero hero : other.heroList) {
			this.addHero(hero);
		}
	}

	public ArrayList<Troop> getAllTroops() {
		return troopList;
	}

	public ArrayList<Hero> getAllHeroes() {
		return heroList;
	}

	/** Returns true if this castle at least has troops left */
	public boolean stillHasTroopLeft() {
		for (Troop troop : troopList) {
			if (troop.getTroopsLeft() > 0) return true;
		}
		return false;
	}

	/** Gets the combined heroes boost for given troop type.
	 * Zero means no boost/gain for this troop attack power */
	public double getHeroesBoostSkill(Troop troop) {
		// calculate the cumulative of all heroes boost gain
		double heroBoostCumul = 0;
		for (Hero hero : heroList) {
			heroBoostCumul += hero.getBoostAttack(troop);
		}
		return heroBoostCumul;
	}

	/** Returns the hero & skin boost multiplier in percentage. Value of 1.0 means no boost. */
	public double getTotalBoostMultiplier(Troop troop) {
		double heroBoost = getHeroesBoostSkill(troop);
		double skinBoost = getSkinBoostSkill(troop);
		return heroBoost + skinBoost;
	}

	/** Gets the troops attack power after it has been multiplied with total boost (from hero + castle) */
	public double getTotalPower(Troop troop) {
		double base = troop.getNumberOfTroops();
		double bonus = base * getTotalBoostMultiplier(troop);
		return base + bonus;
	}

	/** The skin of this castle */
	public abstract String getSkinName();

	/** Lists the heroes and troops that this castle has */
	public String getArmyList() {
		String str = "Heroes:\n";
		for (Hero hero: heroList) {
			str += "-> " + hero.toString() + "\n";
		}
		str += "Troops:\n";
		for (Troop troop: troopList) {
			double percentBoost = getTotalBoostMultiplier(troop) * 100.0;
			String strPercent = "no boost";
			if (percentBoost > 0) strPercent = "+" + (int)percentBoost + "% boost";
			str += "-> " + troop.toString() + " ("+ strPercent + " )" + "\n";
		}
		return str;
	}

	/** Prints the remaining troops */
	public String getAfterBattleReport() {
		String str = "Troops:\n";
		for (Troop troop: troopList) {
			int troopsLeft = troop.getTroopsLeft();
			String s = (int)Math.ceil((double) troopsLeft / 1000.0) + "K survive";
			if (troopsLeft == 0) s = "no survivor";
			str += "-> " + troop.toString() + " ("+ s + ")" + "\n";
		}
		int deadCount = getTotalInitialTroops() - getTotalTroopsLeft();
		String strDeadCount = (int)Math.ceil((double)deadCount / 1000.0) + "K";
		int surviveCount = getTotalTroopsLeft();
		String strSurviveCount = (int)Math.ceil((double)surviveCount / 1000.0) + "K survive";
		if (surviveCount == 0) strSurviveCount = "no survivor";
		str += strDeadCount + " dead, " + strSurviveCount;
		return str;
	}

	/** Resets the number of troops left and calculate the attack power */
	public void prepareForBattle() {
		for (Troop t : troopList) {
			// calculate its initial attack power
			t.setAttackPower(getTotalPower(t));
			t.setTroopsLeft(t.getNumberOfTroops());
		}
	}

	/** Gets the combined total troops left. Zero means this castle has no more troop and lose. */
	public int getTotalTroopsLeft() {
		int total = 0;
		for (Troop troop : troopList) {
			total += troop.getTroopsLeft();
		}
		return total;
	}

	/** Gets the total initial troops. */
	public int getTotalInitialTroops() {
		int total = 0;
		for (Troop troop : troopList) {
			total += troop.getNumberOfTroops();
		}
		return total;
	}

	/** Gets the boost skill of specific troop based on castle skin */
	public double getSkinBoostSkill(Troop troop) {
		if (troop.getType() == getBoostArmyType()) {
			return 0.2;
		}
		return 0.0;
	}

	public abstract ArmyType getBoostArmyType();

	/** Creates an initial 50K of troops and one hero with same type of the castle skin */
	public abstract void initMixArmies();

	@Override
	public String toString() {
		return "" + getSkinName() + " Castle";
	}
}

class HorseCastle extends Castle {

	@Override
	public void initMixArmies() {
		addHero(new CavalryHero(CastleGame.random(1,50)));
		addTroops(new Cavalry(50000));
	}

	@Override
	public ArmyType getBoostArmyType() { return ArmyType.Cavalry; }

	@Override
	public String getSkinName() {
		return "Horse";
	}
}

class WoodCastle extends Castle {

	@Override
	public void initMixArmies() {
		addHero(new ArcherHero(CastleGame.random(1,50)));
		addTroops(new Archer(50000));
	}

	@Override
	public ArmyType getBoostArmyType() { return ArmyType.Archer; }

	@Override
	public String getSkinName() {
		return "Wood";
	}
}

class SteelCastle extends Castle {

	@Override
	public void initMixArmies() {
		addHero(new InfantryHero(CastleGame.random(1,50)));
		addTroops(new Infantry(50000));
	}

	@Override
	public ArmyType getBoostArmyType() { return ArmyType.Infantry; }

	@Override
	public String getSkinName() {
		return "Steel";
	}
}

class StoneCastle extends Castle {

	@Override
	public void initMixArmies() {
		addHero(new CatapultHero(CastleGame.random(1,50)));
		addTroops(new Catapult(50000));
	}

	@Override
	public ArmyType getBoostArmyType() { return ArmyType.Catapult; }

	@Override
	public String getSkinName() {
		return "Stone";
	}
}


enum BattleResult {
	PLAYER_1_WIN,
	PLAYER_2_WIN,
	DRAW
}

enum GameState {
	/** after click "Create New Armies", before user click "Start", all reports are hidden */
	STATE_PREPARATION,
	/** after click "Prepare" before click "Battle!", showing both armies initial reports */
	STATE_BEFORE_BATTLE,
	/** after click "Battle!", showing battle report and who win */
	STATE_AFTER_BATTLE // after click Battle!, showing battle report
}

// there are only two types of starting armies for both players that is described in assignment for NIM Genap
enum StartingArmyType {
	CAVALRY_VS_ARCHER,
	MIX_ARMIES_VS_INFANTRY
}

/**
 * CastleGame contains the logic rule of the game, how armies attack and who wins the game.
 *
 * Students with even id (NIM Genap):
 * Implement a simulation of 2 battles between:
 * -> Cavalry vs Archer AND
 * -> mix armies vs Infantry
 */
public class CastleGame {
	private GameState state = GameState.STATE_PREPARATION;
	private Castle player1 = new HorseCastle();
	private Castle player2 = new SteelCastle();
	private static final Random random = new Random(System.nanoTime());
	private boolean readyForBattle = false;

	public GameState getState() { return this.state; }

	/** Changes the game state to next state */
	public void nextState() {
		switch (state) {
		case STATE_PREPARATION:
			state = GameState.STATE_BEFORE_BATTLE;
			break;
		case STATE_BEFORE_BATTLE:
			state = GameState.STATE_AFTER_BATTLE;
			break;
		case STATE_AFTER_BATTLE:
			state = GameState.STATE_PREPARATION;
			break;
		}
	}

	/** Changes the castle to next skin type */
	private Castle cycleCastleSkin(Castle castle) {
		switch (castle.getBoostArmyType()) {
			case Infantry: return new HorseCastle();
			case Cavalry: return new WoodCastle();
			case Archer: return new StoneCastle();
			default: return new SteelCastle();
		}
	}

	/** Changes the castle of player 1 to next skin type */
	public void cyclePlayer1Castle() {
		Castle newCastle = cycleCastleSkin(player1);
		newCastle.copy(player1);
		player1 = newCastle;
	}

	/** Changes the castle of player 2 to next skin type */
	public void cyclePlayer2Castle() {
		Castle newCastle = cycleCastleSkin(player2);
		newCastle.copy(player2);
		player2 = newCastle;
	}

	public Castle getPlayer1Castle() { return this.player1; }
	public Castle getPlayer2Castle() { return this.player2; }


	/** Returns a random integer between min and max. */
	public static int random(int min, int max) {
		if (max < min) return min;
		return min + random.nextInt(max - min + 1);
	}

	public Hero createRandomHero() {
		int type = random(0,3); // get random type of castle
		int level = random(1,50);
		switch (type) {
			case 0: return new CavalryHero(level);
			case 1: return new InfantryHero(level);
			case 2: return new ArcherHero(level);
			default: return new CatapultHero(level);
		}
	}

	public Troop createRandomTroop(ArmyType exclude, int numberOfTroops) {
		Troop t = null;
		do {
			int type = random(0, 3); // get random type
			switch (type) {
				case 0:
					t = new Infantry(numberOfTroops);
					break;
				case 1:
					t = new Archer(numberOfTroops);
					break;
				case 2:
					t = new Catapult(numberOfTroops);
					break;
				default:
					t = new Cavalry(numberOfTroops);
					break;
			}
		} while (t.getType() == exclude);
		return t;
	}

	/** Creates a new castle along with its heroes and armies */
	public void init(StartingArmyType type) {
		if (readyForBattle) return;

		int heroCount;
		player1.clear();
		player2.clear();

		if (type == StartingArmyType.CAVALRY_VS_ARCHER) {
			// player1 => cavalry unit, player2 => archer unit

			player1.addTroops(new Cavalry(random(70, 100) * 1000));
			heroCount = random(2, 5); // random from 2 to 5 heroes
			for (int i = 0; i < heroCount; i++) {
				player1.addHero(new CavalryHero(random(1, 50)));
			}

			player2.addTroops(new Archer(random(50, 100) * 1000)); // random from 50 to 100K archer troops
			heroCount = random(1, 3); // random from 1 to 3 heroes
			for (int i = 0; i < heroCount; i++) {
				player2.addHero(new ArcherHero(random(1, 50)));
			}

		} else if (type == StartingArmyType.MIX_ARMIES_VS_INFANTRY) {
			// player1 => mixed armies, player2 => infantry unit

			player1.initMixArmies();
			// keep adding troops until can add no more
			while (player1.addTroops(createRandomTroop(player1.getBoostArmyType(), random(10,50) * 1000)) == true) ;
			heroCount = random(2, 5);
			for (int i = 0; i < heroCount; i++) {
				player1.addHero(createRandomHero()); // add hero of random type
			}

			player2 = new SteelCastle();
			// add infantry units until near 100K
			while (player2.addTroops(new Infantry(random(10, 50) * 1000)) == true);
			heroCount = random(1, 3);
			for (int i = 0; i < heroCount; i++) {
				player2.addHero(new InfantryHero(random(1, 50)));
			}
		}

		// prepare the battle
		player1.prepareForBattle();
		player2.prepareForBattle();

		readyForBattle = true;

	}

	/**
	 * This simulates the attack from attacker troop to defender highest stack troop.
	 * If the attacker still has enough attack power left, it overflows to next stack of troop and so on
	 * until either defender has no more troop or the attack power left is zero.
	 */
	public void attack(Troop attacker, Castle defendCastle) {
		// while attacker still has attack power left
		while (attacker.getAttackPowerLeft() > 0) {
			Troop defender = null;
			// get next defender that still has remaining troop > 0
			for (Troop d : defendCastle.getAllTroops()) {
				if (d.getTroopsLeft() > 0) {
					defender = d;
					break;
				}
			}

			// check if there is defender left
			if (defender != null) { // there is still defender, attack this defender
				double powerLeft = attacker.getAttackPowerLeft();
				// calculate how many kill against this specific type of defender
				int killpower = (int)(attacker.getPowerAgainst(defender) * powerLeft);

				// calculate the number of troop dead in defender side
				if (killpower > defender.getTroopsLeft()) { // the attacker kills all of defender troops
					int overflowKill = killpower - defender.getTroopsLeft();
					// calculate the remaining attack power
					double powerRemaining = (double)overflowKill/ (double)killpower * powerLeft;
					// set the remaining power
					attacker.setAttackPower(powerRemaining);
					// set defender troops left to zero
					defender.setTroopsLeft(0);
				}
				else { // the attacker is not able to kill all troops in defender
					// set the remaining attack power to zero
					attacker.setAttackPower(0);
					// set the remaining defender troops
					defender.setTroopsLeft(defender.getTroopsLeft() - killpower);
				}

			} else { // no more defender, finish attacking
				return;
			}

		}
	}

	/** Do the attack from each troop in attackCastle, attacking the defendCastle's troops */
	public void attack(Castle attackCastle, Castle defendCastle) {
		// for each attacker troops
		for (Troop t : attackCastle.getAllTroops()) {
			// attack the next defender troops
			attack(t, defendCastle);
			// check if there is still troops left in defender
			if (defendCastle.stillHasTroopLeft() == false) { // defender has no more troop left
				break; // no need to continue attacking
			}
		}

	}

	public BattleResult battle() {
		readyForBattle = false;

		// player 1 armies attacks player 2
		attack(player1, player2);

		// player 2 armies attacks player 1
		attack(player2, player1);

		// check the number of survivors to determine who win
		int p1Left = player1.getTotalTroopsLeft();
		int p2Left = player2.getTotalTroopsLeft();
		if (p1Left > p2Left) { // player 1 win
			return BattleResult.PLAYER_1_WIN;
		}
		else if (p2Left > p1Left) { // player 2 win
			return BattleResult.PLAYER_2_WIN;
		}
		return BattleResult.DRAW;
	}


}

