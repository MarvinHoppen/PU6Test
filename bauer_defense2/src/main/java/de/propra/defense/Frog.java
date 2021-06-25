package de.propra.defense;

import de.propra.defense.ui.GamePanel;

import java.util.Optional;
import java.util.Set;

public class Frog implements Unit2 {

    final String name = "Frog";
    private final GamePanel game;
    private int row;
    private int col;
    private int delay;
    private double full = 0;

    private int hitpoints;
    private final int strength;

    public Frog(GamePanel game,int row, int col) {
        this.game = game;
        this.row = row;
        this.col = col;
        this.hitpoints = 1;
        this.strength = 2;
    }

    @Override
    public void placeUnit(int row, int col) {
        if (game.occupied(row, col)) return;
        game.removeUnit(this.row, this.col);
        game.placeUnit(UnitType.FROG, row, col);
        this.row = row;
        this.col = col;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHitpoints() {
        return hitpoints;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    @Override
    public boolean isLivingPlant() {
        return false;
    }

    @Override
    public boolean isLivingBug() {
        return false;
    }

    @Override
    public boolean isLivingEnemy() {
        return false;
    }

    @Override
    public boolean isLivingCrow() {
        return false;
    }

    // ACT-METHODE
    @Override
    public void act(Set<Unit2> units) {
        Optional<Unit2> nearest =
                units.stream().filter(Unit2::isLivingBug).min((o1, o2) -> distance(this, o1, o2));
        if (nearest.isPresent()) {
            Unit2 bug = nearest.get();
            if (isNeighbor(bug, 1)) {
                attack(bug);
                full += 0.21;
            }
            else {
                if (delay == (int)full) {
                    moveTowards(bug);
                    delay = 0;
                }
                else {
                    delay++;
                }
            }
        }
    }

    @Override
    public void moveRandomly() {
        placeUnit(randomWalk(row), randomWalk(col));
    }

    @Override
    public void moveTowards(Unit2 other) {
        int nextrow = row;
        int nextcol = col;
        if (row < other.getRow()) nextrow++;
        if (row > other.getRow()) nextrow--;
        if (col < other.getCol()) nextcol++;
        if (col > other.getCol()) nextcol--;
        placeUnit(nextrow, nextcol);
    }

    @Override
    public int randomWalk(int r) {
        if (Math.random() < 0.25) return r - 1;
        if (Math.random() > 0.75) return r + 1;
        return r;
    }

    @Override
    public void attack(Unit2 other) {
        other.hit(strength);
        System.out
                .println(name + " hits " + other.getName() + " Remaining Hitpoints: " + other.getHitpoints());
        other.hit(strength);
    }

    @Override
    public void hit(int strength) {
        hitpoints -= strength;
    }

    @Override
    public boolean isNeighbor(Unit2 other, int dist) {
        return Math.abs(row - other.getRow()) <= dist &&
                Math.abs(col - other.getCol()) <= dist;
    }

    @Override
    public int distance(Unit2 me, Unit2 a, Unit2 b) {
        var d1 = distance(me, a);
        var d2 = distance(me, b);
        return Double.compare(d1, d2);
    }

    @Override
    public double distance(Unit2 me, Unit2 b) {
        return Math.sqrt(square(me.getRow() - b.getRow()) + square(me.getCol() - b.getCol()));
    }

    @Override
    public double square(int v) {
        return v * v;
    }

    @Override
    public boolean isDead() {
        return hitpoints <= 0;
    }

    @Override
    public boolean isAlive() {
        return hitpoints > 0;
    }
}
