package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderTrackingResponse;
import com.fiap.workshop.management.application.port.in.serviceorder.TrackServiceOrderInputPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    private final TrackServiceOrderInputPort trackServiceOrderUseCase;

    public TrackingController(TrackServiceOrderInputPort trackServiceOrderUseCase) {
        this.trackServiceOrderUseCase = trackServiceOrderUseCase;
    }

    @GetMapping("/{osCode}")
    public ServiceOrderTrackingResponse track(@PathVariable String osCode) {
        return trackServiceOrderUseCase.execute(osCode);
    }
}
