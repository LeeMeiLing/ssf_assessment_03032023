package ssf_assessment_03032023.purchaseOrder.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    
    private Map<String,Integer> cart = new LinkedHashMap<>();

    public Map<String, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<String, Integer> cart) {
        this.cart = cart;
    }
    
    public void addToCart(String itemName, Integer quantity){
        
        // check if item already exists in cart
        if (cart.containsKey(itemName)){

            // get current qty
            Integer temp = cart.get(itemName);

            // add new qty to existing item
            cart.put(itemName, temp +  quantity);

        }else{

            // add new item to cart
            cart.put(itemName, quantity);

        }

    }

    @Override
    public String toString() {
        return "Cart [cart=" + cart + "]";
    }

    
}
