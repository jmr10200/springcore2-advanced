package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldService {

    private String nameStore;

    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore);
        // 파라미터로 넘어온 name 을 nameStore 에 저장
        nameStore = name;
        // 1초간 쉰다음
        sleep(1000);
        log.info("조회 nameStore={}", nameStore);
        // 필드에 저장된 nameStore 리턴
        return nameStore;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
