package cn.worldwalker.game.wyqp.web.interceptor;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.worldwalker.game.wyqp.common.utils.RequestUtil;

public class LogFilter extends OncePerRequestFilter {


    private AtomicLong id = new AtomicLong(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    		String requestId = generateRequestId(request);
            request.setAttribute("requestId", requestId);
            response.setHeader("requestId", requestId);
            RequestUtil.setRequset(request);
            RequestUtil.setResponse(response);
            filterChain.doFilter(request, response);
    }
    
    private String generateRequestId(HttpServletRequest request){
    	StringBuilder sb = new StringBuilder();
    	long atomicId = id.incrementAndGet();
    	String remoteAddr = request.getRemoteAddr();
    	long threadNum = Thread.currentThread().getId();
    	long time = System.currentTimeMillis();
    	sb.append(remoteAddr).append("-");
    	sb.append(threadNum).append("-");
    	sb.append(time).append("-");
    	sb.append(atomicId);
    	return sb.toString();
    }
    
}
