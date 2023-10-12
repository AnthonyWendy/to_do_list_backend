package br.com.anthony.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.anthony.todolist.user.repository.UserModelRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    UserModelRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if(servletPath.startsWith("/tasks/")){

            //change information encrypted in string
            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim();
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            var authString = new String(authDecode);
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            //checked if the user exist
            var userChecked = userRepository.findByUserName(username);

            if(userChecked == null){
                response.sendError(401, "Email/Senha inválidos");
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), userChecked.getPassword());

                if(passwordVerify.verified){
                    request.setAttribute("userId", userChecked.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }
        } else {
            filterChain.doFilter(request, response);
        }

    }
}

