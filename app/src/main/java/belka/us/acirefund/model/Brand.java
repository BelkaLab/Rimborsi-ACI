package belka.us.acirefund.model;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

public class Brand {
    String name;

    public Brand(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Brand))
            return false;

        Brand brand = (Brand) obj;
        return brand.getName().equalsIgnoreCase(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
