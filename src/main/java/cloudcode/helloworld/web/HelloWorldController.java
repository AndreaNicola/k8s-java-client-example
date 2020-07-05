package cloudcode.helloworld.web;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.authenticators.GCPAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * defines the REST endpoints managed by the server.
 */
@RestController
@Slf4j
public final class HelloWorldController {

    /**
     * endpoint for the landing page
     *
     * @return a simple hello world message
     */
    @GetMapping("/")
    public String helloWorld() throws Exception {

        try {

            // KubeConfig.registerAuthenticator(new GCPAuthenticator());

            ApiClient client = ClientBuilder.cluster().build();

            // if you prefer not to refresh service account token, please use:
            // ApiClient client = ClientBuilder.oldCluster().build();

            // set the global default api-client to the in-cluster one from above
            Configuration.setDefaultApiClient(client);

            // the CoreV1Api loads default api-client from global configuration.
            CoreV1Api api = new CoreV1Api();

            // invokes the CoreV1Api client
            V1PodList list = api.listNamespacedPod("default", null, null, null, null, null, null, null,null, null);
            V1ServiceList listServices = api.listNamespacedService("default", null, null, null, null, null, null, null, null, null);


            System.out.println("Pods");
            for (V1Pod item : list.getItems()) {
                System.out.println(item.getMetadata().getName());
            }

            System.out.println("Services");
            for (V1Service svc : listServices.getItems()){
                System.out.println(svc.getMetadata().getName());
            }

            V1PodList allNsPods = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

            for (V1Pod pod : allNsPods.getItems()){
                System.out.println(pod.getMetadata().getName());
            }

        } catch (ApiException apiException) {

            log.error("code: {}", apiException.getCode());
            log.error("message: {}", apiException.getMessage());
            log.error("response body: {}", apiException.getResponseBody());
            log.error("response headers: {}", apiException.getResponseHeaders());
            log.error("localized message: {}", apiException.getLocalizedMessage());

        }

        return "Hello World!";
    }
}
