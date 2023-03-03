package ssf_assessment_03032023.purchaseOrder.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
            System.out.println(">>>>>>>> new session cart createdin shopping cart"); // debug
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
            System.out.println(">>>>>>>> new session cart created in addToCart"); // debug

        }
        
        cart.addToCart(item.getItemName(), item.getQuantity());
        System.out.println(">>>>>>>> cart = " + cart); // debug
        model.addAttribute("item", new Item());
        model.addAttribute("cart", cart);

        return"view1";
    }

    @GetMapping("/shippingaddress")
    public String next(Model model, HttpSession session){

        model.addAttribute("shipping", new Shipping());
        return "view2";

    }

    @PostMapping("/checkout")
    public String checkout(@Valid Shipping shipping, BindingResult binding, Model model, 
        HttpSession session) throws Exception{

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

        session.setAttribute("shippping", shipping);

        List<String> itemList = new LinkedList<>();
        itemList.addAll(cart.getCart().keySet());
        System.out.println(">>>>>>>>>>>>>>>>>>Item List<<<<<<<<<<<<<<<<<<<");
        System.out.println(itemList);// debug  // correct
        
        // get quotation
        Quotation quotation = quoSvc.getQuotations(itemList);

        // perform calculation

        // return invoice
        
        return "view3";
    }
}
