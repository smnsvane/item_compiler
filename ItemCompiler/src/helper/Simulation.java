package helper;

import java.util.ArrayList;

import java.util.Random;

import parse.Launcher;
import parse.ParserException;
import parse.Printer;
import parse.Util;
import sql.Weapon;




public class Simulation {
    public static void main(String[] args) throws ParserException {
        new Simulation(false);
    }

    class Player {
        public String name;

        public String toString() {
            return name + "(h:" + h + ")";
        }
        public int h = 10, def;
        public ArrayList<Weapon> w = new ArrayList<Weapon>();
    }

    public Simulation(boolean print) throws ParserException {
        new Launcher(Printer.PrintMode.no_print, true, false);
        int favor0 = 0;
        for (int i = 0; i < 10000; i++){
        Player p0 = new Player(), p1 = new Player();
        p0.name = "p0";
        p0.w.add(Util.item.get("Quaterstaff").weapon);
        p0.def = 3;
        p1.name = "p1";
        p1.w.add(Util.item.get(/*"Pointy Scrap Metal"*//*"Sharpened Scrap Metal"*/"Shiv").weapon);
        p1.w.add(Util.item.get("Pointy Scrap Metal").weapon);
        p1.def = 0;
        Player active = p0, defender = p1;
        Random rnd = new Random();
        while (p0.h > 0 && p1.h > 0) {
            if (print)
                System.out.println("\nNEXT TURN");
            for (Weapon w : active.w) {
                int dmg;
                if (w.maxDamage == w.minDamage)
                    dmg = w.maxDamage;
                else
                    dmg = rnd.nextInt(w.maxDamage - w.minDamage) + w.minDamage;
                if (print)
                    System.out.println(active + " rolls " + dmg + " damage");
                int defence = 0;
                int defenceLeft = defender.def;
                while (defenceLeft > 0) {
                    int defRoll = rnd.nextInt(10) + 1;
                    if (defRoll < defenceLeft && defRoll < 10)
                        defence++;
                    defenceLeft -= defRoll;
                    if (print)
                        System.out.println(defender + " rolls (" + defRoll + "<" + (defenceLeft + defRoll) + ") defence is " + defence);
                }
                dmg -= defence;
                if (dmg < 0)
                    dmg = 0;
                defender.h -= dmg;
                if (print)
                    System.out.println(defender + " was dealt " + dmg + " damage");
            }

            if (active == p0) {
                active = p1;
                defender = p0;
            } else {
                active = p0;
                defender = p1;
            }
        }
        if (p0.h > 1)
            favor0++;
        else
            favor0--;
        if (print)
            System.out.println("player " + (p0.h > 1 ? 0 : 1) + " wins");
        }
        System.out.println(favor0);
    }
}
