package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.recognition.GroupedPostCollectionDto;
import at.ac.tuwien.verifeed.core.dto.recognition.RecognitionServiceRequest;
import at.ac.tuwien.verifeed.core.dto.recognition.TopicsAndPostCollectionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="recognition", url = "${spring.cloud.openfeign.client.config.recognition.url}")
public interface RecognitionServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/recognition", produces = "application/json")
    TopicsAndPostCollectionDto getGroupedAndLabeledPosts(RecognitionServiceRequest request);
}
