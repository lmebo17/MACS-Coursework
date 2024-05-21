package Login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    public LoginServlet(){
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountManager am = (AccountManager)request.getServletContext().getAttribute(AccountManager.NAME);
        String name = request.getParameter("name");
        String password = request.getParameter("pw");

        if (am.isValidLogin(name, password)) {
            RequestDispatcher dispatch = request.getRequestDispatcher("Welcome.jsp");
            dispatch.forward(request, response);
        } else {
            RequestDispatcher dispatch = request.getRequestDispatcher("WrongPass.jsp");
            dispatch.forward(request, response);
        }
    }


}
