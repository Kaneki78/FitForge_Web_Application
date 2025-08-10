// PATH: src/main/java/com/fitforge/FitForge/controller/RankingController.java

package com.fitforge.FitForge.controller;

import com.fitforge.FitForge.dto.UserRankingDto;
import com.fitforge.FitForge.service.RankingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/ranking")
    public String showRankingPage(Model model) {
        List<UserRankingDto> rankings = rankingService.getWeeklyRankings();
        model.addAttribute("rankings", rankings);
        model.addAttribute("activePage", "ranking"); // For the sidebar
        return "ranking";
    }
}