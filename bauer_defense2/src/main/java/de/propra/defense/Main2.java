package de.propra.defense;

import static de.propra.defense.UnitType.BUG1;
import static de.propra.defense.UnitType.BUG2;
import static de.propra.defense.UnitType.FROG;
import static de.propra.defense.UnitType.PLANT;
import static de.propra.defense.ui.GamePanel.H;
import static de.propra.defense.ui.GamePanel.TSZ;
import static de.propra.defense.ui.GamePanel.W;


import de.propra.defense.ui.GamePanel;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JFrame;


public class Main2 {

    Random random = new Random();

    private Set<Unit2> units = new HashSet<>();

    private boolean running = true;

    public static void main(String[] args) {
        Main2 main = new Main2();
        main.start();
    }

    private void start() {
        GamePanel window = setupGamePanel();

        units.addAll(List.of(
                new Frog(window,3,6),
                // new Unit(window, SCARECROW, 8, 4),
                new Crow(window, 8,4),
                new Bug1(window, 9, 28),
                new Bug2(window,4, 22),
                //  new Unit(window, CROW, 12, 26),
                new Scarecrow(window,12,26),
                new Scarecrow(window,12,1),
                new Plant(window,12, 2),
                new Plant(window,12, 3),
                new Plant(window,13, 2),
                new Plant(window,13, 3)));

        initializePositions();

        while (running) {
            allUnitsAct();
            removeDeadUnits(window);
            window.repaint();
            sleep();
            checkGameEnd();
        }


    }

    private GamePanel setupGamePanel() {
        GamePanel window = new GamePanel("Bauer Defence");
        window.setSize(W * TSZ, H * TSZ);
        window.setLayout(new GridLayout(H, W));
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return window;
    }

    private void initializePositions() {
        for (Unit2 unit : units) {
            unit.placeUnit(unit.getRow(), unit.getCol());
        }
    }

    private void allUnitsAct() {
        units.stream().filter(Unit2::isAlive).forEach(u -> u.act(units));
    }

    private void checkGameEnd() {
        long plantCount = units.stream().filter(Unit2::isLivingPlant).count();
        if (plantCount == 0) {
            System.out.println("Spiel verloren");
            running = false;
        }

        long enemyCount = units.stream().filter(Unit2::isLivingEnemy).count();
        if (enemyCount == 0) {
            System.out.println("Spiel gewonnen");
            running = false;
        }
    }

    private void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void removeDeadUnits(GamePanel window) {
        units.stream()
                .filter(Unit2::isDead)
                .forEach(u -> window.removeUnit(u.getRow(), u.getCol()));

        units = units.stream().filter(Unit2::isAlive).collect(Collectors.toSet());
    }


}
