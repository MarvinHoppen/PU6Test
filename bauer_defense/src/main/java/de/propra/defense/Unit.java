package de.propra.defense;

import static de.propra.defense.UnitType.BUG1;
import static de.propra.defense.UnitType.BUG2;
import static de.propra.defense.UnitType.CROW;
import static de.propra.defense.UnitType.FROG;
import static de.propra.defense.UnitType.PLANT;
import static de.propra.defense.UnitType.SCARECROW;


import de.propra.defense.ui.GamePanel;
import java.util.Optional;
import java.util.Set;

public class Unit {

  private final GamePanel game;
  private final UnitType type;
  private int delay;
  private double full = 0;
  private int row;
  private int col;

  private int hitpoints;
  private int strength;

  public Unit(GamePanel game, UnitType type, int row, int col) {
    this.game = game;
    this.type = type;
    this.row = row;
    this.col = col;
    this.hitpoints = 1;
    this.strength = 0;
    if (type == PLANT) hitpoints = 5;
    if (type == BUG1 || type == BUG2) {
      hitpoints = 10;
      strength = 1;
    }
    if (type == FROG) {
      strength = 2;
    }

  }

  public void placeUnit(int row, int col) {
    if (game.occupied(row, col)) return;
    game.removeUnit(this.row, this.col);
    game.placeUnit(type, row, col);
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public boolean isStationary() {
    return type == PLANT || type == SCARECROW;
  }

  public boolean isLivingPlant() {
    return type == PLANT && hitpoints > 0;
  }

  public boolean isLivingBug() {
    return (type == BUG1 || type == BUG2) && hitpoints > 0;
  }

  public boolean isLivingEnemy() {
    return (type == BUG1 || type == BUG2 || type == CROW) && hitpoints > 0;
  }

  public void act(Set<Unit> units) {
    if (type == BUG1 || type == BUG2) {
      Optional<Unit> nearestPlant =
          units.stream().filter(Unit::isLivingPlant).min((o1, o2) -> distance(this, o1, o2));
      if (nearestPlant.isPresent()) {
        Unit plant = nearestPlant.get();
        if (isNeighbor(plant, 1)) {
          attack(plant);
        }
        else {
          double decide = Math.random();
          if (decide > 0.8) {
            moveRandomly();
          }
          else {
            moveTowards(plant);
          }
        }
      }
    }
    if (type == FROG) {
      Optional<Unit> nearest =
          units.stream().filter(Unit::isLivingBug).min((o1, o2) -> distance(this, o1, o2));
      if (nearest.isPresent()) {
        Unit bug = nearest.get();
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
  }

  private void moveRandomly() {
    placeUnit(randomWalk(row), randomWalk(col));
  }

  private void moveTowards(Unit other) {
    int nextrow = row;
    int nextcol = col;
    if (row < other.row) nextrow++;
    if (row > other.row) nextrow--;
    if (col < other.col) nextcol++;
    if (col > other.col) nextcol--;
    placeUnit(nextrow, nextcol);
  }

  private int randomWalk(int r) {
    if (Math.random() < 0.25) return r - 1;
    if (Math.random() > 0.75) return r + 1;
    return r;
  }


  private void attack(Unit other) {
    System.out
        .println(this.type + " hits " + other.type + " Remaining Hitpoints: " + other.hitpoints);
    other.hit(strength);
  }

  private void hit(int strength) {
    hitpoints -= strength;
  }

  private boolean isNeighbor(Unit other, int dist) {
    return Math.abs(row - other.row) <= dist &&
        Math.abs(col - other.col) <= dist;
  }

  private int distance(Unit me, Unit a, Unit b) {
    var d1 = distance(me, a);
    var d2 = distance(me, b);
    return Double.compare(d1, d2);
  }

  private double distance(Unit me, Unit b) {
    return Math.sqrt(square(me.row - b.row) + square(me.col - b.col));
  }

  private double square(int v) {
    return v * v;
  }

  public boolean isDead() {
    return hitpoints <= 0;
  }

  public boolean isAlive() {
    return hitpoints > 0;
  }
}
