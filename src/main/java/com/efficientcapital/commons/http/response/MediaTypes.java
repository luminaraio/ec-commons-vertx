package com.efficientcapital.commons.http.response;

import static io.vertx.core.http.HttpHeaders.createOptimized;

/**
 * Created by Luminara Team.
 */
public class MediaTypes {

    /**
     * application/json;charset=utf-8 header value
     */
    public static final CharSequence APPLICATION_JSON = createOptimized(
            "application/json;charset=utf-8");
}
