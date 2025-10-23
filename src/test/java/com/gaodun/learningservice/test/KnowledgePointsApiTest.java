package com.gaodun.learningservice.test;

import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 知识点API接口测试类
 * 验证知识点的增删改查功能是否正常
 * @author shkstart
 * @create 2025-10-23 3:20
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KnowledgePointsApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:%d/api/knowledge-points";

    @Test
    public void testKnowledgePointsCRUDOperations() {
        // 1. 测试查询所有知识点
        String listUrl = String.format(BASE_URL + "/list", port);
        ResponseEntity<KnowledgePointsEntity[]> listResponse = restTemplate.getForEntity(listUrl, KnowledgePointsEntity[].class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        List<KnowledgePointsEntity> allPoints = Arrays.asList(listResponse.getBody());
        assertTrue(allPoints.size() > 0, "知识点列表不为空");
        System.out.println("查询所有知识点成功，共" + allPoints.size() + "条数据");

        // 2. 测试根据课程ID查询知识点（会计课程ID为4）
        String listByCourseIdUrl = String.format(BASE_URL + "/listByCourseId?courseId=4", port);
        ResponseEntity<KnowledgePointsEntity[]> coursePointsResponse = restTemplate.getForEntity(listByCourseIdUrl, KnowledgePointsEntity[].class);
        assertEquals(HttpStatus.OK, coursePointsResponse.getStatusCode());
        assertNotNull(coursePointsResponse.getBody());
        List<KnowledgePointsEntity> coursePoints = Arrays.asList(coursePointsResponse.getBody());
        assertTrue(coursePoints.size() > 0, "会计课程知识点列表不为空");
        System.out.println("查询会计课程知识点成功，共" + coursePoints.size() + "条数据");

        // 3. 测试根据ID查询知识点（使用会计课程的第一个知识点ID）
        if (!coursePoints.isEmpty()) {
            Integer pointId = coursePoints.get(0).getPointId();
            String getByIdUrl = String.format(BASE_URL + "/getById?pointId=%d", port, pointId);
            ResponseEntity<KnowledgePointsEntity> getByIdResponse = restTemplate.getForEntity(getByIdUrl, KnowledgePointsEntity.class);
            assertEquals(HttpStatus.OK, getByIdResponse.getStatusCode());
            assertNotNull(getByIdResponse.getBody());
            assertEquals(pointId, getByIdResponse.getBody().getPointId());
            System.out.println("根据ID查询知识点成功，ID=" + pointId);

            // 4. 测试新增知识点
            KnowledgePointsEntity newPoint = new KnowledgePointsEntity();
            newPoint.setPointId(allPoints.size() + 1); // 使用最大ID+1作为新ID
            newPoint.setCourseId(4); // 会计课程
            newPoint.setTitle("测试知识点");
            newPoint.setDescription("这是一个测试知识点");
            newPoint.setPrerequisiteId(pointId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<KnowledgePointsEntity> request = new HttpEntity<>(newPoint, headers);
            String insertUrl = String.format(BASE_URL + "/insert", port);
            ResponseEntity<Integer> insertResponse = restTemplate.postForEntity(insertUrl, request, Integer.class);
            assertEquals(HttpStatus.OK, insertResponse.getStatusCode());
            assertEquals(1, insertResponse.getBody());
            System.out.println("新增知识点成功");

            // 5. 测试更新知识点
            newPoint.setTitle("更新后的测试知识点");
            HttpEntity<KnowledgePointsEntity> updateRequest = new HttpEntity<>(newPoint, headers);
            String updateUrl = String.format(BASE_URL + "/update", port);
            ResponseEntity<Integer> updateResponse = restTemplate.postForEntity(updateUrl, updateRequest, Integer.class);
            assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
            assertEquals(1, updateResponse.getBody());
            System.out.println("更新知识点成功");

            // 验证更新结果
            String verifyUrl = String.format(BASE_URL + "/getById?pointId=%d", port, newPoint.getPointId());
            ResponseEntity<KnowledgePointsEntity> verifyResponse = restTemplate.getForEntity(verifyUrl, KnowledgePointsEntity.class);
            assertEquals("更新后的测试知识点", verifyResponse.getBody().getTitle());

            // 6. 测试删除知识点
            String deleteUrl = String.format(BASE_URL + "/delete?pointId=%d", port, newPoint.getPointId());
            restTemplate.delete(deleteUrl);
            System.out.println("删除知识点成功");

            // 验证删除结果
            ResponseEntity<KnowledgePointsEntity> deleteVerifyResponse = restTemplate.getForEntity(verifyUrl, KnowledgePointsEntity.class);
            assertNull(deleteVerifyResponse.getBody());
        }
    }
}