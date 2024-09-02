package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3);
    }

//    핸들러(컨트롤러) 호출
    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ControllerV3 controller = (ControllerV3) handler; //핸들러 어댑터는 handler를 특정 인터페이스로 캐스팅한 후, 해당 컨트롤러의 메서드를 호출
        Map<String, String> paramMap = createParamMap(request);

        //ControllerV3의 경우 controller.process(paramMap) 메서드가 호출 ,
        // 컨트롤러가 처리된 결과를 ModelView 객체로 반환, ModelView에는 논리적인 뷰 이름(viewName)과 뷰에서 사용할 데이터(model)가 포함
        ModelView mv = controller.process(paramMap);
        return mv;
    }
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName,
                        request.getParameter(paramName)));
        return paramMap;
    }
}
