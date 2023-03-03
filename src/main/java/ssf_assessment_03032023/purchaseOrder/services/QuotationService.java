package ssf_assessment_03032023.purchaseOrder.services;

import java.io.StringReader;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import ssf_assessment_03032023.purchaseOrder.models.Cart;
import ssf_assessment_03032023.purchaseOrder.models.Invoice;
import ssf_assessment_03032023.purchaseOrder.models.Quotation;
import ssf_assessment_03032023.purchaseOrder.models.Shipping;

@Service
public class QuotationService {

    public Quotation getQuotations(List<String> items) throws Exception {

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (String item : items) {
            arrBuilder = arrBuilder.add(item);
        }

        JsonArray arr = arrBuilder.build();

        RequestEntity<String> req = RequestEntity
                .post("https://quotation.chuklee.com/quotation")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(arr.toString(), String.class);

        // Make the request
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;
        String payload;

        try {

            resp = template.exchange(req, String.class);
            payload = resp.getBody();

            // response is a Json object 1) quoteId & 2) quotations (a Json object)
            // convert Json responseinto Quotation instance
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject jo = reader.readObject();

            Quotation quotation = new Quotation();

            quotation.setQuoteId(jo.getString("quoteId"));

            JsonArray jsonArr = jo.getJsonArray("quotations");
            for (int i = 0; i < jsonArr.size(); i++){
                jo = jsonArr.getJsonObject(i);
                quotation.addQuotation(jo.getString("item"), jo.getJsonNumber("unitPrice").bigDecimalValue().floatValue());
            }
            
            // return the Quotation instance
            return quotation;

        } catch (HttpClientErrorException ex) {

            // if request fail, response will be an error object
            // throw exception with error message 

            payload = resp.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject jo = reader.readObject();
            throw new Exception(jo.getString("error"));
        }
    
    }

    public Invoice total(Quotation quotation, HttpSession session){

        // retrieve cart
        Cart cart = (Cart) session.getAttribute("cart");

        Float total = 0f;
        Float unitPrice;
        Integer quantity;

        // calculate total cost of all items in the cart
        for (String key : cart.getCart().keySet()){
            quantity = cart.getCart().get(key);
            unitPrice = quotation.getQuotation(key);
            total = total + (quantity * unitPrice);
        }

        //retrieve shipping details
        Shipping shipping = (Shipping) session.getAttribute("shipping");

        // generate invoice
        Invoice invoice = new Invoice(shipping.getName(), shipping.getAddress(),total);

        return invoice;
    }

}
