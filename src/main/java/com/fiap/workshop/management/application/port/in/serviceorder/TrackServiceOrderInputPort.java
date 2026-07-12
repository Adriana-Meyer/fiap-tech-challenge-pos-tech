package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderTrackingResponse;

public interface TrackServiceOrderInputPort {
    ServiceOrderTrackingResponse execute(String osCode);
}
