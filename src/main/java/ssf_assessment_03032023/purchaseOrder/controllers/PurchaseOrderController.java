package ssf_assessment_03032023.purchaseOrder.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf_assessment_03032023.purchaseOrder.models.Cart;
import ssf_assessment_03032023.purchaseOrder.models.Invoice;
import ssf_assessment_03032023.purchaseOrder.models.Item;
import ssf_assessment_03032023.purchaseOrder.models.Quotation;
import ssf_assessment_03032023.purchaseOrder.models.Shipping;
import ssf_assessment_03032023.purchaseOrder.services.QuotationService;

@Controller
public class PurchaseOrderController {

    @Autowired
    private QuotationService quoSvc;

    @GetMapping(path={"/","view1","view1.html"})
    public String shoppingCart(Model model, HttpSession session){

        Cart cart = (Cart) session.getAttribute("cart");

        if (null == cart){
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        model.addAttribute("item", new Item());
        model.addAttribute("cart", cart);

        return "view1";
    }
    
    @GetMapping(path={"/add"})
    public String addToCart(@Valid Item item, BindingResult binding, Model model, HttpSession session){

        if(binding.hasErrors()){

            model.addAttribute("cart", session.getAttribute("cart"));
            return "view1";

        }else if(!item.checkItem()){

            // if item not in the option, create error object
            ObjectError err = new ObjectError("noStock",
                        "We do not stock %s".formatted(item.getItemName()));
            binding.addError(err);
            model.addAttribute("cart", session.getAttribute("cart"));
            return "view1";
        }

        Cart cart = (Cart) session.getAttribute("cart");

        if (null == cart){
            cart = new Cart();
            session.setAttribute("cart", cart);

        }
        
        cart.addToCart(item.getItemName(), item.getQuantity());
        model.addAttribute("item", new Item());
        model.addAttribute("cart", cart);

        return"view1";
    }

    @GetMapping("/shippingaddress")
    public String next(Model model, HttpSession session){

        Shipping shipping = (Shipping) session.getAttribute("shipping");

        if (null == shipping){
            shipping = new Shipping();
        }

        model.addAttribute("shipping", shipping);
        return "view2";

    }

    @PostMapping("/checkout")
    public String checkout(@Valid Shipping shipping, BindingResult binding, Model model, 
        HttpSession session){

        if(binding.hasErrors()){

            return "view2";

        }
        
        // check valid cart
        Cart cart = (Cart) session.getAttribute("cart");

        if(null == cart){
            return "redirect:/";
        }else if( cart.getCart().isEmpty()){
            return "redirect:/";
        }

        session.setAttribute("shipping", shipping);

        List<String> itemList = new LinkedList<>();
        itemList.addAll(cart.getCart().keySet());
        
        // get quotation
        try{
            Quotation quotation = quoSvc.getQuotations(itemList);

            // perform calculation
            Invoice invoice = quoSvc.total(quotation, session);

            model.addAttribute("invoice", invoice);

            session.invalidate();

            // return invoice & display on view 3
            return "view3";

        }catch (Exception ex){

            // create error message & display on view 2
            ObjectError err = new ObjectError("quotationError", ex.getMessage());
            binding.addError(err);
            return "view2";

        }

    }
}
