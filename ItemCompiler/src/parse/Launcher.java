package parse;

public class Launcher {
    public static void main(String[] args) throws ParserException {
        new Launcher(Printer.PrintMode.pretty, true, false);
    }

    public Launcher(Printer.PrintMode mode, boolean validate, boolean generateAndPrintItemClasses) throws ParserException {
        Printer.mode = mode;

        String filename = "wiki.txt";

        new Parser().parse(filename);

        new Printer().printSQL();

        if (generateAndPrintItemClasses)
            new Printer().printItemClasses();

        if (validate)
            new Validator().validate();

 //       ItemPrinter iPrint = new ItemPrinter();//.print("Shiv");

/*
        System.out.println("\nWASTE");
        int find = 0;
        for (Item i : Util.item)
            if (i.origins.contains(Origin.Waste)) {
                System.out.println();
                iPrint.print(i);
               // System.out.println(i + ": M" + (i.is(Label.Metal) ? i.get(Label.Metal).arg0 : 0) + ", " + i.get(Label.Waste).arg0 + "(" +
                 //                  i.get(Label.Waste).arg1 + ")");
                find += Integer.parseInt(i.get(Label.Waste).arg1);
            }
        System.out.println(find);
        /*
        System.out.println("\nNON-WASTE");
        for (Item i : Util.item)
            if (i.is(Label.Metal) && !i.origins.contains(Origin.Waste))
                System.out.println(i + ": M" + i.get(Label.Metal).arg0 + ", " + i.recipes);
        */
    }
}
