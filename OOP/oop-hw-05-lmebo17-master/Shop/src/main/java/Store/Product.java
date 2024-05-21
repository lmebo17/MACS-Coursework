package Store;

public class Product {
    private String productID;
    private String productName;
    private String image;
    private double price;

    public Product(String productID, String productName, String image, double price){
        this.productID = productID;
        this.productName = productName;
        this.image = image;
        this.price = price;
    }

    @Override
    public int hashCode(){
        return productID.hashCode();
    }

    @Override
    public boolean equals(Object o){
        return this.productID.equals(((Product) o).productID);
    }

    public double getPrice(){
        return this.price;
    }

    public String getProductID(){
        return this.productID;
    }

    public String getImage(){
        return this.image;
    }

    public String getProductName(){
        return this.productName;
    }
}
