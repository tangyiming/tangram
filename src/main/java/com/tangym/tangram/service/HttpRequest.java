package com.tangym.tangram.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tangym.tangram.dto.NamedParam;
import com.tangym.tangram.entity.DfBizLine;
import com.tangym.tangram.entity.DfComponent;
import com.tangym.tangram.mapper.DfBizLineMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * <p>
 * 远程http接口的组件调用
 */

@Slf4j
@Component
public class HttpRequest {
    @Resource
    private DfBizLineMapper dfBizLineMapper;

    public String doPost(DfComponent dfComponent, List<DfComponent> scene) throws IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        String baseUrl = getBaseUrl(dfComponent.getBizId());
        HttpPost post = new HttpPost(baseUrl + dfComponent.getUrlpath());
        String response = null;
        String namedParamsStr = dfComponent.getParams();
        List<NamedParam> namedParam = JSONObject.parseObject(namedParamsStr, new TypeReference<List<NamedParam>>() {
        });
        Map<String, Object> m = new HashMap<String, Object>();
        namedParam.forEach(p -> {
            if (p.getMapping() != null) {
                String mapping = p.getMapping();
                String[] split = mapping.split(":");
                String inx = split[0];
                String key = split[1];
                String output = scene.get(Integer.parseInt(inx)).getOutput();
                JSONObject out = JSONObject.parseObject(output);
                String mapv = "";
                for (int o = 0; o < out.size(); o++) {
                    if (out.get(key) != null) {
                        mapv = out.get(key).toString();
                    }
                }
                m.put(p.getKey(), mapv);
            } else {
                m.put(p.getKey(), p.getValue());
            }
        });
        StringEntity entity = new StringEntity(JSONObject.toJSONString(m), StandardCharsets.UTF_8);
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
        HttpResponse res = httpclient.execute(post);
        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            response = EntityUtils.toString(res.getEntity());
        }
        return response;
    }

    private String getBaseUrl(Integer bizId) {
        DfBizLine dfBizLine = dfBizLineMapper.selectByPrimaryKey(bizId);
        if (null == dfBizLine) {
            log.error("bizId:{}未查询到业务域信息", bizId);
            return "";
        }
        String baseUrl = dfBizLine.getBaseUrl();
        if (baseUrl == null) {
            return "";
        }
        return baseUrl;
    }
}
