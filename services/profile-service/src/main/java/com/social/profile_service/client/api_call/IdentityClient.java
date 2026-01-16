package com.social.profile_service.client.api_call;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "identity-client", url="${idp.url}")
public interface IdentityClient {


}
