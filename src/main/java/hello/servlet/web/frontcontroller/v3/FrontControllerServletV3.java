package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form",new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save",new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members",new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI(); // URI = /front-controller/v3/members/new-form

        ControllerV3 controller = controllerMap.get(requestURI); // controller = MemberFormControllerV3() 객체
        System.out.println("FrontControllerServletV3.service");

        if (controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String,String> paramMap = createParamMap(request);
        // paramMap = {
        //    "username": "kim",
        //    "age": "123"
        //}

        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName(); //viewName = new-form
        MyView view = viewResolver(viewName); //논리적인 뷰 이름(new-form)을 물리적인 뷰 경로로 변환 -> /WEB-INF/views/new-form/.jsp
        view.render(mv.getModel(), request, response); //mv.getModel()은 컨트롤러가 준비한 모델 데이터를 반환
        // paramMap = "username": "kim",
        // paramMap = "age": "123"

    }

    private Map<String,String> createParamMap(HttpServletRequest request){
        Map<String,String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName,request.getParameter(paramName)));
        return paramMap;
        // paramMap = {
        //    "username": "kim",
        //    "age": "123"
        //}
        // paramMap = "username": "kim",
        // paramMap = "age": "123"
    }

    private MyView viewResolver(String viewName){
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
