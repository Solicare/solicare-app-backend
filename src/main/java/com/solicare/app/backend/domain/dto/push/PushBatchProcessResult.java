package com.solicare.app.backend.domain.dto.push;

import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class PushBatchProcessResult implements ServiceResult {
    private final List<PushDeliveryResult> details;
    private Status status;

    public PushBatchProcessResult setStatusByDetails() {
        int targetCount = getTargetCount();
        int successCount = getSuccessCount();

        if (targetCount == 0) {
            status = Status.UNAVAILABLE;
        } else if (successCount == targetCount) {
            status = Status.ALL_SENT;
        } else if (successCount > 0) {
            status = Status.PARTIALLY_SENT;
        } else {
            status = Status.NON_SENT;
        }
        return this;
    }

    public int getTargetCount() {
        return (details == null) ? 0 : details.size();
    }

    public int getSuccessCount() {
        return (details == null)
                ? 0
                : details.stream().filter(ServiceResult::isSuccess).toList().size();
    }

    @Override
    public boolean isSuccess() {
        return status == Status.ALL_SENT || status == Status.PARTIALLY_SENT;
    }

    public enum Status {
        UNAVAILABLE,
        NOT_FOUND,
        ALL_SENT,
        PARTIALLY_SENT,
        NON_SENT
    }
}
