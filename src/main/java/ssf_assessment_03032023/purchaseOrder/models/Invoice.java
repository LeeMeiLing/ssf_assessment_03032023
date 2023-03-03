package ssf_assessment_03032023.purchaseOrder.models;

import java.util.UUID;

public class Invoice {
    
    private String invoiceId;
    private String name;
    private String address;
    private Float total;
    
    public Invoice(String name, String address, Float total) {

        this.invoiceId = UUID.randomUUID().toString().substring(0,6);
        this.name = name;
        this.address = address;
        this.total = total;

    }

    public String getInvoiceId() {
        return invoiceId;
    }

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
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }
    @Override
    public String toString() {
        return "Invoice [invoiceId=" + invoiceId + ", name=" + name + ", address=" + address + ", total=" + total + "]";
    }
    
}
