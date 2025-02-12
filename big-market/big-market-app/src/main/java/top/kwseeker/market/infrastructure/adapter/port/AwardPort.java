package top.kwseeker.market.infrastructure.adapter.port;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import top.kwseeker.market.domain.award.adapter.port.IAwardPort;
import top.kwseeker.market.infrastructure.gateway.IOpenAIAccountService;
import top.kwseeker.market.infrastructure.gateway.dto.AdjustQuotaRequestDTO;
import top.kwseeker.market.infrastructure.gateway.dto.AdjustQuotaResponseDTO;
import top.kwseeker.market.infrastructure.gateway.response.Response;
import top.kwseeker.market.types.enums.ResponseCode;
import top.kwseeker.market.types.exception.AppException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description
 * @create 2024-10-06 11:46
 */
@Slf4j
@ApplicationScoped
public class AwardPort implements IAwardPort {

    @Inject
    @ConfigProperty(name = "gateway.config.big-market.app-id")
    String BIG_MARKET_APPID;
    @Inject
    @ConfigProperty(name = "gateway.config.big-market.app-token")
    String BIG_MARKET_APPTOKEN;

    @Inject
    IOpenAIAccountService openAIAccountService;

    @Override
    public void adjustAmount(String userId, Integer increaseQuota) throws Exception {
        try {
            AdjustQuotaRequestDTO requestDTO = AdjustQuotaRequestDTO.builder()
                    .appId(BIG_MARKET_APPID)
                    .appToken(BIG_MARKET_APPTOKEN)
                    .openid(userId)
                    .increaseQuota(increaseQuota)
                    .build();

            Call<Response<AdjustQuotaResponseDTO>> call = openAIAccountService.adjustQuota(requestDTO);
            Response<AdjustQuotaResponseDTO> response = call.execute().body();
            log.info("请求OpenAI应用账户调额接口完成 userId:{} increaseQuota:{} response:{}", userId, increaseQuota, JSON.toJSONString(response));

            if (null == response || null == response.getCode() || !"0000".equals(response.getCode())) {
                throw new AppException(ResponseCode.GATEWAY_ERROR.getCode(), ResponseCode.GATEWAY_ERROR.getInfo());
            }

        } catch (Exception e) {
            log.error("请求OpenAI应用账户调额接口失败 userId:{} increaseQuota:{}", userId, increaseQuota, e);
            throw e;
        }

    }

}
