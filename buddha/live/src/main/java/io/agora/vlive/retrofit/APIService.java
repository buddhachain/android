package io.agora.vlive.retrofit;

import io.reactivex.Observable;

import io.agora.dynamickey.utils.AgoraConfigs;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public interface APIService {

    @GET("/dev/v2/project/" + AgoraConfigs.appId + "/rtm/vendor/user_events")
    Observable<Response<Object>> login(@Header("x-agora-token") String token, @Header("x-agora-uid") String uid);

    @GET("/dev/v1/channel/" + AgoraConfigs.appId)
    Observable<Response<Object>> roomList(@Header("page_no") int page_no, @Header("page_size") int page_size);

}
