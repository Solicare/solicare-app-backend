package com.solicare.app.backend.domain.dto.push;

import com.solicare.app.backend.domain.dto.ServiceResult;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
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
            status = Status.NO_DEVICE;
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

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        NOT_FOUND(ApiStatus._NOT_FOUND, "PUSH404", "푸시 대상을 찾을 수 없습니다"),
        NO_DEVICE(ApiStatus._BAD_REQUEST, "PUSH404", "연결된 디바이스가 없습니다"),
        ALL_SENT(ApiStatus._OK, "PUSH200", "모든 푸시가 성공적으로 전송되었습니다"),
        PARTIALLY_SENT(ApiStatus._OK, "PUSH206", "일부 푸시가 성공적으로 전송되었습니다"),
        NON_SENT(ApiStatus._INTERNAL_SERVER_ERROR, "PUSH500", "푸시 전송에 모두 실패했습니다");

        private final ApiStatus apiStatus;
        private final String code;
        private final String message;
    }
}
