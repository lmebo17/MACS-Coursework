package Store;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;

public class Cart {

    private HashMap<Product, Integer> cart;
    private double totalPrice;

    public Cart(){
        this.cart = new HashMap<>();
        this.totalPrice = 0;
    }

    public HashMap<Product, Integer> listCart(){
        return this.cart;
    }

    public void addProduct(Product product){
        if(cart.containsKey(product)){
            this.cart.put(product, cart.get(product) + 1);
        } else cart.put(product, 1);

        this.totalPrice += product.getPrice();
    }

    public boolean isProductInCart(Product product){
        return this.cart.containsKey(product);
    }

    public double totalPrice(){
        return this.totalPrice;
    }

    public void changeProduct(Product product, int quantity){
        if(quantity < 0) return;
        int prevQuantity = -1;
        if(this.cart.containsKey(product)){
            if(quantity == 0){
                this.totalPrice -= product.getPrice() * this.cart.get(product);
                this.cart.remove(product);
                return;
            }
            prevQuantity = this.cart.get(product);
            this.cart.put(product, quantity);
            this.totalPrice += (quantity - prevQuantity) * product.getPrice();
        } else {
            this.cart.put(product, quantity);
            this.totalPrice += product.getPrice() * quantity;
        }
    }

}
