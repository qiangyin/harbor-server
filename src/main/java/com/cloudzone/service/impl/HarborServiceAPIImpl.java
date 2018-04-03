package com.cloudzone.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.cloudzone.HarborServiceAPI;
import com.cloudzone.common.entity.harbor.*;
import com.cloudzone.common.utils.SSLRestTemplateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * harbor API 实现
 *
 * @author gaoyanlei
 * @params
 * @since 2018/3/9
 */
@Service
public class HarborServiceAPIImpl implements HarborServiceAPI {

    @Value("${harbor.server.hostUrl}")
    private String harborUrl;

    @Value("${harbor.server.loginUrl}")
    private String loginUrl;

    @Value("${harbor.server.projectsUrl}")
    private String projectsUrl;

    @Value("${harbor.server.repositoriesUrl}")
    private String repositoriesUrl;

    @Value("${harbor.server.tagsUrl}")
    private String tagsUrl;

    @Value("${harbor.server.username}")
    private String username;

    @Value("${harbor.server.password}")
    private String password;

    private HttpHeaders httpHeaders;

    @Override
    public HttpHeaders login(LoginUserVo loginUserVo) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("principal", StringUtils.isEmpty(loginUserVo.getPrincipal()) ? username : loginUserVo.getPrincipal());
        params.add("password", StringUtils.isEmpty(loginUserVo.getPrincipal()) ? password : loginUserVo.getPassword());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        HttpHeaders httpHeaders = SSLRestTemplateUtil.getInstance().postForEntity("https://10.112.101.90/login", requestEntity, String.class).getHeaders();
        return httpHeaders;
    }

    @Override
    public List<Projects> projects(String projectName, String isPublic, int page, int pageSize) {
        this.getHttpHeaders();
        String url = harborUrl + projectsUrl;
        Map map = new HashMap();
        map.put("project_name", projectName);
        map.put("is_public", isPublic);
        map.put("page", page);
        map.put("page_size", pageSize);
        String response = (String) SSLRestTemplateUtil.getInstance().getForEntity(url, String.class, map).getBody();
        return JSONArray.parseArray(response, Projects.class);
    }

    @Override
    public List<Repositories> repositories(String projectId, String q, String detail, int page, int pageSize) {
        String url = harborUrl + repositoriesUrl;
        Map map = new HashMap();
        map.put("project_id", projectId);
        map.put("q", q);
        map.put("detail", detail);
        map.put("page", page);
        map.put("page_size", pageSize);
        String response = (String) SSLRestTemplateUtil.getInstance().getForEntity(url, String.class, map).getBody();
        return JSONArray.parseArray(response, Repositories.class);
    }

    @Override
    public List<Tag> tags(@PathVariable("projectName") String projectName,
                          @PathVariable("repositoryName") String repositoryName) {
        String url = harborUrl + tagsUrl;
        Object[] parameter = new Object[2];
        parameter[0] = projectName;
        parameter[1] = repositoryName;
        String response = SSLRestTemplateUtil.getInstance().getForEntity(url, String.class, parameter).getBody();
        return JSONArray.parseArray(response, Tag.class);
    }

    @Override
    public String saveProjects(@RequestBody ProjectVO projectVO) {
        HttpHeaders headers = this.getHttpHeaders();
        String body = SSLRestTemplateUtil.getInstance().postForEntity("https://10.112.101.90/api/projects", JSONArray.toJSONString(projectVO), String.class).getBody();
        return body;
    }

    private HttpHeaders getHttpHeaders() {
        if (httpHeaders == null) {

            synchronized (HttpHeaders.class) {
                if (httpHeaders == null) {
                    httpHeaders = this.login(new LoginUserVo());
                }
            }
        }
        return httpHeaders;
    }

    public static void main(String[] args) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("principal", "admin");
        params.add("password", "Harbor12345");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        HttpHeaders httpHeaders = SSLRestTemplateUtil.getInstance().postForEntity("https://10.112.101.90/login", requestEntity, String.class).getHeaders();

        HttpHeaders headers1 = new HttpHeaders();
        ProjectVO bb = new ProjectVO();
        bb.setProjectName("4561");
        bb.setIspublic(1);
        headers1.setContentType(MediaType.APPLICATION_JSON);
        HttpHeaders httpHeaders1 = SSLRestTemplateUtil.getInstance().postForEntity("https://10.112.101.90/api/projects", JSONArray.toJSONString(bb), String.class).getHeaders();
    }
}
