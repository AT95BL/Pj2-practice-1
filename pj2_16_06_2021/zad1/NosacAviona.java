public class NosacAviona extends VojnoPlovilo implements Radar, RaketniStit, TorpedoStit{

    public NosacAviona(int vrsta){
        super(vrsta);
        this.smjerKretanja=true;
        this.naoruzanje = new Naoruzanje[2];
        this.naoruzanje[0] = new Raketa();
        this.naoruzanje[1] = new Torpedo();
    }

    @Override
    public String toString(){
        return "Nosac Aviona:\n" + super.toString();
    }
}