package top.kwseeker.market.app.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import retrofit2.converter.jackson.JacksonConverterFactory;
import top.kwseeker.market.infrastructure.gateway.IOpenAIAccountService;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Retrofit;

@Slf4j
@ApplicationScoped
//@EnableConfigurationProperties(Retrofit2ConfigProperties.class)
public class Retrofit2Config {

    @Produces
    @ApplicationScoped
    public Retrofit retrofit(Retrofit2ConfigProperties properties) {
        return new Retrofit.Builder()
                .baseUrl(properties.apiHost())
                .addConverterFactory(JacksonConverterFactory.create()).build();
    }

    @Produces
    @ApplicationScoped
    public IOpenAIAccountService openAIAccountService(Retrofit retrofit) {
        return retrofit.create(IOpenAIAccountService.class);
    }
}
