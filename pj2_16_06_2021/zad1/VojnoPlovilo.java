import java.util.Random;
import java.util.Date;

public abstract class VojnoPlovilo extends Thread{

    Random random = new Random();

    public static int ID=1;
    public int id;

    public Naoruzanje[] naoruzanje; // artiljerija

    public boolean smjerKretanja;
    public boolean dosegnutKrajMape = false;
    public boolean unisten = false;

    public int pozicijaX;
    public int pozicijaY;

    public long startTime;
    public long endTime;

    // Constructor
    public VojnoPlovilo(int vrsta){

        this.id = ID++;
        this.pozicijaX = vrsta;
        this.pozicijaY = random.nextInt(Simulacija.BROJ_KOLONA-1);

        while(Simulacija.mapa[this.pozicijaX][this.pozicijaX] != null){
            this.pozicijaY = random.nextInt(Simulacija.BROJ_KOLONA-3);
        }

        Simulacija.mapa[this.pozicijaX][this.pozicijaY] = this;
    }

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
                
                for(var el:Simulacija.vojnaPlovila){
                    if(el.id == this.id){
                        Simulacija.vojnaPlovila.remove(el);
                    }
                }

                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = null;

                break;
            }

            if( (this instanceof Sonar) && (this.pozicijaX == 0) && (this.pozicijaY == 0)){
                System.out.println("Vojno plovilo " + this.id + " napusta mapu..\n");
                this.dosegnutKrajMape=true;
                break;
            }

            if( (this instanceof Radar) && (this.pozicijaY == Simulacija.BROJ_KOLONA-1) ){
                System.out.println("Vojno plovilo " + this.id + " napusta mapu..\n");
                this.dosegnutKrajMape=true;
                break;
            }

            
            // pocetak borbe:
            if( (this instanceof Sonar)  &&  (Simulacija.mapa[this.pozicijaX][this.pozicijaY-1] instanceof Radar)){
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

            if( (this instanceof Sonar)  &&  (Simulacija.mapa[this.pozicijaX][this.pozicijaY-2] instanceof Radar)){
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

            if( (this instanceof Sonar)  &&  (Simulacija.mapa[this.pozicijaX][this.pozicijaY-3] instanceof Radar)){
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
            if( (this instanceof Sonar) && (Simulacija.mapa[this.pozicijaX][this.pozicijaY-1] == null) ){
                this.pozicijaY--;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY] = this;
                Simulacija.mapa[this.pozicijaX][this.pozicijaY+1] = null;
            }

            // kretanje broda:
            if( (this instanceof Radar) && (Simulacija.mapa[this.pozicijaX][this.pozicijaY + 1] == null) ){
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