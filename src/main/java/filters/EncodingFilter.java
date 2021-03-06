package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {
    private String charsetEncoding = "utf-8";
    private String contentType = "text/html";
    public void init(FilterConfig config) throws ServletException {
    }
    public void destroy() {
    }
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        request.setCharacterEncoding(charsetEncoding);
        response.setContentType(contentType);
        response.setCharacterEncoding(charsetEncoding);
        System.out.println("[FLTR] Encoding");
        filterChain.doFilter(request, response);
    }
}
