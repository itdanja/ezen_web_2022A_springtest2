package Springtest2.controller;

import Springtest2.Entitiy.TestEntity;
import Springtest2.Entitiy.TestRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")    // : 현재 클래스는 home 매핑 된다.
public class IndexController {

    @GetMapping("/main")
    public String home(){ return "main"; }  // Response 요구가 없을경우 타임리프 HTML 반환

    @Autowired
    TestRepository testRepository ;

    // 2. main.js 내 작성메소드가 요청하는 URL
    @GetMapping("/save")
    @ResponseBody   // HTML 아닌 객체를 반환하는 어노테이션
    public String save( HttpServletRequest request ){
        // 0. 변수 요청하기
        String content = request.getParameter( "content");
        // 1. 엔티티 생성
        TestEntity testEntity = new TestEntity();
        testEntity.content = content;
        // 2. 엔티티를 db에 저장를 해주는 save 메소드 사용
        testRepository.save( testEntity );
        // 3. 반환
        return "작성 성공";
    }    // Response(응답) Body(객체)  : java객체를 반환하겠다.

    // 3. main.js내 호출 메소드가 요청하는 URL 정의
        // @ResponseBody   뺀이유 :  HttpServletResponse 사용
    @GetMapping("/getlist")
    public void getlist(HttpServletResponse response) {
        // 1. 모든 엔티티를 호출하기
        List<TestEntity>  testEntities =  testRepository.findAll( );
        // 2. JSON 화 하기
        JSONArray jsonArray = new JSONArray(); // 1. json 리스트 선언
        for( TestEntity entity  : testEntities ){ // 2.모든엔티티 반복문
                // 3. json객체 선언
                JSONObject jsonObject = new JSONObject();
                // 4. json객체 엔트리(키:값) 설정
                jsonObject.put( "no" , entity.no );
                jsonObject.put( "content" , entity.content );
                // 5. json객체를 json리스트에 추가
                jsonArray.put(jsonObject);
        }
        try {
            response.setCharacterEncoding("UTF-8");         // 응답객체 한글
            response.setContentType("application/json"); // 응답객체 json형식
            response.getWriter().print( jsonArray );           // 응답 [ json리스트]
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @GetMapping("/delete")
    @ResponseBody
    public String delete( HttpServletRequest request ){
        // 0. 변수 요청
        int no = Integer.parseInt( request.getParameter("no") );
        // 1. pk값 이용한 엔티티 찾기 
        Optional<TestEntity> optionalTestEntity = testRepository.findById(no);
        // 2. Optional객체내 엔티티 삭제
        TestEntity entity = optionalTestEntity.get();
        testRepository.delete(   entity   );
        return "1"; 
    }
    @GetMapping("/update")
    @ResponseBody
    @Transactional // 트랜잭션 : 일 단위[ 결과:COMMIT 혹은 ROLLBACK ]
    public String update( HttpServletRequest request ){
        // 0. 변수 요청
        int no = Integer.parseInt( request.getParameter("no") ) ;
        String content = request.getParameter("content");

        // 1. pk값 이용한 엔티티 찾기
        Optional<TestEntity> optionalTestEntity =  testRepository.findById( no );
                // Optional 클래스 : 제네릭 클래스의 객체를 저장 [ 만약에 null이면 null저장 ]
        // 2. Optional객체내 엔티티 빼오기  [ Optional 객체내 실제 entitiy 가져오기 ]
        TestEntity entity = optionalTestEntity.get();
        // 3. 수정
        entity.content = content;
        // 4. 반환
        return "1";
    }



    // @RequestMapping  : 모든 RUL 매핑 가능

    // @GetMapping : GET 메소드 URL 매핑 [ 요청 변수 보인다 = 보안X 캐시O ]

    // @PostMapping : POST 메소드 URL 매핑 [ 요청 변수 X = 보안O  캐시X ]

    ///////////////////////////////// 스프링에서 지원하는 요청방식 구분 //////////////////////////////

    // @PutMapping : PUT 메소드 URL 매핑 [ 삽입 , 수정 사용 ]

    // @DeleteMapping : DELETE메소드 URL 매핑 [ 삭제시 사용 ]

    // @PathVariable : 경로에 변수를 바인딩( 넘겨주기 )

    // post vs put/delete : 멱등성 : 요청후에 서버에 상태를 남기기
    // 반복 되는 많은 요청이 있을경우에 post 보다 put , delete 사용 권장

}
