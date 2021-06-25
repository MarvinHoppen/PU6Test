package de.propra.defense;

import de.propra.defense.ui.GamePanel;

import java.util.Set;

public interface Unit2 {

    void placeUnit(int row, int col);

    int getRow();

    int getCol();

    boolean isStationary();

    boolean isLivingPlant();

    boolean isLivingBug();

    boolean isLivingEnemy();

    boolean isLivingCrow();

    void act(Set<Unit2> units);

    void moveRandomly();

    void moveTowards(Unit2 other);

    int randomWalk(int r);

    void attack(Unit2 other);

    void hit(int strength);

    boolean isNeighbor(Unit2 other, int dist);

    int distance(Unit2 me, Unit2 a, Unit2 b);

    double distance(Unit2 me, Unit2 b);

    double square(int v);

    boolean isDead();

    boolean isAlive();

    String getName();

    int getHitpoints();
}
