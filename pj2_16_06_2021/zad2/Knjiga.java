import java.util.Objects;

public class Knjiga {
    private String naslov;
    private String pisac;
    private int godinaIzdavanja;
    private Zanr zanr;

    // Constructor, getters, setters, and other methods...
    public Knjiga(String naslov, String pisac, int godinaIzdavanja, Zanr zanr){
        this.naslov = naslov;
        this.pisac = pisac;
        this.godinaIzdavanja = godinaIzdavanja;
        this.zanr = zanr;
    }

    public Zanr getZanr(){
        return this.zanr;
    }

    public int getGodinaIzdavanja(){
        return this.godinaIzdavanja;
    }

    public String getPisac(){
        return this.pisac;
    }

    public String getNaslov(){
        return this.naslov;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Knjiga knjiga = (Knjiga) o;
        return godinaIzdavanja == knjiga.godinaIzdavanja && Objects.equals(naslov, knjiga.naslov);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naslov, godinaIzdavanja);
    }
}