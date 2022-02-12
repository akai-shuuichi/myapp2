package club.bluetroy.controller;

import club.bluetroy.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class CrawlerController {
    @Autowired
    private ProjectService projectService;

    @PatchMapping(value = "/start")
    void start() {
        projectService.process();
    }

    @PatchMapping("/shutdown")
    void shutdown() {
        projectService.shutdown();
    }
}
