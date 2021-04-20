package io.agora.vlive.retrofit;

import io.reactivex.Observable;

import io.agora.dynamickey.utils.AgoraConfigs;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public interface APIService {

    @GET("/dev/v2/project/" + AgoraConfigs.appId + "/rtm/vendor/user_events")
    Observable<Response<Object>> user_events(@Header("x-agora-token") String token, @Header("x-agora-uid") String uid);

}
