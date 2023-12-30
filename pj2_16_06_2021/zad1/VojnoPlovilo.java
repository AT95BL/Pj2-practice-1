import java.util.Random;
import java.util.Date;
import java.util.Iterator;

public abstract class VojnoPlovilo extends Thread{

    Random random = new Random();

    public static int ID=0;
    public int id;

    public Naoruzanje[] naoruzanje; // artiljerija

    public boolean smjerKretanja;
    public boolean dosegnutKrajMape = false;
    public boolean unisten = false;

    public int pozicijaX;
    public int pozicijaY;

    public long startTime;
    public long endTime;

    private static final Object lock = new Object();

    // Constructor
    public VojnoPlovilo(int vrsta){
        this.id = ID++;
        this.pozicijaX = vrsta;
        try {
            this.pozicijaY = findEmptyPosition(vrsta);
            synchronized (lock) {
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = this;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Unable to find an empty position in Simulacija.mapa.");
        }
    }
    
    private int findEmptyPosition(int vrsta) {
        int posY;
        synchronized (lock) {
            Random random = new Random();
            posY = random.nextInt(Simulacija.BROJ_KOLONA - 1);
            while (Simulacija.mapa[vrsta][posY] != null) {
                posY = random.nextInt(Simulacija.BROJ_KOLONA - 3);
            }
        }
        return posY;
    }
    /* 
    public VojnoPlovilo(int vrsta){

        this.id = ID++;
        this.pozicijaX = vrsta;
        this.pozicijaY = random.nextInt(Simulacija.BROJ_KOLONA-1);

        while(Simulacija.mapa[this.pozicijaX][this.pozicijaX] != null){
            this.pozicijaY = random.nextInt(Simulacija.BROJ_KOLONA-3);
        }

        Simulacija.mapa[this.pozicijaX][this.pozicijaY] = this;
    }
    */

    // Kretanje duz mape, borbena formacija..
    @Override
    public void run(){

        this.startTime = new Date().getTime();

        while(true){

            if(Simulacija.pauza){					 
				synchronized(Simulacija.pauzaLock){				
					this.endTime = new Date().getTime();		
					try{		
						Simulacija.pauzaLock.wait();
					}
					catch(InterruptedException ex){
						System.out.println("PREKID!");
					}
				}
			}

            // o zaustavljanu simulacije..
            if(this.unisten == true){
                Iterator<VojnoPlovilo> iterator = Simulacija.vojnaPlovila.iterator();
                while (iterator.hasNext()){
                    VojnoPlovilo el = iterator.next();
                    if (el.id == this.id) {
                        iterator.remove();
                    }
                }  
                System.out.println("Vojno plovilo: " + this.id + " je unisteno!!!\n");
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = null;
                break;
            }

            //  zaustavi podmornicu jer je ista stigla do kraja granica mape..
            if( (this instanceof Sonar) /* && (this.pozicijaX == 0 || this.pozicijaX == 1 || this.pozicijaX == 2)*/ && (this.pozicijaY == 0)){
                System.out.println("Vojno plovilo " + this.id + " napusta mapu..OVDE SAM!!!\n");
                this.dosegnutKrajMape=true;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = null;
                break;
            }

            //  zaustavi brod jer je isti stigao do kraja granica mape..
            if( (this instanceof Radar) && (this.pozicijaY == Simulacija.BROJ_KOLONA-1) ){
                System.out.println("Vojno plovilo " + this.id + " napusta mapu..\n");
                this.dosegnutKrajMape=true;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = null;
                break;
            }

            // pocetak borbe:
            if( (this instanceof Sonar)  && (this.pozicijaY - 1 > 0) && (Simulacija.mapa[this.pozicijaX][this.pozicijaY-1] instanceof Radar)){
                System.out.println("Napad je u toku..\n");
                if( Simulacija.mapa[this.pozicijaX][this.pozicijaY] instanceof TorpedoStit){
                    System.out.println("Napad je odbijen.. \n");
                    this.unisten=true;
                }
                else{
                    for(var el:Simulacija.vojnaPlovila){
                        if(el.id == ((VojnoPlovilo)Simulacija.mapa[this.pozicijaX][this.pozicijaY-1]).id){
                            el.unisten=true;
                        }
                    }
                }
            }

            if( (this instanceof Sonar)  && (this.pozicijaY - 2 > 0) && (Simulacija.mapa[this.pozicijaX][this.pozicijaY-2] instanceof Radar)){
                System.out.println("Napad je u toku..\n");
                if( Simulacija.mapa[this.pozicijaX][this.pozicijaY] instanceof TorpedoStit){
                    System.out.println("Napad je odbijen.. \n");
                    this.unisten=true;
                }
                   else{
                    for(var el:Simulacija.vojnaPlovila){
                        if(el.id == ((VojnoPlovilo)Simulacija.mapa[this.pozicijaX][this.pozicijaY-2]).id){
                            el.unisten=true;
                        }
                    }
                }
            }

            if( (this instanceof Sonar)  && (this.pozicijaY - 3 > 0) &&  (Simulacija.mapa[this.pozicijaX][this.pozicijaY-3] instanceof Radar)){
                System.out.println("Napad je u toku..\n");
                if( Simulacija.mapa[this.pozicijaX][this.pozicijaY] instanceof TorpedoStit){
                    System.out.println("Napad je odbijen.. \n");
                    this.unisten=true;
                }
                else{
                    for(var el:Simulacija.vojnaPlovila){
                        if(el.id == ((VojnoPlovilo)Simulacija.mapa[this.pozicijaX][this.pozicijaY-3]).id){
                            el.unisten=true;
                        }
                    }
                }
            }
            

            // kretanje podmornice:
            if ((this instanceof Sonar) && (this.pozicijaY - 1 >= 0)) {
                if (this.pozicijaY > 1 && Simulacija.mapa[this.pozicijaX][this.pozicijaY - 1] == null) {
                this.pozicijaY--;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = this;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY + 1] = null;
            } else if (this.pozicijaY == 1 && Simulacija.mapa[this.pozicijaX][this.pozicijaY - 1] == null) {
                this.pozicijaY--;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = this;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY + 1] = null;
                this.dosegnutKrajMape = true;
            } else {
                System.out.println("Submarine movement blocked: Position out of bounds or occupied.");
                this.dosegnutKrajMape = true;  // Set this flag to prevent further movement attempts
            }
}


            // kretanje broda:
            if( (this instanceof Radar) && (this.pozicijaY + 1 <  30) && (Simulacija.mapa[this.pozicijaX][this.pozicijaY + 1] == null) ){
                this.pozicijaY++;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = this;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY-1] = null;
            }

            // situacija:
            System.out.println(this);

            try{
                sleep(1000);
            }catch(InterruptedException e){
                System.err.println(e);
            }
        }

        this.endTime = new Date().getTime();
    }

    @Override
    public String toString(){
        return "Vojno plovilo " + this.id + " se nalazi na poziciji(" + this.pozicijaX + "," + this.pozicijaY + ") \n";
    }

    /* 
    public void setUnisten(){

        this.unisten=true;

        for(var el:Simulacija.vojnaPlovila){
            if(el.id == this.id){
                Simulacija.vojnaPlovila.remove(el);
            }
        }

        Simulacija.mapa[this.pozicijaX][this.pozicijaY] = null;
    }
    */
}