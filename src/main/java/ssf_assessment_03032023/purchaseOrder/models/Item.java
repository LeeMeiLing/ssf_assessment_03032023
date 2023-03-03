package ssf_assessment_03032023.purchaseOrder.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Item {

    private final static String[] option = {"apple","orange","bread","cheese","chicken","mineral_water","instant_noodles"};

    private String itemName;

    @NotNull(message = "You must add at least 1 item")
    @Min(value = 1, message = "You must add at least 1 item")
    private Integer quantity;

    

    public static String[] getOption() {
        return option;
    }



    public String getItemName() {
        return itemName;
    }



    public void setItemName(String itemName) {
        this.itemName = itemName;
    }



    public Integer getQuantity() {
        return quantity;
    }



    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    // return true if item in the option
    public Boolean checkItem(){

        for( String o : option ){

            if(this.itemName.equalsIgnoreCase(o)){
                return true;
            }

        }

        return false;
    }



    @Override
    public String toString() {
        return "Item [itemName=" + itemName + ", quantity=" + quantity + "]";
    }

    
}
