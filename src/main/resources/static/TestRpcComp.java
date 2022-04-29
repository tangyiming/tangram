package com.ymm.qa.tangram.component;

import com.tangym.tangram.component.ExeComponent;
import com.xxx.common.rpc.XxxResult;
import com.xxx.uc.uic.common.facade.dto.AccountInfoDTO;
import com.xxx.uc.uic.common.facade.service.AccountCenterService;
import io.xxxxx.xxxxx.api.rpc.annotation.RPCReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 *
 * 使用了service mesh rpc接口的组件（目前无法演示使用，需要相关service mesh环境与依赖，只在以前公司内部验证使用过，已使用xxx脱敏）
 */

@Component
public class TestRpcComp implements ExeComponent {
    @RPCReference(url = "http://com.xxx.services/uic/accountCenterService", retries = 3, timeout = 15000)
    AccountCenterService accountCenterService;

    @Override
    public void preExecute() {

    }

    @Override
    public Map<String, Object> execute(Map<String, ?> map) {
        XxxResult<AccountInfoDTO> accountInfoByAccountId = accountCenterService.getAccountInfoByAccountId(289910018L);
        Map<String, Object> res = new HashMap<>();
        res.put("data", accountInfoByAccountId.getData());
        res.put("int", 1);
        System.out.printf(res.toString());
        return res;
    }
}
