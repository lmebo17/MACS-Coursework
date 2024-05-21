package Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;


@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    public CartServlet(){

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = (Cart)request.getSession().getAttribute("CART");
        if(cart == null){
            System.out.println("DOGET");
            cart = new Cart();
            request.getSession().setAttribute("CART", cart);
        }
        RequestDispatcher dispatch = request.getRequestDispatcher("ShoppingCart.jsp");
        dispatch.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = (Cart)request.getSession().getAttribute("CART");
        if(cart == null){

            cart = new Cart();
        }
        Shop shop;
        try {
            shop = new Shop();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String productID = request.getParameter("productID");
        if (productID != null) {
            cart.addProduct(shop.getProductWithID(productID));
        } else {
            Enumeration<String> productIDs = request.getParameterNames();
            while(productIDs.hasMoreElements()){
                productID = productIDs.nextElement();
                int quantity = Integer.parseInt(request.getParameter(productID));
                cart.changeProduct(shop.getProductWithID(productID), quantity);
            }
        }

        request.getSession().setAttribute("CART", cart);
        RequestDispatcher dispatch = request.getRequestDispatcher("ShoppingCart.jsp");
        dispatch.forward(request, response);



    }
}
