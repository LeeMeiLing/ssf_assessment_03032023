package ssf_assessment_03032023.purchaseOrder.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Shipping {
    
    @NotNull(message = "Name is mandatory")
    @Size(min=2, message = "Your name must have minimum 2 characters")
    private String name;

    @NotBlank(message = "Address is mandatory")
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return "Shipping [name=" + name + ", address=" + address + "]";
    }

    
    
}
