public class Metropolis {

    private String City;
    private String Continent;
    private String Population;

    public Metropolis(String City, String Continent, String Population){
        this.City = City;
        this.Continent = Continent;
        this.Population = Population;
    }

    public String getCity(){
        return this.City;
    }

    public String getContinent(){
        return this.Continent;
    }

    public String getPopulation(){
        return this.Population;
    }

}
