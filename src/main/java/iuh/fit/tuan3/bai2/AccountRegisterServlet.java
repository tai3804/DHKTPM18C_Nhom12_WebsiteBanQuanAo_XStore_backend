package iuh.fit.tuan3.bai2;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/registerform")
public class AccountRegisterServlet extends HttpServlet {
    private  AccountUtil accountUtil;

    @Resource(name="jdbc/storedb")
    private DataSource ds;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            accountUtil = new AccountUtil(ds);
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String dobStr = req.getParameter("dob");

        try {
            LocalDate localDate = LocalDate.parse(dobStr);
            java.sql.Date dob = java.sql.Date.valueOf(localDate);

            Account account = new Account(firstname, lastname, email, password, dob);
            accountUtil.addAccount(account);

            List<Account> accounts = accountUtil.getAccounts();
            req.setAttribute("accounts", accounts);

            RequestDispatcher rd = req.getRequestDispatcher("account.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
