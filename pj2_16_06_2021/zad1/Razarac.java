public class Razarac extends VojnoPlovilo implements Radar, RaketniStit{

    public Razarac(int vrsta){
        super(vrsta);
        this.smjerKretanja=true;
        this.naoruzanje = new Naoruzanje[2];
        this.naoruzanje[0] = new Raketa();
        this.naoruzanje[1] = new Torpedo();
    }
}