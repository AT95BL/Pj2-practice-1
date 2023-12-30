import java.util.ArrayList;
import java.util.Scanner;

public class Simulacija {

    public static int BROJ_VRSTA = 3;
    public static int BROJ_KOLONA = 30;
    public static Object[][] mapa;
    public static ArrayList<VojnoPlovilo> vojnaPlovila;

    public Simulacija() {
        mapa = new Object[BROJ_VRSTA][BROJ_KOLONA];
        vojnaPlovila = new ArrayList<>();
    }

    public void formiranjeListeVojnihPlovila() {
        Razarac razarac = new Razarac(0);
        this.vojnaPlovila.add(razarac);
        NosacAviona nosacAviona = new NosacAviona(2);
        this.vojnaPlovila.add(nosacAviona);
        Podmornica podmornica1 = new Podmornica(1);
        this.vojnaPlovila.add(podmornica1);
        Podmornica podmornica2 = new Podmornica(2);
        this.vojnaPlovila.add(podmornica2);
    }

    public void start() {
        for (var el : vojnaPlovila) {
            el.start();
        }
    }

    public static boolean pauza = false;
    public static Object pauzaLock = new Object();

    public static synchronized void aktivirajPauzuSimulacije() {
        pauza = true;
    }

    public static synchronized void deaktivirajPauzuSimulacije() {
        pauza = false;
        synchronized (pauzaLock) {
            pauzaLock.notifyAll();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Upotreba: java Simulacija <n>");
            System.exit(1);
        }

        try {
            int n = Integer.parseInt(args[0]);
            if (n <= 20 || n >= 40) {
                System.out.println("Greška: n mora biti u opsegu (20, 40)");
                System.exit(1);
            }

            Simulacija.BROJ_KOLONA = n;
            Simulacija simulacija = new Simulacija();
            simulacija.formiranjeListeVojnihPlovila();
            simulacija.start();
            Scanner scanner = new Scanner(System.in);
            String line = "ETF UNIBL";

            while (!"END".equals(line)) {
                try {
                    if ("WAIT".equals(line)) {
                        simulacija.aktivirajPauzuSimulacije();
                    } else if ("NOTIFY".equals(line)) {
                        simulacija.deaktivirajPauzuSimulacije();
                    } else if (line.startsWith("INFO")) {
                        if (!Simulacija.pauza) {
                            throw new CommandNotValidException();
                        }

                        String[] tmp = line.split(" ");
                        try {
                            int id = Integer.parseInt(tmp[1]);
                            for (var el : vojnaPlovila) {
                                if (el.id == id) {
                                    System.out.println(el);
                                    break;
                                }
                            }
                        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                            System.out.println("ID nije validan!!!");
                        }
                    } else if (line.startsWith("TIME")) {
                        if (!Simulacija.pauza) {
                            throw new CommandNotValidException();
                        }

                        String[] tmp = line.split(" ");
                        try {
                            int id = Integer.parseInt(tmp[1]);
                            for (var el : vojnaPlovila) {
                                if (el.id == id) {
                                    System.out.println(el + "Vreme kretanja: " +
                                            (el.endTime - el.startTime) + "[ms] \n");
                                    break;
                                }
                            }
                        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                            System.out.println("ID nije validan!!!");
                        }
                    }
                } catch (CommandNotValidException ex) {
                    System.out.println(ex.getMessage());
                }

                line = scanner.nextLine();
            }

            scanner.close();
        } catch (NumberFormatException ex) {
            System.out.println("Greška: Nevalidan argument za n");
            System.exit(1);
        }
    }
}