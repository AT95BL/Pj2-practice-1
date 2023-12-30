public class Podmornica extends VojnoPlovilo implements Sonar, TorpedoStit {

    public Podmornica(int vrsta){
        super(vrsta);
        this.smjerKretanja=false;
        this.naoruzanje = new Naoruzanje[1];
        this.naoruzanje[0] = new Torpedo();
    }

    @Override
    public String toString(){
        return "Podmornica\n" + super.toString();
    }
}