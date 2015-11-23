package hu.bme.aut.kutatasinplo.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;

import com.google.inject.Singleton;

@Singleton
public class AuthFilter implements Filter {

	private PassThruAuthenticationFilter filter;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		filter = new PassThruAuthenticationFilter();
		filter.setLoginUrl("/login.html");
		filter.setSuccessUrl("");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		// HttpServletRequest request = (HttpServletRequest) req;
		// HttpServletResponse response = (HttpServletResponse) res;
		//
		// URL url = new URL(request.getRequestURL().toString());
		// String domain = url.getHost();
		//
		// response.sendRedirect("/index.html");
		// chain.doFilter(req, res);
		try {
			Subject subject = SecurityUtils.getSubject();
			filter.onPreHandle(req, res, "/auth/*");
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
